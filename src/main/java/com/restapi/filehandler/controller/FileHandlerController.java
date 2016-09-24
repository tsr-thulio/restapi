package com.restapi.filehandler.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.restapi.filehandler.http.Status;
import com.restapi.filehandler.http.Upload;
import com.restapi.repository.entity.UploadEntity;

@Path("/service")
public class FileHandlerController {
	
	@POST	
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@Path("/upload")
	public String Cadastrar(Upload upload){
 
		UploadEntity entity = new UploadEntity();
 
		try {
			entity.setChunkFileNumber(upload.getChunkFileNumber());
			entity.setDownloadLink(upload.getDownloadLink());
			entity.setFileName(upload.getFileName());
			entity.setUploadStatus(upload.getUploadStatus());
			entity.setUploadTime(upload.getUploadTime());
			return "Upload successfuly!";
 
		} catch (Exception e) {
 
			return "Error uploading file " + e.getMessage();
		}
 
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/listUploads")
	public List<Upload> listUploads(){
 
		List<Upload> uploads =  new ArrayList<Upload>();
 
		Upload upload;
		for (int i = 0; i < 3; i++) {
			upload = new Upload();
			upload.setChunkFileNumber(i);
			upload.setDownloadLink("linktodownload" + i);
			upload.setFileName("File number " + i);
			upload.setUploadTime(100 + i);
			if(i == 1) {
				upload.setUploadStatus(Status.DONE);
			} else if(i == 2) {
				upload.setUploadStatus(Status.FAILED);
			} else {
				upload.setUploadStatus(Status.PROCESSING);
				
			}
			uploads.add(upload);
		}
 
		return uploads;
	}
}
