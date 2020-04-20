package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.entity.Termino;

@Repository
public class TerminoDaoImpl implements ITerminoDao
{
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Termino> findAll() {
		
		return em.createQuery("from Termino").getResultList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Termino findOne(Long id) {
		
		return em.find(Termino.class, id);
	}
	
	@Override
	@Transactional
	public void save(Termino termino) {
		
		if(termino.getId() != null && termino.getId() > 0)
		{
			em.merge(termino);
		}
		else
		{
			em.persist(termino);
		}
	}

	@Override
	@Transactional
	public void delete(Long id)
	{	
		Termino termino = findOne(id);	
		em.remove(termino);	
	}
}
