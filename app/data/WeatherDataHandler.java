package data;

import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

import model.FileModel;


public interface WeatherDataHandler {

	public  void saveWeatherRecord(List<Map<String, String>> records);

	public  void saveFile(FileModel file, String id);

	public  Map<String, Object> loadById(String id);

	public  List<Map<String,Object>> searchByLocationAndDate(String location,String date);

	public  void changeFileStatus(String id);

	public void createIndex();

	public boolean isFileListInitialized();

	public String getNextFileName();

}