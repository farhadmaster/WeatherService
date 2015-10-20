package controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import javax.inject.Inject;

import util.HashUtil;
import views.html.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.WeatherService;
import play.*;
import play.mvc.*;


public class WeatherRestAPI extends Controller  {

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

	public Result getWeatherByLocationAndDate(String location,String date){
		
		List<Map<String,Object>> hits = weatherService.searchWeather(location,date);

		JsonNode json;
		json = Json.toJson(hits);
		Result jsonResult = ok(json);
		return jsonResult;

	}

}
