
import services.WeatherService;
import services.WeatherServiceImpl;
import com.google.inject.AbstractModule;
import data.WeatherDataHandler;
import data.WeatherDataHandlerImpl;

public class SimpleModule extends AbstractModule {

	@Override
	protected void configure() {
		System.out.println("hii");
		
		requestStaticInjection(Global.class);
		
		bind(WeatherService.class).to(WeatherServiceImpl.class);
		bind(WeatherDataHandler.class).to(WeatherDataHandlerImpl.class);
		//bindActor();
		
	}
}