package com.restapi.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.restapi.repository.entity.FileEntity;

public class FileRepository {
	
	private final EntityManagerFactory entityManagerFactory;
	 
	private final EntityManager entityManager;
 
	public FileRepository(){
		this.entityManagerFactory = Persistence.createEntityManagerFactory("persistence_restapi_db");
 
		this.entityManager = this.entityManagerFactory.createEntityManager();
	}
 
	public void Save(FileEntity fileEntity){
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(fileEntity);
		this.entityManager.getTransaction().commit();
	}
}
