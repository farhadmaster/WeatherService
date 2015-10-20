package services;

import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

public interface WeatherService {

	public  boolean indexNextFile();

	public  Map<String, Object> loadWeatherById(String id);

	public  List<Map<String,Object>> searchWeather(String location,String date);

	public  void initializeFileList();

	public  void initializeIndex();

}