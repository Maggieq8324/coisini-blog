package com.coisini.controller;

import com.coisini.model.Result;
import com.coisini.model.StatusCode;
import com.coisini.model.UnifyCode;
import com.coisini.model.UnifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 错误API
 * @author coisini
 * @date Jan 18, 2021
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErrorController {

    public static final String FREQUENT_OPERATION = "/frequentOperation";

    private final HttpServletResponse response;

    /**
     * 有匹配的路径，但是路径下的资源不存在
     * 如 /img/xxx.jpg
     * @return
     */
    @RequestMapping("/notfound")
    public UnifyResponse<?> notfound() {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        return Result.create(StatusCode.NOTFOUND, "文件不存在");
        return UnifyResponse.fail(UnifyCode.SERVER_404, "文件不存在", null);
    }

    /**
     * 操作过于频繁
     * @return
     */
    @RequestMapping(FREQUENT_OPERATION)
    public UnifyResponse<?> frequentOperation() {
//        return Result.create(StatusCode.REPERROR, "操作过于频繁");
        return UnifyResponse.fail(UnifyCode.SERVER_205, "操作过于频繁", null);
    }

}
