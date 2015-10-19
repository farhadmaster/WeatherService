
import com.google.inject.AbstractModule;

public class SimpleModule extends AbstractModule {

	@Override
	protected void configure() {
		System.out.println("hii");
		requestStaticInjection(Global.class);
	}

}