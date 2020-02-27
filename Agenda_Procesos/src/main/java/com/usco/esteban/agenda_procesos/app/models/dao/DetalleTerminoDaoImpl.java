package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;

@Repository
public class DetalleTerminoDaoImpl implements IDetalleTerminoDao {

	@PersistenceContext
	private EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DetalleTermino> findAll()
	{		
		return em.createQuery("from DetalleTermino").getResultList();
	}


	@Override
	@Transactional
	public void save(DetalleTermino detalleTermino) {
		
		em.persist(detalleTermino);
		
	}

}
