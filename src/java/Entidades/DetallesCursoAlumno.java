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
@Table(name = "Detalles_Curso_Alumno")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesCursoAlumno.findAll", query = "SELECT d FROM DetallesCursoAlumno d"),
    @NamedQuery(name = "DetallesCursoAlumno.findByIdDetalleCurso", query = "SELECT d FROM DetallesCursoAlumno d WHERE d.idDetalleCurso = :idDetalleCurso")})
public class DetallesCursoAlumno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalle_Curso")
    private Integer idDetalleCurso;
    @JoinColumn(name = "idAlumno", referencedColumnName = "idAlumno")
    @ManyToOne
    private Alumno idAlumno;
    @JoinColumn(name = "idCurso", referencedColumnName = "idCurso")
    @ManyToOne
    private Curso idCurso;

    public DetallesCursoAlumno() {
    }

    public DetallesCursoAlumno(Integer idDetalleCurso) {
        this.idDetalleCurso = idDetalleCurso;
    }

    public Integer getIdDetalleCurso() {
        return idDetalleCurso;
    }

    public void setIdDetalleCurso(Integer idDetalleCurso) {
        this.idDetalleCurso = idDetalleCurso;
    }

    public Alumno getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Alumno idAlumno) {
        this.idAlumno = idAlumno;
    }

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleCurso != null ? idDetalleCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesCursoAlumno)) {
            return false;
        }
        DetallesCursoAlumno other = (DetallesCursoAlumno) object;
        if ((this.idDetalleCurso == null && other.idDetalleCurso != null) || (this.idDetalleCurso != null && !this.idDetalleCurso.equals(other.idDetalleCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesCursoAlumno[ idDetalleCurso=" + idDetalleCurso + " ]";
    }
    
}
