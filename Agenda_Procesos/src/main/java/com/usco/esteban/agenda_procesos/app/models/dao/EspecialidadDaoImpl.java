package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;

@Repository
public class EspecialidadDaoImpl implements IEspecialidadDao {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Especialidad> findAll() {
		
		return em.createQuery("from Especialidad").getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Especialidad findOne(Long id) {
		
		return em.find(Especialidad.class, id);
	}

}
