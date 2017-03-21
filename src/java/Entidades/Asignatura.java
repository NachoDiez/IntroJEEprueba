/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "Asignatura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asignatura.findAll", query = "SELECT a FROM Asignatura a"),
    @NamedQuery(name = "Asignatura.findByIdAsignatura", query = "SELECT a FROM Asignatura a WHERE a.idAsignatura = :idAsignatura"),
    @NamedQuery(name = "Asignatura.findByNombreAsignatura", query = "SELECT a FROM Asignatura a WHERE a.nombreAsignatura = :nombreAsignatura")})
public class Asignatura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAsignatura")
    private Integer idAsignatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreAsignatura")
    private String nombreAsignatura;
    @OneToMany(mappedBy = "idAsignatura")
    private Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollection;
    @OneToMany(mappedBy = "idAsignatura")
    private Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollection;

    public Asignatura() {
    }

    public Asignatura(Integer idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public Asignatura(Integer idAsignatura, String nombreAsignatura) {
        this.idAsignatura = idAsignatura;
        this.nombreAsignatura = nombreAsignatura;
    }

    public Integer getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(Integer idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    @XmlTransient
    public Collection<DetalleCursoAsignatura> getDetalleCursoAsignaturaCollection() {
        return detalleCursoAsignaturaCollection;
    }

    public void setDetalleCursoAsignaturaCollection(Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollection) {
        this.detalleCursoAsignaturaCollection = detalleCursoAsignaturaCollection;
    }

    @XmlTransient
    public Collection<DetalleProfesorAsignatura> getDetalleProfesorAsignaturaCollection() {
        return detalleProfesorAsignaturaCollection;
    }

    public void setDetalleProfesorAsignaturaCollection(Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollection) {
        this.detalleProfesorAsignaturaCollection = detalleProfesorAsignaturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAsignatura != null ? idAsignatura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asignatura)) {
            return false;
        }
        Asignatura other = (Asignatura) object;
        if ((this.idAsignatura == null && other.idAsignatura != null) || (this.idAsignatura != null && !this.idAsignatura.equals(other.idAsignatura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Asignatura[ idAsignatura=" + idAsignatura + " ]";
    }
    
}
