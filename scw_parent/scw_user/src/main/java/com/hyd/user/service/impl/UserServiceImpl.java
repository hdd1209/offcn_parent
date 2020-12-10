package com.hyd.user.service.impl;

import com.hyd.user.enums.UserExceptionEnum;
import com.hyd.user.exception.UserException;
import com.hyd.user.mapper.TMemberAddressMapper;
import com.hyd.user.mapper.TMemberMapper;
import com.hyd.user.pojo.TMember;
import com.hyd.user.pojo.TMemberAddress;
import com.hyd.user.pojo.TMemberAddressExample;
import com.hyd.user.pojo.TMemberExample;
import com.hyd.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    private TMemberAddressMapper addressMapper;

    // 获取用户收货地址
    @Override
    public List<TMemberAddress> findAddressList(Integer memberId) {
        TMemberAddressExample example = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = example.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        return addressMapper.selectByExample(example);
    }

    //根据用户id，获取用户信息
    public TMember findTmemberById(Integer id){

        return memberMapper.selectByPrimaryKey(id);
    }

    //登录方法
    @Override
    public TMember login(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TMember> tMembers = memberMapper.selectByExample(example);
        if(tMembers!=null && tMembers.size()==1){
            TMember tMember = tMembers.get(0);

            boolean matches = encoder.matches(password, tMember.getUserpswd());
            return matches?tMember:null;
        }
        return null;
    }

    @Override
    public void registerUser(TMember tMember) {
        // 1. 验证手机号是否在数据表中存在
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(tMember.getLoginacct());
        List<TMember> tMembers = memberMapper.selectByExample(example);

        if(tMembers.size()>0){
            // 当前用户已存在
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        // 2.手机没有被注册
        // 2.1 密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        tMember.setUserpswd(encoder.encode(tMember.getUserpswd()));
        tMember.setUsername(tMember.getLoginacct());
        //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        tMember.setAuthstatus("0");
        //用户类型: 0 - 个人， 1 - 企业
        tMember.setUsertype("0");
        //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
        tMember.setAccttype("2");
        memberMapper.insert(tMember);

    }
}
