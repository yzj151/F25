package com.learn.api.service;

import com.learn.api.dto.OmsReturnApplyQueryParam;
import com.learn.api.dto.OmsUpdateStatusParam;
import com.learn.api.model.OmsOrderReturnApply;
import com.learn.common.CommonPage;

import java.util.List;

/**
 * 申请退货原因
 */

public interface IOmsOrderReturnApplyService {
    CommonPage list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

    OmsOrderReturnApply getItem(Long id);

    int delete(List<Long> ids);

    OmsOrderReturnApply updateStatus(Long id, OmsUpdateStatusParam statusParam);
}
