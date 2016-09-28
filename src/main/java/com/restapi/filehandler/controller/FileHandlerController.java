package com.restapi.filehandler.controller;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.restapi.filehandler.http.Upload;
import com.restapi.repository.FileRepository;
import com.restapi.repository.UploadRepository;
import com.restapi.repository.entity.FileEntity;
import com.restapi.repository.entity.UploadEntity;

@Path("/service")
public class FileHandlerController {

	private final  UploadRepository uploadRepository = new UploadRepository();
	private final  FileRepository fileRepository = new FileRepository();

	@POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("fileName") String fileName,
            @FormDataParam("filePartName") String filePartName,
            @FormDataParam("userId") InputStream userId,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

		if(userId == null) {
			return Response.status(401).build();
		}
		
		FileEntity file = new FileEntity();
		try {
        	file.setFile(IOUtils.toByteArray(uploadedInputStream));
        	file.setFileName(fileName);
        	file.setFilePartName(filePartName);
        	fileRepository.SaveOrUpdate(file);
        	return Response.status(200).build();
        } catch (IOException e) {
        	e.printStackTrace();
        	return Response.status(500).build();
    	}
    }

	@POST
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@Path("/uploadReport")
	public Response SaveReport(Upload upload) {

		UploadEntity entity = new UploadEntity();

		try {
			if(upload.getUploadTime() == null) {
				upload.setUploadTime(0);
			}
			
			entity.setChunkFileNumber(upload.getChunkFileNumber());
			entity.setDownloadLink(upload.getDownloadLink());
			entity.setFileName(upload.getFileName());
			entity.setUploadStatus(upload.getUploadStatus());
			entity.setUploadTime(upload.getUploadTime());
			entity.setUserId(upload.getUserId());
			uploadRepository.SaveOrUpdate(entity, upload.getUploadStatus());
			return Response.status(200).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/listUploads")
	public List<Upload> listUploads() {
 
		List<Upload> uploads =  new ArrayList<Upload>();
		List<UploadEntity> uploadEntityList = uploadRepository.GetUploads();

		Upload upload;
		for (UploadEntity uploadEntity : uploadEntityList) {
			upload = new Upload();
			upload.setChunkFileNumber(uploadEntity.getChunkFileNumber());
			upload.setDownloadLink(uploadEntity.getDownloadLink());
			upload.setFileName(uploadEntity.getFileName());
			upload.setUploadStatus(uploadEntity.getUploadStatus());
			upload.setUploadTime(uploadEntity.getUploadTime());
			upload.setUserId(uploadEntity.getUserId());
			uploads.add(upload);
		}
		return uploads;
	}
	
	@GET
	@Path("/getfile/{fileName}")
	public Response getFile(@PathParam("fileName") String fileName) {
 
		StreamingOutput fileStream =  new StreamingOutput() 
        {
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException 
            {
                try
                {
                	List<FileEntity> files = fileRepository.FindByName(fileName);
                	for (FileEntity fileEntity : files) {
						output.write(fileEntity.getFile());
					}
                    output.flush();
                } 
                catch (Exception e) 
                {
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = " + fileName)
                .build();
    }
}
