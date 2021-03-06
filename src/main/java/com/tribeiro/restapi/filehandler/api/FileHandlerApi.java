package com.tribeiro.restapi.filehandler.api;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
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

import com.tribeiro.restapi.filehandler.models.Report;
import com.tribeiro.restapi.filehandler.models.Status;
import com.tribeiro.restapi.filehandler.models.Upload;
import com.tribeiro.restapi.filehandler.service.FileHandlerService;
import com.tribeiro.restapi.repository.FileRepository;
import com.tribeiro.restapi.repository.UploadRepository;
import com.tribeiro.restapi.repository.entity.FileEntity;
import com.tribeiro.restapi.repository.entity.UploadEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {"filehandler"})
@Path("/filehandler")
public class FileHandlerApi {

	private final  UploadRepository uploadRepository = new UploadRepository();
	private final  FileRepository fileRepository = new FileRepository();
	private static final String UPLOAD_SUCCESS_MESSAGE = "File uploaded successfuly";
	private static final String UPLOAD_REPORT_SUCCESS_MESSAGE = "Report saved successfuly";
	private static final String GET_FILE_ERROR_MESSAGE = "Error joining files";
	private static final String NO_ENTITY_FOUND_MESSAGE = "No entity found";

	@POST
    @Path("/upload")
	@ApiOperation(value = "Upload file for databade", 
	    notes = "This method saves the uploaded file validating the size of input (limited for 1Mb) and the user identification",
	    response = Response.class)
	@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized to perform this action, set user identificator!"),
			@ApiResponse(code = 500, message = "Error treating file parse"),
			@ApiResponse(code = 500, message = "Please fill all fields"),
			@ApiResponse(code = 200, message = "File uploaded successfuly")})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
    		@ApiParam(name = "fileName", value = "Name of the file being uploaded", required = false)
            @FormDataParam("fileName") String fileName,
            @ApiParam(name = "filePartName", value = "Name of the file part of the archive", required = false)
            @FormDataParam("filePartName") String filePartName,
            @ApiParam(name = "userId", value = "Identificator of uploader", required = false)
            @FormDataParam("userId") String userId,
            @ApiParam(name = "file", value = "File to be uploaded", required = false)
            @FormDataParam("file") InputStream uploadedInputStream,
            @ApiParam(name = "fileDetail", value = "Details of the file to be uploaded", required = false)
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
		
		FileEntity file = new FileEntity();
		try {
			FileHandlerService.validateUploadFile(fileName, filePartName, userId, fileDetail, uploadedInputStream);
        	file.setFile(IOUtils.toByteArray(uploadedInputStream));
        	file.setFileName(fileName);
        	file.setFilePartName(filePartName);
        	file.setUserId(userId);
        	fileRepository.SaveOrUpdate(file);
        	return Response.status(200).entity(UPLOAD_SUCCESS_MESSAGE).build();
        } catch (IOException e) {
        	e.printStackTrace();
        	return Response.status(500).entity("Error treating file parse").build();
    	} catch (NotAuthorizedException e) {
			e.printStackTrace();
			return Response.status(e.getResponse().getStatus()).entity(e.getMessage()).build();
		} catch (InternalServerErrorException e) {
			e.printStackTrace();
			return Response.status(e.getResponse().getStatus()).entity(e.getMessage()).build();
		}
    }
	
	@POST
	@ApiOperation(value = "Upload details report", 
	    notes = "This method saves the uploaded file informations",
	    response = Response.class)
	@ApiResponses(value = {@ApiResponse(code = 401, message = "User identification not present (unauthorized)"),
			@ApiResponse(code = 500, message = "Internal server error"),
			@ApiResponse(code = 500, message = "No entity found"),
			@ApiResponse(code = 500, message = "Please fill all fields"),
			@ApiResponse(code = 200, message = "Upload Report saved successfuly")})
	@Consumes("application/json; charset=UTF-8")
	@Path("/uploadReport")
	public Response SaveReport(
			@ApiParam(name = "upload", value = "Object that contains the information about the uploaded file", required = false)
			Report report) {

		UploadEntity entity = new UploadEntity();

		try {
			if(report.getUploadTime() == null) {
				report.setUploadTime(0);
			}
			
			List<FileEntity> fileEntityList = fileRepository.FindByName(report.getFileName());
			FileHandlerService.validateReport(report);
			
			entity.setChunkFileNumber(fileEntityList.size());
			entity.setDownloadLink("rest/filehandler/getfile/" + report.getFileName());
			entity.setFileName(report.getFileName());
			entity.setUploadTime(report.getUploadTime());
			entity.setUserId(report.getUserId());
			if(fileEntityList.size() > 0) {
				entity.setUserId(fileEntityList.get(0).getUserId());
			}
			Status reportStatus = FileHandlerService.defineStatus(report, fileEntityList);
			entity.setUploadStatus(reportStatus);
			uploadRepository.SaveOrUpdate(entity, reportStatus);
			return Response.status(200).entity(UPLOAD_REPORT_SUCCESS_MESSAGE).build();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return Response.status(500).entity(NO_ENTITY_FOUND_MESSAGE).build();
		} catch (InternalServerErrorException e) {
			e.printStackTrace();
			return Response.status(e.getResponse().getStatus()).entity(e.getMessage()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(e.getMessage()).build();
		}
	}

	@GET
	@ApiOperation(value = "List uploaded files", 
	    notes = "This method lists the files uploaded with their information, as file name, time to upload etc.",
	    response = List.class)
	@ApiResponse(response = List.class, code = 200, message = "The list of upload reports found")
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
	@ApiOperation(value = "Download specific file", 
	    notes = "This method join the chunk files if exists and returns the file for client download",
	    response = Response.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Returned file as attachment to download"),
			@ApiResponse(code = 500, message = "Error joining files")})
	@Path("/getfile/{fileName}")
	public Response getFile(
			@ApiParam(name = "fileName", value = "String with the name of file to download", required = false)
			@PathParam("fileName") String fileName) {
 
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
                    throw new WebApplicationException(GET_FILE_ERROR_MESSAGE);
                }
            }
        };
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = " + fileName)
                .build();
    }
}
