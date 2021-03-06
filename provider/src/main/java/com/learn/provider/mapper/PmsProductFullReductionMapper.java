package com.learn.provider.mapper;


import java.util.List;

import com.learn.api.model.PmsProductFullReduction;
import com.learn.api.model.PmsProductFullReductionExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface PmsProductFullReductionMapper {
    @SelectProvider(type=PmsProductFullReductionSqlProvider.class, method="countByExample")
    long countByExample(PmsProductFullReductionExample example);

    @DeleteProvider(type=PmsProductFullReductionSqlProvider.class, method="deleteByExample")
    int deleteByExample(PmsProductFullReductionExample example);

    @Delete({
        "delete from pms_product_full_reduction",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into pms_product_full_reduction (id, product_id, ",
        "full_price, reduce_price)",
        "values (#{id,jdbcType=BIGINT}, #{productId,jdbcType=BIGINT}, ",
        "#{fullPrice,jdbcType=DECIMAL}, #{reducePrice,jdbcType=DECIMAL})"
    })
    int insert(PmsProductFullReduction record);

    @InsertProvider(type=PmsProductFullReductionSqlProvider.class, method="insertSelective")
    int insertSelective(PmsProductFullReduction record);

    @SelectProvider(type=PmsProductFullReductionSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
            @Result(column="full_price", property="fullPrice", jdbcType=JdbcType.DECIMAL),
            @Result(column="reduce_price", property="reducePrice", jdbcType=JdbcType.DECIMAL)
    })
    List<PmsProductFullReduction> selectByExample(PmsProductFullReductionExample example);

    @Select({
            "select",
            "id, product_id, full_price, reduce_price",
            "from pms_product_full_reduction",
            "where product_id = #{id,jdbcType=BIGINT}"
    })

    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
            @Result(column="full_price", property="fullPrice", jdbcType=JdbcType.DECIMAL),
            @Result(column="reduce_price", property="reducePrice", jdbcType=JdbcType.DECIMAL)
    })
    List<PmsProductFullReduction> selectFullByProductId(Long id);


    @Select({
        "select",
        "id, product_id, full_price, reduce_price",
        "from pms_product_full_reduction",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
        @Result(column="full_price", property="fullPrice", jdbcType=JdbcType.DECIMAL),
        @Result(column="reduce_price", property="reducePrice", jdbcType=JdbcType.DECIMAL)
    })
    PmsProductFullReduction selectByPrimaryKey(Long id);

    @UpdateProvider(type=PmsProductFullReductionSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") PmsProductFullReduction record, @Param("example") PmsProductFullReductionExample example);

    @UpdateProvider(type=PmsProductFullReductionSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") PmsProductFullReduction record, @Param("example") PmsProductFullReductionExample example);

    @UpdateProvider(type=PmsProductFullReductionSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(PmsProductFullReduction record);

    @Update({
        "update pms_product_full_reduction",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "full_price = #{fullPrice,jdbcType=DECIMAL},",
          "reduce_price = #{reducePrice,jdbcType=DECIMAL}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(PmsProductFullReduction record);

    //????????????????????????
    @Insert({
            "<script> " ,
           " INSERT INTO pms_product_full_reduction (product_id, full_price, reduce_price) VALUES " ,
                   "<foreach collection=\"list\" separator=\",\" item=\"item\" index=\"index\"> " ,
                   "( " ,
                   "#{item.productId,jdbcType=BIGINT}, " ,
                   "#{item.fullPrice,jdbcType=DECIMAL}, " ,
                   "#{item.reducePrice,jdbcType=DECIMAL} " +
                   ") " ,
                   "</foreach> ",
            "</script> "
    })
    int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}