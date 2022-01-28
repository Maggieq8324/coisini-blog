package com.coisini.exception;

import com.coisini.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @Description 全局异常处理
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 找不到资源 -> com.coisini.config.ErrorConfig
     * 未找到处理器 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public UnifyResponse noHandlerFoundExceptionHandler(Exception e) {
        log.error(UnifyCode.SERVER_404.getMsg(), e);
        return UnifyResponse.fail(UnifyCode.SERVER_404);
    }

    /**
     * 权限不足
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public UnifyResponse accessDeniedExceptionHandler(Exception e) {
        log.error(UnifyCode.SERVER_403.getMsg(), e);
        return UnifyResponse.fail(UnifyCode.SERVER_403);
    }

    /**
     * 请求方式错误
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public UnifyResponse httpRequestMethodNotSupportedExceptionHandler(Exception e) {
        log.error(UnifyCode.SERVER_201.getMsg(), e);
        return UnifyResponse.fail(UnifyCode.SERVER_201);
    }

    /**
     * controller参数异常/缺少
     * @param e
     * @return
     */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            RequestRejectedException.class}
    )
    public UnifyResponse missingServletRequestParameterException(Exception e) {
        log.error(UnifyCode.SERVER_ERROR_PARAM.getMsg(), e);
        return UnifyResponse.fail(UnifyCode.SERVER_ERROR_PARAM);
    }

    /**
     * 单次上传文件过大
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public UnifyResponse maxUploadSizeExceededException(Exception e) {
        log.error("文件过大：", e);
        return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "文件过大", null);
    }

    /**
     * 客户端错误
     * @param e
     * @return
     */
    @ExceptionHandler(ClientAbortException.class)
    public UnifyResponse clientAbortExceptionException(Exception e) {
        log.error("客户端错误：", e);
        return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "客户端错误", null);
    }

    /**
     * 其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public UnifyResponse exceptionHandler(Exception e) {
        log.error("服务异常 请联系管理员：", e);
        return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "服务异常 请联系管理员", null);
    }
    
    /**
     * 主动throw的异常
     * @param e
     * @return
     */
	@ExceptionHandler(BusinessException.class)
	public UnifyResponse handleUnBusinessException(BusinessException e) {
		//日志记录
        log.error("服务异常：", e);
	    return UnifyResponse.fail(UnifyCode.SERVER_ERROR, "服务异常：" + e.getMessage(), null);
	}

}

