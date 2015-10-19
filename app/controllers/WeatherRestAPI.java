package controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;

import com.fasterxml.jackson.databind.JsonNode;

import util.HashUtil;
import views.html.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.WeatherService;
import play.*;
import play.mvc.*;


public class WeatherRestAPI extends Controller {

	@Inject WeatherService weatherService;
	@Inject HashUtil hashUtil;

	public Result index() {
		return ok(index.render("Your new application is ready."));
		//return redirect(controllers.routes.Application.hello("farhad"));
	}

	public Result getWeatherById(String id){
		Map<String,Object> hit = weatherService.loadWeatherById(id);
		JsonNode json = Json.toJson(hit);
		Result jsonResult = ok(json);
		return jsonResult;
	}

	public Result getWeatherByLocationAndDate(String input){
		
		final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
     
        String location = request().getQueryString("loaction");
        String date = request().getQueryString("date");
		
		List<Map<String,Object>> hits = weatherService.searchWeather(location,date);
		
		
		
		JsonNode json;
		try {
			json = Json.toJson(hits.string());
			Result jsonResult = ok(json);
			return jsonResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
