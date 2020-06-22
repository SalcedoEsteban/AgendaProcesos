package com.usco.esteban.agenda_procesos.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IProcesoUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;

@Service
public class ProcesoUsuarioServiceImpl implements IProcesoUsuarioService {

	@Autowired
	private IProcesoUsuarioDao procesoUsuarioDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllById(Long id, Pageable pageable, Juzgado juzgado) {
		
		return procesoUsuarioDao.findAllById(id, pageable, juzgado);
	}

	@Override
	@Transactional
	public void save(ProcesoUsuario procesoUsuario) {
		
		procesoUsuarioDao.save(procesoUsuario);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		procesoUsuarioDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findByIdAndRadicado(Long id, Pageable pageable, String radicado) {
		
		return procesoUsuarioDao.findByIdAndRadicado(id, pageable, radicado);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findByUsuarioAndEstadoAndJuzgadoAndPrioritario(Long id, Pageable pageable,
			Juzgado juzgado) {
		
		return procesoUsuarioDao.findByUsuarioAndEstadoAndJuzgadoAndPrioritario(id, pageable, juzgado);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllBy(Long id, Pageable pageable, Juzgado juzgado) {
		
		return procesoUsuarioDao.findAllBy(id, pageable, juzgado);
	}

}
