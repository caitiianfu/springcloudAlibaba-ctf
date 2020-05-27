package com.unicom.common.exception;

import com.unicom.common.api.ResultEnum;

public class ApiException extends  RuntimeException{
    private ResultEnum resultEnum;
    public  ApiException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.resultEnum=resultEnum;
    }
    public ApiException(Throwable cause){ super(cause.getMessage()); }
    public ApiException(String msg){ super(msg);
    }
    public ApiException(String msg,Throwable cause){super(msg,cause);}
    public ResultEnum getResultEnum(){
        return  resultEnum;
    }
}
