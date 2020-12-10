package com.hyd.user.controller;

import com.hyd.common.response.AppResponse;
import com.hyd.user.pojo.TMemberAddress;
import com.hyd.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "获取会员信息/跟新个人信息/获取用户收货地址")
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户收货地址")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(value = "访问令牌", name = "accessToken", required = true)
    })
    @GetMapping("/findAddressList")
    public AppResponse<List<TMemberAddress>> findAddressList(String accessToken){
        // 1.得到会员ID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(memberId)){
            AppResponse fail = new AppResponse();
            fail.setMsg("用户没有登录，请重新登录");
            return fail;
        }
        // 2.查询会员收货地址列表
        List<TMemberAddress> addressList = userService.findAddressList(Integer.parseInt(memberId));
        return AppResponse.ok(addressList);
    }
}
