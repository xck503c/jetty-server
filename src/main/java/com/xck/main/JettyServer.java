package com.xck.main;

import com.alibaba.fastjson.JSONObject;
import com.xck.HttpUtils;
import com.xck.ResultBean;
import com.xck.dataCenter.SysConstants;
import com.xck.util.PropertiesUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyServer extends AbstractHandler {
    private static Logger infoLog = LoggerFactory.getLogger("info");

    private volatile static boolean isRunning = false;

    private SelectChannelConnector selector = null; //http
    private SslSocketConnector sslSelector = null; //https
    private Server server = null;

    //服务端参数
    private int httpPort = 8091;
    private int httpsPort = 8092;
    private int acceptors = 1; //acceptor数量
    private int maxThreads = 15; //最大线程数量
    private int maxIdleTime = 10000; //连接最大空闲数

    public void doInit(){
        try {
            //====================init param===================
            httpPort = PropertiesUtils.getPropertyInt("server.http.port", httpPort);
            httpsPort = PropertiesUtils.getPropertyInt("server.http.port", httpsPort);
            int N = PropertiesUtils.getPropertyInt("server.acceptorsN", acceptors);
            acceptors = SysConstants.CPU_COUNT + (N<1?1:N);
            int M = PropertiesUtils.getPropertyInt("server.maxThreadsM", maxThreads);
            maxThreads = acceptors*(M<3?3:M);
            maxIdleTime = PropertiesUtils.getPropertyInt("server.maxIdleTime", maxIdleTime);
            //=================================================
            //http
            selector = new SelectChannelConnector();
            selector.setPort(httpPort);
            selector.setMaxIdleTime(maxIdleTime);
            selector.setAcceptors(acceptors);
            selector.setAcceptQueueSize(3000);
            selector.setThreadPool(new QueuedThreadPool(maxThreads));
            selector.setLowResourcesConnections(2000);
            selector.setRequestHeaderSize(1024*30);
            selector.setRequestBufferSize(1024*30);

            server = new Server();
            server.setConnectors(new Connector[]{selector});
        } catch (Exception e) {
            infoLog.info("init server fail: ", e);
        }
    }

    @Override
    public void doStart(){
        try {
            if(server != null){
                server.setHandler(this);
                server.start();
                isRunning = true;
            }else{
                infoLog.info("server is null, start fail");
            }
        } catch (Exception e) {
            infoLog.info("start server fail: ", e);
        }
    }

    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String result = ResultBean.getServerErrorResult();
        try {
            result = parseRequest(request, response);
            if("".equals(result)){
                result = "404";
                String reqURL = request.getRequestURL().toString();
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                infoLog.info("access url="+reqURL + " is not found");
            }else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=UTF-8");
            }
        } catch (Exception e) {
            infoLog.error("server handle error: ", e);
        } finally {
            baseRequest.setHandled(true);
            response.getWriter().write(result);
        }
    }

    private String parseRequest(HttpServletRequest request, HttpServletResponse response){
        String json = HttpUtils.getJsonReqParam(infoLog, request);
        //非json数据
        if(!HttpUtils.isJsonStr(json)){
            return ResultBean.getNoJsonResult();
        }
        infoLog.info("req suc, reqJson=" + json);

        return ResultBean.getSucResult();
    }

    public static void main(String[] args) {
        JettyServer server = new JettyServer();
        server.doInit();
        server.doStart();
    }
}
