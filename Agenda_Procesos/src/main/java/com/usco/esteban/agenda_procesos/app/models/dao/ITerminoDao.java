package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Termino;

public interface ITerminoDao {

	
	public List<Termino> findAll();
	
	public void save(Termino termino);
	
	public Termino findOne(Long id);
	
	public void delete(Long id);
}
