package com.tribeiro.restapi.filehandler.models;

import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement
@ApiModel(description = "Report class to create upload details", value = "ReportModel")
public class Report {
	
	String fileName;
	Integer chunkFileNumber;
	Integer uploadTime;
	Boolean finishedUpload;
	String userId;

	public String getFileName() {
		return fileName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public Boolean getFinishedUpload() {
		return finishedUpload;
	}
	public void setFinishedUpload(Boolean finishedUpload) {
		this.finishedUpload = finishedUpload;
	}
	
}
