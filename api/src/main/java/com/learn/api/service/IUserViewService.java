package com.learn.api.service;

import com.learn.common.CommonPage;

/**
 * 主页
 */

public interface IUserViewService {

    CommonPage listUV(String start, String end, String type) ;

    CommonPage listTypeUV() throws Exception;
}