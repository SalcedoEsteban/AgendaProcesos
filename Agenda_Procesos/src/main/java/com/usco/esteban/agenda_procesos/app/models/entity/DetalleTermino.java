package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name ="DetalleTermino")
public class DetalleTermino implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="detTer_id")
	private Long id;
	
	/*@Column(name ="detTer_nombre")
	private String nombre;*/
	
	@Column(name ="detTer_dias_habiles")
	private boolean diasHabiles;
	
	@Column(name="detTer_fecha_incial")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fechaInicial;
	
	@Column(name ="detTer_fecha_final")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fechaFinal;
	
	@Column(name="det_ter_termino")
	private String ter;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ter_id_det_ter", updatable = false, nullable = false)
	private Termino termino;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="pro_id_det_ter", updatable = false, nullable = false)
	private Proceso proceso;
	
	/*@Column(name="ter_numero_dias")
	private Integer numeroDias;¨*/
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/*public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}*/

	public boolean isDiasHabiles() {
		return diasHabiles;
	}

	public void setDiasHabiles(boolean diasHabiles) {
		this.diasHabiles = diasHabiles;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/*public Integer getNumeroDias() {
		return numeroDias;
	}

	public void setNumeroDias(Integer numeroDias) {
		this.numeroDias = numeroDias;
	}*/

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public String getTer() {
		return ter;
	}

	public void setTer(String ter) {
		this.ter = ter;
	}

	public Termino getTermino() {
		return termino;
	}

	public void setTermino(Termino termino) {
		this.termino = termino;
	}

	
}
