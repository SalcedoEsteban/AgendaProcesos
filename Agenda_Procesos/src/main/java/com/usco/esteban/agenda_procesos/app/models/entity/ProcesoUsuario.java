package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name ="procesoUsuario")
public class ProcesoUsuario implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	//@Column(name ="pro_id_pro_usu")
	private Proceso proceso;
	
	@Id
	@ManyToOne
	//@Column(name ="usu_id_pro_usu")
	private Usuario usuario;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name ="pro_usu_create_at")
	private Date creatAt;
	
	@PrePersist
	public void prePersist()
	{
		this.creatAt = new Date();
	}
	
	public ProcesoUsuario(Proceso proceso, Usuario usuario) {
		super();
		this.proceso = proceso;
		this.usuario = usuario;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
		{
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		
		ProcesoUsuario that = (ProcesoUsuario) obj;
		return Objects.equals(proceso, that.proceso) &&
				Objects.equals(usuario, that.usuario);
		
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(proceso, usuario);
	}
}
