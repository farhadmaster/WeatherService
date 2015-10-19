package services;

import java.util.List;
import java.util.Map;

public interface WeatherService {

	public  void indexNextFile();

	public  Map<String, Object> loadWeatherById(String id);

	public  List<Map<String,Object>> searchWeather(String location,String date);

	public  void initializeFileList();

	public  void initializeIndex();

}