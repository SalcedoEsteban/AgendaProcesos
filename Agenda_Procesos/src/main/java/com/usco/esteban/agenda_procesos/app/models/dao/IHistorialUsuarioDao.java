package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

public interface IHistorialUsuarioDao extends JpaRepository<HistorialUsuario, Long>
{
	public List<HistorialUsuario> findByUsuario(Usuario usuario);
	
	public List<HistorialUsuario> findByUsuarioAndEspecialidad(Usuario usuario, Especialidad especialidad);
	
}
