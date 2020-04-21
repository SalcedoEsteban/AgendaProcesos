package com.usco.esteban.agenda_procesos.app.editors;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;

@Component
public class JuzgadoPropertyEditor extends PropertyEditorSupport
{
	@Autowired
	private IJuzgadoService juzgadoService;

	@Override
	public void setAsText(String idString) throws IllegalArgumentException {
		
		Long id = Long.parseLong(idString);
		
		this.setValue(juzgadoService.findOne(id));
	}
	
}