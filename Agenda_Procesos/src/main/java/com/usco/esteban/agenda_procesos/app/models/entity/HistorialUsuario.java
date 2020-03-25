package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name ="historial_usuario")
public class HistorialUsuario implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="his_usu_id")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name ="his_usu_fecha_ingreso")
	//esta columna se debe llenar cuando se cree el usuario en la tabla de usuarios
	//puede ser por un "prepersist" REVISAR.
	private Date fechaIngreso;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "his_usu_fecha_salida")
	private Date fechaSalida;
	
	@Column(name ="his_usu_descripcion")
	private String descripcion; 
	
	//Acá falta la relación de OneToMany con usuarios.
	//Un historial tiene muchos usuarios y un usuario 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="esp_id_his_usu", nullable = false, updatable = false)
	private Especialidad especialidad;
	
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}
	
	

}
