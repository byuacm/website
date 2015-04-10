package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

	public Challenge() {

	}

	public Challenge(String name, Timestamp startTime, Timestamp endTime, String question,
			String solution) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.question = question;
		this.solution = solution;
	}

}
