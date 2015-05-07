package com.dictionary.object;



public class DictionaryObject implements Comparable<DictionaryObject>{
	private String name = "";
	private String source = "";
	private String source_2 = "";
	private String sizeInfo = "";
	private int version = 1;
	private String groupName;
	private boolean isSectionHeader;

	public DictionaryObject(String name, String source, String source_2, String sizeInfo, int version, String groupName) {
		this.name = name;
		this.sizeInfo = sizeInfo;
		this.source = source;
		this.source_2 = source_2;
		this.version = version;
		this.groupName = groupName;
		isSectionHeader = false;
	}
	
	// Header
	public DictionaryObject(String groupName) {
		this.groupName = groupName;
		isSectionHeader = true;
	}

	public String getName() {
		return name;
	}

	public String getSource() {
		return source;
	}

	public String getSizeInfo() {
		return sizeInfo;
	}

	public int getVersion() {
		return version;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource_2() {
		return source_2;
	}

	public void setSource_2(String source_2) {
		this.source_2 = source_2;
	}

	public void setSizeInfo(String sizeInfo) {
		this.sizeInfo = sizeInfo;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "name: " + getName() + " source: " + getSource() + " size = " + getSizeInfo() + " version = " + getVersion() + " groupName: "
				+ getGroupName();
	}
	
	@Override
	public int compareTo(DictionaryObject other) {
		return this.groupName.compareTo(other.groupName);
	}

	public boolean isSectionHeader() {
		return isSectionHeader;
	}

	public void setSectionHeader(boolean isSectionHeader) {
		this.isSectionHeader = isSectionHeader;
	}

}
