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
@Table(name = "Alumno")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a"),
    @NamedQuery(name = "Alumno.findByIdAlumno", query = "SELECT a FROM Alumno a WHERE a.idAlumno = :idAlumno"),
    @NamedQuery(name = "Alumno.findByNombreAlumno", query = "SELECT a FROM Alumno a WHERE a.nombreAlumno = :nombreAlumno"),
    @NamedQuery(name = "Alumno.findByApellidoAlumno", query = "SELECT a FROM Alumno a WHERE a.apellidoAlumno = :apellidoAlumno"),
    @NamedQuery(name = "Alumno.findByDireccionAlumno", query = "SELECT a FROM Alumno a WHERE a.direccionAlumno = :direccionAlumno"),
    @NamedQuery(name = "Alumno.findByTelefonoAlumno", query = "SELECT a FROM Alumno a WHERE a.telefonoAlumno = :telefonoAlumno")})
public class Alumno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAlumno")
    private Integer idAlumno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombreAlumno")
    private String nombreAlumno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apellidoAlumno")
    private String apellidoAlumno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "direccionAlumno")
    private String direccionAlumno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "telefonoAlumno")
    private String telefonoAlumno;
    @OneToMany(mappedBy = "idAlumno")
    private Collection<DetallesCursoAlumno> detallesCursoAlumnoCollection;

    public Alumno() {
    }

    public Alumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
    }

    public Alumno(Integer idAlumno, String nombreAlumno, String apellidoAlumno, String direccionAlumno, String telefonoAlumno) {
        this.idAlumno = idAlumno;
        this.nombreAlumno = nombreAlumno;
        this.apellidoAlumno = apellidoAlumno;
        this.direccionAlumno = direccionAlumno;
        this.telefonoAlumno = telefonoAlumno;
    }

    public Integer getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidoAlumno() {
        return apellidoAlumno;
    }

    public void setApellidoAlumno(String apellidoAlumno) {
        this.apellidoAlumno = apellidoAlumno;
    }

    public String getDireccionAlumno() {
        return direccionAlumno;
    }

    public void setDireccionAlumno(String direccionAlumno) {
        this.direccionAlumno = direccionAlumno;
    }

    public String getTelefonoAlumno() {
        return telefonoAlumno;
    }

    public void setTelefonoAlumno(String telefonoAlumno) {
        this.telefonoAlumno = telefonoAlumno;
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
        hash += (idAlumno != null ? idAlumno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.idAlumno == null && other.idAlumno != null) || (this.idAlumno != null && !this.idAlumno.equals(other.idAlumno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Alumno[ idAlumno=" + idAlumno + " ]";
    }
    
}
