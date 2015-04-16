package models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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

	public void addSuccessfulUser(User user) {
		this.successfulUsers.add(user);
	}

}
