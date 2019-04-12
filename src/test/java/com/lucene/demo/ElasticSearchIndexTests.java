package com.lucene.demo;

import java.util.HashMap;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchIndexTests {

	@Autowired
	RestHighLevelClient restHighLevelClient;
	
	private static String jsonStr;
	
	static {
		jsonStr = "{\r\n" + 
				"                \"properties\": {\r\n" + 
				"                    \"description\": {\r\n" + 
				"                        \"type\": \"text\",\r\n" + 
				"                        \"analyzer\": \"ik_max_word\",\r\n" + 
				"                        \"search_analyzer\": \"ik_smart\"\r\n" + 
				"                    },\r\n" + 
				"                    \"name\": {\r\n" + 
				"                        \"type\": \"text\",\r\n" + 
				"                        \"analyzer\": \"ik_max_word\",\r\n" + 
				"                        \"search_analyzer\": \"ik_smart\"\r\n" + 
				"                    },\r\n" + 
				"                    \"pic\":{\r\n" + 
				"                        \"type\":\"text\",\r\n" + 
				"                        \"index\":false\r\n" + 
				"                    },\r\n" + 
				"                    \"price\": {\r\n" + 
				"                        \"type\": \"float\"\r\n" + 
				"                    },\r\n" + 
				"                    \"studymodel\": {\r\n" + 
				"                        \"type\": \"keyword\"\r\n" + 
				"                    },\r\n" + 
				"                    \"timestamp\": {\r\n" + 
				"                        \"type\": \"date\",\r\n" + 
				"                        \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\r\n" + 
				"                    }\r\n" + 
				"                }\r\n" + 
				"            }";
	}
	
	//创建索引库
    @SuppressWarnings("unchecked")
	@Test
    public void testCreateIndex()throws Exception{
        //创建索引请求对象、并设置索引名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("course");
        //设置参数
        Settings settings = Settings.builder().put("number_of_shards", 5)//数据分片数
                          .put("number_of_replicas",1)//备份数
                          .build();
        createIndexRequest.settings(settings);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> readValue = objectMapper.readValue(jsonStr, HashMap.class);
        //设置映射
        createIndexRequest.mapping("doc",readValue);
        //创建索引操作对象
        IndicesClient indices = restHighLevelClient.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest,RequestOptions.DEFAULT);
        //获得相应是否成功
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }
    
  //删除索引库
    @Test
    public void testDeleteIndex()throws Exception{
        //穿建删除索引库请求对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("course");
        //删除索引库
        DeleteIndexResponse deleteIndexResponse= restHighLevelClient.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT);        
        //删除结果
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }
}
