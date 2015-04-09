package controllers;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.AuthenticationMethod;
import models.AuthenticationMethod_;
import models.User;
import models.User_;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.close;

import com.fasterxml.jackson.databind.JsonNode;

public class UsersController extends Controller {

	private static final long TIMEOUT_LENGTH = 5000L;

	@Transactional
	public static Result getProfiles() {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		CriteriaQuery<User> all = cq.select(root);
		TypedQuery<User> allQuery = JPA.em().createQuery(all);
		JsonNode jsonNodes = Json.toJson(allQuery.getResultList());

		Logger.debug("got all users");
		return ok(jsonNodes);
	}

	@Transactional
	public static Result getProfile(long id) {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> challengeRoot = cq.from(User.class);
		cq.where(cb.equal(challengeRoot.get(User_.id), id));
		List<User> results = JPA.em().createQuery(cq).getResultList();

		if (results == null || results.size() == 0) {
			Logger.debug("user with id=" + id + " not found");
			return badRequest("user with id=" + id + " not found");
		}
		else {
			Logger.debug("found user with id=" + id);
			JsonNode json = Json.toJson(results.get(0));
			return ok(json);
		}
	}

	@Transactional
	public static Result registerCas() {
		try {
			String netId = authenticateCas().get(TIMEOUT_LENGTH);
			User user = getUser(netId);
			if (user == null) {
				user = new User(netId);
				user.authMethods.add(new AuthenticationMethod(user, netId));
				JPA.em().persist(user);
				Logger.debug("registered user with netId" + netId + " and userId " + user.id);
			}
			session("userAuth", String.valueOf(user.id));
			return ok(close.render("Success"));
		} catch (Exception e) {
			e.printStackTrace();
			return badRequest(close.render("Unable to authenticate user with CAS"));
		}
	}

	@Transactional
	public static Result loginCas() {
		try {
			String netId = authenticateCas().get(TIMEOUT_LENGTH);
			Logger.debug("authenticated user");
			User user = getUser(netId);
			if (user == null) {
				Logger.debug("user with " + netId + " does not exist");
				return badRequest("user does not exist");
			}
			else {
				session("userAuth", String.valueOf(user.id));
				Logger.debug("logged in user with netId " + netId + " and userId " + user.id);
				return ok(close.render("Success"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return badRequest(close.render("Unable to authenticate user with CAS"));
		}
	}

	public static Promise<String> authenticateCas() throws Exception {
		String[] tickets = request().queryString().get("ticket");
		if (tickets == null) {
			throw new Exception("CAS ticket required");
		}

		String ticket = tickets[0];
		String validateTicketURL = "https://cas.byu.edu/cas/serviceValidate"
				+ "?ticket=" + ticket
				+ "&service=" + "http://" + request().host() + request().path();

		Promise<String> netIdPromise = WS.url(validateTicketURL).get().map(response -> {
			String body = response.getBody();
			int start = body.indexOf("<cas:netId>") + "<cas:netId>".length();
			int end = body.indexOf("</cas:netId>");
			String netId = body.substring(start, end);

			return netId;
		});

		return netIdPromise;
	}

	@Transactional
	public static User getUser(String authKey) {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<AuthenticationMethod> cq = cb.createQuery(AuthenticationMethod.class);
		Root<AuthenticationMethod> authenticationMethod = cq.from(AuthenticationMethod.class);
		cq.where(cb.equal(authenticationMethod.get(AuthenticationMethod_.authKey), authKey));
		List<AuthenticationMethod> results = JPA.em().createQuery(cq).getResultList();

		if (results == null || results.size() == 0) {
			Logger.debug("user with authKey=" + authKey + " not found");
			return null;
		}
		else {
			Logger.debug("found user with id=" + results.get(0).id);
			return results.get(0).user;
		}
	}

	@Transactional
	public static Result deleteUser(Long id) {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> challengeRoot = cq.from(User.class);
		cq.where(cb.equal(challengeRoot.get(User_.id), id));
		List<User> results = JPA.em().createQuery(cq).getResultList();

		if (results == null || results.size() == 0) {
			Logger.debug("user with id=" + id + " not found");
			return badRequest("user with id=" + id + " not found");
		}
		else {
			User user = results.get(0);
			JPA.em().remove(user);

			Logger.debug("deleted user with id=" + id);
			return ok("deleted user with id=" + id);
		}
	}
}
