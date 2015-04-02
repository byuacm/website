package controllers;

import play.libs.F.Promise;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Result;

import com.typesafe.config.ConfigFactory;

public class EventsController extends Controller {

	public static Promise<Result> getEvents() {
		String apiKey = ConfigFactory.load().getString("application.calendar_api_key");
		String calendarRequestURL = "https://www.googleapis.com/calendar/v3/calendars/cs.byu.acm%40gmail.com/events"
				+ "?fields=items%28description%2Cend%2ChtmlLink%2Clocation%2Cstart%2Csummary%29"
				+ "&key=" + apiKey;

		Promise<Result> promise = WS.url(calendarRequestURL).get().map(response -> {
			return ok(response.asJson());
		});

		return promise;
	}
}
