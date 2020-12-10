package com.hyd.project.controller;


import com.alibaba.fastjson.JSON;
import com.hyd.common.enums.ProjectStatusEnume;
import com.hyd.common.response.AppResponse;
import com.hyd.common.vo.BaseVo;
import com.hyd.project.pojo.*;
import com.hyd.project.service.ProjectCreateService;
import com.hyd.project.vo.req.ProjectBaseInfoVo;
import com.hyd.project.vo.req.ProjectRedisStoreVo;
import com.hyd.project.vo.req.ProjectReturnVo;
import com.hyd.project.vo.req.resp.ProjectDetailVo;
import com.hyd.project.vo.req.resp.ProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "新建项目的四个步骤")
@RestController
@RequestMapping("createProject")
public class ProjectCreateController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation("项目新建的第一步:初始化项目")
    @PostMapping("/init")
    public AppResponse<String> inti(BaseVo baseVo){
        // 可以通过baseVo中的用户令牌获取用户信息
        String accessToken = baseVo.getAccessToken();
        // 从redis中获取memberId
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if(memberId==null || memberId.length()==0){
            AppResponse fail = AppResponse.fail(null);
            fail.setMsg("该用户没有登录，请登录之后再尝试");
            return fail;
        }

        // 通过数据库获取member信息
        String projectToken = projectCreateService.initCreateProject(Integer.parseInt(memberId));

        return AppResponse.ok(projectToken);

    }


    @ApiOperation("项目新建的第二步:添加项目的基本信息")
    @PostMapping("/saveBaseInfo")
    public AppResponse<String> saveBaseInfo(ProjectBaseInfoVo infoVo){
        // 从redis中获取第一步存入的对象
        String projectToken = infoVo.getProjectToken();
        // 从redis中获取的String类型的JSON值
        String redisStoreVoStr = redisTemplate.opsForValue().get(projectToken);
        // String类型的JSON值转换为对象
        ProjectRedisStoreVo redisVo = JSON.parseObject(redisStoreVoStr, ProjectRedisStoreVo.class);
        // 将参数InfoVo对象中的数据 复制到  redisVo 完成基本数据的添加
        BeanUtils.copyProperties(infoVo,redisVo);
        // 将添加完基本数据的 redisVo 写回到redis中
        redisStoreVoStr = JSON.toJSONString(redisVo);
        redisTemplate.opsForValue().set(projectToken,redisStoreVoStr);
        return AppResponse.ok(projectToken);
    }


    @ApiOperation("项目新建的第三步:添加项目的回报信息")
    @PostMapping("/saveReturn")
    public AppResponse<String> saveReturn(@RequestBody List<ProjectReturnVo> returnVoList){
        // 从参数中获取项目的临时令牌
        if(returnVoList!=null && returnVoList.size()>0){

            // 获取第一个元素的临时令牌
            String projectToken = returnVoList.get(0).getProjectToken();
            // 通过项目令牌获取项目数量
            String redisStoreVoStr = redisTemplate.opsForValue().get(projectToken);
            // 将redisStr转换为ProjectRedisStoreVo
            ProjectRedisStoreVo redisVo = JSON.parseObject(redisStoreVoStr, ProjectRedisStoreVo.class);
            // 将页面收集的数据加入到redisVo中
            // 将 参数returnVoList 中的数据  到 List<return>
            List tReturns = new ArrayList();
            for (ProjectReturnVo returnVo : returnVoList) {
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(returnVo,tReturn);
                tReturns.add(tReturn);
            }
            // 添加到redisVo
            redisVo.setProjectReturns(tReturns);
            // 将加入完成的数据写入到redis中
            redisStoreVoStr=JSON.toJSONString(redisVo);
            redisTemplate.opsForValue().set(projectToken,redisStoreVoStr);
            return AppResponse.ok(projectToken);

        }
        return AppResponse.fail("请输入参数");

    }


    @ApiOperation("项目发起第4步添加项目到mysql数据库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectToken",value = "项目标识",required = true),
            @ApiImplicitParam(name = "accessToken",value="用户令牌",required = true),
            @ApiImplicitParam(name="status",value="用户操作类型 0-保存草稿 1-提交审核",required = true)
    })
    @PostMapping("/saveToMysql")
    public AppResponse<Object> saveToMysql(String projectToken,String accessToken,String status) {
       //  1. 获取当前用户信息
        String memberId = redisTemplate.opsForValue().get(accessToken);

        if(memberId==null||memberId.length()==0){
            return AppResponse.fail("当前用户没有登录，请登录之后再尝试");
        }

        // 2.获取前三步完成的项目
        String redisVoStr = redisTemplate.opsForValue().get(projectToken);
        ProjectRedisStoreVo redisStoreVo = JSON.parseObject(redisVoStr, ProjectRedisStoreVo.class);
        // 3.判断数据非空
        if(redisStoreVo!=null){

            if(status.equalsIgnoreCase("1")){
                // 获取添加的操作
                projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH,redisStoreVo);
                return AppResponse.ok("添加成功");
            }
        }

        return AppResponse.fail(null);
    }

    @ApiOperation("获取系统所有项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> findAllProject(){
        // 1、创建集合存储全部项目的Vo
        List<ProjectVo> pros = new ArrayList<>();

        // 2、查询全部项目
        List<TProject> allProject = projectCreateService.findAllProject();
        // 3]遍历项目集合
        for (TProject tProject : allProject) {
            // 获取项目编号
            Integer id = tProject.getId();
            // 根据项目编号获取项目配图
            List<TProjectImages> projectImages = projectCreateService.getProjectImages(id);
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(tProject,projectVo);
            // 遍历项目配图集合
            for (TProjectImages projectImage : projectImages) {
                // 如果图片类型头部图片,则设置图片路径到VO
                if(projectImage.getImgtype()==0){
                    projectVo.setHeaderImage(projectImage.getImgurl());
                }
            }
            // 把项目Vo添加到项目集合
            pros.add(projectVo);
        }
        return AppResponse.ok(pros);
    }

    @ApiOperation("获取项目信息详情")
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId){
        TProject projectInfo = projectCreateService.findProjectInfo(projectId);

        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        // 1、 查出这个项目的所有图片
        List<TProjectImages> projectImages = projectCreateService.getProjectImages(projectInfo.getId());
        List<String> detailsImage = projectDetailVo.getDetailsImage();
        if(detailsImage==null){
            detailsImage=new ArrayList<>();
        }
        for (TProjectImages projectImage : projectImages) {
            if(projectImage.getImgtype()==0){
                projectDetailVo.setHeaderImage(projectImage.getImgurl());
            }else{
                detailsImage.add(projectImage.getImgurl());
            }
        }
        projectDetailVo.setDetailsImage(detailsImage);
        // 2、 项目所有支持回报
        List<TReturn> projectReturns = projectCreateService.getProjectReturns(projectInfo.getId());
        projectDetailVo.setProjectReturns(projectReturns);
        BeanUtils.copyProperties(projectInfo,projectDetailVo);
        return AppResponse.ok(projectDetailVo);
    }

    @ApiOperation("获取系统所有的项目标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag(){
        List<TTag> allTags = projectCreateService.findAllTags();
        AppResponse<List<TTag>> ok = AppResponse.ok(allTags);
        return ok;
    }

    @ApiOperation("获取系统所有项目分类")
    @GetMapping("findAllType")
    public AppResponse<List<TType>> findAllType(){
        List<TType> allTypes = projectCreateService.findAllTypes();
        return AppResponse.ok(allTypes);
    }

    @ApiOperation("获取回报信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> findReturnInfo(@PathVariable("returnId") Integer returnId){
        TReturn returnInfo = projectCreateService.findReturnInfo(returnId);
        return AppResponse.ok(returnInfo);
    }
}
