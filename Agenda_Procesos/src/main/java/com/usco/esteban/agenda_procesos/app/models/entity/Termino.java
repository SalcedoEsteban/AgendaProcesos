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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "termino")
public class Termino implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ter_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name ="ter_nombre")
	private String nombre;
	
	@Column(name ="ter_numero_dias")
	private int numeroDias;
	
	@Column(name = "ter_basico")
	private boolean basico;
	
	@Column(name ="ter_esp")
	@NotEmpty
	private String esp;
	
	@OneToMany(mappedBy = "termino", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<DetalleTermino> detalleTerminos;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="tip_pro_id_ter", nullable = false, updatable = false)
	private TipoProceso tipoProceso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="esp_id_ter", nullable = false, updatable = false)
	private Especialidad especialidad;

	public Termino() {
		
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

	public int getNumeroDias() {
		return numeroDias;
	}

	public void setNumeroDias(int numeroDias) {
		this.numeroDias = numeroDias;
	}

	public boolean isBasico() {
		return basico;
	}

	public void setBasico(boolean basico) {
		this.basico = basico;
	}

	
	
	public TipoProceso getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(TipoProceso tipoProceso) {
		this.tipoProceso = tipoProceso;
	}


	public String getEsp() {
		return esp;
	}

	public void setEsp(String esp) {
		this.esp = esp;
	}

	public List<DetalleTermino> getDetalleTerminos() {
		return detalleTerminos;
	}

	public void setDetalleTerminos(List<DetalleTermino> detalleTerminos) {
		this.detalleTerminos = detalleTerminos;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	
}
