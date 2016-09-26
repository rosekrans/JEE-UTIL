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
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 *
 * @author genesis
 */
@Produces({"application/json"})
@Provider
public class MapListWriter implements MessageBodyWriter<List<Map<String, String>>> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        System.out.println("{\"Class<?> type\" :" + type + "}");
        System.out.println("{\"Type genericType\" :" + genericType.toString() + "}");

        if (ArrayList.class == type) {
            return genericType.toString().equals("java.util.List<java.util.Map<java.lang.String, java.lang.String>>");
        } else {
            return false;
        }
    }

    @Override
    public long getSize(List<Map<String, String>> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(List<Map<String, String>> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"data\":[");
        String nextRow = "";
        for (Map<String, String> map : t) {
            stringBuilder.append(nextRow);
            stringBuilder.append("{");
            String nextColumn = "";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuilder.append(nextColumn);
                String key = JsonHelper.escape(entry.getKey());
                if (entry.getValue() == null) {
                    stringBuilder.append("\"").append(key).append("\":null");
                } else {
                    String value = JsonHelper.escape(entry.getValue());
                    stringBuilder.append("\"").append(key).append("\":\"").append(value).append("\"");
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
