package high_elasticsearch_client;

import java.io.IOException;

import java.util.Map;

import javax.inject.Singleton;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import elements.ConvertClass;


/**
 * 
 * create an elastic search client for seaching 
 *
 */
public class ElasticClient {

	//The config parameters for the connection
	private static final String HOST = "localhost";
	private static final int PORT_ONE = 9200;
	private static final int PORT_TWO = 9201;
	private static final String SCHEME = "http";
	 
	public static RestHighLevelClient restHighLevelClient;
	
	
	/**
	 * Connect to elastic search
	 * @return
	 */
	public static  RestHighLevelClient makeConnection() {
		 
	    if(restHighLevelClient == null) {
	        restHighLevelClient = new RestHighLevelClient(
	                RestClient.builder(
	                        new HttpHost(HOST, PORT_ONE, SCHEME),
	                        new HttpHost(HOST, PORT_TWO, SCHEME)));
	    }
	 
	    return restHighLevelClient;
	}
	
	/**
	 * close elastic search
	 * @throws IOException
	 */
	public static synchronized void closeConnection() throws IOException {
	    restHighLevelClient.close();
	    restHighLevelClient = null;
	}

}
