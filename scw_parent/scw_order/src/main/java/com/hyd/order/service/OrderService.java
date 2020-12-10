package com.hyd.order.service;

import com.hyd.order.pojo.TOrder;
import com.hyd.order.vo.req.OrderInfoSubmitVo;

public interface OrderService {

    /**
     * 保存订单的方法
     * @param orderInfoSubmitVo
     * @return
     */
    TOrder saveOrder(OrderInfoSubmitVo orderInfoSubmitVo);


}
