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
public class DetalleCursoAsignaturaJpaController implements Serializable {

    public DetalleCursoAsignaturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleCursoAsignatura detalleCursoAsignatura) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Asignatura idAsignatura = detalleCursoAsignatura.getIdAsignatura();
            if (idAsignatura != null) {
                idAsignatura = em.getReference(idAsignatura.getClass(), idAsignatura.getIdAsignatura());
                detalleCursoAsignatura.setIdAsignatura(idAsignatura);
            }
            Curso idCurso = detalleCursoAsignatura.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                detalleCursoAsignatura.setIdCurso(idCurso);
            }
            em.persist(detalleCursoAsignatura);
            if (idAsignatura != null) {
                idAsignatura.getDetalleCursoAsignaturaCollection().add(detalleCursoAsignatura);
                idAsignatura = em.merge(idAsignatura);
            }
            if (idCurso != null) {
                idCurso.getDetalleCursoAsignaturaCollection().add(detalleCursoAsignatura);
                idCurso = em.merge(idCurso);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleCursoAsignatura(detalleCursoAsignatura.getIdDetalle()) != null) {
                throw new PreexistingEntityException("DetalleCursoAsignatura " + detalleCursoAsignatura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleCursoAsignatura detalleCursoAsignatura) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleCursoAsignatura persistentDetalleCursoAsignatura = em.find(DetalleCursoAsignatura.class, detalleCursoAsignatura.getIdDetalle());
            Asignatura idAsignaturaOld = persistentDetalleCursoAsignatura.getIdAsignatura();
            Asignatura idAsignaturaNew = detalleCursoAsignatura.getIdAsignatura();
            Curso idCursoOld = persistentDetalleCursoAsignatura.getIdCurso();
            Curso idCursoNew = detalleCursoAsignatura.getIdCurso();
            if (idAsignaturaNew != null) {
                idAsignaturaNew = em.getReference(idAsignaturaNew.getClass(), idAsignaturaNew.getIdAsignatura());
                detalleCursoAsignatura.setIdAsignatura(idAsignaturaNew);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                detalleCursoAsignatura.setIdCurso(idCursoNew);
            }
            detalleCursoAsignatura = em.merge(detalleCursoAsignatura);
            if (idAsignaturaOld != null && !idAsignaturaOld.equals(idAsignaturaNew)) {
                idAsignaturaOld.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignatura);
                idAsignaturaOld = em.merge(idAsignaturaOld);
            }
            if (idAsignaturaNew != null && !idAsignaturaNew.equals(idAsignaturaOld)) {
                idAsignaturaNew.getDetalleCursoAsignaturaCollection().add(detalleCursoAsignatura);
                idAsignaturaNew = em.merge(idAsignaturaNew);
            }
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignatura);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getDetalleCursoAsignaturaCollection().add(detalleCursoAsignatura);
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
                Integer id = detalleCursoAsignatura.getIdDetalle();
                if (findDetalleCursoAsignatura(id) == null) {
                    throw new NonexistentEntityException("The detalleCursoAsignatura with id " + id + " no longer exists.");
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
            DetalleCursoAsignatura detalleCursoAsignatura;
            try {
                detalleCursoAsignatura = em.getReference(DetalleCursoAsignatura.class, id);
                detalleCursoAsignatura.getIdDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleCursoAsignatura with id " + id + " no longer exists.", enfe);
            }
            Asignatura idAsignatura = detalleCursoAsignatura.getIdAsignatura();
            if (idAsignatura != null) {
                idAsignatura.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignatura);
                idAsignatura = em.merge(idAsignatura);
            }
            Curso idCurso = detalleCursoAsignatura.getIdCurso();
            if (idCurso != null) {
                idCurso.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignatura);
                idCurso = em.merge(idCurso);
            }
            em.remove(detalleCursoAsignatura);
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

    public List<DetalleCursoAsignatura> findDetalleCursoAsignaturaEntities() {
        return findDetalleCursoAsignaturaEntities(true, -1, -1);
    }

    public List<DetalleCursoAsignatura> findDetalleCursoAsignaturaEntities(int maxResults, int firstResult) {
        return findDetalleCursoAsignaturaEntities(false, maxResults, firstResult);
    }

    private List<DetalleCursoAsignatura> findDetalleCursoAsignaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleCursoAsignatura.class));
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

    public DetalleCursoAsignatura findDetalleCursoAsignatura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleCursoAsignatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleCursoAsignaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleCursoAsignatura> rt = cq.from(DetalleCursoAsignatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
