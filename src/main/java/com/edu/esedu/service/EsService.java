package com.edu.esedu.service;

import com.alibaba.fastjson.JSONObject;

public interface EsService {
    public boolean indexExist(String indexName);
    public void addIndex(String indexName);
    public String addData(String object,String indexName,String type,String id);
    public void delDataById(String idexName,String type,String id);
}
