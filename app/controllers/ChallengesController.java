package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class ChallengesController extends Controller {

	@Transactional
	public static Result getChallenges() {
		return ok("got challenges");
	}

	@Transactional
	public static Result getChallenge(long id) {
		return ok("got challenge with id " + id);
	}
}
