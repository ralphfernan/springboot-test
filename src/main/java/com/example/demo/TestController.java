package com.example.demo;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.ws.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

// ENABLE @Configuration on ESConfig (for Transport Client) OR EsRestConfig first
@RestController
@RequestMapping("/")
public class TestController {
	
  private static final Logger LOG = LogManager.getLogger(TestController.class);

  private  ElasticsearchOperations elasticsearchOperations;
  
  private RestHighLevelClient restClient;
  
  private Client client;
  
  
  private final ObjectMapper om = new ObjectMapper();

  @Autowired
  private EntityMapper em;
 
//  public  TestController(Client cl) {
//	  this.client = cl;
//  }
  
  public TestController(ElasticsearchOperations ops, @Qualifier("elasticsearchClient") RestHighLevelClient cl, EntityMapper m) {
	  this.elasticsearchOperations = ops;
	  restClient = cl;
	  em = m;
  }

  /*
   curl -X POST "http://localhost:8080/person" -H 'Content-Type: application/json' -d'
	{
	  "id": 123,
	  "name": "Ralph",
	  "email": "Ralph@abc.com"
	}
	'
   * Spring data will include _class type hint.
   */
  @PostMapping("/person")
  public String saveUsingTemplate(@RequestBody Person person) {                         

    IndexQuery indexQuery = new IndexQueryBuilder()
      .withId(person.getId().toString())
      .withObject(person)
      .build();
    String documentId = elasticsearchOperations.index(indexQuery);
    return documentId;
  }
  
  /*
   * ES RestClient will *not* include _class typehint unlike spring-data elasticsearchOperations/template
   */
  @PostMapping("/rest/person")
  public boolean saveUsingRest(@RequestBody Person person) throws IOException {
	  String req = om.writeValueAsString(person);
	  IndexRequest indexRequest = new IndexRequest("person")
			  .id(person.getId())
			  .type("doc")
			  .source(req, XContentType.JSON);

	  IndexResponse response = restClient.index(indexRequest, RequestOptions.DEFAULT);
	  LOG.info("IndexResponse: {}",response.toString());

	  return response.getShardInfo().getSuccessful() > 0;
	  
  }

  /*
   curl -X GET "http://localhost:8080/person/123"
   */
  @GetMapping("/person/{id}")
  public Person findById(@PathVariable("id")  Long id) {                   
    Person person = elasticsearchOperations
      // GetQuery is used for querying by ids only
      .queryForObject(GetQuery.getById(id.toString()), Person.class); 
    return person;
	 //GetResponse person = client.get(new GetRequest("person", "doc", String.valueOf(id))).actionGet();
	 
    //return person.getSourceAsString();
  }
  
  @GetMapping("/person")
  public Person findByName(@Param(value = "name") String name) { 
	CriteriaQuery c = new CriteriaQuery(Criteria.where("name").is(name));
    Person person = elasticsearchOperations
      .queryForObject(c, Person.class);
    return person;
  }
  
  /*
   * Use spring-data entitymapper instead of Jackson object mapper to avoid issues with stored type hints.
   */
  @GetMapping("/person/matching")
  public List<Person> findByMatch(@Param(value = "name") String name) throws IOException { 
	  MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", name);
	  SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
	  sourceBuilder.query(matchQueryBuilder);
	  SearchRequest searchRequest = new SearchRequest();
	  searchRequest.source(sourceBuilder);
	  searchRequest.indices("person");
	  LOG.info("SearchRequest {}", searchRequest.toString());
	  SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
	  List<Person> result = new ArrayList<>();
//	  searchResponse.getHits().forEach(hit -> {
//		try {
//			result.add(em.mapToObject(hit.getSourceAsString(), Person.class));
//		} catch (IOException e) {
//		}
//	  	});
	  // Using JacksonMapper - need to skip _class or set to JsonIgnore in Person
	  searchResponse.getHits().forEach(hit->result.add(om.convertValue(hit.getSourceAsMap(), Person.class)));
	  return result;	  
  }
  
  
  @GetMapping("/fields/person")
  public String getPeopleWithField(@RequestParam(value="name") String name) throws IOException {
	  
	  SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource();
	  String [] includes = new String[2];
	  includes[0] = "email";
	  includes[1] = name;
	  searchSource.fetchSource(includes, null);

	  SearchRequest searchRequest = new SearchRequest("person");
	  searchRequest.indices("person");
	  searchRequest.types("doc");
	  searchRequest.source(searchSource);
	  
	  SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
	  return searchResponse.toString();
	  
  }
}
