package com.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id")
	private Long id;
   @Column(name = "name")
	private String name;
   @Column(name = "email")
   	private String email;
   @Column(name = "authToken")
	private String authToken;
   @Column(name = "refreshToken")
    private String refreshToken;
   @Column(name="expirytime")
    private Long expirytime;
	public String getEmail() {
		return email;
	}
	public User setEmail(String email) {
		this.email = email;
		return this;
	}
	public String getName() {
		return name;
	}
	public User setName(String name) {
		this.name = name;
		return this;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb
		.append(" ID : ").append(getId())
		.append(" NAME: ").append(getName())
		.append(" EMAIL: ").append(getEmail())
		.append(" AUTH: ").append(getAuthToken())
		.append(" REFRESH: ").append(getRefreshToken())
		.append(" EXPIRY: ").append(getExpirytime());
		
		return sb.toString();
	}
	public String getAuthToken() {
		return authToken;
	}
	public User setAuthToken(String authToken) {
		this.authToken = authToken;
		return this;
	}
	public Long getId() {
		return id;
	}
	public User setId(Long id) {
		this.id = id;
		return this;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public Long getExpirytime() {
		return expirytime;
	}
	public void setExpirytime(Long expirytime) {
		this.expirytime = expirytime;
	}
}

