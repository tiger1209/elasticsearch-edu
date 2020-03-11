package com.edu.esedu.config;

import java.util.ArrayList;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
public class EsConfiguration {
 	@Value("${elasticsearch.host}")
	private  String hosts;
 	@Value("${elasticsearch.port}")
 	private  int port;
 	@Value("${elasticsearch.protocol}")
 	private  String protocol;
	/*private static String hosts = "192.168.3.155"; // 集群地址，多个用,隔开
    private static int port = 9200; // 使用的端口号
	private static String protocol = "http"; // 使用的协议*/
	private static ArrayList<HttpHost> hostList = null;
 
	private static int connectTimeOut = 1000; // 连接超时时间
	private static int socketTimeOut = 30000; // 连接超时时间
	private static int connectionRequestTimeOut = 500; // 获取连接的超时时间
 
	private static int maxConnectNum = 100; // 最大连接数
	private static int maxConnectPerRoute = 100; // 最大路由连接数
 
	static {

	}
 
	@Bean
	public RestHighLevelClient client() {

		hostList = new ArrayList<>();
		String[] hostStrs = hosts.split(",");
		for (String host : hostStrs) {
			hostList.add(new HttpHost(host, port, protocol));
		}

		RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
		// 异步httpclient连接延时配置
		builder.setRequestConfigCallback(new RequestConfigCallback() {
			@Override
			public Builder customizeRequestConfig(Builder requestConfigBuilder) {
				requestConfigBuilder.setConnectTimeout(connectTimeOut);
				requestConfigBuilder.setSocketTimeout(socketTimeOut);
				requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
				return requestConfigBuilder;
			}
		});
		// 异步httpclient连接数配置
		builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
			@Override
			public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
				httpClientBuilder.setMaxConnTotal(maxConnectNum);
				httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
				return httpClientBuilder;
			}
		});
		RestHighLevelClient client = new RestHighLevelClient(builder);
		return client;
	}
 
}