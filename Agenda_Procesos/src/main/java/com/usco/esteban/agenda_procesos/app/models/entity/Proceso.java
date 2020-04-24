package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "proceso")
public class Proceso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_id")
	private Long id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name ="pro_create_at")
	private Date createAt;
	
	@Column(name = "pro_radicado")
	@NotEmpty
	private String radicado;

	@Column(name = "pro_demandante")
	@NotEmpty
	private String demandante;

	@Column(name = "pro_demandado")
	private String demandado;

	@Column(name = "pro_fecha_reparto")
	@Temporal(TemporalType.DATE)
	@NotNull
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fechaReparto;

	@Column(name = "pro_ultima_actuacion")
	private String ultimaActuacion;

	@Column(name = "pro_estado_actual")
	private String estadoActual;

	//CAMBIAR POR TIPO BOOLEAN Y NOMBRE CAMPO 'ENABLED'
	@Column(name = "pro_estado")
	private int estado;

	@Column(name = "pro_tipo_proceso")
	@NotEmpty
	private String tProceso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tip_pro_id_pro", nullable = false, updatable = false)
	private TipoProceso tipoProceso;

	@OneToMany(mappedBy = "proceso", fetch = FetchType.LAZY, cascade = CascadeType.ALL) /*
																						 * con el atributo Mappedby lo
																						 * que expresa es que la
																						 * relacion es bidireccional, es
																						 * decir, un proceso tiene una
																						 * lista de terminos y un
																						 * término tiene un proceso
																						 */
	private List<DetalleTermino> detalleTerminos;

	@Column(name = "pro_juzgado")
	private String juz;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "juz_id_pro", nullable = false, updatable = false)
	private Juzgado juzgado;

	@OneToMany(mappedBy = "proceso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Alarma> alarma;

	/*
	 * @JoinTable( name = "proceso_usuario", joinColumns = @JoinColumn(name =
	 * "FK_PROCESO", nullable = false), inverseJoinColumns = @JoinColumn(name =
	 * "FK_USUARIO", nullable = false) )
	 * 
	 * @ManyToMany(cascade = CascadeType.ALL) private List<Usuario> usuarios;
	 */

	@OneToMany(mappedBy = "proceso", orphanRemoval = true)
	private List<ProcesoUsuario> procesosUsuarios = new ArrayList<>();

	@PrePersist
	public void prePersist()
	{
		this.createAt = new Date();
	}
	
	public Proceso() {
		detalleTerminos = new ArrayList<DetalleTermino>();
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public void addDetalleTermino(DetalleTermino detalleTermino) {
		detalleTerminos.add(detalleTermino);
	}

	/*
	 * public String getTipoProceso() { return tipoProceso; }
	 * 
	 * public void setTipoProceso(String tipoProceso) { this.tipoProceso =
	 * tipoProceso; }
	 */

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

	public String getJuz() {
		return juz;
	}

	public void setJuz(String juz) {
		this.juz = juz;
	}

	public Juzgado getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(Juzgado juzgado) {
		this.juzgado = juzgado;
	}

	public List<Alarma> getAlarma() {
		return alarma;
	}

	public void setAlarma(List<Alarma> alarma) {
		this.alarma = alarma;
	}

	/*
	 * public List<Usuario> getUsuarios() { return usuarios; }
	 * 
	 * public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
	 * 
	 * /* metodo para agregar usuarios a un proceso public void addUsuario(Usuario
	 * usuario) { if(this.usuarios == null) { this.usuarios = new ArrayList<>(); }
	 * 
	 * this.usuarios.add(usuario); }
	 */

	public List<ProcesoUsuario> getProcesosUsuarios() {
		return procesosUsuarios;
	}

	public void setProcesosUsuarios(List<ProcesoUsuario> procesosUsuarios) {
		this.procesosUsuarios = procesosUsuarios;
	}

	public void addUsuario(Usuario usuario) {
		ProcesoUsuario procesoUsuario = new ProcesoUsuario(this, usuario);
		procesosUsuarios.add(procesoUsuario);
		usuario.getListProcesosUsuarios().add(procesoUsuario);
	}
	
	public void removeUsuario(Usuario usuario)
	{
		ProcesoUsuario procesoUsuario = new ProcesoUsuario(this, usuario);
		usuario.getListProcesosUsuarios().remove(procesoUsuario);
		procesosUsuarios.remove(procesoUsuario);
		procesoUsuario.setProceso(null);
		procesoUsuario.setUsuario(null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alarma == null) ? 0 : alarma.hashCode());
		result = prime * result + ((demandado == null) ? 0 : demandado.hashCode());
		result = prime * result + ((demandante == null) ? 0 : demandante.hashCode());
		result = prime * result + ((detalleTerminos == null) ? 0 : detalleTerminos.hashCode());
		result = prime * result + estado;
		result = prime * result + ((estadoActual == null) ? 0 : estadoActual.hashCode());
		result = prime * result + ((fechaReparto == null) ? 0 : fechaReparto.hashCode());
		result = prime * result + ((juz == null) ? 0 : juz.hashCode());
		result = prime * result + ((juzgado == null) ? 0 : juzgado.hashCode());
		result = prime * result + ((radicado == null) ? 0 : radicado.hashCode());
		result = prime * result + ((tProceso == null) ? 0 : tProceso.hashCode());
		result = prime * result + ((tipoProceso == null) ? 0 : tipoProceso.hashCode());
		result = prime * result + ((ultimaActuacion == null) ? 0 : ultimaActuacion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proceso other = (Proceso) obj;
		if (alarma == null) {
			if (other.alarma != null)
				return false;
		} else if (!alarma.equals(other.alarma))
			return false;
		if (demandado == null) {
			if (other.demandado != null)
				return false;
		} else if (!demandado.equals(other.demandado))
			return false;
		if (demandante == null) {
			if (other.demandante != null)
				return false;
		} else if (!demandante.equals(other.demandante))
			return false;
		if (detalleTerminos == null) {
			if (other.detalleTerminos != null)
				return false;
		} else if (!detalleTerminos.equals(other.detalleTerminos))
			return false;
		if (estado != other.estado)
			return false;
		if (estadoActual == null) {
			if (other.estadoActual != null)
				return false;
		} else if (!estadoActual.equals(other.estadoActual))
			return false;
		if (fechaReparto == null) {
			if (other.fechaReparto != null)
				return false;
		} else if (!fechaReparto.equals(other.fechaReparto))
			return false;
		if (juz == null) {
			if (other.juz != null)
				return false;
		} else if (!juz.equals(other.juz))
			return false;
		if (juzgado == null) {
			if (other.juzgado != null)
				return false;
		} else if (!juzgado.equals(other.juzgado))
			return false;
		if (radicado == null) {
			if (other.radicado != null)
				return false;
		} else if (!radicado.equals(other.radicado))
			return false;
		if (tProceso == null) {
			if (other.tProceso != null)
				return false;
		} else if (!tProceso.equals(other.tProceso))
			return false;
		if (tipoProceso == null) {
			if (other.tipoProceso != null)
				return false;
		} else if (!tipoProceso.equals(other.tipoProceso))
			return false;
		if (ultimaActuacion == null) {
			if (other.ultimaActuacion != null)
				return false;
		} else if (!ultimaActuacion.equals(other.ultimaActuacion))
			return false;
		return true;
	}

	
	
	
	/*@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if(obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		
		Proceso proceso =(Proceso) obj;
		
		return Objects.equals(radicado, proceso.radicado)
				&& Objects.equals(demandante, proceso.demandante)
				&& Objects.equals(demandado, proceso.demandado)
				&& Objects.equals(fechaReparto, proceso.fechaReparto)
				&& Objects.equals(ultimaActuacion, proceso.ultimaActuacion)
				&& Objects.equals(estadoActual, proceso.estadoActual)
				&& Objects.equals(estado, proceso.estado)
				&& Objects.equals(tProceso, proceso.tProceso)
				&& Objects.equals(tipoProceso, proceso.tipoProceso)
				&& Objects.equals(detalleTerminos, proceso.detalleTerminos)
				&& Objects.equals(juz, proceso.juz)
				&& Objects.equals(juzgado, proceso.juzgado)
				&& Objects.equals(alarma, proceso.alarma);
	}*/
	
	

}
