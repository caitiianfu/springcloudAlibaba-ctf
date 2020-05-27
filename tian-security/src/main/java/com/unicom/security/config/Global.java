package com.unicom.security.config;

import com.unicom.common.api.ResultCode;
import com.unicom.common.api.ResultUtils;
import com.unicom.common.global.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class Global extends GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResultUtils handle(UsernameNotFoundException e){
        log.error(e.getMessage(),e);
        return  ResultUtils.unauthorized(e.getMessage());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResultUtils handle(BadCredentialsException e){
        log.error(e.getMessage(),e);
        return  ResultUtils.unauthorized(e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public ResultUtils handle(NullPointerException e){
        log.error(e.getMessage(),e);
        return  ResultUtils.failed(ResultCode.NULL_POINT);
    }

    /**
     * 这里对其他异常进行处理
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultUtils handle(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(),e);


        //return ResultUtils.failed(ResultCode.SERVER_ERROR);
        return ResultUtils.failedMsg(e.getMessage());



    }
}
