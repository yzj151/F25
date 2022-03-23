package com.learn.api.service;

import com.learn.api.model.OmsOrderReturnReason;
import com.learn.common.CommonPage;

import java.util.List;

public interface IOmsOrderReturnReasonService {
    //添加退货原因
    OmsOrderReturnReason create(OmsOrderReturnReason returnReason);
    //分页获取退货原因
    CommonPage list(Integer pageSize,Integer pageNum);

    //批量删除退货原因
    int delete(List<Long> ids);

    //修改退货原因
    OmsOrderReturnReason update(Long id,OmsOrderReturnReason returnReason);
    //获取当个退货原因详情信息
    OmsOrderReturnReason getOmsOrderReturnReason(Long id);
    //修改状态
    OmsOrderReturnReason updateStatus(Long id,Integer status);
}