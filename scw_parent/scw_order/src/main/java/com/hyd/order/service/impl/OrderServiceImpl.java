package com.hyd.order.service.impl;

import com.hyd.common.enums.OrderStatusEnumes;
import com.hyd.common.response.AppResponse;
import com.hyd.common.utils.AppDateUtils;
import com.hyd.order.mapper.TOrderMapper;
import com.hyd.order.pojo.TOrder;
import com.hyd.order.service.OrderService;
import com.hyd.order.service.ProjectServiceFeign;
import com.hyd.order.vo.req.OrderInfoSubmitVo;
import com.hyd.order.vo.resp.TReturn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo orderInfoSubmitVo) {
        // 1、创建订单对象
        TOrder order = new TOrder();
        String accessToken = orderInfoSubmitVo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);//得到会员id
        order.setMemberid(Integer.parseInt(memberId));
        // 2、 复制对象  orderInfoSubmitVo -> order
        BeanUtils.copyProperties(orderInfoSubmitVo,order);

        String orderNum = UUID.randomUUID().toString().replace("-", ""); //产生订单编号
        order.setOrdernum(orderNum);
        order.setStatus(OrderStatusEnumes.UNPAY.getCode()+""); // 未支付
        order.setInvoice(orderInfoSubmitVo.getInvoice().toString());// 发票状态
        order.setCreatedate(AppDateUtils.getFormatTime()); // 创建时间

        // 3. 服务远程调用 查询回报增量列表
        AppResponse<List<TReturn>> response = projectServiceFeign.getReutrnList(orderInfoSubmitVo.getProjectid());

        TReturn tReturn = new TReturn();

        List<TReturn> returnList = response.getData();
        for (TReturn TTreturn : returnList) {
            if(orderInfoSubmitVo.getReturnid().equals(TTreturn.getId())){
                tReturn=TTreturn;
            }
        }

        // 默认取得第一个回报增量信息

        // 支持金额 回报数量*回报支持金额+运费
        Integer money = order.getRtncount()*tReturn.getSupportmoney()+tReturn.getFreight();
        order.setMoney(money);
        // 4.执行保存操作
        orderMapper.insertSelective(order);
        return order;
    }
}
