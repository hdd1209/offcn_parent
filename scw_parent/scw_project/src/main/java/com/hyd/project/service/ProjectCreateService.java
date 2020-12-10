package com.hyd.project.service;

import com.hyd.common.enums.ProjectStatusEnume;
import com.hyd.project.pojo.*;
import com.hyd.project.vo.req.ProjectRedisStoreVo;
import io.swagger.models.auth.In;

import java.util.List;

public interface ProjectCreateService {

    // 1 初始化项目
    public String initCreateProject(Integer memberId);

    // 保存项目
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStoreVo redisStoreVo);

    /**
     *  获取系统中所有项目
     * @return
     */
    List<TProject> findAllProject();

    /**
     * 获取项目图片
     * @param id
     * @return
     */
    List<TProjectImages> getProjectImages(Integer id);

    /**
     * 获取项目信息
     * @param projectId
     * @return
     */
    TProject findProjectInfo(Integer projectId);


    /**
     * 获取回报信息
     * @param id
     * @return
     */
    List<TReturn> getProjectReturns(Integer id);

    /**
     * 获取项目标签信息
     * @return
     */
    List<TTag> findAllTags();

    /**
     * 获取所有项目分类
     * @return
     */
    List<TType> findAllTypes();

    /**
     * 获取回报信息
     * @param returnId
     * @return
     */
    TReturn findReturnInfo(Integer returnId);

    /**
     * 获取项目回报列表
     * @param projectId
     * @return
     */
    List<TReturn> getReturnList(Integer projectId);


}
