package com.tribeiro.restapi.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tribeiro.restapi.filehandler.models.Status;
import com.tribeiro.restapi.repository.entity.UploadEntity;

public class UploadRepository {
	
	private final EntityManagerFactory entityManagerFactory;
	 
	private final EntityManager entityManager;
	
	private static final String STATUS_FIELD = "uploadStatus";
	private static final String UPLOAD_TIME_FIELD = "uploadTime";
 
	public UploadRepository(){
		this.entityManagerFactory = Persistence.createEntityManagerFactory("persistence_restapi_db");
 
		this.entityManager = this.entityManagerFactory.createEntityManager();
	}
 
	public void SaveOrUpdate(UploadEntity uploadEntity, Status newStatus){
		this.entityManager.getTransaction().begin();
		UploadEntity foundEntity = FindByName(uploadEntity.getFileName());
		if(foundEntity == null) {
			this.entityManager.persist(uploadEntity);
		} else {
			UpdateUpload(foundEntity.getId(), STATUS_FIELD, newStatus.ordinal());
			UpdateUpload(foundEntity.getId(), UPLOAD_TIME_FIELD, uploadEntity.getUploadTime());
		}
		this.entityManager.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<UploadEntity> GetUploads(){
		try {
			return this.entityManager.createQuery("SELECT u FROM UploadEntity u").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UploadEntity>();
		}
	}
	
	public UploadEntity FindByName(String name) {
		try {
			return (UploadEntity) this.entityManager.createQuery("SELECT u FROM UploadEntity u WHERE u.fileName='" + name + "'").getSingleResult();			
		} catch (Exception e) {
			return null;
		}
	}
	
	public void UpdateUpload(Long id, String field, Integer newValue) {
		this.entityManager.createQuery("UPDATE UploadEntity u SET u." + field + "='" + newValue + "' WHERE u.id='" + id + "'").executeUpdate();
	}
}
