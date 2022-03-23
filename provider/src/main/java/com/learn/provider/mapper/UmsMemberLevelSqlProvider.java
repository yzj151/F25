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

import com.learn.api.model.UmsMemberLevel;
import com.learn.api.model.UmsMemberLevelExample.Criteria;
import com.learn.api.model.UmsMemberLevelExample.Criterion;
import com.learn.api.model.UmsMemberLevelExample;
import java.util.List;
import java.util.Map;

public class UmsMemberLevelSqlProvider {

    public String countByExample(UmsMemberLevelExample example) {
        BEGIN();
        SELECT("count(*)");
        FROM("ums_member_level");
        applyWhere(example, false);
        return SQL();
    }

    public String deleteByExample(UmsMemberLevelExample example) {
        BEGIN();
        DELETE_FROM("ums_member_level");
        applyWhere(example, false);
        return SQL();
    }

    public String insertSelective(UmsMemberLevel record) {
        BEGIN();
        INSERT_INTO("ums_member_level");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getGrowthPoint() != null) {
            VALUES("growth_point", "#{growthPoint,jdbcType=INTEGER}");
        }
        
        if (record.getDefaultStatus() != null) {
            VALUES("default_status", "#{defaultStatus,jdbcType=INTEGER}");
        }
        
        if (record.getFreeFreightPoint() != null) {
            VALUES("free_freight_point", "#{freeFreightPoint,jdbcType=DECIMAL}");
        }
        
