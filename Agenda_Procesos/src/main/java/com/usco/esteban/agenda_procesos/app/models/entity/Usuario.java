package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name ="usuario")
public class Usuario implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name ="usu_id")
	private Long id;
	
	@Column(name = "usu_username", unique = true, length = 40)
	private String username;
	
	@Column(name = "usu_password", length = 60)
	private String password;
	
	@Column(name = "usu_enabled")
	private boolean enabled;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	/* llave foranea de la tabla rol*/
	@JoinColumn(name = "usu_id_rol")
	private List<Rol> roles;
	
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	
	

}
