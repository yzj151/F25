package com.learn.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor //使用后添加一个构造函数
@NoArgsConstructor //无参构造
public class UmsLog implements Serializable {
    private Long id;

    private String createTime;

    private String updateTime;

    private Integer costTime;

    private String ip;

    private String name;

    private String requestParam;

    private String requestType;

    private String requestUrl;

    private Integer userid;

    private Integer logType;




}