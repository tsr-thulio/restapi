package com.restapi.filehandler.controller;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.restapi.filehandler.http.Status;
import com.restapi.filehandler.http.Upload;
import com.restapi.repository.UploadRepository;
import com.restapi.repository.entity.FileEntity;
import com.restapi.repository.entity.UploadEntity;

@Path("/service")
public class FileHandlerController {
	
	private final  UploadRepository repository = new UploadRepository();
	
	@POST  
    @Path("/upload")  
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
    public Response uploadFile(  
            @FormDataParam("fileName") String fileName,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {  
    	
		FileEntity file = new FileEntity();
		try {
        	file.setFile(IOUtils.toByteArray(uploadedInputStream));
        	file.setFileName(fileName);
        	repository.Teste(file);
        	return Response.status(200).build();
        } catch (IOException e) {
        	e.printStackTrace();
        	return Response.status(500).build();
    	}  
    }  
	
//	@POST	
//	@Consumes("application/json; charset=UTF-8")
//	@Produces("application/json; charset=UTF-8")
//	@Path("/upload")
//	public String Cadastrar(Upload upload){
// 
//		UploadEntity entity = new UploadEntity();
// 
//		try {
//			entity.setChunkFileNumber(upload.getChunkFileNumber());
//			entity.setDownloadLink(upload.getDownloadLink());
//			entity.setFileName(upload.getFileName());
//			entity.setUploadStatus(upload.getUploadStatus());
//			entity.setUploadTime(upload.getUploadTime());
//			repository.Save(entity);
//			return "Upload successfuly!";
// 
//		} catch (Exception e) {
// 
//			return "Error uploading file " + e.getMessage();
//		}
// 
//	}
//
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/listUploads")
	public List<Upload> listUploads(){
 
		List<Upload> uploads =  new ArrayList<Upload>();
		List<UploadEntity> uploadEntityList = repository.GetUploads();

		Upload upload;
		for (UploadEntity uploadEntity : uploadEntityList) {
			upload = new Upload();
			upload.setChunkFileNumber(uploadEntity.getChunkFileNumber());
			upload.setDownloadLink(uploadEntity.getDownloadLink());
			upload.setFileName(uploadEntity.getFileName());
			upload.setUploadStatus(uploadEntity.getUploadStatus());
			upload.setUploadTime(uploadEntity.getUploadTime());
			uploads.add(upload);
		} 
		return uploads;
	}
}
