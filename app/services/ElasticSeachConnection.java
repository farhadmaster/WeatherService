package services;

import javax.inject.Singleton;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


@Singleton
public class ElasticSeachConnection {
	private Client client;

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
