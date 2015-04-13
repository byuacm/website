package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;

import play.Logger;
import play.db.jpa.JPA;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue
	public Long id;
	public String username;
	public String firstName;
	public String lastName;
	public String email;
	@ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "successfulUsers")
	@JsonBackReference
	public Set<Challenge> completedChallenges;

	@JsonIgnore
	public String salt;
	@JsonIgnore
	public String password;
	@OneToMany(cascade = { CascadeType.ALL })
	@JsonIgnore
	public List<AuthenticationMethod> authMethods;

	public User() {

	}

	public User(String username) {
		this.username = username;
		this.salt = BCrypt.gensalt();
		this.password = BCrypt.hashpw("", this.salt);
		this.authMethods = new ArrayList<AuthenticationMethod>();
		this.completedChallenges = new HashSet<Challenge>();
	}

	@Transactional
	public static User getUser(Long userId) {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> challengeRoot = cq.from(User.class);
		cq.where(cb.equal(challengeRoot.get(User_.id), userId));
		List<User> results = JPA.em().createQuery(cq).getResultList();

		if (results == null || results.size() == 0) {
			Logger.debug("user with id=" + userId + " not found");
			return null;
		}
		else {
			Logger.debug("found user with id=" + userId);
			return results.get(0);
		}
	}

	public boolean hasCompletedChallenge(Challenge challenge) {
		return this.completedChallenges.contains(challenge);
	}

	public void completeChallenge(Challenge challenge) {
		this.completedChallenges.add(challenge);
	}
}
