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
public class DetallesCursoAlumnoJpaController implements Serializable {

    public DetallesCursoAlumnoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetallesCursoAlumno detallesCursoAlumno) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Alumno idAlumno = detallesCursoAlumno.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getIdAlumno());
                detallesCursoAlumno.setIdAlumno(idAlumno);
            }
            Curso idCurso = detallesCursoAlumno.getIdCurso();
            if (idCurso != null) {
                idCurso = em.getReference(idCurso.getClass(), idCurso.getIdCurso());
                detallesCursoAlumno.setIdCurso(idCurso);
            }
            em.persist(detallesCursoAlumno);
            if (idAlumno != null) {
                idAlumno.getDetallesCursoAlumnoCollection().add(detallesCursoAlumno);
                idAlumno = em.merge(idAlumno);
            }
            if (idCurso != null) {
                idCurso.getDetallesCursoAlumnoCollection().add(detallesCursoAlumno);
                idCurso = em.merge(idCurso);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallesCursoAlumno(detallesCursoAlumno.getIdDetalleCurso()) != null) {
                throw new PreexistingEntityException("DetallesCursoAlumno " + detallesCursoAlumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetallesCursoAlumno detallesCursoAlumno) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetallesCursoAlumno persistentDetallesCursoAlumno = em.find(DetallesCursoAlumno.class, detallesCursoAlumno.getIdDetalleCurso());
            Alumno idAlumnoOld = persistentDetallesCursoAlumno.getIdAlumno();
            Alumno idAlumnoNew = detallesCursoAlumno.getIdAlumno();
            Curso idCursoOld = persistentDetallesCursoAlumno.getIdCurso();
            Curso idCursoNew = detallesCursoAlumno.getIdCurso();
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getIdAlumno());
                detallesCursoAlumno.setIdAlumno(idAlumnoNew);
            }
            if (idCursoNew != null) {
                idCursoNew = em.getReference(idCursoNew.getClass(), idCursoNew.getIdCurso());
                detallesCursoAlumno.setIdCurso(idCursoNew);
            }
            detallesCursoAlumno = em.merge(detallesCursoAlumno);
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumno);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getDetallesCursoAlumnoCollection().add(detallesCursoAlumno);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            if (idCursoOld != null && !idCursoOld.equals(idCursoNew)) {
                idCursoOld.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumno);
                idCursoOld = em.merge(idCursoOld);
            }
            if (idCursoNew != null && !idCursoNew.equals(idCursoOld)) {
                idCursoNew.getDetallesCursoAlumnoCollection().add(detallesCursoAlumno);
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
                Integer id = detallesCursoAlumno.getIdDetalleCurso();
                if (findDetallesCursoAlumno(id) == null) {
                    throw new NonexistentEntityException("The detallesCursoAlumno with id " + id + " no longer exists.");
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
            DetallesCursoAlumno detallesCursoAlumno;
            try {
                detallesCursoAlumno = em.getReference(DetallesCursoAlumno.class, id);
                detallesCursoAlumno.getIdDetalleCurso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallesCursoAlumno with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumno = detallesCursoAlumno.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumno);
                idAlumno = em.merge(idAlumno);
            }
            Curso idCurso = detallesCursoAlumno.getIdCurso();
            if (idCurso != null) {
                idCurso.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumno);
                idCurso = em.merge(idCurso);
            }
            em.remove(detallesCursoAlumno);
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

    public List<DetallesCursoAlumno> findDetallesCursoAlumnoEntities() {
        return findDetallesCursoAlumnoEntities(true, -1, -1);
    }

    public List<DetallesCursoAlumno> findDetallesCursoAlumnoEntities(int maxResults, int firstResult) {
        return findDetallesCursoAlumnoEntities(false, maxResults, firstResult);
    }

    private List<DetallesCursoAlumno> findDetallesCursoAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetallesCursoAlumno.class));
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

    public DetallesCursoAlumno findDetallesCursoAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetallesCursoAlumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallesCursoAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetallesCursoAlumno> rt = cq.from(DetallesCursoAlumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
