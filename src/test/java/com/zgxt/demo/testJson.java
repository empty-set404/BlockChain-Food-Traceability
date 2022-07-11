package com.zgxt.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class testJson {
    @Test
    public static void main(String[] args) {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("foodName","薯条");
        jsonParam.put("traceName","金拱门");
        jsonParam.put("traceNumber",2);
        System.out.println("未转化====="+jsonParam.getString("foodName"));
        System.out.println("未转化====="+jsonParam.getString("traceName"));
        System.out.println("jsonParam.toJSONString()"+jsonParam.toJSONString());
        System.out.println(jsonParam.toJSONString());
        System.out.println("["+"trace_number"+",\""+"traceName"+"\","+1+"]");
        // 转义字符
        String foodName = "\"" + jsonParam.getString("foodName") + "\"";
        String traceName = "\"" + jsonParam.getString("traceName") + "\"";
        int traceNumber = jsonParam.getInteger("traceNumber");
        System.out.print("================"+foodName);
        System.out.println("================"+traceName);
        System.out.println("未转换================"+traceNumber);

        // 数组转字符串再转json
        JSONArray params = JSONArray.parseArray(Arrays.asList(foodName, traceNumber, traceName).toString());
        System.out.println("Arrays.asList(foodName, traceNumber, traceName).toString");
        System.out.println(Arrays.asList(foodName, traceNumber, traceName).toString());
        System.out.println("JSONArray.parseArray");
        System.out.println(params);
    }
}
