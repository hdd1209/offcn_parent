package com.hyd.project.service;

import com.hyd.project.pojo.TReturn;

import java.util.List;

public interface ProjectInfoService {

    /**
     * 获取项目回报列表
     * @param projectId
     * @return
     */
    List<TReturn> getReturnList(Integer projectId);


}
