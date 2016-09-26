/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.sanlam.jee.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author genesis
 */
public class DescribedData {
    public final List<Map<String, String>> data;
    public final Map<String, String> metaData;
    public final Map<String,Map<String, String>> contextData;
    public DescribedData(List<Map<String, String>> data,Map<String, String> metaData) {
        this.data = data;
        this.metaData = metaData;
        this.contextData = new HashMap<>();
    }
    public DescribedData(List<Map<String, String>> data,Map<String, String> metaData,Map<String,Map<String, String>> contextData) {
        this.data = data;
        this.metaData = metaData;
        if (contextData != null) {
            this.contextData = contextData;
        } else {
            throw new RuntimeException("Oops!");
        }
    }
}
