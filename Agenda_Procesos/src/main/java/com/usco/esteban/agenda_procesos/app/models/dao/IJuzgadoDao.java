package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;

public interface IJuzgadoDao {
	
	public List<Juzgado> findAll();
	
	public void save(Juzgado juzgado);
	
	public Juzgado findOne(Long id);
}
