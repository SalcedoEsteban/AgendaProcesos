package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;

public interface IEspecialidadDao {
	
	public List<Especialidad> findAll();
	
	public Especialidad findOne(Long id);
}
