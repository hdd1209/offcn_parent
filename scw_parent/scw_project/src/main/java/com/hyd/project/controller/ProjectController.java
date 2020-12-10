package com.hyd.project.controller;

import com.hyd.common.response.AppResponse;
import com.hyd.common.utils.OSSTemplate;
import com.hyd.project.pojo.*;
import com.hyd.project.service.ProjectCreateService;
import com.hyd.project.service.ProjectInfoService;
import com.hyd.project.vo.req.resp.ProjectDetailVo;
import com.hyd.project.vo.req.resp.ProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="项目基本功能模块（文件上传、项目信息获取等）")
@RequestMapping("/project")
@RestController
public class ProjectController {

    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @Autowired
    private ProjectCreateService projectCreateService;


    @ApiOperation("文件上传功能")
    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> uploadFile(@RequestParam("file")MultipartFile[] files) throws IOException {
        Map<String,Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        if(files!=null && files.length>0){

            for (MultipartFile item : files) {
                if(!item.isEmpty()){
//                    item.getInputStream()  获取文件的文件流
//                    item.getOriginalFilename()  获取文件名
                    String upload = ossTemplate.upload(item.getInputStream(), item.getOriginalFilename());
                    list.add(upload);
                }
            }

        }
        map.put("urls",list);
        return AppResponse.ok(map);
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

    @ApiOperation("获取回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReutrnList(@PathVariable("projectId") Integer projectId){
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        return AppResponse.ok(returnList);
    }

}
