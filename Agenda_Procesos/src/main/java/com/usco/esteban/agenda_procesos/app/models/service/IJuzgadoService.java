package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;

public interface IJuzgadoService
{
	public List<Juzgado> findAll();
	
	public void save(Juzgado juzgado);
	
	public Juzgado findOne(Long id);

}
