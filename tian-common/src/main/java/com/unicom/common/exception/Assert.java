package com.unicom.common.exception;

import com.unicom.common.api.ResultEnum;

/**
 * 断言处理类用于处理各种异常
 */
public class Assert {
        public static void fail(String msg){throw  new ApiException(msg);};
        public static void fail(ResultEnum resultEnum){throw  new ApiException(resultEnum);};

}
