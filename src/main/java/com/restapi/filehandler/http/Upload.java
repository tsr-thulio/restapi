package com.restapi.filehandler.http;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Upload {
	
	String fileName;
	Integer chunkFileNumber;
	Integer uploadTime;
	String downloadLink;
	Status uploadStatus;
	String userId;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getChunkFileNumber() {
		return chunkFileNumber;
	}
	public void setChunkFileNumber(Integer chunkFileNumber) {
		this.chunkFileNumber = chunkFileNumber;
	}
	public Integer getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Integer uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getDownloadLink() {
		return downloadLink;
	}
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
	public Status getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(Status uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
