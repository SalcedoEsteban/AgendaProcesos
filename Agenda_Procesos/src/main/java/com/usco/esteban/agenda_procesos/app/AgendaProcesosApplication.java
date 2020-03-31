package com.usco.esteban.agenda_procesos.app;

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
		
		for(int i =0; i<2; i++)
		{
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
		
		
	}

}
