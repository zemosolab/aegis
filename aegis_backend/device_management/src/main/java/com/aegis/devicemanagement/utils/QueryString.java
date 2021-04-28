package com.aegis.devicemanagement.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

class QueryString {

    private String query = "";
    private String url;

    public QueryString(String url, Map<String, String> queryParams) {
        this.url = url;
        queryParams.keySet().forEach(p -> {
            add(Helper.convertCamelCase(p), queryParams.get(p).toString());
        });
    }

    public void add(String name, String value) {
        query += "&";
        encode(name, value);
    }

    private void encode(String name, String value) {
        try {
            query += URLEncoder.encode(name, "UTF-8");
            query += "=";
            query += URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Broken VM does not support UTF-8");
        }
    }

    public String getQuery() {
        return url + query;
    }

    public String toString() {
        return getQuery();
    }
}