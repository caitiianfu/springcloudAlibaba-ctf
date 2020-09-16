package com.unicom.common.global;

import com.unicom.common.api.ResultCode;
import com.unicom.common.api.ResultEnum;
import com.unicom.common.api.ResultUtils;
import com.unicom.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
      @ExceptionHandler(value = ApiException.class)
      public ResultUtils handle(ApiException e){
          log.error(e.getMessage(),e);
          if (e.getResultEnum()!=null){
              return  ResultUtils.failed(e.getResultEnum());
          }
          return  ResultUtils.failedMsg(e.getMessage());
      }
    //方案1   通过exception来获取异常，代码量较多
   /* @ExceptionHandler(Exception.class)
    public ResultUtils handle(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> list = ex.getAllErrors();
            ObjectError oError = list.get(0);
            String msg = oError.getDefaultMessage();
            return ResultUtils.validateFailed(msg);
        } else if (e instanceof MethodArgumentNotValidException) {
            BindingResult ex = ((MethodArgumentNotValidException) e).getBindingResult();
            List<ObjectError> list = ex.getAllErrors();
            ObjectError oError = list.get(0);
            String msg = oError.getDefaultMessage();
            return ResultUtils.validateFailed(msg);
        } else if (e instanceof ApiException) {
            if (((ApiException) e).getResultEnum() != null) {
                return ResultUtils.failed(((ApiException) e).getResultEnum());
            } else {

                return ResultUtils.failedMsg(e.getMessage());
            }
        }else {
            //return ResultUtils.failed(ResultCode.SERVER_ERROR);
            return ResultUtils.failedMsg(e.getMessage());

        }

    }*/

   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultUtils handle(MethodArgumentNotValidException e) {
       log.error(e.getMessage(),e);
       BindingResult ex = e.getBindingResult();
        List<ObjectError> list = ex.getAllErrors();
        ObjectError oError = list.get(0);
        String msg = oError.getDefaultMessage();
        return ResultUtils.validateFailed(msg);
    }

    @ExceptionHandler(BindException.class)
    public ResultUtils handle(BindException e) {
        log.error(e.getMessage(),e);
        BindException ex = (BindException) e;
        List<ObjectError> list = ex.getAllErrors();
        ObjectError oError = list.get(0);
        String msg = oError.getDefaultMessage();
        return ResultUtils.validateFailed(msg);

    }


  /*  @ExceptionHandler(Exception.class)
    public ResultUtils handle(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(),e);


        //return ResultUtils.failed(ResultCode.SERVER_ERROR);
            return ResultUtils.failedMsg(e.getMessage());



    }*/


    }