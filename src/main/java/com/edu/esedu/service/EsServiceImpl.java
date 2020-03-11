package com.edu.esedu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsServiceImpl implements EsService {
    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean indexExist(String indexName) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        boolean exists = false;
        try {
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("existsIndex: " + exists);
        return exists;
    }

    @Override
    public void addIndex(String indexName) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("createIndex: " + JSON.toJSONString(createIndexResponse));
    }

    @Override
    public String addData(String object, String indexName, String type, String id) {
        IndexRequest indexRequest = new IndexRequest(indexName, type,id);
        indexRequest.source(object, XContentType.JSON);
        IndexResponse indexResponse = new IndexResponse();
        try {
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexResponse.getId();
    }

    @Override
    public void delDataById(String idexName, String type, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(idexName, type, id);
        DeleteResponse response = null;
        try {
            response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("delete: " + JSON.toJSONString(response));
    }


}
