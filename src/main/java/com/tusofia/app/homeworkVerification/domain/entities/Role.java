package com.tusofia.app.homeworkVerification.domain.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "`roles`")
@Access(AccessType.PROPERTY)
public class Role extends BaseEntity implements GrantedAuthority{

	private String authority;

	public Role() {
	}
	
	public Role(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + authority.hashCode();
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Role other = (Role) obj;
	    if (!authority.equals(other.authority))
	        return false;
	    return true;
	}
}
