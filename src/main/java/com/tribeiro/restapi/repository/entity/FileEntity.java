package com.tribeiro.restapi.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tribeiro.restapi.filehandler.models.Status;

@Entity
@Table(name="tb_file")
public class FileEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	Long id;
	
	@Column(name="filename")
	String fileName;
	
	@Column(name="filePartName")
	String filePartName;
	
	@Column(name="file", columnDefinition = "LONGBLOB")
	byte[] file;
	
	@Column(name="userId")
	String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFilePartName() {
		return filePartName;
	}

	public void setFilePartName(String filePartName) {
		this.filePartName = filePartName;
	}
	
}
