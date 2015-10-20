package services;


import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class ElasticSeachConnection {
	private Client client;
	
	@Inject
	private ElasticSeachConnection(){
		initializeInstance();
	}

	private void initializeInstance(){
		try {
			client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1",9300));
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		}
	}

	public Client getInstance(){
		return client;
	}
	public void destroy(){
		client.close();
	}
}
