import com.learn.api.model.UmsLog;
import com.learn.common.GsonUtil;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class UVReceiver {
    public static void main(String[] args) throws Exception {
        String topic="malluv1";
        String host="master";
        int port=9092;
        int database_id=0;//redis在flink输出的数据库索引号
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(TimeUnit.MINUTES.toMillis(1));//1分钟做一次检查点
        env.setParallelism(5);

        CheckpointConfig checkpointConfig=env.getCheckpointConfig();
        //防止最后处理的结果和数据丢失与重复
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //一旦flink处理数据被cancel后 会自动保留checkpoint数据 以便可以恢复指定的checkpoint处理
        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        Properties pro=new Properties();
        pro.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,host+":"+port);
        pro.put(ConsumerConfig.GROUP_ID_CONFIG,"app-uv-stat");
        //flink和kafka
        FlinkKafkaConsumerBase<String> kafkaConsumer=//从topic中指定group上次消费的位置开始消费，所以必须配置groupid
                new FlinkKafkaConsumer<String>(topic,new SimpleStringSchema(),pro).setStartFromGroupOffsets();
        //flink 和redis
        FlinkJedisPoolConfig conf=new FlinkJedisPoolConfig.Builder().setDatabase(database_id).setHost(host).build();
        //"20783","2021-01-06","2021-01-06","37","192.168.1.199","查询订单","{'pageNum':['1']&&'pageSize':['10']}","GET","/order/list","80","17"
        env.addSource(kafkaConsumer).map(s->{
            //对s进行拆解
            StringBuilder sb=new StringBuilder();
            try{
                List<String> list=Arrays.asList(s.split(","));
                //把数据拆解 加入其字段 组合成json格式
                sb.append("{");
                Field[] fs= UmsLog.class.getDeclaredFields();
                for(int i=0;i<list.size();i++){
                    sb.append(fs[i].getName()+":"+list.get(i)+",");

                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                sb.append("}");
                //组合成类似于
                //{id:"54",createBy:"system",createTime:"2020-02-05 18:05:15.0",delFlag:"0",
                // updateBy:"system",updateTime:"2020-02-05",costTime:"23",ip:"127.0.0.1",ipInfo:"",
                // name:"品牌列表",
                // requestParam:"{""pageNum"":[""1""]&&""pageSize"":[""100""]}",requestType:"GET",requestUrl:"/brand/list",
                // userid:"3",logType:"5"}


            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println(sb.toString());

            UmsLog u=null;
            u=GsonUtil.fromJson(sb.toString(),UmsLog.class);

            return u;
        }).keyBy("updateTime","logType")//按照日期和页面类型进行分组
                .map(new RichMapFunction<UmsLog, Tuple2<String,Long>>() {
                    //存储当前可以对应的userId集合
                    private MapState<String,Boolean> userIdState;
                    private ValueState<Long> uvState;
                    @Override
                    public Tuple2<String, Long> map(UmsLog umsLog) throws Exception {
                        if(uvState.value()==null){
                            uvState.update(0L);
                        }
                        //userId中如果不包含当前访问的userId,该用户今天还未访问过该页面
                        //则将userId put到userIdState中，并把UV值+1
                        if(!userIdState.contains(umsLog.getUserid().toString())){
                            userIdState.put(umsLog.getUserid().toString(),null);
                            uvState.update(uvState.value()+1);
                        }
                        //生成redisKey 格式是 日期_LogType：20191102_0
                        String redisKey=umsLog.getUpdateTime()+"_"+umsLog.getLogType();

                        System.out.println(redisKey + "   :::   " + uvState.value());
                        System.out.println("由"+umsLog.getUserid()+"号用户，在"+umsLog.getUpdateTime()+
                                "时间操作了地址【"+umsLog.getRequestUrl()+"】1次！"+"；数据库的序号是："+umsLog.getId());
                        System.out.println("【"+umsLog.getRequestUrl()+"】，共操作了"+uvState.value()+"次！");


                        return Tuple2.of(redisKey,uvState.value());
                    }

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        //从状态中恢复userIdState
                        userIdState=getRuntimeContext().getMapState(new MapStateDescriptor<>("userIdState",
                                TypeInformation.of(new TypeHint<String>(){}),TypeInformation.of(new TypeHint<Boolean>(){})));
                        //从状态中恢复uvState
                        uvState=getRuntimeContext().getState(
                                new ValueStateDescriptor<>("uvState",TypeInformation.of(new TypeHint<Long>(){})));
                    }
                }).addSink(new RedisSink<>(conf,new RedisSetSinkMapper()));//UmsLog--->Tuple2<String,Long>

        env.execute();
    }
    public  static class  RedisSetSinkMapper implements RedisMapper<Tuple2<String,Long>> {

        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.SET);
        }

        @Override
        public String getKeyFromData(Tuple2<String, Long> stringLongTuple2) {
            return stringLongTuple2.f0;
        }

        @Override
        public String getValueFromData(Tuple2<String, Long> stringLongTuple2) {
            return stringLongTuple2.f1.toString();
        }
    }
}