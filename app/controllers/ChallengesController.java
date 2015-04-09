package controllers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import models.Challenge;
import models.Challenge_;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class ChallengesController extends Controller {

	@Transactional
	public static Result getChallenges() {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<Challenge> cq = cb.createQuery(Challenge.class);
		Root<Challenge> root = cq.from(Challenge.class);
		CriteriaQuery<Challenge> all = cq.select(root);
		TypedQuery<Challenge> allQuery = JPA.em().createQuery(all);
		JsonNode jsonNodes = Json.toJson(allQuery.getResultList());

		Logger.debug("got all challenges");
		return ok(jsonNodes);
	}

	@Transactional
	public static Result getChallenge(Long id) {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<Challenge> cq = cb.createQuery(Challenge.class);
		Root<Challenge> challengeRoot = cq.from(Challenge.class);
		cq.where(cb.equal(challengeRoot.get(Challenge_.id), id));
		List<Challenge> results = JPA.em().createQuery(cq).getResultList();

		if (results == null || results.size() == 0) {
			Logger.debug("challenge with id=" + id + " not found");
			return badRequest("challenge with id=" + id + " not found");
		}
		else {
			Logger.debug("found challenge with id=" + id);
			JsonNode json = Json.toJson(results.get(0));
			return ok(json);
		}
	}

	@Transactional
	public static Result getOpenChallenges() {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<Challenge> cq = cb.createQuery(Challenge.class);
		Root<Challenge> challengeRoot = cq.from(Challenge.class);
		Timestamp now = new Timestamp(System.currentTimeMillis());

		Path<Timestamp> startTime = challengeRoot.get(Challenge_.startTime);
		Path<Timestamp> endTime = challengeRoot.get(Challenge_.endTime);

		Predicate hasOpened = cb.lessThanOrEqualTo(startTime, now);
		Predicate hasNotClosed = cb.greaterThanOrEqualTo(endTime, now);
		Predicate isOpen = cb.and(hasOpened, hasNotClosed);
		cq.where(isOpen);

		List<Challenge> results = JPA.em().createQuery(cq).getResultList();

		JsonNode json = null;
		if (results == null || results.size() == 0) {
			Logger.debug("no open challenges found");
			json = Json.newObject();
		}
		else {
			json = Json.toJson(results);
		}
		return ok(json);
	}

	@Transactional
	public static Result createChallenge() {
		try {
			JsonNode json = request().body().asJson();
			String name = json.findPath("name").textValue();
			Timestamp startTime = Timestamp.valueOf(json.findPath("startTime").textValue());
			Timestamp endTime = Timestamp.valueOf(json.findPath("endTime").textValue());
			String question = json.findPath("question").textValue();
			String solution = json.findPath("solution").textValue();

			Challenge newChallenge = new Challenge(name, startTime, endTime, question, solution);
			JPA.em().persist(newChallenge);

			Logger.debug("created challenge with name=" + name);
			return ok("created challenge");
		} catch (NullPointerException e) {
			Logger.debug("failed to create challenge - invalid format");
			return badRequest("failed to create challenge - invalid format");
		}
	}

	@Transactional
	public static Result deleteChallenge(Long id) {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<Challenge> cq = cb.createQuery(Challenge.class);
		Root<Challenge> challengeRoot = cq.from(Challenge.class);
		cq.where(cb.equal(challengeRoot.get(Challenge_.id), id));
		List<Challenge> results = JPA.em().createQuery(cq).getResultList();

		if (results == null || results.size() == 0) {
			Logger.debug("challenge with id=" + id + " not found");
			return badRequest("challenge with id=" + id + " not found");
		}
		else {
			Challenge challenge = results.get(0);
			JPA.em().remove(challenge);

			Logger.debug("deleted challenge with id=" + id);
			return ok("deleted challenge with id=" + id);
		}
	}
}
