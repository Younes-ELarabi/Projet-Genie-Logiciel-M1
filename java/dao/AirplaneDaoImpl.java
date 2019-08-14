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

import elements.Airplane;
import elements.Airport;
import elements.ConvertClass;
import high_elasticsearch_client.ElasticClient;
import interfaces.AirplaneDao;

/**
 * This class implements AirplaneDao and uses elastic search to realize different functions
 * 
 *
 */
public class AirplaneDaoImpl implements AirplaneDao{

	@Override
	public String addElement(Airplane e) throws Exception {
		ElasticClient.makeConnection();
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_airplane", "airplane")
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
	public Airplane modifyElement(String id, Airplane element) throws Exception {
		ElasticClient.makeConnection();
		UpdateRequest updateRequest = new UpdateRequest("index_airplane","airplane",id).fetchSource(true);    
		Map<String, Object> airport = ConvertClass.objectToJson(element);
		updateRequest.doc(airport);
        UpdateResponse updateResponse = ElasticClient.restHighLevelClient.update(updateRequest);
        ElasticClient.closeConnection();
        return (Airplane) ConvertClass.jsonToObject(updateResponse.getGetResult().getSource(),"Airplane");
	}
	
	@Override
	public boolean deleteElement(Airplane e) throws Exception {
		return false;
	}
	
	@Override
	public boolean deleteElement(String id) {
		ElasticClient.makeConnection();
		DeleteRequest deletePilotRequest = new DeleteRequest("index_airplane", "airplane",id);
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
	public List<Airplane> consultElement() throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airplane");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airplane> airplanes = new ArrayList<Airplane>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airplanes
							.add((Airplane) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airplane"));
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
		return airplanes;
	}
	
	public List<Airplane> consultElement(String field,String value) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airplane");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(field,value));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airplane> airplanes = new ArrayList<Airplane>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airplanes
							.add((Airplane) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airplane"));
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
		return airplanes;
	}

	@Override
	public Airplane searchePlane(int id) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airplane");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("id",id));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airplane> airplanes = new ArrayList<Airplane>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airplanes
							.add((Airplane) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airplane"));
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
		if( !airplanes.isEmpty() ) {
			return airplanes.get(0);
		}
		return null;
	}

	@Override
	public List<Airplane> searchePlane(String model) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_airplane");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("model",model));
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Airplane> airplanes = new ArrayList<Airplane>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					airplanes
							.add((Airplane) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Airplane"));
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
		return airplanes;
	}

}
