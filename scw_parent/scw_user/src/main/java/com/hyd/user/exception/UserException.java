package com.hyd.user.exception;

import com.hyd.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException{

    public UserException(UserExceptionEnum userEnum){
        super(userEnum.getMsg());
    }
}
