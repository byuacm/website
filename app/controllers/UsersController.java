package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class UsersController extends Controller {

	@Transactional
	public static Result getProfile(long id) {
		return ok("got profile with id " + id);
	}

	@Transactional
	public static Result register() {
		return ok("registered new user");
	}

	@Transactional
	public static Result login() {
		return ok("logged in");
	}

}
