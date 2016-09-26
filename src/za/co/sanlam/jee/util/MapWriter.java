/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.sanlam.jee.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 *
 * @author genesis
 */
@Produces({"application/json"})
@Provider
public class MapWriter implements MessageBodyWriter<Map<String, String>> {

    @Override
    public long getSize(Map<String, String> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Map<String, String> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        String next = "";
        for (Map.Entry<String, String> entry : t.entrySet()) {
            stringBuilder.append(next);
            String value = entry.getValue().replace("\\", "\\\\").replace("\"", "\\\"");
            stringBuilder.append("\"").append(entry.getKey()).append("\":\"").append(value).append("\"");
            next = ",";
        }
        stringBuilder.append("}");
        //writer.write('\uFEFF');
        Writer writer = new OutputStreamWriter(entityStream);
        //writer.write('\uFEFF');
        writer.write(stringBuilder.toString());
        writer.flush();
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        System.out.println("{\"Class<?> type\" :" + type + "}");
        return HashMap.class == type;
    }

}
