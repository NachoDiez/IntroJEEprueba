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
@Table(name = "NivelFormativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelFormativo.findAll", query = "SELECT n FROM NivelFormativo n"),
    @NamedQuery(name = "NivelFormativo.findByIdNivelFormativo", query = "SELECT n FROM NivelFormativo n WHERE n.idNivelFormativo = :idNivelFormativo"),
    @NamedQuery(name = "NivelFormativo.findByDescripcionNivelFormativo", query = "SELECT n FROM NivelFormativo n WHERE n.descripcionNivelFormativo = :descripcionNivelFormativo")})
public class NivelFormativo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idNivelFormativo")
    private Integer idNivelFormativo;
    @Size(max = 50)
    @Column(name = "descripcionNivelFormativo")
    private String descripcionNivelFormativo;
    @OneToMany(mappedBy = "idNivelFormativo")
    private Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollection;

    public NivelFormativo() {
    }

    public NivelFormativo(Integer idNivelFormativo) {
        this.idNivelFormativo = idNivelFormativo;
    }

    public Integer getIdNivelFormativo() {
        return idNivelFormativo;
    }

    public void setIdNivelFormativo(Integer idNivelFormativo) {
        this.idNivelFormativo = idNivelFormativo;
    }

    public String getDescripcionNivelFormativo() {
        return descripcionNivelFormativo;
    }

    public void setDescripcionNivelFormativo(String descripcionNivelFormativo) {
        this.descripcionNivelFormativo = descripcionNivelFormativo;
    }

    @XmlTransient
    public Collection<DetallesProfesornivelFormativo> getDetallesProfesornivelFormativoCollection() {
        return detallesProfesornivelFormativoCollection;
    }

    public void setDetallesProfesornivelFormativoCollection(Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollection) {
        this.detallesProfesornivelFormativoCollection = detallesProfesornivelFormativoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNivelFormativo != null ? idNivelFormativo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelFormativo)) {
            return false;
        }
        NivelFormativo other = (NivelFormativo) object;
        if ((this.idNivelFormativo == null && other.idNivelFormativo != null) || (this.idNivelFormativo != null && !this.idNivelFormativo.equals(other.idNivelFormativo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.NivelFormativo[ idNivelFormativo=" + idNivelFormativo + " ]";
    }
    
}
