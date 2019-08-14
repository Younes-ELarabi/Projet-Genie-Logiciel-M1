package dao;

import java.io.IOException;
import java.util.ArrayList;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import elements.ConvertClass;
import elements.Flight;
import elements.Passenger;
import elements.Pilot;
import elements.Reservation;
import high_elasticsearch_client.ElasticClient;
import interfaces.ReservationDao;



/**
 * 
 * This class implements ReservationDao and uses elastic search to realize different functions
 *
 */
public class ReservationDaoImpl implements ReservationDao{

	@Override
	public String addElement(String id,Reservation e) throws Exception {
		ElasticClient.makeConnection();
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_reservation", "reservation",id)
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
			System.out.println(response);
			return response.toString();
		}
		return "no-response";
	}

	@Override
	public Reservation modifyElement(String id, Reservation element) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteElement(Reservation e) throws Exception {
		ElasticClient.makeConnection();
		DeleteRequest deleteRequest = new DeleteRequest("index_reservation","reservation", String.valueOf(e.getId()));
		@SuppressWarnings("unused")
		DeleteResponse deleteResponse = null;
	    try {
	        deleteResponse = ElasticClient.restHighLevelClient.delete(deleteRequest);
	        ElasticClient.closeConnection();
	        return true;
	    } catch (java.io.IOException exp){
	        exp.getLocalizedMessage();
	    }
	    ElasticClient.closeConnection();
		return false;

	}

	@Override
	public boolean deleteElement(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Reservation> consultElement() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Reservation> searchPilotReservation(String pilot_username) throws IOException {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_reservation");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("flight.pilot.userName",pilot_username));										
		//QueryBuilder query=QueryBuilders.matchAllQuery();
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Reservation> reservations = new ArrayList<Reservation>();

		if (searchHit.length > 0) {
			for(int i=0;i<searchHit.length;i++) {
					try {
						reservations.add((Reservation) ConvertClass.jsonToObject(searchHit[i].getSourceAsMap(), "Reservation"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		ElasticClient.closeConnection();
		return reservations;
	}

	@Override
	public List<Reservation> searchPassengerReservation(String p) throws IOException {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_reservation");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("passenger.userName",p));
													
		//QueryBuilder query=QueryBuilders.matchAllQuery();
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Reservation> reservations = new ArrayList<Reservation>();

		if (searchHit.length > 0) {
			for(int i=0;i<searchHit.length;i++) {
					try {
						reservations.add((Reservation) ConvertClass.jsonToObject(searchHit[i].getSourceAsMap(), "Reservation"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		ElasticClient.closeConnection();
		return reservations;
	}

	@Override
	public String addElement(Reservation e) throws Exception {
		ElasticClient.makeConnection();
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_reservation", "reservation",e.getId())
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
			System.out.println(response);
			return response.toString();
		}
		return "no-response";
	
	}

	
	@Override
	public String agree(Reservation e) throws Exception {
		Passenger passenger=e.getpassenger();
		int nbseat=e.getNbseat();
		Flight flight=e.getFlight();
		flight.getPassengers().add(passenger);
		flight.setAvailableSeat(flight.getAvailableSeat()-nbseat);
		(new FlightDaoImpl()).addElement(flight);
		this.deleteElement(e);
		return "agree";
	}
	
	

	@Override
	public String refuse(Reservation e) throws Exception {
		this.deleteElement(e);
		return "refuse";
	}
	
	
	
	
	public Reservation searchReservation(String id) throws Exception {
		ElasticClient.makeConnection();
		GetRequest getreservationRequest = new GetRequest("index_reservation", "reservation",id);
		Reservation reservation = null;
		GetResponse getResponse = null;
		getResponse = ElasticClient.restHighLevelClient.get(getreservationRequest);
		reservation = (Reservation) ConvertClass.jsonToObject(getResponse.getSourceAsMap(), "Reservation");
		ElasticClient.closeConnection();
		return reservation;
	}

	
}
