package com.learn.provider.mapper;


import java.util.List;

import com.learn.api.model.PmsMemberPrice;
import com.learn.api.model.PmsMemberPriceExample;
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

public interface PmsMemberPriceMapper {
    @SelectProvider(type=PmsMemberPriceSqlProvider.class, method="countByExample")
    long countByExample(PmsMemberPriceExample example);

    @DeleteProvider(type=PmsMemberPriceSqlProvider.class, method="deleteByExample")
    int deleteByExample(PmsMemberPriceExample example);

    @Delete({
        "delete from pms_member_price",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into pms_member_price (id, product_id, ",
        "member_level_id, member_price, ",
        "member_level_name)",
        "values (#{id,jdbcType=BIGINT}, #{productId,jdbcType=BIGINT}, ",
        "#{memberLevelId,jdbcType=BIGINT}, #{memberPrice,jdbcType=DECIMAL}, ",
        "#{memberLevelName,jdbcType=VARCHAR})"
    })
    int insert(PmsMemberPrice record);

    @InsertProvider(type=PmsMemberPriceSqlProvider.class, method="insertSelective")
    int insertSelective(PmsMemberPrice record);

    @SelectProvider(type=PmsMemberPriceSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
        @Result(column="member_level_id", property="memberLevelId", jdbcType=JdbcType.BIGINT),
        @Result(column="member_price", property="memberPrice", jdbcType=JdbcType.DECIMAL),
        @Result(column="member_level_name", property="memberLevelName", jdbcType=JdbcType.VARCHAR)
    })
    List<PmsMemberPrice> selectByExample(PmsMemberPriceExample example);

    @Select({
            "select",
            "id, product_id, member_level_id, member_price, member_level_name",
            "from pms_member_price",
            "where product_id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
            @Result(column="member_level_id", property="memberLevelId", jdbcType=JdbcType.BIGINT),
            @Result(column="member_price", property="memberPrice", jdbcType=JdbcType.DECIMAL),
            @Result(column="member_level_name", property="memberLevelName", jdbcType=JdbcType.VARCHAR)
    })
    List<PmsMemberPrice> selectMemberByParentId(Long id);


    @Select({
        "select",
        "id, product_id, member_level_id, member_price, member_level_name",
        "from pms_member_price",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
        @Result(column="member_level_id", property="memberLevelId", jdbcType=JdbcType.BIGINT),
        @Result(column="member_price", property="memberPrice", jdbcType=JdbcType.DECIMAL),
        @Result(column="member_level_name", property="memberLevelName", jdbcType=JdbcType.VARCHAR)
    })
    PmsMemberPrice selectByPrimaryKey(Long id);

    @UpdateProvider(type=PmsMemberPriceSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") PmsMemberPrice record, @Param("example") PmsMemberPriceExample example);

    @UpdateProvider(type=PmsMemberPriceSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") PmsMemberPrice record, @Param("example") PmsMemberPriceExample example);

    @UpdateProvider(type=PmsMemberPriceSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(PmsMemberPrice record);

    @Update({
        "update pms_member_price",
        "set product_id = #{productId,jdbcType=BIGINT},",
          "member_level_id = #{memberLevelId,jdbcType=BIGINT},",
          "member_price = #{memberPrice,jdbcType=DECIMAL},",
          "member_level_name = #{memberLevelName,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(PmsMemberPrice record);

    @Insert({
            "<script>" ,
           "  INSERT INTO pms_member_price (product_id, member_level_id, member_price,member_level_name) VALUES " ,
                   "<foreach collection=\"list\" separator=\",\" item=\"item\" index=\"index\"> " ,
                   "( " ,
                   "#{item.productId,jdbcType=BIGINT}, " ,
                   "#{item.memberLevelId,jdbcType=BIGINT}, " ,
                   "#{item.memberPrice,jdbcType=DECIMAL}, " ,
                   "#{item.memberLevelName,jdbcType=VARCHAR} " ,
                  ") " ,
                   "</foreach> ",
             "</script>"})

    //???????????????????????????
    int insertList(@Param("list") List<PmsMemberPrice> memberPriceList);
}