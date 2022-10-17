package com.w2m.superheroes;

import com.w2m.superheroes.exceptions.W2M_Exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApiExceptionHandler {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorMessage implements Serializable {

        private static final long serialVersionUID = 1L;

        private String requestUri, error;

    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({W2M_Exception.class})
    public ErrorMessage notFoundRequest(HttpServletRequest request, Exception exception) {
        return new ErrorMessage(request.getRequestURI(), exception.getMessage());
    }

}