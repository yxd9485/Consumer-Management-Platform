package com.dbt.platform.mn.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author shuDa
 * @date 2021/12/17
 **/
@Data
public class ResultJSON {
    private String msg;
    private String status;
    private String message;
    private String error;
    private int code;
    private JSONObject page;
    private JSONArray data;
}
