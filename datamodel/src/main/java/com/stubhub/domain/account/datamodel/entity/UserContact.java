
package com.stubhub.domain.account.datamodel.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "UserContact.getUserContactById", query = "from UserContact uc WHERE uc.userContactId = :userContactId"),
        @NamedQuery(name = "UserContact.getDefaultUserContactByOwnerId", query = "FROM UserContact uc WHERE uc.ownerId = :ownerId and uc.defaultContact=1")
})
@Table(name = "USER_CONTACTS")
public class UserContact implements Serializable {

    private static final long serialVersionUID = 6152494156582622269L;

    @Id
    @Column(name = "ID")
    private Long userContactId;

    @Column(name = "OWNER_ID")
    private Long ownerId;

    @Column(name = "ADDR_COUNTRY")
    private String country;

    @Column(name = "ADDR_STATE")
    private String state;

    @Column(name = "ADDR_CITY")
    private String city;

    @Column(name = "ADDR_ZIP")
    private String zip;

    @Column(name = "NAME_FIRST")
    private String firstName;

    @Column(name = "NAME_LAST")
    private String lastName;

    @Column(name = "ADDR1")
    private String street;

    @Column(name = "PHONE1")
    private String phoneNumber;

    @Column(name = "PHONE1_COUNTRY_CALLING_CODE")
    private String phoneCountryCd;

    @Column(name = "DEFAULT_CONTACT")
    private Long defaultContact;
    
    @Column(name = "USER_CONTACT_GUID")
    private String userContactGuid;
    
    @Column(name = "ADDR_EMAIL")
    private String email;

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getUserContactId() {
        return userContactId;
    }

    public void setUserContactId(Long userContactId) {
        this.userContactId = userContactId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getDefaultContact() {
        return defaultContact;
    }

    public void setDefaultContact(Long defaultContact) {
        this.defaultContact = defaultContact;
    }
    
    public String getUserContactGuid() {
		return userContactGuid;
	}

	public void setUserContactGuid(String userContactGuid) {
		this.userContactGuid = userContactGuid;
	}

    public String getPhoneCountryCd() {
        return phoneCountryCd;
    }

    public void setPhoneCountryCd(String phoneCountryCd) {
        this.phoneCountryCd = phoneCountryCd;
    }
}