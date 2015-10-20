package services;

import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Singleton;
import services.WeatherService;


@Singleton
public class IndexingFileService extends TimerTask{
	@Inject  WeatherService weatherservice;
	
	@Override 
	public void run() {
		boolean status = weatherservice.indexNextFile(); //indexnextfile
		if (status == false){
			this.cancel();
		}
	}

}
