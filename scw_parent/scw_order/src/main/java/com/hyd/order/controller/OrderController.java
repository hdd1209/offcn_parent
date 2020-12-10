package com.hyd.order.controller;

import com.hyd.common.response.AppResponse;
import com.hyd.order.pojo.TOrder;
import com.hyd.order.service.OrderService;
import com.hyd.order.vo.req.OrderInfoSubmitVo;
import com.sun.corba.se.impl.oa.toa.TOA;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("createOrder")
    @ApiOperation("保存订单")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoSubmitVo orderInfoSubmitVo){

        String memberId = redisTemplate.opsForValue().get(orderInfoSubmitVo.getAccessToken());
        if(memberId==null){
            AppResponse fail = new AppResponse();
            fail.setMsg("无此权限，请先登录");
            return fail;
        }

        try {
            TOrder order = orderService.saveOrder(orderInfoSubmitVo);
            return AppResponse.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            AppResponse fail = new AppResponse();
            fail.setMsg("没有查询到相关消息！");
            return fail;
        }

    }

}
