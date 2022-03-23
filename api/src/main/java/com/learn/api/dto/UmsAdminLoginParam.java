package com.learn.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author weikun
 * @date 2021/8/1 18:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UmsAdminLoginParam implements Serializable {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
