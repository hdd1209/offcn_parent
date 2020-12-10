package com.hyd.project.vo.req;

import com.hyd.common.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ProjectBaseInfoVo extends BaseVo {
    @ApiModelProperty(value = "项目的临时令牌")
    private String projectToken;  //项目的临时token
    @ApiModelProperty(value = "项目名称")
    private String name;  //项目名称
    @ApiModelProperty(value = "项目简介")
    private String remark; //项目简介
    @ApiModelProperty(value = "筹措的金额")
    private Long money;   //筹措的金额
    @ApiModelProperty(value = "天数")
    private Integer day;  // 天数

    @ApiModelProperty(value = "创建的时间")
    private String createdate;  //创建的时间


    // 头部图片 一张
    @ApiModelProperty(value = "头图片")
    private String headerImage;
    // 详情图片 多张
    @ApiModelProperty(value = "详情图片")
    private List<String> detailImages;
    // 标签 多个
    @ApiModelProperty(value = "标签(可多选)")
    private List<Integer> tagIds;
    // 类型 多个
    @ApiModelProperty(value = "标签(可多选)")
    private List<Integer> typeIds;


}
