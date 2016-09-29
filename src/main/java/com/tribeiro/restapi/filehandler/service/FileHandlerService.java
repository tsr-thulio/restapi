package com.tribeiro.restapi.filehandler.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.tribeiro.restapi.filehandler.models.Report;
import com.tribeiro.restapi.filehandler.models.Status;
import com.tribeiro.restapi.repository.entity.FileEntity;

public class FileHandlerService {
	private static final String USER_NOT_AUTHORIZED_MESSAGE = "Unauthorized to perform this action, set user identificator!";
	private static final String REQUIRED_FIELD_ERROR_MESSAGE = "Please fill all fields";

	public static void validateUploadFile(String fileName, String filePartName, String user, FormDataContentDisposition fileDetail, InputStream file) throws NotAuthorizedException, InternalServerErrorException, IOException{
		if(user.equals(null) || user.equals("undefined")) {
			throw new NotAuthorizedException(USER_NOT_AUTHORIZED_MESSAGE);
		}
		if(fileDetail.equals(null) || fileName.equals("undefined") || filePartName.equals("undefined") || file.equals(null) || fileName.equals(null) || filePartName.equals(null)) {
			throw new InternalServerErrorException(REQUIRED_FIELD_ERROR_MESSAGE);
		}
	}
	
	public static Status defineStatus(Report report, List<FileEntity> fileEntityList) {
		if(report.getFinishedUpload() && fileEntityList.size() == report.getChunkFileNumber()) {
			return Status.DONE;
		} else if(report.getFinishedUpload() && fileEntityList.size() != report.getChunkFileNumber()) {
			return Status.FAILED;
		} else {
			return Status.PROCESSING;
		}
	}

	public static void validateReport(Report report) {
		if(report.getFileName() == null || report.getFileName().equals("undefined") || report.getChunkFileNumber() == null || report.getFinishedUpload() == null) {
			throw new InternalServerErrorException(REQUIRED_FIELD_ERROR_MESSAGE);
		}
	}
}
