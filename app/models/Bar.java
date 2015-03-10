package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bar {

    @Id
    @GeneratedValue
    public long id;

    public long foo_id;



}
