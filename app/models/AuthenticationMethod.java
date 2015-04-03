package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AuthenticationMethod {
	@Id
	@GeneratedValue
	public Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	public User user;
	public String authKey;

	public AuthenticationMethod() {

	}

	public AuthenticationMethod(User user, String authKey) {
		this.user = user;
		this.authKey = authKey;
	}
}