        if (record.getCommentGrowthPoint() != null) {
            VALUES("comment_growth_point", "#{commentGrowthPoint,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeFreeFreight() != null) {
            VALUES("priviledge_free_freight", "#{priviledgeFreeFreight,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeSignIn() != null) {
            VALUES("priviledge_sign_in", "#{priviledgeSignIn,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeComment() != null) {
            VALUES("priviledge_comment", "#{priviledgeComment,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgePromotion() != null) {
            VALUES("priviledge_promotion", "#{priviledgePromotion,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeMemberPrice() != null) {
            VALUES("priviledge_member_price", "#{priviledgeMemberPrice,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeBirthday() != null) {
            VALUES("priviledge_birthday", "#{priviledgeBirthday,jdbcType=INTEGER}");
        }
        
        if (record.getNote() != null) {
            VALUES("note", "#{note,jdbcType=VARCHAR}");
        }
        
        return SQL();
    }

    public String selectByExample(UmsMemberLevelExample example) {
        BEGIN();
        if (example != null && example.isDistinct()) {
            SELECT_DISTINCT("id");
        } else {
            SELECT("id");
        }
        SELECT("name");
        SELECT("growth_point");
        SELECT("default_status");
        SELECT("free_freight_point");
        SELECT("comment_growth_point");
        SELECT("priviledge_free_freight");
        SELECT("priviledge_sign_in");
        SELECT("priviledge_comment");
        SELECT("priviledge_promotion");
        SELECT("priviledge_member_price");
        SELECT("priviledge_birthday");
        SELECT("note");
        FROM("ums_member_level");
        applyWhere(example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            ORDER_BY(example.getOrderByClause());
        }
        
        return SQL();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        UmsMemberLevel record = (UmsMemberLevel) parameter.get("record");
        UmsMemberLevelExample example = (UmsMemberLevelExample) parameter.get("example");
        
        BEGIN();
        UPDATE("ums_member_level");
        
        if (record.getId() != null) {
            SET("id = #{record.id,jdbcType=BIGINT}");
        }
        
        if (record.getName() != null) {
            SET("name = #{record.name,jdbcType=VARCHAR}");
        }
        
        if (record.getGrowthPoint() != null) {
            SET("growth_point = #{record.growthPoint,jdbcType=INTEGER}");
        }
        
        if (record.getDefaultStatus() != null) {
            SET("default_status = #{record.defaultStatus,jdbcType=INTEGER}");
        }
        
        if (record.getFreeFreightPoint() != null) {
            SET("free_freight_point = #{record.freeFreightPoint,jdbcType=DECIMAL}");
        }
        
        if (record.getCommentGrowthPoint() != null) {
            SET("comment_growth_point = #{record.commentGrowthPoint,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeFreeFreight() != null) {
            SET("priviledge_free_freight = #{record.priviledgeFreeFreight,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeSignIn() != null) {
            SET("priviledge_sign_in = #{record.priviledgeSignIn,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeComment() != null) {
            SET("priviledge_comment = #{record.priviledgeComment,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgePromotion() != null) {
            SET("priviledge_promotion = #{record.priviledgePromotion,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeMemberPrice() != null) {
            SET("priviledge_member_price = #{record.priviledgeMemberPrice,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeBirthday() != null) {
            SET("priviledge_birthday = #{record.priviledgeBirthday,jdbcType=INTEGER}");
        }
        
        if (record.getNote() != null) {
            SET("note = #{record.note,jdbcType=VARCHAR}");
        }
        
        applyWhere(example, true);
        return SQL();
    }

    public String updateByExample(Map<String, Object> parameter) {
        BEGIN();
        UPDATE("ums_member_level");
        
        SET("id = #{record.id,jdbcType=BIGINT}");
        SET("name = #{record.name,jdbcType=VARCHAR}");
        SET("growth_point = #{record.growthPoint,jdbcType=INTEGER}");
        SET("default_status = #{record.defaultStatus,jdbcType=INTEGER}");
        SET("free_freight_point = #{record.freeFreightPoint,jdbcType=DECIMAL}");
        SET("comment_growth_point = #{record.commentGrowthPoint,jdbcType=INTEGER}");
        SET("priviledge_free_freight = #{record.priviledgeFreeFreight,jdbcType=INTEGER}");
        SET("priviledge_sign_in = #{record.priviledgeSignIn,jdbcType=INTEGER}");
        SET("priviledge_comment = #{record.priviledgeComment,jdbcType=INTEGER}");
        SET("priviledge_promotion = #{record.priviledgePromotion,jdbcType=INTEGER}");
        SET("priviledge_member_price = #{record.priviledgeMemberPrice,jdbcType=INTEGER}");
        SET("priviledge_birthday = #{record.priviledgeBirthday,jdbcType=INTEGER}");
        SET("note = #{record.note,jdbcType=VARCHAR}");
        
        UmsMemberLevelExample example = (UmsMemberLevelExample) parameter.get("example");
        applyWhere(example, true);
        return SQL();
    }

    public String updateByPrimaryKeySelective(UmsMemberLevel record) {
        BEGIN();
        UPDATE("ums_member_level");
        
        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getGrowthPoint() != null) {
            SET("growth_point = #{growthPoint,jdbcType=INTEGER}");
        }
        
        if (record.getDefaultStatus() != null) {
            SET("default_status = #{defaultStatus,jdbcType=INTEGER}");
        }
        
        if (record.getFreeFreightPoint() != null) {
            SET("free_freight_point = #{freeFreightPoint,jdbcType=DECIMAL}");
        }
        
        if (record.getCommentGrowthPoint() != null) {
            SET("comment_growth_point = #{commentGrowthPoint,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeFreeFreight() != null) {
            SET("priviledge_free_freight = #{priviledgeFreeFreight,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeSignIn() != null) {
            SET("priviledge_sign_in = #{priviledgeSignIn,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeComment() != null) {
            SET("priviledge_comment = #{priviledgeComment,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgePromotion() != null) {
            SET("priviledge_promotion = #{priviledgePromotion,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeMemberPrice() != null) {
            SET("priviledge_member_price = #{priviledgeMemberPrice,jdbcType=INTEGER}");
        }
        
        if (record.getPriviledgeBirthday() != null) {
            SET("priviledge_birthday = #{priviledgeBirthday,jdbcType=INTEGER}");
        }
        
        if (record.getNote() != null) {
            SET("note = #{note,jdbcType=VARCHAR}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }

    protected void applyWhere(UmsMemberLevelExample example, boolean includeExamplePhrase) {
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