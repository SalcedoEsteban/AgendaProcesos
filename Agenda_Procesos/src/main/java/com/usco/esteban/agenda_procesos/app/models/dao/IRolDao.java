package com.usco.esteban.agenda_procesos.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Rol;

public interface IRolDao extends CrudRepository<Rol, Long>
{
	
}
