package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import elements.ConvertClass;
import elements.Passenger;
import high_elasticsearch_client.ElasticClient;
import interfaces.PassengerDao;


/**
 * This class implements PassengerDao and uses elastic search to realize different functions
 * 
 *
 */
public class PassengerDaoImpl implements PassengerDao {
	
	@Override
	public String addElement(Passenger e) throws Exception {
		ElasticClient.makeConnection();
	    System.out.println("id of the passenger created : "+e.getId());
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_passenger", "passenger",e.getId())
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
	public Passenger modifyElement(String id, Passenger element) throws Exception {
		ElasticClient.makeConnection();
		UpdateRequest updateRequest = new UpdateRequest("index_passenger","passenger",id).fetchSource(true);    
		Map<String, Object> passenger = ConvertClass.objectToJson(element);
		updateRequest.doc(passenger);
        UpdateResponse updateResponse = ElasticClient.restHighLevelClient.update(updateRequest);
        ElasticClient.closeConnection();
        return (Passenger) ConvertClass.jsonToObject(updateResponse.getGetResult().getSource(),"Passenger");
	}



	@Override
	public boolean deleteElement(Passenger e) throws Exception {
		ElasticClient.makeConnection();
		//System.out.println("id of element to delete :"+e.getId());
		DeleteRequest deleteRequest = new DeleteRequest("index_passenger","passenger", e.getId());
		@SuppressWarnings("unused")
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
	public boolean deleteElement(String id) {
		ElasticClient.makeConnection();
		DeleteRequest deletePilotRequest = new DeleteRequest("index_passenger", "passenger",id);
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
	public List<Passenger> consultElement() throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_passenger");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Passenger> passengers = new ArrayList<Passenger>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					passengers
							.add((Passenger) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Passenger"));
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
		System.out.println(passengers);
		return passengers;
	}
	
	public List<Passenger> consultElement(String field,String value) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_passenger");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(field,value));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Passenger> passengers = new ArrayList<Passenger>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					passengers
							.add((Passenger) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Passenger"));
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
		System.out.println("consultElement :"+passengers);
		return passengers;
	}
	
	

	@Override
	public Passenger searchPassenger(String id) throws Exception {
		ElasticClient.makeConnection();
		GetRequest getPassengerRequest = new GetRequest("index_passenger", "passenger",id);
		Passenger passenger = null;
		GetResponse getResponse = null;
		getResponse = ElasticClient.restHighLevelClient.get(getPassengerRequest);
		passenger = (Passenger) ConvertClass.jsonToObject(getResponse.getSourceAsMap(), "Passenger");
		ElasticClient.closeConnection();
		return passenger;
	}

	
@Override
public Passenger searchPassenger(String username, String password) throws Exception {
	ElasticClient.makeConnection();
	SearchRequest searchRequest = new SearchRequest("index_passenger");
	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName",username))
												.must(QueryBuilders.termQuery("passWord", password));
	
	System.out.println(query);
	searchSourceBuilder.query(query);		
	searchSourceBuilder.size(10);
	searchRequest.source(searchSourceBuilder);
	SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
	SearchHits hits = searchResponse.getHits();
	SearchHit[] searchHit = hits.getHits();
	
	Passenger p = null;
	System.out.println(searchHit.length);
	if (searchHit.length > 0) {

	try {
		p=((Passenger) ConvertClass.jsonToObject(searchHit[0].getSourceAsMap(), "Passenger"));
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