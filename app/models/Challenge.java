package models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import play.Logger;
import play.db.jpa.JPA;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Challenge {

	@Id
	@GeneratedValue
	public Long id;
	public String name;
	public Timestamp startTime;
	public Timestamp endTime;
	public String question;
	public String solution;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JsonManagedReference
	public Set<User> successfulUsers;

	public Challenge() {

	}

	public Challenge(String name, Timestamp startTime, Timestamp endTime, String question,
			String solution) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.question = question;
		this.solution = solution;
		this.successfulUsers = new HashSet<User>();
	}

	@Transactional
	public static Challenge getChallenge(Long challengeId) {
		CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
		CriteriaQuery<Challenge> cq = cb.createQuery(Challenge.class);
		Root<Challenge> challengeRoot = cq.from(Challenge.class);
		cq.where(cb.equal(challengeRoot.get(Challenge_.id), challengeId));
		List<Challenge> results = JPA.em().createQuery(cq).getResultList();

		if (results == null || results.size() == 0) {
			Logger.debug("challenge with id=" + challengeId + " not found");
			return null;
		}
		else {
			Logger.debug("found challenge with id=" + challengeId);
			return results.get(0);
		}
	}

	public void addSuccessfulUser(User user) {
		this.successfulUsers.add(user);
	}

}
