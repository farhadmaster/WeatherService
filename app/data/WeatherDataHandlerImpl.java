package data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import model.FileModel;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.min.Min;

import services.ElasticSeachConnection;
import util.HashUtil;

import com.google.gson.Gson;

@Singleton
public class WeatherDataHandlerImpl implements WeatherDataHandler {
	@Inject
	HashUtil hashUtil;
	@Inject
	ElasticSeachConnection connection;
	private static final String INDEX = "weather";
	private static final String TYPE_FILE = "file";
	private static final String TYPE_WEATHER = "weatherecord";

	/* (non-Javadoc)
	 * @see Data.WeatherDataHandler#saveRecord(java.util.List, java.lang.String)
	 */
	@Override
	public void saveWeatherRecord(List<Map<String, String>> records) {

		Client client = connection.getInstance();
			for(Map<String, String> record: records){
				if(!"DATE".equals(record.get("DATE"))){
					String id = hashUtil.enocode(record.get("LOCATION"),record.get("DATE"), record.get("TIME"), record.get("LEADTIME"));
					IndexRequest indexRequest = new IndexRequest(INDEX, TYPE_WEATHER,id);
					indexRequest.source(record);
					IndexResponse response = client.index(indexRequest).actionGet();
				}
			}
	}

	/* (non-Javadoc)
	 * @see Data.WeatherDataHandler#saveFile(model.FileModel, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveFile(FileModel file,String id) {

		Client client = connection.getInstance();
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE_FILE,id);
		GetResponse idExist = client.prepareGet(INDEX, TYPE_FILE, id).setRefresh(true).execute().actionGet();
		if(!idExist.isExists()){
			indexRequest.source(new Gson().toJson(file));
			IndexResponse response = client.index(indexRequest).actionGet();
		}
	}

	/* (non-Javadoc)
	 * @see Data.WeatherDataHandler#loadById(java.lang.String)
	 */
	@Override
	public Map<String,Object> loadById(String id) {

		String codedId = hashUtil.enocode(id);
		Client client = connection.getInstance();
		GetResponse response = client.prepareGet(INDEX, TYPE_WEATHER, codedId)
				.execute()
				.actionGet();
		Map<String,Object> record = response.getSourceAsMap();
		if(response.isExists()){
			return record;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see Data.WeatherDataHandler#load(java.lang.String)
	 */
	@Override
	public List<Map<String,Object>> searchByLocationAndDate(String location, String date) {

		List<Map<String,Object>> weatherRecord = new ArrayList<Map<String,Object>>();
		Client client = connection.getInstance();
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		QueryBuilder queryBuilder1 = QueryBuilders.matchQuery("LOCATION",location);
		QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("DATE", date);
		boolQuery.must(queryBuilder1).must(queryBuilder2);
		SearchResponse result = client.prepareSearch(INDEX).setTypes(TYPE_WEATHER).setQuery(boolQuery).execute().actionGet();
		SearchHit[] hits = result.getHits().hits();
		for (SearchHit hit : hits) {
			weatherRecord.add(hit.sourceAsMap());
		}
		return weatherRecord;
	}

	/* (non-Javadoc)
	 * @see Data.WeatherDataHandler#changeFileStatus(java.lang.String)
	 */
	@Override
	public void changeFileStatus(String filename) {
		Client client = connection.getInstance();
		 

//		 client.prepareUpdate(INDEX, TYPE_FILE, filename)
//        .setScript("ctx._source.fileReaded = " + true  , ScriptType.INLINE)
//        .get();
		
		   UpdateRequestBuilder br = client.prepareUpdate(INDEX, TYPE_FILE, filename);
		    br.setDoc("{\"fileReaded\":" + true +"}");
		    br.execute();
		    
		
		    
//		try {
//			client.prepareUpdate(INDEX, TYPE_FILE, filename).setScript("ctx._source.fileReaded += fileReaded", ScriptType.INLINE ).addScriptParam("fileReaded", false).execute().actionGet();
//		} catch (ElasticsearchException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}

	@Override
	public void createIndex() {

		Client client = connection.getInstance();
		ListenableActionFuture<IndicesExistsResponse> checkIndex = client.admin().indices().prepareExists(INDEX).execute();
		if (!checkIndex.actionGet().isExists()) {
			Settings indexSettings = ImmutableSettings.settingsBuilder()
					.put("number_of_shards", 1).put("number_of_replicas", 1)
					.build();
			CreateIndexRequest createRequest = new CreateIndexRequest(INDEX, indexSettings);
			client.admin().indices().create(createRequest).actionGet();
		}
	}

	@Override
	public boolean isFileListInitialized() {
		Client client = connection.getInstance();
		TypesExistsResponse response = client.admin().indices().prepareTypesExists(INDEX).setTypes(TYPE_FILE).execute().actionGet();
		if(response.isExists()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String getNextFileName() {
		Client client = connection.getInstance();
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		QueryBuilder queryBuilder1 = QueryBuilders.matchQuery("fileReaded",false);
		SearchResponse result1 = client.prepareSearch(INDEX).setTypes(TYPE_FILE).setQuery(queryBuilder1).setSize(0).addAggregation(AggregationBuilders.min("minorderId").field("orderId")).execute().actionGet();
		int min = (int)((Min)result1.getAggregations().get("minorderId")).getValue();
		
		QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("orderId",min);
		SearchResponse result2 = client.prepareSearch(INDEX).setTypes(TYPE_FILE).setQuery(queryBuilder2).execute().actionGet();
		
		if(result2.getHits().totalHits()>0)	{	
		return result2.getHits().getAt(0).getSource().get("filename").toString();
		}
		return null;
	}
	
	
	
}
