import play.api.inject.guice.GuiceApplicationLoader;


public class CustomApplicationLoader extends GuiceApplicationLoader {
	
	@Override
	public play.api.inject.guice.GuiceApplicationBuilder builder(play.api.ApplicationLoader.Context context) {
		System.out.println("hiii ");
		
		return super.builder(context);
	}
	
}
