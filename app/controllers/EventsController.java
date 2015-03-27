package controllers;

import static play.libs.Json.toJson;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.EventsModel;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class EventsController extends Controller {

	@Transactional
	public static Result getEvents() {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<EventsModel> cq = cb.createQuery(EventsModel.class);
		Root<EventsModel> root = cq.from(EventsModel.class);
		CriteriaQuery<EventsModel> all = cq.select(root);
		TypedQuery<EventsModel> allQuery = JPA.em().createQuery(all);
		JsonNode jsonNodes = toJson(allQuery.getResultList());
		return ok(jsonNodes);
	}

	@Transactional
	public static Result addEvent() {
		JsonNode eventNode = request().body().asJson();
		EventsModel event = Json.fromJson(eventNode, EventsModel.class);
		JPA.em().persist(event);
		return redirect(controllers.routes.EventsController.getEvents());
	}

}
