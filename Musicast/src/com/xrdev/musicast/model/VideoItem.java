package com.xrdev.musicast.model;

import java.math.BigInteger;

public class VideoItem {
	private String videoId;
	private String title;
	private String description;
	private BigInteger viewCount;
	
	public VideoItem(String id, String title, String description, BigInteger viewCount) {
		this.videoId = id;
		this.title = title;
		this.description = description;
		this.viewCount = viewCount;
	}
	
	public String getUrl() {
		return videoId;
	}
	public void setUrl(String url) {
		this.videoId = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setViewCount(BigInteger viewCount) {
		this.viewCount = viewCount;
	}
	public BigInteger getViewCountBigInt() {
		return this.viewCount;
	}
	public String getViewCount() {
		String viewString = this.viewCount.toString();
		return viewString;
	}
	
	
}
