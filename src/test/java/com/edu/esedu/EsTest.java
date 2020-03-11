package com.edu.esedu;

import com.alibaba.fastjson.JSON;
import com.edu.esedu.model.Message;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.lucene.queryparser.xml.QueryBuilder;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsEduApplication.class) // 指定启动类
public class EsTest  {
    @Autowired
    private RestHighLevelClient client;

    public static String INDEX_TEST = null;
    public static String TYPE_TEST = null;
    public static Message tests = null;
    public static List<Message> testsList = null;

    @BeforeClass()
    public static void before() {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        INDEX_TEST = "index_msg"; // 索引名称
        TYPE_TEST = "type_msg"; // 索引类型
        testsList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests = new Message();
            tests.setMsgId(Long.valueOf(i));
            tests.setContent("this is the test " + i);
            tests.setCreateTime(sdf.format(new Date()));
            testsList.add(tests);
        }
    }


    @Test
    public void testIndex() throws IOException {
        // 判断是否存在索引
        INDEX_TEST = "index_msg";
        if (!existsIndex(INDEX_TEST)) {
            // 不存在则创建索引
            createIndex(INDEX_TEST);
        }
    }
    @Test
    public void addTest() throws IOException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String index =  "index_msg";
        String type = "type_msg";
        Message tests = new Message();
        tests.setMsgId(Long.valueOf(4));
        tests.setContent("this is the test " + 4);
        tests.setCreateTime(sdf.format(new Date()));
        IndexRequest indexRequest = new IndexRequest(index, type, String.valueOf(tests.getMsgId()));
        indexRequest.source(JSON.toJSONString(tests), XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("add: " + JSON.toJSONString(indexResponse));
    }
    @Test
    public void getTest() throws IOException {
        String index = "index_msg";
        String type = "type_msg";
        String id ="3";
        GetRequest getRequest = new GetRequest(index, type, id.toString());
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println("get: " + JSON.toJSONString(getResponse));

    }

    @Test
    public void updateTest() throws IOException {
        Message tests = new Message();
        tests.setContent("abcdefggggg" + "updated");
        String index = "index_msg";
        String type = "type_msg";
        String id ="3";
        UpdateRequest request = new UpdateRequest(index, type, id);
        request.doc(JSON.toJSONString(tests), XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        System.out.println("update: " + JSON.toJSONString(updateResponse));
    }
    @Test
    public void delTest() throws IOException {
        String index = "index_msg";
        String type = "type_msg";
        String id ="3";
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id.toString());
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("delete: " + JSON.toJSONString(response));

    }
    @Test
    public void searchTest() throws IOException {
        String index = "index_msg";
        String type = "type_msg";
        String name = "1";
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchQuery("msgId", name)); // 这里可以根据字段进行搜索，must表示符合条件的，相反的mustnot表示不符合条件的
        // boolBuilder.must(QueryBuilders.matchQuery("id", tests.getId().toString()));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(100); // 获取记录数，默认10
        sourceBuilder.fetchSource(new String[]{}, new String[]{}); // 第一个是获取字段，第二个是过滤的字段，默认获取全部
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("search: " + JSON.toJSONString(response));
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            System.out.println("search -> " + hit.getSourceAsString());


        }
    }
    public void createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("createIndex: " + JSON.toJSONString(createIndexResponse));
    }

    public boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("existsIndex: " + exists);
        return exists;
    }

}
