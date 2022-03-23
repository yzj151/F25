package com.learn.api.service;

import com.learn.api.model.OmsOrderSetting;

/**
 * 订单设置
 */

public interface IOmsOrderSettingService {
    OmsOrderSetting getItem(Long id);
    OmsOrderSetting update(Long id,OmsOrderSetting omsOrderSetting);

}
