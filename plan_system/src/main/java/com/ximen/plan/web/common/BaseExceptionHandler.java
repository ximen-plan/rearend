package com.ximen.plan.web.common;

import com.ximen.plan.dto.responsedto.ResultDTO;
import com.ximen.plan.exception.ApiException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResultDTO errorHandle(Throwable ee) {
        Exception e = (Exception) ee;
        ResultDTO result = new ResultDTO();
        if (e instanceof ApiException) {
            ApiException ae = (ApiException) e;
            result.setFlag(false);
            result.setCode(ae.getCode());
            result.setMessage(ae.getMessage());
            result.setData(ae.getData());
            return result;
        }
        // 系统异常
        result.setFlag(false);
        result.setCode(20000);
        result.setMessage(ee.getMessage());
        return result;
    }
}
