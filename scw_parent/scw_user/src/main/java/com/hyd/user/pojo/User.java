package com.hyd.user.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class User {

    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "账号")
    private Integer id;
    @ApiModelProperty(value = "年龄")
    private Integer age;
}
