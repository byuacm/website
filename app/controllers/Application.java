package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Event;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

	@Transactional
	public static Result getEvents() {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<Event> cq = cb.createQuery(Event.class);
		Root<Event> root = cq.from(Event.class);
		CriteriaQuery<Event> all = cq.select(root);
		TypedQuery<Event> allQuery = JPA.em().createQuery(all);
		JsonNode jsonNodes = toJson(allQuery.getResultList());
		return ok(jsonNodes);
	}

	@Transactional
	public static Result addEvent() {
		JsonNode eventNode = request().body().asJson();
		Event event = Json.fromJson(eventNode, Event.class);
		JPA.em().persist(event);
		return redirect(controllers.routes.Application.getEvents());
	}

}
