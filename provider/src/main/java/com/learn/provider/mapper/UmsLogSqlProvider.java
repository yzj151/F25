package com.learn.provider.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT_DISTINCT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.learn.api.model.UmsLog;
import com.learn.api.model.UmsLogExample.Criteria;
import com.learn.api.model.UmsLogExample.Criterion;
import com.learn.api.model.UmsLogExample;
import java.util.List;
import java.util.Map;

public class UmsLogSqlProvider {

    public String countByExample(UmsLogExample example) {
        BEGIN();
        SELECT("count(*)");
        FROM("ums_log");
        applyWhere(example, false);
        return SQL();
    }

    public String deleteByExample(UmsLogExample example) {
        BEGIN();
        DELETE_FROM("ums_log");
        applyWhere(example, false);
        return SQL();
    }

    public String insertSelective(UmsLog record) {
        BEGIN();
        INSERT_INTO("ums_log");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=VARCHAR}");
        }
        
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=VARCHAR}");
        }
        
        if (record.getCostTime() != null) {
            VALUES("cost_time", "#{costTime,jdbcType=INTEGER}");
        }
        
        if (record.getIp() != null) {
            VALUES("ip", "#{ip,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getRequestType() != null) {
            VALUES("request_type", "#{requestType,jdbcType=VARCHAR}");
        }
        
        if (record.getRequestUrl() != null) {
            VALUES("request_url", "#{requestUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getUserid() != null) {
            VALUES("userid", "#{userid,jdbcType=INTEGER}");
        }
        
        if (record.getLogType() != null) {
            VALUES("log_type", "#{logType,jdbcType=INTEGER}");
        }
        
        if (record.getRequestParam() != null) {
            VALUES("request_param", "#{requestParam,jdbcType=LONGVARCHAR}");
        }
        
        return SQL();
    }

    public String selectByExampleWithBLOBs(UmsLogExample example) {
        BEGIN();
        if (example != null && example.isDistinct()) {
            SELECT_DISTINCT("id");
        } else {
            SELECT("id");
        }
        SELECT("create_time");
        SELECT("update_time");
        SELECT("cost_time");
        SELECT("ip");
        SELECT("name");
        SELECT("request_type");
        SELECT("request_url");
        SELECT("userid");
        SELECT("log_type");
        SELECT("request_param");
        FROM("ums_log");
        applyWhere(example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            ORDER_BY(example.getOrderByClause());
        }
        
        return SQL();
    }

    public String selectByExample(UmsLogExample example) {
        BEGIN();
        if (example != null && example.isDistinct()) {
            SELECT_DISTINCT("id");
        } else {
            SELECT("id");
        }
        SELECT("create_time");
        SELECT("update_time");
        SELECT("cost_time");
        SELECT("ip");
        SELECT("name");
        SELECT("request_type");
        SELECT("request_url");
        SELECT("userid");
        SELECT("log_type");
        FROM("ums_log");
        applyWhere(example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            ORDER_BY(example.getOrderByClause());
        }
        
        return SQL();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        UmsLog record = (UmsLog) parameter.get("record");
        UmsLogExample example = (UmsLogExample) parameter.get("example");
        
        BEGIN();
        UPDATE("ums_log");
        
        if (record.getId() != null) {
            SET("id = #{record.id,jdbcType=BIGINT}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{record.createTime,jdbcType=VARCHAR}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{record.updateTime,jdbcType=VARCHAR}");
        }
        
        if (record.getCostTime() != null) {
            SET("cost_time = #{record.costTime,jdbcType=INTEGER}");
        }
        
        if (record.getIp() != null) {
            SET("ip = #{record.ip,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            SET("name = #{record.name,jdbcType=VARCHAR}");
        }
        
        if (record.getRequestType() != null) {
            SET("request_type = #{record.requestType,jdbcType=VARCHAR}");
        }
        
        if (record.getRequestUrl() != null) {
            SET("request_url = #{record.requestUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getUserid() != null) {
            SET("userid = #{record.userid,jdbcType=INTEGER}");
        }
        
        if (record.getLogType() != null) {
            SET("log_type = #{record.logType,jdbcType=INTEGER}");
        }
        
        if (record.getRequestParam() != null) {
            SET("request_param = #{record.requestParam,jdbcType=LONGVARCHAR}");
        }
        
        applyWhere(example, true);
        return SQL();
    }

    public String updateByExampleWithBLOBs(Map<String, Object> parameter) {
        BEGIN();
        UPDATE("ums_log");
        
        SET("id = #{record.id,jdbcType=BIGINT}");
        SET("create_time = #{record.createTime,jdbcType=VARCHAR}");
        SET("update_time = #{record.updateTime,jdbcType=VARCHAR}");
        SET("cost_time = #{record.costTime,jdbcType=INTEGER}");
        SET("ip = #{record.ip,jdbcType=VARCHAR}");
        SET("name = #{record.name,jdbcType=VARCHAR}");
        SET("request_type = #{record.requestType,jdbcType=VARCHAR}");
        SET("request_url = #{record.requestUrl,jdbcType=VARCHAR}");
        SET("userid = #{record.userid,jdbcType=INTEGER}");
        SET("log_type = #{record.logType,jdbcType=INTEGER}");
        SET("request_param = #{record.requestParam,jdbcType=LONGVARCHAR}");
        
        UmsLogExample example = (UmsLogExample) parameter.get("example");
        applyWhere(example, true);
        return SQL();
    }

    public String updateByExample(Map<String, Object> parameter) {
        BEGIN();
        UPDATE("ums_log");
        
        SET("id = #{record.id,jdbcType=BIGINT}");
        SET("create_time = #{record.createTime,jdbcType=VARCHAR}");
        SET("update_time = #{record.updateTime,jdbcType=VARCHAR}");
        SET("cost_time = #{record.costTime,jdbcType=INTEGER}");
        SET("ip = #{record.ip,jdbcType=VARCHAR}");
        SET("name = #{record.name,jdbcType=VARCHAR}");
        SET("request_type = #{record.requestType,jdbcType=VARCHAR}");
        SET("request_url = #{record.requestUrl,jdbcType=VARCHAR}");
        SET("userid = #{record.userid,jdbcType=INTEGER}");
        SET("log_type = #{record.logType,jdbcType=INTEGER}");
        
        UmsLogExample example = (UmsLogExample) parameter.get("example");
        applyWhere(example, true);
        return SQL();
    }

    public String updateByPrimaryKeySelective(UmsLog record) {
        BEGIN();
        UPDATE("ums_log");
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=VARCHAR}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=VARCHAR}");
        }
        
        if (record.getCostTime() != null) {
            SET("cost_time = #{costTime,jdbcType=INTEGER}");
        }
        
        if (record.getIp() != null) {
            SET("ip = #{ip,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getRequestType() != null) {
            SET("request_type = #{requestType,jdbcType=VARCHAR}");
        }
        
        if (record.getRequestUrl() != null) {
            SET("request_url = #{requestUrl,jdbcType=VARCHAR}");
        }
        
        if (record.getUserid() != null) {
            SET("userid = #{userid,jdbcType=INTEGER}");
        }
        
        if (record.getLogType() != null) {
            SET("log_type = #{logType,jdbcType=INTEGER}");
        }
        
        if (record.getRequestParam() != null) {
            SET("request_param = #{requestParam,jdbcType=LONGVARCHAR}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }

    protected void applyWhere(UmsLogExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            WHERE(sb.toString());
        }
    }
}