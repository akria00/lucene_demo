package com.lucene.demo;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchQueryTests {

	@Autowired
	RestHighLevelClient restHighLevelClient;
	
	@Test
	public void SearchTests() throws IOException {
		//高亮
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<tag>")//设置签缀
		.postTags("</tag>");//设置后缀
		//设置高亮字段
		highlightBuilder.fields().add(new Field("title"));
		highlightBuilder.fields().add(new Field("summary"));
		
		SearchRequest searchRequest = new SearchRequest("lucene");
		searchRequest.types("doc")
			.source(SearchSourceBuilder.searchSource()
					.fetchSource(new String[]{"title","summary"}, new String[]{})
					.query(QueryBuilders.boolQuery()
							.filter(QueryBuilders.multiMatchQuery("北京", "title","summary")))
							.highlighter(highlightBuilder));
		//5、client执行查询
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
      //6、取出高亮字段
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit:hits){
            System.out.println(hit.getHighlightFields());
        }
	}
}
