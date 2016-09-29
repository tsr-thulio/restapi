package com.tribeiro.restapi.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tribeiro.restapi.repository.entity.FileEntity;

public class FileRepository {
	
	private final EntityManagerFactory entityManagerFactory;
	 
	private final EntityManager entityManager;
	
	private static final String FILE_FIELD = "file";
 
	public FileRepository(){
		this.entityManagerFactory = Persistence.createEntityManagerFactory("persistence_restapi_db");
 
		this.entityManager = this.entityManagerFactory.createEntityManager();
	}
 
	public void SaveOrUpdate(FileEntity fileEntity){
		this.entityManager.getTransaction().begin();
		
		FileEntity foundEntity = FindByPartName(fileEntity.getFilePartName());
		if(foundEntity == null) {
			this.entityManager.persist(fileEntity);
		} else {
			UpdateFile(foundEntity.getId(), FILE_FIELD, fileEntity.getFile());
		}
		this.entityManager.getTransaction().commit();
	}
	
	private void UpdateFile(Long id, String field, Object value) {
		this.entityManager.createQuery("UPDATE FileEntity f SET f." + field + "='" + value + "' WHERE f.id='" + id + "'").executeUpdate();
	}

	private FileEntity FindByPartName(String filePartName) {
		try {
			return (FileEntity) this.entityManager.createQuery("SELECT f FROM FileEntity f WHERE f.filePartName='" + filePartName + "'").getSingleResult();			
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<FileEntity> FindByName(String name) {
		return this.entityManager.createQuery("SELECT u FROM FileEntity u WHERE u.fileName='" + name + "'").getResultList();			
	}
}
