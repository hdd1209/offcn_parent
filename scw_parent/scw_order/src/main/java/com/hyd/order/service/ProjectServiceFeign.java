package com.hyd.order.service;

import com.hyd.common.response.AppResponse;
import com.hyd.order.service.impl.ProjectServiceFeignImpl;
import com.hyd.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT",fallback = ProjectServiceFeignImpl.class)
public interface ProjectServiceFeign {
    @GetMapping("/project/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReutrnList(@PathVariable("projectId") Integer projectId);
}
