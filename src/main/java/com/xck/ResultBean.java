package com.xck;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * 公共响应结果
 */
public class ResultBean {
    //错误码
    public static final int STATUS_CODE_SUCCESS = 0; //请求成功
    public static final int STATUS_CODE_NO_JSON = 100; //请求参数非json
    public static final int STATUS_CODE_REQ_EXPIRED = 101; //请求过期
    public static final int STATUS_CODE_SERVER_ERROR = 102; //服务端异常

    //固定返回信息
    private static String SUC_RESULT = null;
    private static String NO_JSON_RESULT = null;
    private static String SERVER_ERROR_RESULT = null;

    private int statusCode;
    private String message;

    public ResultBean(){}

    private static String getStatusCodeMessage(int statusCode){
        String msg = "";
        switch (statusCode){
            case STATUS_CODE_SUCCESS: msg="成功";break;
            case STATUS_CODE_NO_JSON: msg="请求参数非json";break;
            case STATUS_CODE_REQ_EXPIRED: msg="请求已过期";break;
            case STATUS_CODE_SERVER_ERROR: msg="服务端异常";break;
            default: msg="";
        }
        return msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        message = getStatusCodeMessage(statusCode);
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            this.message += ", " + message;
        }
    }


    public static String getNoJsonResult() {
        if(NO_JSON_RESULT == null){
            ResultBean rb = new ResultBean();
            rb.setStatusCode(STATUS_CODE_NO_JSON);
            NO_JSON_RESULT = JSONObject.toJSONString(rb);
        }
        return NO_JSON_RESULT;
    }

    public static String getSucResult() {
        if(SUC_RESULT == null){
            ResultBean rb = new ResultBean();
            rb.setStatusCode(STATUS_CODE_SUCCESS);
            SUC_RESULT = JSONObject.toJSONString(rb);
        }
        return SUC_RESULT;
    }

    public static String getServerErrorResult() {
        if(SERVER_ERROR_RESULT == null){
            ResultBean rb = new ResultBean();
            rb.setStatusCode(STATUS_CODE_SERVER_ERROR);
            SERVER_ERROR_RESULT = JSONObject.toJSONString(rb);
        }
        return SERVER_ERROR_RESULT;
    }
}
