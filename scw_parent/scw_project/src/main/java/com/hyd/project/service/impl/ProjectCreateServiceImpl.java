package com.hyd.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.hyd.common.enums.ProjectStatusEnume;
import com.hyd.project.enums.ProjectImageTypeEnume;
import com.hyd.project.mapper.*;
import com.hyd.project.pojo.*;
import com.hyd.project.service.ProjectCreateService;
import com.hyd.project.vo.req.ProjectRedisStoreVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private TProjectMapper projectMapper;

    @Autowired(required = false)
    private TProjectTagMapper projectTagMapper;

    @Autowired(required = false)
    private TProjectTypeMapper projectTypeMapper;

    @Autowired(required = false)
    private TProjectImagesMapper projectImagesMapper;

    @Autowired(required = false)
    private TReturnMapper returnMapper;

    @Autowired(required = false)
    private TTagMapper tagMapper ;

    @Autowired(required = false)
    private TTypeMapper typeMapper ;

    // 保存项目
    @Override
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStoreVo redisStoreVo) {

        TProject tProject = new TProject();
        BeanUtils.copyProperties(redisStoreVo,tProject);
        tProject.setStatus(status.getCode()+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tProject.setCreatedate(simpleDateFormat.format(new Date()));
        // 插入数据到mysql中
        projectMapper.insert(tProject);
        // 获取刚刚插入数据的id
        Integer projectId = tProject.getId();

        // 插入头图片

        String headerImage = redisStoreVo.getHeaderImage();
        if(headerImage!=null){
            TProjectImages tProjectImages = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.HEADER.getCode());
            projectImagesMapper.insert(tProjectImages);
        }

        // 详情图片
        List<String> detailImages = redisStoreVo.getDetailImages();
        if(detailImages!=null && detailImages.size()>0){
            List<String> detailImageList = new ArrayList<>();
            for (String detailImage : detailImages) {
                TProjectImages detailImageObj = new TProjectImages(null, projectId, detailImage, ProjectImageTypeEnume.DETAILS.getCode());
                projectImagesMapper.insert(detailImageObj);
            }
        }

        // 标签信息
        List<Integer> tagIds = redisStoreVo.getTagIds();
        if(tagIds!=null && tagIds.size()>0){
            for (Integer tagId : tagIds) {
                TProjectTag tProjectTag = new TProjectTag(null, projectId, tagId);
                projectTagMapper.insert(tProjectTag);
            }
        }

        // 分类信息
        List<Integer> typeIds = redisStoreVo.getTypeIds();
        if(typeIds!=null && typeIds.size()>0){
            for (Integer typeId : typeIds) {
                TProjectType tProjectType = new TProjectType(null, projectId, typeId);
                projectTypeMapper.insert(tProjectType);

            }
        }

        //回报数据
        List<TReturn> projectReturns = redisStoreVo.getProjectReturns();
        if(projectReturns!=null && projectReturns.size()>0){
            for (TReturn projectReturn : projectReturns) {
                projectReturn.setProjectid(projectId);
                returnMapper.insert(projectReturn);
            }
        }

        // 清空redis
//        redisTemplate.delete(redisStoreVo.getProjectToken());

    }

    @Override
    public String initCreateProject(Integer memberId) {
        // 为项目创建一个 临时令牌 token,方便后续进行存取的操作
        String projectToken = UUID.randomUUID().toString().replace("-", "")+"_Project";
        // 创建一个 ProjectRedisStoreVo 的空对象
        ProjectRedisStoreVo redisStoreVo = new ProjectRedisStoreVo();
        // 将memberId 加入到初始化项目中
        redisStoreVo.setMemberid(memberId);
        // 将临时token引入到redis中
        String redisStoreVoStr = JSON.toJSONString(redisStoreVo);
        redisTemplate.opsForValue().set(projectToken,redisStoreVoStr);

        return projectToken;
    }

    //获取系统中所有项目
    @Override
    public List<TProject> findAllProject() {
        return projectMapper.selectByExample(null);
    }


    //获取项目图片
    @Override
    public List<TProjectImages> getProjectImages(Integer id) {
        TProjectImagesExample example = new TProjectImagesExample();
        example.createCriteria().andProjectidEqualTo(id);
        return projectImagesMapper.selectByExample(example);
    }

    // 获取项目信息
    @Override
    public TProject findProjectInfo(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    // 获取回报信息

    @Override
    public List<TReturn> getProjectReturns(Integer id) {
        TReturnExample example = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(id);
        return returnMapper.selectByExample(example);
    }

    //获取项目标签信息
    @Override
    public List<TTag> findAllTags() {
        return tagMapper.selectByExample(null);
    }

    //获取所有项目分类
    @Override
    public List<TType> findAllTypes() {
        return typeMapper.selectByExample(null);
    }

    //获取回报信息
    @Override
    public TReturn findReturnInfo(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }

    //获取项目回报列表
    @Override
    public List<TReturn> getReturnList(Integer projectId) {
        TReturnExample example = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(example);
    }

}
