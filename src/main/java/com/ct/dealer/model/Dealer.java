package com.ct.dealer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Cacheable
@Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@Table( name = "dealer", uniqueConstraints = { @UniqueConstraint( columnNames = { "email" } ) } )
public class Dealer {
	@Id
	@Column( name = "id" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private long id;

	@Size( min = 1, max = 50 )
	@Column( name = "name" )
	private String name;

	@Size( min = 0, max = 50 )
	@Column( name = "city" )
	private String city;

	@NaturalId
	@NotBlank
	@Size( max = 50 )
	@Email
	private String email;

	@Column( name = "is_active", nullable = false )
	private Boolean active = false;

	public Dealer() {}

	public Dealer( long id, @Size( min = 1, max = 50 ) String name, @Size( min = 0, max = 50 ) String city, Boolean active,
			User user, String email ) {
		this.name = name;
		this.city = city;
		this.active = active;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId( long id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity( String city ) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive( Boolean active ) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Dealer: '" + name + " from City: " + city + ", Registered: " + active;
	}
}
