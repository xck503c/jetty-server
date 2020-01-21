package com.xck;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpUtils {

    public static String getJsonReqParam(Logger log, HttpServletRequest request){
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder json = new StringBuilder();

        try {
            is = request.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String tmp = null;
            while ((tmp = br.readLine()) != null){
                json.append(tmp);
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try{
                if(br!=null) br.close();
                if(isr!=null) isr.close();
                if(is!=null) is.close();
            }catch (IOException e){
            }
        }
        return json.toString();
    }

    public static boolean isJsonStr(String jsonStr){
        if(StringUtils.isBlank(jsonStr)) return false;

        try {
            JSONObject.parse(jsonStr);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
