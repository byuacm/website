package models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
public class EventsModel {
    @Id
    @GeneratedValue
    public Long id;

	@Column
    public String name;

	@Column
	@Type(type="date")
	public Date date;

	@Column
	public String description;


	public EventsModel() {
		this.name = "";
		this.date = new Date();
		this.description = "";
	}

	public EventsModel(String name, Date date, String description) {
		this.name = name;
		this.date = date;
		this.description = description;
	}
}