package com.anandhuarjunan.imagetools.model;

import java.util.Objects;

public class Groups {

	private String groupId = null;
	private String name = null;
	private String groupParentId = null;
	
	
	public Groups(String groupId, String name, String groupParentId) {
		super();
		this.groupId = groupId;
		this.name = name;
		this.groupParentId = groupParentId;
	}
	
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupParentId() {
		return groupParentId;
	}
	public void setGroupParentId(String groupParentId) {
		this.groupParentId = groupParentId;
	}


	@Override
	public int hashCode() {
		return Objects.hash(groupId);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Groups other = (Groups) obj;
		return Objects.equals(groupId, other.groupId);
	}


	@Override
	public String toString() {
		return  name;
	}
	
	
}
