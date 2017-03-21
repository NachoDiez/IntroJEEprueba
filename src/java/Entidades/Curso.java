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
@Table(name = "Curso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c"),
    @NamedQuery(name = "Curso.findByIdCurso", query = "SELECT c FROM Curso c WHERE c.idCurso = :idCurso"),
    @NamedQuery(name = "Curso.findByNombreCurso", query = "SELECT c FROM Curso c WHERE c.nombreCurso = :nombreCurso"),
    @NamedQuery(name = "Curso.findByFechaInicioCurso", query = "SELECT c FROM Curso c WHERE c.fechaInicioCurso = :fechaInicioCurso"),
    @NamedQuery(name = "Curso.findByFechaFinCurso", query = "SELECT c FROM Curso c WHERE c.fechaFinCurso = :fechaFinCurso"),
    @NamedQuery(name = "Curso.findByPrecioCurso", query = "SELECT c FROM Curso c WHERE c.precioCurso = :precioCurso")})
public class Curso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCurso")
    private Integer idCurso;
    @Size(max = 50)
    @Column(name = "nombreCurso")
    private String nombreCurso;
    @Size(max = 50)
    @Column(name = "fechaInicioCurso")
    private String fechaInicioCurso;
    @Size(max = 50)
    @Column(name = "fechaFinCurso")
    private String fechaFinCurso;
    @Column(name = "precioCurso")
    private Integer precioCurso;
    @OneToMany(mappedBy = "idCurso")
    private Collection<DetallesCursoProfesor> detallesCursoProfesorCollection;
    @OneToMany(mappedBy = "idCurso")
    private Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollection;
    @OneToMany(mappedBy = "idCurso")
    private Collection<DetallesCursoAlumno> detallesCursoAlumnoCollection;

    public Curso() {
    }

    public Curso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getFechaInicioCurso() {
        return fechaInicioCurso;
    }

    public void setFechaInicioCurso(String fechaInicioCurso) {
        this.fechaInicioCurso = fechaInicioCurso;
    }

    public String getFechaFinCurso() {
        return fechaFinCurso;
    }

    public void setFechaFinCurso(String fechaFinCurso) {
        this.fechaFinCurso = fechaFinCurso;
    }

    public Integer getPrecioCurso() {
        return precioCurso;
    }

    public void setPrecioCurso(Integer precioCurso) {
        this.precioCurso = precioCurso;
    }

    @XmlTransient
    public Collection<DetallesCursoProfesor> getDetallesCursoProfesorCollection() {
        return detallesCursoProfesorCollection;
    }

    public void setDetallesCursoProfesorCollection(Collection<DetallesCursoProfesor> detallesCursoProfesorCollection) {
        this.detallesCursoProfesorCollection = detallesCursoProfesorCollection;
    }

    @XmlTransient
    public Collection<DetalleCursoAsignatura> getDetalleCursoAsignaturaCollection() {
        return detalleCursoAsignaturaCollection;
    }

    public void setDetalleCursoAsignaturaCollection(Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollection) {
        this.detalleCursoAsignaturaCollection = detalleCursoAsignaturaCollection;
    }

    @XmlTransient
    public Collection<DetallesCursoAlumno> getDetallesCursoAlumnoCollection() {
        return detallesCursoAlumnoCollection;
    }

    public void setDetallesCursoAlumnoCollection(Collection<DetallesCursoAlumno> detallesCursoAlumnoCollection) {
        this.detallesCursoAlumnoCollection = detallesCursoAlumnoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCurso != null ? idCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.idCurso == null && other.idCurso != null) || (this.idCurso != null && !this.idCurso.equals(other.idCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Curso[ idCurso=" + idCurso + " ]";
    }
    
}
