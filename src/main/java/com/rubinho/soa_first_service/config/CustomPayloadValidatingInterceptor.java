package com.rubinho.soa_first_service.config;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.QName;


public class CustomPayloadValidatingInterceptor extends PayloadValidatingInterceptor {
    private static final QName CODE = new QName("code");
    private static final QName DESCRIPTION = new QName("description");

    @Override
    protected boolean handleRequestValidationErrors(MessageContext messageContext, SAXParseException[] errors) {
        SoapMessage response = (SoapMessage) messageContext.getResponse();
        SoapBody body = response.getSoapBody();

        SoapFault fault = body.addServerOrReceiverFault("Validation Error", null);
        fault.addFaultDetail();
        SoapFaultDetail detail = fault.getFaultDetail();
        detail.addFaultDetailElement(CODE).addText(String.valueOf(422));
        detail.addFaultDetailElement(DESCRIPTION).addText(errors[0].getMessage());
        return false;
    }
}
