package com.xrdev.musicast.model;

public class VideoItem {
	private String url;
	private String title;
	private String description;
	private int duration;
	
	public VideoItem(String url, String title, String description, int duration) {
		this.url = url;
		this.title = title;
		this.description = description;
		this.duration = duration;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
	
}
