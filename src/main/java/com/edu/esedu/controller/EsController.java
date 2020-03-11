package com.edu.esedu.controller;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.edu.esedu.model.Message;
import com.edu.esedu.util.IdWorker;
import	java.awt.TrayIcon.MessageType;

import com.alibaba.fastjson.JSONObject;
import com.edu.esedu.service.EsService;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wangcunlei
 * @Date 2020-1-3 14:55
 * @Description
 */
@RestController
public class EsController {
    @Autowired
    private EsService esService;
    @RequestMapping("/hello")
    public String hello(){
        return "hello..";
    }
    @RequestMapping("/add")
    public void addData(HttpServletRequest request,@RequestBody Map<String, Object> params){
        String index = MapUtils.getString(params,"index","");
        IdWorker worker = new IdWorker(1,1,1);
        String id = String.valueOf(worker.nextId());
        Message message = new Message();
        message.setMsgId(Long.parseLong(id));
        message.setContent(MapUtils.getString(params,"content",""));
        message.setCreateTime(DateUtil.now());
        esService.addData(JSON.toJSONString(message),index,index,id);
    }
}
