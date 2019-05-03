package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import elements.ConvertClass;
import elements.Passenger;
import elements.Pilot;
import high_elasticsearch_client.ElasticClient;
import interfaces.PilotDao;

/**
 * This class implements PilotDao and uses elastic search to realize different functions
 * @author fengyiwei
 *
 */
public class PilotDaoImpl implements PilotDao{
	/**
	 * All methods works good ^___^ 
	 */
	@Override
	public String addElement(Pilot e) throws Exception {
		ElasticClient.makeConnection();
		System.out.println("id of the pilot created : "+e.getId());
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_pilot", "pilot",e.getId())
	            .source(data);
	    IndexResponse response = null;
	    try {
	        response = ElasticClient.restHighLevelClient.index(indexRequest);
	    } catch(ElasticsearchException exp) {
	        exp.getDetailedMessage();
	    } catch (java.io.IOException exp){
	        exp.getLocalizedMessage();
	    }
		ElasticClient.closeConnection();
		if(response != null) {
			return response.toString();
		}
		return "no-response";
	}
	
	@Override
	public Pilot modifyElement(String id, Pilot element) throws Exception {
		
		ElasticClient.makeConnection();
		UpdateRequest updateRequest = new UpdateRequest("index_pilot","pilot",id).fetchSource(true);    
		Map<String, Object> pilot = ConvertClass.objectToJson(element);
		updateRequest.doc(pilot);
        UpdateResponse updateResponse = ElasticClient.restHighLevelClient.update(updateRequest);
        ElasticClient.closeConnection();
        return (Pilot) ConvertClass.jsonToObject(updateResponse.getGetResult().getSource(),"Pilot");
	}
	

	@Override
	public boolean deleteElement(Pilot e) throws Exception {
		ElasticClient.makeConnection();
		DeleteRequest deleteRequest = new DeleteRequest("index_pilot","pilot", e.getId());
		DeleteResponse deleteResponse = null;
	    try {
	        deleteResponse = ElasticClient.restHighLevelClient.delete(deleteRequest);
	        if( deleteResponse.getResult().toString().equals("DELETED") ) {
				System.out.println(deleteResponse.getResult().toString());
				ElasticClient.closeConnection();
				return true;
			}
	    } catch (java.io.IOException exp){
	        exp.getLocalizedMessage();
	    }
	    ElasticClient.closeConnection();
		return false;
	}
	
	@Override
	public boolean deleteElement(String id){
		ElasticClient.makeConnection();
		DeleteRequest deletePilotRequest = new DeleteRequest("index_pilot", "pilot",id);
		DeleteResponse deleteResponse = null;
		try {
			deleteResponse = ElasticClient.restHighLevelClient.delete(deletePilotRequest);
			if( deleteResponse.getResult().toString().equals("DELETED") ) {
				System.out.println(deleteResponse.getResult().toString());
				ElasticClient.closeConnection();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ElasticClient.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Pilot> consultElement() throws Exception {				
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_pilot");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Pilot> pilots = new ArrayList<Pilot>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					pilots
							.add((Pilot) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Pilot"));
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
					);
		}
		ElasticClient.closeConnection();
		return pilots;
	}
	
	public List<Pilot> consultElement(String field,String value) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_pilot");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(field,value));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Pilot> pilots = new ArrayList<Pilot>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					pilots
							.add((Pilot) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Pilot"));
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
					);
		}
		ElasticClient.closeConnection();
		System.out.println("consultElement :"+pilots);
		return pilots;
	}
	//
	@Override
	public Pilot searchPilot(String id) throws Exception {
		ElasticClient.makeConnection();
		GetRequest getPilotRequest = new GetRequest("index_pilot", "pilot",id);
		Pilot pilot = null;
		GetResponse getResponse = null;
		getResponse = ElasticClient.restHighLevelClient.get(getPilotRequest);
		pilot = (Pilot) ConvertClass.jsonToObject(getResponse.getSourceAsMap(), "Pilot");
		ElasticClient.closeConnection();
		return pilot;
	}
	@Override
	public Pilot searchPilotByUsername(String username) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_pilot");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName",username));										
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(10);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();
		SearchHit hit=searchHit[0];
		Pilot p = (Pilot) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Pilot");
		System.out.println(p+""+searchHit.length);

		ElasticClient.closeConnection();
		return p;
	}
	
	@Override
	public Pilot searchPilot(String username, String password) throws Exception {
		
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_pilot");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName",username))
													.must(QueryBuilders.termQuery("passWord", password));
		
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(20);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		
		Pilot p = null;
		System.out.println(searchHit.length>0);

		if (searchHit.length > 0) {
		
					try {
						p=((Pilot) ConvertClass.jsonToObject(searchHit[0].getSourceAsMap(), "Pilot"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
		ElasticClient.closeConnection();
		return p;
	}
}
