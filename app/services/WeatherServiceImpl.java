package services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import model.FileModel;
import util.WeatherFTPReader;
import data.WeatherDataHandler;

@Singleton
public class WeatherServiceImpl implements WeatherService {
	@Inject  WeatherFTPReader weatherFtpReader;
	@Inject  WeatherDataHandler weatherDataHandeling;
	@Inject  FileModel filemodel;
	@Inject  ElasticSeachConnection connectionFactory;

	/* (non-Javadoc)
	 * @see services.WeatherService#initializeRecordIndex()
	 */
	@Override
	public boolean indexNextFile(){
		String filename = getNextFileName();
		if(filename.equals("weeronl20150903M34_999257.csv.bz2")){
		
		if(filename != null){
			List<Map<String, String>> records = weatherFtpReader.readCSVFileToMap(filename);
			
			if(records != null){
				weatherDataHandeling.saveWeatherRecord(records);
				weatherDataHandeling.changeFileStatus(filename);
				
			}
			return true;
		}}
		return false;
	}

	private String getNextFileName() {
		return weatherDataHandeling.getNextFileName();
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
			int orderId = 0;
			for(String filename: fileList){
				file = new FileModel();
				String fileDate = filename.substring(7,15);
				String fileSeq = filename.substring(16,18);
				file.setFileDate(fileDate);
				file.setFilename(filename);
				file.setFileReaded(false);
				file.setFileSeq(fileSeq);
				file.setOrderId(orderId);
				weatherDataHandeling.saveFile(file,filename);
				orderId++;
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
