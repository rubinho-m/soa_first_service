package com.rubinho.soa_first_service.exceptions;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;
import java.util.stream.Collectors;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {
    private static final QName CODE = new QName("code");
    private static final QName DESCRIPTION = new QName("description");

    @Override
    protected void customizeFault(Object endpoint, Exception exception, SoapFault fault) {
        final String message = getMessage(exception);
        final SoapFaultDetail detail = fault.addFaultDetail();
        System.out.println("Caught exception: " + exception);
        if (exception instanceof ResponseStatusException responseStatusException) {
            final int code = responseStatusException.getStatusCode().value();
            detail.addFaultDetailElement(CODE).addText(String.valueOf(code));
            detail.addFaultDetailElement(DESCRIPTION).addText(message);
        } else {
            detail.addFaultDetailElement(CODE).addText("500");
            detail.addFaultDetailElement(DESCRIPTION).addText(message);
        }
    }

    private String getMessage(Exception exception) {
        if (exception instanceof HttpMessageNotReadableException) {
            return "Invalid request.";
        }
        if (exception instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return "Bad request. Invalid param: %s".formatted(methodArgumentTypeMismatchException.getPropertyName());
        }

        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            return getValidationMessage(methodArgumentNotValidException);
        }
        return exception.getMessage();
    }

    private String getValidationMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("Invalid request body. Field '%s' %s",
                        error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining());
    }
}
