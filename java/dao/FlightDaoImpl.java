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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import elements.Airplane;
import elements.Airport;
import elements.ConvertClass;
import elements.Flight;
import elements.Passenger;
import high_elasticsearch_client.ElasticClient;
import interfaces.FlightDao;


/**
 * 
 * This class implements FlightDao and uses elastic search to realize different functions
 *
 */
public class FlightDaoImpl implements FlightDao{
	
	@Override
	public String addElement(Flight e) throws Exception {
		ElasticClient.makeConnection();
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_flight", "flight",e.getId())
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
	public Flight modifyElement(String id, Flight element) throws Exception {
		ElasticClient.makeConnection();
		UpdateRequest updateRequest = new UpdateRequest("index_flight","flight",id).fetchSource(true);    
		Map<String, Object> flight = ConvertClass.objectToJson(element);
		updateRequest.doc(flight);
        UpdateResponse updateResponse = ElasticClient.restHighLevelClient.update(updateRequest);
        ElasticClient.closeConnection();
        return (Flight) ConvertClass.jsonToObject(updateResponse.getGetResult().getSource(),"Flight");
	}

	@Override
	public boolean deleteElement(Flight e) throws Exception {
		ElasticClient.makeConnection();
		DeleteRequest deleteRequest = new DeleteRequest("index_flight","flight", String.valueOf(e.getId()));
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
		ElasticClient.makeConnection();
		DeleteRequest deleteRequest = new DeleteRequest("index_flight","flight",id);
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
	    try {
			ElasticClient.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Flight> consultElement() throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Flight> flights = new ArrayList<Flight>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					Map<String,Object> response = hit.getSourceAsMap();
					//System.out.println("response :"+response);
					flights
							.add((Flight) ConvertClass.jsonToObject(response, "Flight"));
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
		//System.out.println(flights);
		return flights;
	}
	
	public List<Flight> consultElement(String date,String location) throws Exception {
		System.out.println(date+" "+location);
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("departureDate").from(date))
													.must(QueryBuilders.termQuery("departureAirport.location", location));
		//QueryBuilder query=QueryBuilders.matchAllQuery();
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Flight> flights = new ArrayList<Flight>();

		if (searchHit.length > 0) {
			for(int i=0;i<searchHit.length;i++) {
					try {
						flights.add((Flight) ConvertClass.jsonToObject(searchHit[i].getSourceAsMap(), "Flight"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		System.out.println(flights+""+searchHit.length);

		ElasticClient.closeConnection();
		return flights;
	}
	
	public List<Flight> consultElement(String query) throws Exception {
		System.out.println(query);
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "departureDate","departureAirport.location"));
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Flight> flights = new ArrayList<Flight>();

		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
			.forEach(hit -> {
				try {
					flights
							.add((Flight) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Flight"));
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
		//System.out.println(flights);
		return flights;
	}

	@Override
	public Flight searchFlight(String id) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("id",id));
												
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(10);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();
		SearchHit hit=searchHit[0];
		Flight flight = (Flight) ConvertClass.jsonToObject(hit.getSourceAsMap(), "Flight");
		System.out.println(flight+""+searchHit.length);

		ElasticClient.closeConnection();
		return flight;
	}
	
	@Override
	public boolean addPassengerToFlight(Passenger p, Flight f) {
		f.setPassenger(p);
		try {
			modifyElement(f.getId(), f);
			//modifyElement("06HmE2oBdRgtEgfcH9RB",f);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Flight> searchFlight(String departureAirport, String duree, String departureDate) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public List<Flight> searchFlightDuree(String duree) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Flight> searchFlightDeparDate(String departureDate) throws Exception {
		
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("departureDate").from(departureDate));
													
		//QueryBuilder query=QueryBuilders.matchAllQuery();
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Flight> flights = new ArrayList<Flight>();

		if (searchHit.length > 0) {
			for(int i=0;i<searchHit.length;i++) {
					try {
						flights.add((Flight) ConvertClass.jsonToObject(searchHit[i].getSourceAsMap(), "Flight"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		System.out.println(flights+""+searchHit.length);

		ElasticClient.closeConnection();
		return flights;
		
	}

	@Override
	public List<Flight> searchFlightPlane(Airplane plane) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Flight> searchFlightArrivAirport(Airport arrivAirport) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Flight> searchFlightPliot(String pilotUsername) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("pilot.userName",pilotUsername));
													
		//QueryBuilder query=QueryBuilders.matchAllQuery();
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Flight> flights = new ArrayList<Flight>();

		if (searchHit.length > 0) {
			for(int i=0;i<searchHit.length;i++) {
					try {
						flights.add((Flight) ConvertClass.jsonToObject(searchHit[i].getSourceAsMap(), "Flight"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		System.out.println(flights+""+searchHit.length);

		ElasticClient.closeConnection();
		return flights;
		
	}

	

	@Override
	public List<Flight> searchFlightPassenger(String p) throws Exception {
		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("passengers.userName",p));
													
		//QueryBuilder query=QueryBuilders.matchAllQuery();
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Flight> flights = new ArrayList<Flight>();

		if (searchHit.length > 0) {
			for(int i=0;i<searchHit.length;i++) {
					try {
						flights.add((Flight) ConvertClass.jsonToObject(searchHit[i].getSourceAsMap(), "Flight"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		System.out.println(flights+""+searchHit.length);

		ElasticClient.closeConnection();
		return flights;
	}

	@Override
	public List<Flight> searchFlightNbSeat(int nb) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Flight> searchFlightPrice(double price) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Flight> searchFlightAirport(String airport_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Flight> searchFlightAirportAndDate(String airport_name, String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Flight> searchFlightLocation(String location) throws Exception {

		ElasticClient.makeConnection();
		SearchRequest searchRequest = new SearchRequest("index_flight");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("departureAirport.location",location));
													
		//QueryBuilder query=QueryBuilders.matchAllQuery();
		System.out.println(query);
		searchSourceBuilder.query(query);		
		searchSourceBuilder.size(100);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = ElasticClient.restHighLevelClient.search(searchRequest);
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHit = hits.getHits();
		//SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Flight> flights = new ArrayList<Flight>();

		if (searchHit.length > 0) {
			for(int i=0;i<searchHit.length;i++) {
					try {
						flights.add((Flight) ConvertClass.jsonToObject(searchHit[i].getSourceAsMap(), "Flight"));
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		System.out.println(flights+""+searchHit.length);
		ElasticClient.closeConnection();
		return flights;
		
	}

	@Override
	public String addElement(String id, Flight e) throws Exception {
		ElasticClient.makeConnection();
	    Map<String, Object> data = ConvertClass.objectToJson(e);
	    IndexRequest indexRequest = new IndexRequest("index_flight", "flight",id)
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

}








