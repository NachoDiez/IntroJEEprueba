/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Entidades.exceptions.NonexistentEntityException;
import Entidades.exceptions.PreexistingEntityException;
import Entidades.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author admin
 */
public class DetallesCursoProfesorJpaController implements Serializable {

    public DetallesCursoProfesorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetallesCursoProfesor detallesCursoProfesor) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Curso idCurso = detallesCursoProfesor.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                detallesCursoProfesor.setIdCurso(idCurso);
            }
            em.persist(detallesCursoProfesor);
            if (idCurso != null) {
                idCurso.getDetallesCursoProfesorCollection().add(detallesCursoProfesor);
                idCurso = em.merge(idCurso);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallesCursoProfesor(detallesCursoProfesor.getIdDetalleCurso2()) != null) {
                throw new PreexistingEntityException("DetallesCursoProfesor " + detallesCursoProfesor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetallesCursoProfesor detallesCursoProfesor) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetallesCursoProfesor persistentDetallesCursoProfesor = em.find(DetallesCursoProfesor.class, detallesCursoProfesor.getIdDetalleCurso2());
            Curso idCursoOld = persistentDetallesCursoProfesor.getIdCurso();
            Curso idCursoNew = detallesCursoProfesor.getIdCurso();
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                detallesCursoProfesor.setIdCurso(idCursoNew);
            }
            detallesCursoProfesor = em.merge(detallesCursoProfesor);
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getDetallesCursoProfesorCollection().remove(detallesCursoProfesor);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getDetallesCursoProfesorCollection().add(detallesCursoProfesor);
                idCursoNew = em.merge(idCursoNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallesCursoProfesor.getIdDetalleCurso2();
                if (findDetallesCursoProfesor(id) == null) {
                    throw new NonexistentEntityException("The detallesCursoProfesor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetallesCursoProfesor detallesCursoProfesor;
            try {
                detallesCursoProfesor = em.getReference(DetallesCursoProfesor.class, id);
                detallesCursoProfesor.getIdDetalleCurso2();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallesCursoProfesor with id " + id + " no longer exists.", enfe);
            }
            Curso idCurso = detallesCursoProfesor.getIdCurso();
            if (idCurso != null) {
                idCurso.getDetallesCursoProfesorCollection().remove(detallesCursoProfesor);
                idCurso = em.merge(idCurso);
            }
            em.remove(detallesCursoProfesor);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetallesCursoProfesor> findDetallesCursoProfesorEntities() {
        return findDetallesCursoProfesorEntities(true, -1, -1);
    }

    public List<DetallesCursoProfesor> findDetallesCursoProfesorEntities(int maxResults, int firstResult) {
        return findDetallesCursoProfesorEntities(false, maxResults, firstResult);
    }

    private List<DetallesCursoProfesor> findDetallesCursoProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetallesCursoProfesor.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DetallesCursoProfesor findDetallesCursoProfesor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetallesCursoProfesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallesCursoProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetallesCursoProfesor> rt = cq.from(DetallesCursoProfesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
