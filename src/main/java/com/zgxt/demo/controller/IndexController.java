package com.zgxt.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class IndexController {
    //填写WeBASE-Front地址，用于后续交互
    private static final String URL = "http://192.168.23.142:5002/WeBASE-Front/trans/handle";

    private static final String CONTRACT_NAME = "Trace";
    private static final String CONTRACT_ADDRESS = "0xd888ef7d6fcc8f61710364bce523ff57f5d59910";
    private static final String CONTRACT_ABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"traceNumber\",\"type\":\"uint256\"}],\"name\":\"getFood\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"traceNumber\",\"type\":\"uint256\"}],\"name\":\"getTraceInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"},{\"name\":\"\",\"type\":\"string[]\"},{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint8[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"traceNumber\",\"type\":\"uint256\"},{\"name\":\"traceName\",\"type\":\"string\"},{\"name\":\"quality\",\"type\":\"uint8\"}],\"name\":\"newFood\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"traceNumber\",\"type\":\"uint256\"},{\"name\":\"traceName\",\"type\":\"string\"},{\"name\":\"quality\",\"type\":\"uint8\"}],\"name\":\"addTraceInfoByDistributor\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"isRetailer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"renounceDistributor\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getAllFood\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"addDistributor\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"addRetailer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"isDistributor\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"traceNumber\",\"type\":\"uint256\"},{\"name\":\"traceName\",\"type\":\"string\"},{\"name\":\"quality\",\"type\":\"uint8\"}],\"name\":\"addTraceInfoByRetailer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"renounceRetailer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"addProducer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"isProducer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"renounceProducer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"producer\",\"type\":\"address\"},{\"name\":\"distributor\",\"type\":\"address\"},{\"name\":\"retailer\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"RetailerAdded\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"RetailerRemoved\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"DistributorAdded\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"DistributorRemoved\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"ProducerAdded\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"account\",\"type\":\"address\"}],\"name\":\"ProducerRemoved\",\"type\":\"event\"}]";

    private static final String PRODUCER_ADDRESS = "0x4de6ce4e4b2f956220c134d6f7c452ed6b4737ff";
    private static final String DISTRIBUTOR_ADDRESS = "0x5fd3fb86120a050380a96954e53169653d08eadd";
    private static final String RETAILER_ADDRESS = "0x2b05951c7a19f4b5e5bc1d36a5bc0cec4fbed885";


    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 获取用户地址
     * userinfo: 用户角色（producer=农场 distributor=中间商 retailer=超市）
     * @return: 角色对应用户地址
     */
    @ResponseBody
    @GetMapping(path="/userinfo", produces=MediaType.APPLICATION_JSON_VALUE)
    public String userInfo(String userName){
        //声明返回对象
        JSONObject _outPut = new JSONObject();

        //返回各个用户的地址
        if (userName.equals("producer")){
            _outPut.put("address", PRODUCER_ADDRESS);
        }else if (userName.equals("distributor")){
            _outPut.put("address", DISTRIBUTOR_ADDRESS);
        }else if (userName.equals("retailer")){
            _outPut.put("address", RETAILER_ADDRESS);
        }else {
            _outPut.put("error","user not found");
        }
        return _outPut.toJSONString();
    }

    /**
     * 添加食品生产信息
     * traceNumber: 食品溯源id，食品溯源过程中的标识符
     * foodName: 食物名称
     * traceName: 用户名，食品流转过程各个阶段的用户名
     * quality: 当前食品质量（0=优质 1=合格 2=不合格）
     * @return：添加食品生产信息结果
     */
    @ResponseBody
    @PostMapping(path="/produce", produces=MediaType.APPLICATION_JSON_VALUE)
    public String produce(@RequestBody JSONObject jsonParam) {
        //声明返回对象
        JSONObject _outPutObj = new JSONObject();

        //生产商生产食品
        if(jsonParam == null){
            _outPutObj.put("error","invalid parameter");
            return _outPutObj.toJSONString();
        }

        int traceNumber = jsonParam.getInteger("traceNumber");
        String foodName = "\"" + jsonParam.getString("foodName") + "\"";
        String traceName = "\"" + jsonParam.getString("traceName") + "\"";
        int quality = jsonParam.getInteger("quality");

        JSONArray params = JSONArray.parseArray(Arrays.asList(foodName, traceNumber, traceName, quality).toString());
        JSONObject _jsonObj = new JSONObject();

        _jsonObj.put("contractName",CONTRACT_NAME);
        _jsonObj.put("contractAddress",CONTRACT_ADDRESS);
        _jsonObj.put("contractAbi",JSONArray.parseArray(CONTRACT_ABI));
        _jsonObj.put("user",PRODUCER_ADDRESS);

        _jsonObj.put("funcName","newFood");
        // JSON数组，多个参数以逗号分隔（参数为数组时同理），如：["str1",["arr1","arr2"]]，根据所调用的合约方法判断是否必填
        _jsonObj.put("funcParam",params);

        String responseStr = httpPost(URL,_jsonObj.toJSONString());

        JSONObject responseJsonObj = JSON.parseObject(responseStr);
        String msg = responseJsonObj.getString("message");
        if (responseJsonObj.get("status").equals("0x0")){
            _outPutObj.put("ret", 1);
            _outPutObj.put("msg", msg);
        } else {
            _outPutObj.put("ret", 0);
            _outPutObj.put("msg", msg);
        }

        return _outPutObj.toJSONString();
    }

    /**
     * 中间商添加食品流转信息
     * traceNumber: 食品溯源id，食品溯源过程中的标识符
     * traceName: 用户名，食品流转过程各个阶段的用户名
     * quality: 当前食品质量（0=优质 1=合格 2=不合格）
     * @return：中间商添加食品流转信息结果
     */
    @ResponseBody
    @PostMapping(path="/adddistribution", produces=MediaType.APPLICATION_JSON_VALUE)
    public String add_trace_by_distrubutor(@RequestBody JSONObject jsonParam) {
        //声明返回对象
        JSONObject _outPutObj = new JSONObject();

        if(jsonParam == null){
            _outPutObj.put("error","invalid parameter");
            return _outPutObj.toJSONString();
        }

        String trace_number = (String) jsonParam.get("traceNumber");
        String trace_name = (String) jsonParam.get("traceName");
        int quality = (int) jsonParam.get("quality");

        JSONArray params = JSONArray.parseArray("["+trace_number+",\""+trace_name+"\","+quality+"]");
        JSONObject _jsonObj = new JSONObject();
        _jsonObj.put("contractName",CONTRACT_NAME);
        _jsonObj.put("contractAddress",CONTRACT_ADDRESS);
        _jsonObj.put("contractAbi",JSONArray.parseArray(CONTRACT_ABI));
        _jsonObj.put("user",DISTRIBUTOR_ADDRESS);
        _jsonObj.put("funcName","addTraceInfoByDistributor");
        _jsonObj.put("funcParam",params);

        String responseStr = httpPost(URL,_jsonObj.toJSONString());
        JSONObject responseJsonObj = JSON.parseObject(responseStr);
        String msg = responseJsonObj.getString("message");
        if (msg.equals("Success")){
            _outPutObj.put("ret",1);
            _outPutObj.put("msg",msg);
        }else{
            _outPutObj.put("ret",0);
            _outPutObj.put("msg",msg);
        }

        return _outPutObj.toJSONString();

    }

    /**
     * 超市添加食品流转信息
     * traceNumber: 食品溯源id，食品溯源过程中的标识符
     * traceName: 用户名，食品流转过程各个阶段的用户名
     * quality: 当前食品质量（0=优质 1=合格 2=不合格）
     * @param jsonParam
     * @return 超市添加食品流转信息结果
     */
    @ResponseBody
    @PostMapping(path="/addretail", produces=MediaType.APPLICATION_JSON_VALUE)
    public String add_trace_by_retailer(@RequestBody JSONObject jsonParam) {
        //声明返回对象
        JSONObject _outPutObj = new JSONObject();

        if(jsonParam == null){
            _outPutObj.put("error","invalid parameter");
            return _outPutObj.toJSONString();
        }

        String trace_number = (String) jsonParam.get("traceNumber");
        String trace_name = (String) jsonParam.get("traceName");
        int quality = (int) jsonParam.get("quality");


        JSONArray params = JSONArray.parseArray("["+trace_number+",\""+trace_name+"\","+quality+"]");
        JSONObject _jsonObj = new JSONObject();
        _jsonObj.put("contractName",CONTRACT_NAME);
        _jsonObj.put("contractAddress",CONTRACT_ADDRESS);
        _jsonObj.put("contractAbi",JSONArray.parseArray(CONTRACT_ABI));
        _jsonObj.put("user",RETAILER_ADDRESS);
        _jsonObj.put("funcName","addTraceInfoByRetailer");
        _jsonObj.put("funcParam",params);

        String responseStr = httpPost(URL,_jsonObj.toJSONString());
        JSONObject responseJsonObj = JSON.parseObject(responseStr);
        String msg = responseJsonObj.getString("message");
        if (msg.equals("Success")){
            _outPutObj.put("ret",1);
            _outPutObj.put("msg",msg);
        }else{
            _outPutObj.put("ret",0);
            _outPutObj.put("msg",msg);
        }

        return _outPutObj.toJSONString();

    }


    /**
     *
     # 获取所有食物信息
     * @return 所有食品信息列表
     */
    @ResponseBody
    @GetMapping(path="/foodlist", produces=MediaType.APPLICATION_JSON_VALUE)
    public String getlist(){
        JSONArray num_list = get_food_list();
        JSONArray num_list2 = num_list.getJSONArray(0);
        JSONArray resList = new JSONArray();

        for (int i=0;i<num_list2.size();i++){
            String food = get_food(num_list2.get(i).toString());
            resList.add(food);
        }
        return resList.toJSONString();
    }


    /**
     * 获取某个食品的溯源信息
     * @param traceNumber 食品溯源id，食品溯源过程中的标识符
     * @return 对应食品的溯源信息
     */
    @ResponseBody
    @GetMapping(path="/trace", produces=MediaType.APPLICATION_JSON_VALUE)
    public String trace(String traceNumber){

        JSONObject _outPut = new JSONObject();

        if (Integer.parseInt(traceNumber) <= 0){
            _outPut.put("error","invalid parameter");
            return _outPut.toJSONString();
        }

        List res = get_trace(traceNumber);
        JSONArray o = new JSONArray(res);
        return o.toJSONString();

    }

    /**
     * 获取某个食品的当前信息
     * @param traceNumber 食品溯源id，食品溯源过程中的标识符
     * @return 对应食品的当前信息
     */
    @ResponseBody
    @GetMapping(path="/food", produces=MediaType.APPLICATION_JSON_VALUE)
    public String food(String traceNumber){

        JSONObject _outPut = new JSONObject();

        if (Integer.parseInt(traceNumber) <= 0){
            _outPut.put("error","invalid parameter");
            return _outPut.toJSONString();
        }

        String res = get_food(traceNumber);
        return res;

    }


    /**
     * 获取所有食品的最新溯源信息
     * @return 所有食品的最新溯源信息
     */
    @ResponseBody
    @GetMapping(path="/newtracelist", produces=MediaType.APPLICATION_JSON_VALUE)
    public String get_latest(){
        JSONArray num_list = get_food_list();
        JSONArray num_list2 = num_list.getJSONArray(0);
        JSONArray resList = new JSONArray();

        for (int i=0;i<num_list2.size();i++){
            List trace = get_trace(num_list2.get(i).toString());
            resList.add(trace.get(-1));
        }
        return resList.toJSONString();
    }


    /**
     * 获取位于生产商的的食物信息
     * @return 所有位于生产商的食品信息列表
     */
    @ResponseBody
    @GetMapping(path="/producing", produces=MediaType.APPLICATION_JSON_VALUE)
    public String get_producing(){
        JSONArray num_list = get_food_list();
        JSONArray num_list2 = num_list.getJSONArray(0);
        JSONArray resList = new JSONArray();

        for (int i=0;i<num_list2.size();i++){
            JSONArray trace = get_trace(num_list2.get(i).toString());
            if(trace.size() == 1){
                resList.add(trace.get(0));
            }
        }
        String a = resList.toJSONString();
        return resList.toJSONString();
    }

    /**
     * 获取位于中间商的食物信息
     * @return 所有位于中间商的食品信息列表
     */
    @ResponseBody
    @GetMapping(path="/distributing", produces=MediaType.APPLICATION_JSON_VALUE)
    public String get_distributing(){
        JSONArray num_list = get_food_list();
        JSONArray num_list2 = num_list.getJSONArray(0);
        JSONArray resList = new JSONArray();

        for (int i=0;i<num_list2.size();i++){
            List trace = get_trace(num_list2.get(i).toString());
            if(trace.size() == 2){
                resList.add(trace.get(1));
            }
        }
        return resList.toJSONString();
    }


    /**
     * 获取位于超市的食物信息
     * @return 所有位于超市的食品信息列表
     */
    @ResponseBody
    @GetMapping(path="/retailing", produces=MediaType.APPLICATION_JSON_VALUE)
    public String get_retailing(){

        JSONArray num_list = get_food_list();
        JSONArray num_list2 = num_list.getJSONArray(0);
        JSONArray resList = new JSONArray();

        for (int i=0;i<num_list2.size();i++){
            JSONArray trace = get_trace(num_list2.get(i).toString());
            if(trace.size() == 3){
                resList.add(trace.get(2));
            }
        }
        String a = resList.toJSONString();
        return resList.toJSONString();
    }

    /**
     * # 从链上获取所有食品信息
     * @return 所有食品信息列表
     */
    private JSONArray get_food_list(){
        JSONObject _jsonObj = new JSONObject();
        _jsonObj.put("contractName",CONTRACT_NAME);
        _jsonObj.put("contractAddress",CONTRACT_ADDRESS);
        _jsonObj.put("contractAbi",JSONArray.parseArray(CONTRACT_ABI));
        _jsonObj.put("user","");
        _jsonObj.put("funcName","getAllFood");

        String responseStr = httpPost(URL,_jsonObj.toJSONString());
        JSONArray responseJsonObj = JSON.parseArray(responseStr);
        return responseJsonObj;
    }


    /**
     * 从链上获取某个食品的基本信息
     * @param traceNumber: 食品溯源id，食品溯源过程中的标识符
     * @return 对应食品的信息
     */
    private String get_food(String traceNumber){
        JSONArray params = JSONArray.parseArray("["+traceNumber+"]");

        JSONObject _jsonObj = new JSONObject();
        _jsonObj.put("contractName",CONTRACT_NAME);
        _jsonObj.put("contractAddress",CONTRACT_ADDRESS);
        _jsonObj.put("contractAbi",JSONArray.parseArray(CONTRACT_ABI));
        _jsonObj.put("user","");
        _jsonObj.put("funcName","getFood");
        _jsonObj.put("funcParam",params);

        String responseStr = httpPost(URL,_jsonObj.toJSONString());
        JSONArray food  = JSON.parseArray(responseStr);

        JSONObject _outPut = new JSONObject();
        _outPut.put("timestamp", food.get(0));
        _outPut.put("produce",food.get(1));
        _outPut.put("name",food.get(2));
        _outPut.put("current",food.get(3));
        _outPut.put("address",food.get(4));
        _outPut.put("quality",food.get(5));

        return _outPut.toJSONString();
    }


    /**
     * 从链上获取某个食品的溯源信息
     * @param traceNumber 食品溯源id，食品溯源过程中的标识符
     * @return 对应食品的溯源信息
     */
    private JSONArray get_trace(String traceNumber){
        //获取食品基本信息
        JSONArray params = JSONArray.parseArray("["+traceNumber+"]");

        JSONObject _jsonObj = new JSONObject();
        _jsonObj.put("contractName",CONTRACT_NAME);
        _jsonObj.put("contractAddress",CONTRACT_ADDRESS);
        _jsonObj.put("contractAbi",JSONArray.parseArray(CONTRACT_ABI));
        _jsonObj.put("user","");
        _jsonObj.put("funcName","getFood");
        _jsonObj.put("funcParam",params);

        String responseStr = httpPost(URL,_jsonObj.toJSONString());
        JSONArray food  = JSON.parseArray(responseStr);

        //获取食品溯源信息
        JSONObject _jsonObj2 = new JSONObject();
        _jsonObj2.put("contractName",CONTRACT_NAME);
        _jsonObj2.put("contractAddress",CONTRACT_ADDRESS);
        _jsonObj2.put("contractAbi",JSONArray.parseArray(CONTRACT_ABI));
        _jsonObj2.put("user","");
        _jsonObj2.put("funcName","getTraceInfo");
        _jsonObj2.put("funcParam",params);

        String responseStr2 = httpPost(URL,_jsonObj2.toJSONString());
        JSONArray traceInfoList  = JSON.parseArray(responseStr2);
        JSONArray time_list = traceInfoList.getJSONArray(0);
        JSONArray name_list = traceInfoList.getJSONArray(1);
        JSONArray address_list = traceInfoList.getJSONArray(2);
        JSONArray quality_list = traceInfoList.getJSONArray(3);

        JSONArray _outPut = new JSONArray();
        for (int i=0;i<time_list.size();i++){
            if (i==0){
                JSONObject _outPutObj = new JSONObject();
                _outPutObj.put("traceNumber",traceNumber);
                _outPutObj.put("name",food.get(2));
                _outPutObj.put("produce_time",food.get(0));
                _outPutObj.put("timestamp",time_list.get(i));
                _outPutObj.put("from",name_list.get(i));
                _outPutObj.put("quality",quality_list.get(i));
                _outPutObj.put("from_address",address_list.get(i));
                _outPut.add(_outPutObj);
            }else{
                JSONObject _outPutObj = new JSONObject();
                _outPutObj.put("traceNumber",traceNumber);
                _outPutObj.put("name",food.get(2));
                _outPutObj.put("produce_time",food.get(0));
                _outPutObj.put("timestamp",time_list.get(i));
                _outPutObj.put("from",name_list.get(i-1));
                _outPutObj.put("to",name_list.get(i));
                _outPutObj.put("quality",quality_list.get(i));
                _outPutObj.put("from_address",address_list.get(i-1));
                _outPutObj.put("to_address",address_list.get(i));
                _outPut.add(_outPutObj);
            }
        }
        return _outPut;
    }

    /**
     * 发送 post 请求
     * @param url     请求地址
     * @param jsonStr Form表单json字符串
     * @return 请求结果
     */
    private String httpPost(String url, String jsonStr) {
        // 创建httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post请求方式实例
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头 发送的是json数据格式
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        // 设置参数---设置消息实体 也就是携带的数据
        StringEntity entity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        // 设置编码格式
        entity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        entity.setContentType("application/json");
        // 把请求消息实体塞进去
        httpPost.setEntity(entity);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
