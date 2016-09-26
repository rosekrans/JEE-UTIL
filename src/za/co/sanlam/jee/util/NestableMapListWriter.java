package za.co.sanlam.jee.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 *
 * @author genesis
 */
@Produces({"application/json"})
@Provider
public class NestableMapListWriter implements MessageBodyWriter<List<Map<String, Object>>> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        System.out.println("{\"Class<?> type\" :" + type + "}");
        System.out.println("{\"Type genericType\" :" + genericType.toString() + "}");

        if (ArrayList.class == type) {
            return genericType.toString().equals("java.util.List<java.util.Map<java.lang.String, java.lang.Object>>");
        } else {
            return false;
        }
    }

    @Override
    public long getSize(List<Map<String, Object>> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(List<Map<String, Object>> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"data\":[");
        String nextRow = "";
        for (Map<String, Object> map : t) {
            stringBuilder.append(nextRow);
            stringBuilder.append("{");
            String nextColumn = "";
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                stringBuilder.append(nextColumn);
                if (entry.getValue() == null) {
                    stringBuilder.append("\"").append(entry.getKey()).append("\":null");
                } else {
                    Object objectValue = entry.getValue();
                    if (objectValue instanceof String) {
                        String stringValue = (String) objectValue;
                        String value = JsonHelper.escape(stringValue);
                        stringBuilder.append("\"").append(entry.getKey()).append("\":\"").append(value).append("\"");
                    } else if (objectValue instanceof List) {
                        List valueList = (List) objectValue;
                        stringBuilder.append("\"").append(entry.getKey()).append("/count").append("\":\"").append(valueList.size()).append("\"");
                        stringBuilder.append(",\"").append(entry.getKey()).append("\":[");
                        String nextListItem = "";
                        for (Object listValue : valueList) {
                            stringBuilder.append(nextListItem).append("\"").append(listValue.toString()).append("\"");
                            nextListItem = ",";
                        }
                        stringBuilder.append("]");
                    } else {
                        stringBuilder.append("\"").append(entry.getKey()).append("/count").append("\":null");
                    }
                }
                nextColumn = ",";
            }
            stringBuilder.append("}");
            nextRow = ",";
        }
        stringBuilder.append("]}");
        //writer.write('\uFEFF');
        Writer writer = new OutputStreamWriter(entityStream);
        //writer.write('\uFEFF');
        writer.write(stringBuilder.toString());
        writer.flush();

    }

}
