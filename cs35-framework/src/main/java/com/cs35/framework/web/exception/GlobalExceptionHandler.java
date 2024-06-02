package com.cs35.framework.web.exception;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.cs35.common.constant.HttpStatus;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.exception.DemoModeException;
import com.cs35.common.exception.ServiceException;
import com.cs35.common.utils.StringUtils;

/**
 * 
 * 
 * @author cs35
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("'{}','{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "If no, contact the administrator for authorization");
    }

    /**
     * 
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("Request address '{}', '{}' request is not supported", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * 
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("'{}',.", requestURI, e);
        return AjaxResult.error(String.format("Required path variable missing in request path [%s]", e.getVariableName()));
    }

    /**
     * 
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("'{}',.", requestURI, e);
        return AjaxResult.error(String.format("Request parameter type mismatch, parameter [%s] requires type: '%s', but input value is:'%s'", e.getName(), e.getRequiredType().getName(), e.getValue()));
    }

    /**
     * 
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("An unknown exception occurred when address '{}' was requested.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("System exception occurred when address '{}' was requested.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("Demonstration mode, no operation allowed");
    }
}
