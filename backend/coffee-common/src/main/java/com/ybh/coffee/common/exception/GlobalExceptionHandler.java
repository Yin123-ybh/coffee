package com.ybh.coffee.common.exception;

import com.ybh.coffee.common.api.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBiz(BizException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public Result<Void> handleValidate(Exception e) {
        return Result.fail(400, "参数校验失败");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleDefault(Exception e) {
        return Result.fail("系统繁忙，请稍后重试");
    }
}
