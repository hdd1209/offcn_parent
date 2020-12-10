package com.hyd.user.service;


import com.hyd.user.pojo.TMember;
import com.hyd.user.pojo.TMemberAddress;

import java.util.List;

public interface UserService {

    public void registerUser(TMember tMember);


    public TMember login(String username,String password);

    //根据用户id，获取用户信息
    public TMember findTmemberById(Integer id);

    /**
     * 获取用户收货地址
     * @param memberId
     * @return
     */
    List<TMemberAddress> findAddressList(Integer memberId);

}
