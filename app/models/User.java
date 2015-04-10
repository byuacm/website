package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue
	public Long id;
	public String username;
	@JsonIgnore
	public String salt;
	@JsonIgnore
	public String password;
	public String email;
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
	}

	public static User sanitize(User user) {
		User newUser = new User();
		newUser.id = user.id;
		newUser.username = user.username;
		newUser.email = user.email;
		return newUser;
	}
}
