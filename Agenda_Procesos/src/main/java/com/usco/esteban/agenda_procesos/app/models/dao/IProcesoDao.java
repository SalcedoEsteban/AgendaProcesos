package com.usco.esteban.agenda_procesos.app.models.dao;


//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;

public interface IProcesoDao extends PagingAndSortingRepository<Proceso, Long> 
{
	
}
