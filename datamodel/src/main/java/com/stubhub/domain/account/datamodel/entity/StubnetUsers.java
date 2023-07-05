package com.stubhub.domain.account.datamodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQuery(name="stubnetUsers.getUserByLoginName",query="from StubnetUsers where nameLogin=:arg1")
@Entity
@Table(name = "STUBNET_USERS")
public class StubnetUsers implements java.io.Serializable {

	@Id
	private Long id;
	@Column(name="NAME_LOGIN")
	private String nameLogin;
	@Column(name="ACTIVE")
	private Boolean active;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNameLogin() {
		return nameLogin;
	}
	public void setNameLogin(String nameLogin) {
		this.nameLogin = nameLogin;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}	
}
