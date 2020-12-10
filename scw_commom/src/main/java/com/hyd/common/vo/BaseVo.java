package com.hyd.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BaseVo {
    @ApiModelProperty("用户令牌")
    private String  accessToken;   //用户令牌
}
