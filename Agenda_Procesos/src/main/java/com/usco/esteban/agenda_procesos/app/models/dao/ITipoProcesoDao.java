package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

public interface ITipoProcesoDao {

	/* acá van todos los metodos que deben implementar todas las clases que implementan
	 * la interfaz */
	public List<TipoProceso> findAll();
	
	public void save(TipoProceso tipoProceso);
	
	public TipoProceso findOne(Long id);
	
	public void delete(Long id);
}
