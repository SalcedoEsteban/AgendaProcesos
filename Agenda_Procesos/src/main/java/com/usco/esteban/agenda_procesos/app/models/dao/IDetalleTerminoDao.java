package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;

public interface IDetalleTerminoDao
{
	public List<DetalleTermino> findAll();
	
	public void save(DetalleTermino detalleTermino);
}
