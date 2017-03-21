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
@Table(name = "Detalles_Curso_Profesor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesCursoProfesor.findAll", query = "SELECT d FROM DetallesCursoProfesor d"),
    @NamedQuery(name = "DetallesCursoProfesor.findByIdDetalleCurso2", query = "SELECT d FROM DetallesCursoProfesor d WHERE d.idDetalleCurso2 = :idDetalleCurso2"),
    @NamedQuery(name = "DetallesCursoProfesor.findByIdProfesor", query = "SELECT d FROM DetallesCursoProfesor d WHERE d.idProfesor = :idProfesor")})
public class DetallesCursoProfesor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDetalle_Curso2")
    private Integer idDetalleCurso2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idProfesor")
    private int idProfesor;
    @JoinColumn(name = "idCurso", referencedColumnName = "idCurso")
    @ManyToOne
    private Curso idCurso;

    public DetallesCursoProfesor() {
    }

    public DetallesCursoProfesor(Integer idDetalleCurso2) {
        this.idDetalleCurso2 = idDetalleCurso2;
    }

    public DetallesCursoProfesor(Integer idDetalleCurso2, int idProfesor) {
        this.idDetalleCurso2 = idDetalleCurso2;
        this.idProfesor = idProfesor;
    }

    public Integer getIdDetalleCurso2() {
        return idDetalleCurso2;
    }

    public void setIdDetalleCurso2(Integer idDetalleCurso2) {
        this.idDetalleCurso2 = idDetalleCurso2;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
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
        hash += (idDetalleCurso2 != null ? idDetalleCurso2.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesCursoProfesor)) {
            return false;
        }
        DetallesCursoProfesor other = (DetallesCursoProfesor) object;
        if ((this.idDetalleCurso2 == null && other.idDetalleCurso2 != null) || (this.idDetalleCurso2 != null && !this.idDetalleCurso2.equals(other.idDetalleCurso2))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesCursoProfesor[ idDetalleCurso2=" + idDetalleCurso2 + " ]";
    }
    
}
