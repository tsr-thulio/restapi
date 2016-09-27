package com.restapi.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.restapi.repository.entity.FileEntity;
import com.restapi.repository.entity.UploadEntity;

public class UploadRepository {
	
	private final EntityManagerFactory entityManagerFactory;
	 
	private final EntityManager entityManager;
 
	public UploadRepository(){
		this.entityManagerFactory = Persistence.createEntityManagerFactory("persistence_restapi_db");
 
		this.entityManager = this.entityManagerFactory.createEntityManager();
	}
 
	public void Save(UploadEntity uploadEntity){
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(uploadEntity);
		this.entityManager.getTransaction().commit();
	}
	
	public void Teste(FileEntity fileEntity){
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(fileEntity);
		this.entityManager.getTransaction().commit();
	}
 
	@SuppressWarnings("unchecked")
	public List<UploadEntity> GetUploads(){
		return this.entityManager.createQuery("SELECT p FROM UploadEntity p").getResultList();
	}
}
