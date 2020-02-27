package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name ="proceso")
public class Proceso implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name ="pro_id")
	private Long id;
	
	@Column(name="pro_radicado")
	@NotEmpty
	private String radicado;
	
	@Column(name = "pro_demandante")
	@NotEmpty
	private String demandante;
	
	@Column(name ="pro_demandado")
	private String demandado;
	
	@Column(name ="pro_fecha_reparto")
	@Temporal(TemporalType.DATE)
	@NotNull
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fechaReparto;
	
	@Column(name="pro_ultima_actuacion")
	private String ultimaActuacion;
	
	@Column(name="pro_estado_actual")
	private String estadoActual;
	
	@Column(name="pro_estado")
	private int estado;
	
	@Column(name ="pro_tipo_proceso")
	@NotEmpty
	private String tProceso;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="tip_pro_id_pro", nullable = false, updatable = false)
	private TipoProceso tipoProceso;
	
	
	@OneToMany(mappedBy = "proceso", fetch = FetchType.LAZY, cascade=CascadeType.ALL) /* con el atributo
	Mappedby lo que expresa es que la relacion es bidireccional, es decir, un proceso tiene una lista de
	terminos y un término tiene un proceso */
	private List<DetalleTermino> detalleTerminos;
	
	

	public Proceso() {
		detalleTerminos = new ArrayList<DetalleTermino>();
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRadicado() {
		return radicado;
	}

	public void setRadicado(String radicado) {
		this.radicado = radicado;
	}

	public String getDemandante() {
		return demandante;
	}

	public void setDemandante(String demandante) {
		this.demandante = demandante;
	}

	public String getDemandado() {
		return demandado;
	}

	public void setDemandado(String demandado) {
		this.demandado = demandado;
	}

	public Date getFechaReparto() {
		return fechaReparto;
	}

	public void setFechaReparto(Date fechaReparto) {
		this.fechaReparto = fechaReparto;
	}

	public String getUltimaActuacion() {
		return ultimaActuacion;
	}

	public void setUltimaActuacion(String ultimaActuacion) {
		this.ultimaActuacion = ultimaActuacion;
	}

	public String getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	

	public List<DetalleTermino> getDetalleTerminos() {
		return detalleTerminos;
	}

	public void setDetalleTerminos(List<DetalleTermino> detalleTerminos) {
		this.detalleTerminos = detalleTerminos;
	}

	public void addDetalleTermino(DetalleTermino detalleTermino)
	{
		detalleTerminos.add(detalleTermino);
	}

	/*public String getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}*/
	
	public String gettProceso() {
		return tProceso;
	}

	public void settProceso(String tProceso) {
		this.tProceso = tProceso;
	}

	public TipoProceso getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(TipoProceso tipoProceso) {
		this.tipoProceso = tipoProceso;
	}
	
	
	
}
