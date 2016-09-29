package com.tribeiro.restapi.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tribeiro.restapi.filehandler.models.Status;

@Entity
@Table(name="tb_upload")
public class UploadEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	Long id;
	
	@Column(name="filename")
	String fileName;
	
	@Column(name="chunkFileNumber")
	Integer chunkFileNumber;
	
	@Column(name="uploadTime")
	Integer uploadTime;
	
	@Column(name="downloadLink")
	String downloadLink;
	
	@Column(name="uploadStatus")
	Status uploadStatus;
	
	@Column(name="userId")
	String userId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
