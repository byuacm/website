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

import org.mindrot.jbcrypt.BCrypt;

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

	public boolean hasCompletedChallenge(Challenge challenge) {
		return this.completedChallenges.contains(challenge);
	}

	public void completeChallenge(Challenge challenge) {
		this.completedChallenges.add(challenge);
	}
}
