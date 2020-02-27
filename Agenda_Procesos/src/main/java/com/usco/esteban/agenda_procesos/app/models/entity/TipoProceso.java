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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name ="TipoProceso")
public class TipoProceso implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "tipPro_id")
	private Long id;
	
	@Column(name = "tipPro_nombre")
	@NotEmpty
	private String nombre;
	
	@OneToMany(mappedBy = "tipoProceso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Proceso> Procesos;
	
	@OneToMany(mappedBy = "tipoProceso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Termino> terminos;

	
	
	
	public TipoProceso() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Proceso> getProcesos() {
		return Procesos;
	}

	public void setProcesos(List<Proceso> procesos) {
		Procesos = procesos;
	}

	public List<Termino> getTerminos() {
		return terminos;
	}

	public void setTerminos(List<Termino> terminos) {
		this.terminos = terminos;
	}

	
	
	
}
