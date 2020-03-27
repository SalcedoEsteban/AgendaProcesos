package com.usco.esteban.agenda_procesos.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.usco.esteban.agenda_procesos.app.auth.handler.LoginSuccessHandler;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter
{
	/* inyeccion de dependencias*/
	@Autowired
	private LoginSuccessHandler successHandler;
	
	
	/* se registra el password encoder 'BCrypt' como un componente de spring */
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	/*se debe implementar un metodo para poder registrar y configurar los usuarios se guardaran
	 *  los usuarios en memoria*/
	/* se anota con @autowired para poder inyectar el objeto de srping AuthenticationManagerBuilder*/
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception
	{
		/* se usa el componente invocando al metodo */
		PasswordEncoder encoder = passwordEncoder();
		
		/* se configura la forma en la que se va a encriptar la contraseña*/
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		
		builder.inMemoryAuthentication()
		.withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
		.withUser(users.username("esteban").password("12345").roles("USER"));
	}


	/* autorizaciones http en las rutas */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/icons/**").permitAll() //rutas publicas
		.antMatchers("/").hasAnyRole("USER")
		.antMatchers("/listar").hasAnyRole("USER")
		.antMatchers("/listarProcesos").hasAnyRole("USER")
		.antMatchers("/verDetalleProceso/**").hasAnyRole("USER")
		.antMatchers("/verTerminos/**").hasAnyRole("USER")
		.antMatchers("/listarTiposProceso").hasAnyRole("USER")
		.antMatchers("/formProceso/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminarProceso/**").hasAnyRole("ADMIN")	
		.anyRequest().authenticated()
		.and()
			.formLogin()
				.loginPage("/login")
				.successHandler(successHandler)
			.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}
}
