import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import com.google.inject.Guice;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import services.ElasticSeachConnection;
import services.WeatherService;
import services.IndexingFileService;

public class Global extends GlobalSettings {
	@Inject static WeatherService weatherservice;
	@Inject static ElasticSeachConnection connectionFactory;
	@Inject static IndexingFileService indexingFileService;
	
	public void onStart(Application app) {
		Logger.info("Application has started");
		weatherservice.initializeIndex();
		weatherservice.initializeFileList();
		new Timer().schedule(indexingFileService, 1000);
	}

	public void onStop(Application app) {
		Logger.info("Application shutdown...");
		connectionFactory.destroy();
	}
}