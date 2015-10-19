package services;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import model.FileModel;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import util.WeatherFTPReader;
import data.WeatherDataHandler;

@Singleton
public class WeatherServiceImpl implements WeatherService {
	@Inject  WeatherFTPReader weatherFtpReader;
	@Inject  WeatherDataHandler weatherDataHandeling;
	@Inject  FileModel filemodel;
	@Inject  ElasticSeachConnection connectionFactory;

	final String index = "weather";
	private static final String type = "filelist";

	/* (non-Javadoc)
	 * @see services.WeatherService#initializeRecordIndex()
	 */
	@Override
	public void indexNextFile(){
			List<Map<String, String>> records = weatherFtpReader.readCSVFileToMap(getNextFileName());
			if(records != null){
				weatherDataHandeling.saveWeatherRecord(records);
				weatherDataHandeling.changeFileStatus(filename);
			}
	}

	private String getNextFileName() {
		weatherDataHandeling.getNextFileName();
		return null;
	}

	/* (non-Javadoc)
	 * @see services.WeatherService#loadWeatherById(java.lang.String)
	 */
	@Override
	public Map<String,Object> loadWeatherById (String id){
		return weatherDataHandeling.loadById(id);
	}

	/* (non-Javadoc)
	 * @see services.WeatherService#loadWeather(java.lang.String)
	 */
	@Override
	public List<Map<String,Object>> searchWeather (String location,String date){
		return weatherDataHandeling.searchByLocationAndDate(location,date);
	}

	/* (non-Javadoc)
	 * @see services.WeatherService#initializeFileList()
	 */
	@Override
	public void initializeFileList() {
		if(!isFileListInitialized()){
			List<String> fileList = weatherFtpReader.listFileName();
			FileModel file = null;
			for(String filename: fileList){
				file = new FileModel();
				String fileDate = filename.substring(7,15);
				String fileSeq = filename.substring(16,18);
				file.setFileDate(fileDate);
				file.setFilename(filename);
				file.setFileReaded(false);
				file.setFileSeq(fileSeq);
				weatherDataHandeling.saveFile(file,filename);
			}
		}
	}

	private boolean isFileListInitialized(){

		return weatherDataHandeling.isFileListInitialized();

	}

	/* (non-Javadoc)
	 * @see services.WeatherService#initializeIndex()
	 */
	@Override
	public void initializeIndex() {
		weatherDataHandeling.createIndex();
	}


}
