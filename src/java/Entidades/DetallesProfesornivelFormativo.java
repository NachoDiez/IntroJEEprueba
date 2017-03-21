/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "detalles_Profesor_nivelFormativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesProfesornivelFormativo.findAll", query = "SELECT d FROM DetallesProfesornivelFormativo d"),
    @NamedQuery(name = "DetallesProfesornivelFormativo.findByIdDetalle", query = "SELECT d FROM DetallesProfesornivelFormativo d WHERE d.idDetalle = :idDetalle")})
public class DetallesProfesornivelFormativo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalle")
    private Integer idDetalle;
    @JoinColumn(name = "idNivelFormativo", referencedColumnName = "idNivelFormativo")
    @ManyToOne
    private NivelFormativo idNivelFormativo;
    @JoinColumn(name = "idProfesor", referencedColumnName = "idProfesor")
    @ManyToOne
    private Profesor idProfesor;

    public DetallesProfesornivelFormativo() {
    }

    public DetallesProfesornivelFormativo(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public NivelFormativo getIdNivelFormativo() {
        return idNivelFormativo;
    }

    public void setIdNivelFormativo(NivelFormativo idNivelFormativo) {
        this.idNivelFormativo = idNivelFormativo;
    }

    public Profesor getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Profesor idProfesor) {
        this.idProfesor = idProfesor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalle != null ? idDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesProfesornivelFormativo)) {
            return false;
        }
        DetallesProfesornivelFormativo other = (DetallesProfesornivelFormativo) object;
        if ((this.idDetalle == null && other.idDetalle != null) || (this.idDetalle != null && !this.idDetalle.equals(other.idDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesProfesornivelFormativo[ idDetalle=" + idDetalle + " ]";
    }
    
}
