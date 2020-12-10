package com.hyd.project.service.impl;

import com.hyd.project.mapper.TReturnMapper;
import com.hyd.project.pojo.TReturn;
import com.hyd.project.pojo.TReturnExample;
import com.hyd.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private TReturnMapper returnMapper;

    //获取项目回报列表
    @Override
    public List<TReturn> getReturnList(Integer projectId) {
        TReturnExample example = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(example);
    }
}
