package com.hyd.user.controller;


import com.hyd.common.response.AppResponse;
import com.hyd.user.pojo.TMember;
import com.hyd.user.service.UserService;
import com.hyd.user.utils.SmsTemplate;
import com.hyd.user.vo.UserResisVo;
import com.hyd.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "用户注册和登录")
public class UserLoginController {

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据用户编号，获取用户基本信息")
    @GetMapping("findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember member = userService.findTmemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(member,userRespVo);
        return AppResponse.ok(userRespVo);
    }

    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })//@ApiImplicitParams：描述所有参数；@ApiImplicitParam描述某个参数
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username,String password){
        //1、尝试登录
        TMember member = userService.login(username, password);
        if (member==null){
            //登录失败
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名或者密码错误");
            return fail;
        }
        //2、登录成功，生成令牌
        String token = UUID.randomUUID().toString();
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member,vo);
        vo.setAccessToken(token);
        // 3、 根据令牌查询用户信息
        stringRedisTemplate.opsForValue().set(token,member.getId()+"",2,TimeUnit.HOURS);
        return AppResponse.ok(vo);
    }


    @ApiOperation(value = "获取验证码信息")
    @PostMapping("/sendSms")
    public AppResponse<Object> sendSms(String phoneNum){

        //生成验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        System.out.println("当前验证码是:"+code);
        // 2.将验证码存储在redis中 5分钟有效
        stringRedisTemplate.opsForValue().set(phoneNum,code,5, TimeUnit.MINUTES);
        //3,短信发送
        try {
//            String okMsg = smsTemplate.sendCode(phoneNum, code);
            String okMsg = "ok";
            return AppResponse.ok(okMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail("短信发送失败");
        }

    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/regist")
    public AppResponse<Object> regist(UserResisVo userResisVo){

        //1. 获取验证码
        // redis中存储的验证码
        String code = stringRedisTemplate.opsForValue().get(userResisVo.getLoginacct());
        if(code!=null&&code.length()>0){
            boolean flag = code.equalsIgnoreCase(userResisVo.getCode()); //用户填入的验证码

            if(flag){
                // 完成注册
                TMember tMember = new TMember();
//                tMember.setLoginacct(userResisVo.getLoginacct());
//                tMember.setUserpswd(userResisVo.getUserpswd());
//                tMember.setEmail(userResisVo.getEmail());
                // 该方法要求属性名必须一致,负责不一样的属性赋值为空
                BeanUtils.copyProperties(userResisVo,tMember);
                userService.registerUser(tMember);
                //删除验证码
                stringRedisTemplate.delete(tMember.getLoginacct());
                return AppResponse.ok("注册成功");

            }  else{
                //用户输入的验证码和存储的不一致
                return AppResponse.fail("验证码错误");
            }
        }else{
            return AppResponse.fail("当前验证码失效");
        }

    }


}
