package com.hyd.order.service.impl;

import com.hyd.common.response.AppResponse;
import com.hyd.order.service.ProjectServiceFeign;
import com.hyd.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignImpl implements ProjectServiceFeign {

    @Override
    public AppResponse<List<TReturn>> getReutrnList(Integer projectId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("调用远程服务器失败[订单]");
        return null;
    }
}
