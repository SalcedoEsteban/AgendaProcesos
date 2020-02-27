package com.usco.esteban.agenda_procesos.app.models.entity;

import org.springframework.core.convert.converter.Converter;

public class StringToTerminoConverter implements Converter<String, Termino>{

	@Override
	public Termino convert(String source) {
		
		String[] data = source.split(",");
		
		return new Termino();
		}
	}

	
	

