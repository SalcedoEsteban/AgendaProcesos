package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;

@Repository
public class JuzgadoDaoImpl implements IJuzgadoDao {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Juzgado> findAll()
	{	
		return em.createQuery("from Juzgado").getResultList();
	}

	@Override
	public void save(Juzgado juzgado) {
		
		if(juzgado.getId() != null && juzgado.getId() >0 )
		{
			em.merge(juzgado);
		}
		else
		{
			em.persist(juzgado);
		}
		
	}

	@Override
	public Juzgado findOne(Long id) {
		
		return em.find(Juzgado.class, id);
	}

}
