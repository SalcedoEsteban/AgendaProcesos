package com.usco.esteban.agenda_procesos.app;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AgendaProcesosApplication implements CommandLineRunner
{
	/* CommandLineRunner es una interfaz para el metodo run()*/
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(AgendaProcesosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		
		/*acá se genera las contraseñas de ejemplo */
		String password = "12345";
		String password1 = "123456";
		
		/*for(int i =0; i<2; i++)
		{
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}*/
		
		
		String bcryptPassword1 = passwordEncoder.encode(password1);
		System.out.println(bcryptPassword1);
		
		
		Calendar fechaActual = Calendar.getInstance();
		Calendar fechaFinal = Calendar.getInstance();
		
		//Calendar fechaInicial = detalle.getFechaInicial();
		int dia = 27;
		int mes = 6;
		int año = 2020;
		
		fechaFinal.set(año, mes-1, dia);
		
		fechaActual.set(Calendar.HOUR, 0);
		fechaActual.set(Calendar.HOUR_OF_DAY, 0);
		fechaActual.set(Calendar.MINUTE, 0);
		fechaActual.set(Calendar.SECOND, 0);
		
		fechaFinal.set(Calendar.HOUR, 0);
		fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
		fechaFinal.set(Calendar.MINUTE, 0);
		fechaFinal.set(Calendar.SECOND, 0);
		
		
		
		//long fechaIncialMS = fechaInicial.getTimeInMillis();
		long fechaFinalMS = fechaFinal.getTimeInMillis();
		long fechaActualMS = fechaActual.getTimeInMillis();
		
		int dias = (int) ((Math.abs(fechaFinalMS - fechaActualMS)) / (1000 * 60 * 60* 24));
		
		System.out.println("los dias son" + dias);
		
		if(dias == 30)
		{
			System.out.println("dias igual a 30");
		}
		
		String nombre1 = "notificacion demandado";
		String nombre2 = "notificacion demandado";
		
		if(nombre1.equals("notificacion demandado"))
		{
			System.out.println("las dos son iguales");
		}
		
		fechaFinal.add(Calendar.DAY_OF_YEAR, 365);
		System.out.println("la fecha mas 365 es: " + fechaFinal.getTime());
	}

}
