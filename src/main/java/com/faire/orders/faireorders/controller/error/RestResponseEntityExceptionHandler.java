package com.faire.orders.faireorders.controller.error;

import com.faire.orders.faireorders.controller.error.domain.ErrorMessage;
import com.faire.orders.faireorders.exception.TechnicalException;
import feign.FeignException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    public RestResponseEntityExceptionHandler() {
        super();
    }
    // 400

    @ExceptionHandler({TechnicalException.class})
    public ResponseEntity<Object> handleBadRequest(final TechnicalException ex, final WebRequest request) {
        ErrorMessage msg = new ErrorMessage();
        msg.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        msg.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, msg, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        ErrorMessage msg = new ErrorMessage();
        msg.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        msg.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, msg, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        //Locale locale = LocaleContextHolder.getLocale();
        String code = ex.getBindingResult().getFieldError().getDefaultMessage();

        code = code.replaceAll("\\{field\\}", ex.getBindingResult().getFieldError().getField());

        ErrorMessage msg = new ErrorMessage();
        msg.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        msg.setMessage(code);

        return handleExceptionInternal(ex, msg, headers, HttpStatus.BAD_REQUEST, request);
    }

    // 500
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body instanceof ErrorMessage) {
            return super.handleExceptionInternal(ex, body, headers, status, request);
        }
        ErrorMessage msg = new ErrorMessage();
        msg.setMessage(ex.getMessage());
        msg.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        return super.handleExceptionInternal(ex, msg, headers, status, request);
    }

    @ExceptionHandler({ FeignException.class })
    private ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        ex.printStackTrace();
        ErrorMessage msg = new ErrorMessage();
        msg.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        msg.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, msg, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
