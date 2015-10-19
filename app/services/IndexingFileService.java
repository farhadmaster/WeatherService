package services;

import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IndexingFileService extends TimerTask{
	@Inject WeatherService weatherservice;
	
	@Override
	public void run() {
		
		weatherservice.indexNextFile(); //indexnextfile
	}

}
