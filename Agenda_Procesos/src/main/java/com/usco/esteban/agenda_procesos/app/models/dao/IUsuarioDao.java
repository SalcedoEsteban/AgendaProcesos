package com.usco.esteban.agenda_procesos.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>
{
	public Usuario findByUsername(String username);
}
