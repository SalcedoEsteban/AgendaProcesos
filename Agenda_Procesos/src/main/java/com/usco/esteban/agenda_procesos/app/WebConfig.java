package com.usco.esteban.agenda_procesos.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.usco.esteban.agenda_procesos.app.models.entity.StringToTerminoConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	
	@Override
	public void addFormatters(FormatterRegistry registry)
	{
		registry.addConverter(new StringToTerminoConverter());
	}
}
