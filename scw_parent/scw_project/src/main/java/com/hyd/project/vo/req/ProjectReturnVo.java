package com.hyd.project.vo.req;

import com.hyd.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ProjectReturnVo extends BaseVo {

    @ApiModelProperty(value = "项目的临时令牌")
    private String projectToken;  //项目的临时token
    @ApiModelProperty(value = "0 - 实物回报,1虚拟物品回报")
    private String type;
    @ApiModelProperty(value = "支持金额",example = "0")
    private Integer supportmoney;
    @ApiModelProperty(value = "回报内容")
    private String content;
    @ApiModelProperty(value = "回报数量 0 为不限回报数量")
    private Integer count;
    @ApiModelProperty(value = "单笔限购")
    private Integer signalpurchase;
    @ApiModelProperty(value = "限购数量")
    private Integer purchase;
    @ApiModelProperty(value = "运费")
    private Integer freight;
    @ApiModelProperty(value = "0-不开发票，1-开发票")
    private String invoice;
    @ApiModelProperty(value = "回报时间,众筹成功后多少天进行回报")
    private Integer rtndate;
}
