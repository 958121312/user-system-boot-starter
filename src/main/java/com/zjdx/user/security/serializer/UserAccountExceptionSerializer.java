package com.zjdx.user.security.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义异常信息序列化
 * @author Yuntian
 * @since 2022-02-21
 */
public class UserAccountExceptionSerializer extends StdSerializer<OAuth2Exception> {

    protected UserAccountExceptionSerializer() {
        super(OAuth2Exception.class);
    }
    @Override
    public void serialize(OAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", String.valueOf(e.getHttpErrorCode()));
        jsonGenerator.writeStringField("message", e.getMessage());
        jsonGenerator.writeStringField("data",null);
        jsonGenerator.writeEndObject();
    }
}
