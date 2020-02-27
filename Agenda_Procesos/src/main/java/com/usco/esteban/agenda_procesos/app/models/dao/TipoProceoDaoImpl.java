package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

@Repository
public class TipoProceoDaoImpl implements ITipoProcesoDao {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoProceso> findAll() {
		
		return em.createQuery("from TipoProceso").getResultList();
	}
	
	@Override
	public TipoProceso findOne(Long id) {
		
		return em.find(TipoProceso.class, id);
	}

	@Override
	public void save(TipoProceso tipoProceso) {
		
		/* validaciones, si se debe editar o guardar*/
		/* si el id obtenido es diferente de nul y mayor a cero
		 * es porque ese registro ya existe y se debe actualizar*/
		if(tipoProceso.getId() != null && tipoProceso.getId() > 0)
		{
			em.merge(tipoProceso);
		}
		else
		{
			em.persist(tipoProceso);
		}
	}

	@Override
	public void delete(Long id) {
		
		/* primero se obtiene el objeto que se quiere eliminar*/
		TipoProceso tipoProceso = findOne(id);
		
		em.remove(tipoProceso);
		
	}

}
