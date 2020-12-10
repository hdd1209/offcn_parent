package com.hyd.user.controller;

import com.hyd.user.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;

@RestController
@Api(tags = "用户模块的请求类")
public class UserController {


    @ApiOperation(value = "swagger的测试方法")
    @GetMapping("/swagger")
    public String helloSwagger2(){
        return "这个是swagger2中方法";
    }

    @PostMapping("/user")
    @ApiOperation(value = "用户添加的方法")
    public String saveUser(User user){
        return user.toString();
    }

    @PutMapping("/user/{id}")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "id",value = "主键的id",required = true)
            }
    )
    public String updateUser(@PathVariable Integer id,User user){
        return "修改了id为"+id+"的用户信息,修改之后的内容:"+user.toString();
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Integer id){
        return "删除了id为:"+id+"的用户信息";
    }
}
