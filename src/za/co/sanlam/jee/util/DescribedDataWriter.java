package za.co.sanlam.jee.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

/**
 *
 * @author genesis
 */
@Produces({"application/json", "text/csv"})
@Provider
public class DescribedDataWriter implements MessageBodyWriter<DescribedData> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        System.out.println("{\"DescribedDataWriter Class<?> type\" :" + type + "}");
        System.out.println("{\"DescribedDataWriter Type genericType\" :" + genericType.toString() + "}");

        return genericType.toString().equals("class za.co.sanlam.jee.util.DescribedData");
    }

    @Override
    public long getSize(DescribedData t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(DescribedData d, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        System.out.println("mediaType.getType():" + mediaType.getType());
        System.out.println("mediaType.getSubtype():" + mediaType.getSubtype());
        if ((mediaType.getType().equals("application")) && (mediaType.getSubtype().equals("json"))) {
            this.writeToJson(d, type, genericType, annotations, mediaType, httpHeaders, entityStream);
        } else if ((mediaType.getType().equals("text")) && (mediaType.getSubtype().equals("csv"))) {
            this.writeToCsv(d, type, genericType, annotations, mediaType, httpHeaders, entityStream);
        } else {
            throw new RuntimeException("Oops!");
        }
    }

    private void writeToJson(DescribedData d, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        System.out.println("mediaType.getType():" + mediaType.getType());
        System.out.println("mediaType.getSubtype():" + mediaType.getSubtype());
        StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append("{\"result\":{");
        stringBuilder.append("{");
        String nextColumn = "";
        Map<String, Map<String, String>> contextData = d.contextData;
        if ((contextData != null) && (contextData.size() > 0)) {
            stringBuilder.append(nextColumn + "\"metadata\":[");
            String nextMeta = "";
            for (Map.Entry<String, Map<String, String>> entrySet : contextData.entrySet()) {
                String key = entrySet.getKey();
                Map<String, String> map = entrySet.getValue();
                stringBuilder.append(nextMeta + "{");
                String keyValue = JsonHelper.escape(key);
                stringBuilder.append("\"").append("name").append("\":\"").append(keyValue).append("\"");
                String nextRow = ",";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    stringBuilder.append(nextRow);
                    if (entry.getValue() == null) {
                        stringBuilder.append("\"").append(keyValue).append("/").append(entry.getKey()).append("\":null");
                    } else {
                        String value = JsonHelper.escape(entry.getValue());
                        stringBuilder.append("\"").append(keyValue).append("/").append(entry.getKey()).append("\":\"").append(value).append("\"");
                    }
                    nextRow = ",";
                }
                stringBuilder.append("}");
                nextMeta = ",";
            }
            stringBuilder.append("]");
            nextColumn = ",";
        }

        Map<String, String> metas = d.metaData;
        if ((metas != null) && (metas.size() > 0)) {
            for (Map.Entry<String, String> entrySet : metas.entrySet()) {
                String key = JsonHelper.escape(entrySet.getKey());
                String value = JsonHelper.escape(entrySet.getValue());
                stringBuilder.append(nextColumn);
                stringBuilder.append("\"" + key + "\":" + "\"" + value + "\"");
            }
            nextColumn = ",";
        }

        List<Map<String, String>> t = d.data;
        stringBuilder.append(nextColumn + "\"data\":[");
        String nextRow = "";
        for (Map<String, String> map : t) {
            stringBuilder.append(nextRow);
            stringBuilder.append("{");
            nextColumn = "";
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
        //stringBuilder.append("}");
        //writer.write('\uFEFF');
        Writer writer = new OutputStreamWriter(entityStream);
        //writer.write('\uFEFF');
        writer.write(stringBuilder.toString());
        writer.flush();

    }

    private void writeToCsv(DescribedData d, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        System.out.println("writeToCsv():");
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder typeBuilder = new StringBuilder();
        StringBuilder multiplicityBuilder = new StringBuilder();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(entityStream));

        //stringBuilder.append("{\"result\":{");
        SortedSet<String> headings = new TreeSet<>();
        Map<String, Map<String, String>> contextData = d.contextData;
        if ((contextData != null) && (contextData.size() > 0)) {
            for (Map.Entry<String, Map<String, String>> entrySet : contextData.entrySet()) {
                String heading = entrySet.getKey();
                headings.add(heading);
            }

            String next = "";
            for (String heading : headings) {
                nameBuilder.append(next).append("\"").append(heading.replaceAll("\"", "\\\"")).append("\"");
                next = ",";
            }
            writer.write(nameBuilder.toString());

            next = "";
            for (String heading : headings) {
                Map<String, String> attributes = contextData.get(heading);
                typeBuilder.append(next).append("\"").append(attributes.get("type").replaceAll("\"", "\\\"")).append("\"");
                next = ",";
            }
            writer.newLine();
            writer.write(typeBuilder.toString());

            next = "";
            for (String heading : headings) {
                Map<String, String> attributes = contextData.get(heading);
                multiplicityBuilder.append(next).append("\"").append(attributes.get("multiplicity").replaceAll("\"", "\\\"")).append("\"");
                next = ",";
            }
            writer.newLine();
            writer.write(multiplicityBuilder.toString());

            //node/link/server/Server/target/node/name
            List<Map<String, String>> nodes = d.data;
            for (Map<String, String> node : nodes) {
                StringBuilder dataBuilder = new StringBuilder();
                next = "";
                for (String heading : headings) {
                    Map<String, String> attributes = contextData.get(heading);
                    String targetType = attributes.get("type");
                    String relativeName = "node/link/" + heading + "/" + targetType + "/target/node/name";
                    String relativeValue = node.get(relativeName);
                    if (relativeValue == null) {
                        relativeValue = "";
                    }
                    dataBuilder.append(next).append("\"").append(relativeValue.replaceAll("\"", "\\\"")).append("\"");
                    next = ",";
                }
                writer.newLine();
                writer.write(dataBuilder.toString());
            }
        }

        writer.flush();

    }
}
