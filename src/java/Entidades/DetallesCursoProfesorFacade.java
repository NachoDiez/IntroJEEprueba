/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
public class DetallesCursoProfesorFacade extends AbstractFacade<DetallesCursoProfesor> {
    @PersistenceContext(unitName = "IntroJEEPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetallesCursoProfesorFacade() {
        super(DetallesCursoProfesor.class);
    }
    
}
