package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import elements.Airport;
import elements.ConvertClass;
import high_elasticsearch_client.ElasticClient;
import interfaces.AirportDao;




/**
 * 
 * This class implements AirportDao and uses elastic search to realize different functions
 *
 */
public class AirportDaoImpl implements AirportDao{

	@Override
	public String addElement(Airport e) throws Exception {
		ElasticClient.makeConnection();
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_airport", "airport")
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
	public Airport modifyElement(String id, Airport element) throws Exception {
		ElasticClient.makeConnection();
		UpdateRequest updateRequest = new UpdateRequest("index_airport","airport",id).fetchSource(true);    
		Map<String, Object> airport = ConvertClass.objectToJson(element);
		updateRequest.doc(airport);
        UpdateResponse updateResponse = ElasticClient.restHighLevelClient.update(updateRequest);
        ElasticClient.closeConnection();
        return (Airport) ConvertClass.jsonToObject(updateResponse.getGetResult().getSource(),"Airport");
	}

	// 
	@Override
	public boolean deleteElement(Airport e) throws Exception {
		ElasticClient.makeConnection();
		DeleteRequest deleteRequest = new DeleteRequest("index_airport","airport",e.getIcaoCode());
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
		DeleteRequest deletePilotRequest = new DeleteRequest("index_airport", "airport",id);
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
	public List<Airport> consultElement() throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airport");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airport> airports = new ArrayList<Airport>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airports
							.add((Airport) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airport"));
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
		return airports;
	}
	
	public List<Airport> consultElement(String field,String value) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airport");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(field,value));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airport> airports = new ArrayList<Airport>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airports
							.add((Airport) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airport"));
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
		return airports;
	}

	@Override
	public List<Airport> searcheAirportByName(String name) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airport");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("name",name));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airport> airports = new ArrayList<Airport>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airports
							.add((Airport) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airport"));
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
		if( ! airports.isEmpty() ) {
			return airports;
		}
		return null;
	}

	@Override
	public List<Airport> searcheAirportByCode(String code) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airport");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("icaoCode",code));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airport> airports = new ArrayList<Airport>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airports
							.add((Airport) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airport"));
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
		if( ! airports.isEmpty() ) {
			return airports;
		}
		return null;
	}

	@Override
	public List<Airport> searcheAirportByLocation(String location) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airport");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("location",location));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airport> airports = new ArrayList<Airport>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airports
							.add((Airport) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airport"));
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
		if( ! airports.isEmpty() ) {
			return airports;
		}
		return null;
	}


	

	

	
}
