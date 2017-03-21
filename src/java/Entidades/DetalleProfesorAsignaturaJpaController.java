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
public class DetalleProfesorAsignaturaJpaController implements Serializable {

    public DetalleProfesorAsignaturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleProfesorAsignatura detalleProfesorAsignatura) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Asignatura idAsignatura = detalleProfesorAsignatura.getIdAsignatura();
            if (idAsignatura != null) {
                idAsignatura = em.getReference(idAsignatura.getClass(), idAsignatura.getIdAsignatura());
                detalleProfesorAsignatura.setIdAsignatura(idAsignatura);
            }
            Profesor idProfesor = detalleProfesorAsignatura.getIdProfesor();
            if (idProfesor != null) {
                idProfesor = em.getReference(idProfesor.getClass(), idProfesor.getIdProfesor());
                detalleProfesorAsignatura.setIdProfesor(idProfesor);
            }
            em.persist(detalleProfesorAsignatura);
            if (idAsignatura != null) {
                idAsignatura.getDetalleProfesorAsignaturaCollection().add(detalleProfesorAsignatura);
                idAsignatura = em.merge(idAsignatura);
            }
            if (idProfesor != null) {
                idProfesor.getDetalleProfesorAsignaturaCollection().add(detalleProfesorAsignatura);
                idProfesor = em.merge(idProfesor);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleProfesorAsignatura(detalleProfesorAsignatura.getIdDetalle()) != null) {
                throw new PreexistingEntityException("DetalleProfesorAsignatura " + detalleProfesorAsignatura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleProfesorAsignatura detalleProfesorAsignatura) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleProfesorAsignatura persistentDetalleProfesorAsignatura = em.find(DetalleProfesorAsignatura.class, detalleProfesorAsignatura.getIdDetalle());
            Asignatura idAsignaturaOld = persistentDetalleProfesorAsignatura.getIdAsignatura();
            Asignatura idAsignaturaNew = detalleProfesorAsignatura.getIdAsignatura();
            Profesor idProfesorOld = persistentDetalleProfesorAsignatura.getIdProfesor();
            Profesor idProfesorNew = detalleProfesorAsignatura.getIdProfesor();
            if (idAsignaturaNew != null) {
                idAsignaturaNew = em.getReference(idAsignaturaNew.getClass(), idAsignaturaNew.getIdAsignatura());
                detalleProfesorAsignatura.setIdAsignatura(idAsignaturaNew);
            }
            if (idProfesorNew != null) {
                idProfesorNew = em.getReference(idProfesorNew.getClass(), idProfesorNew.getIdProfesor());
                detalleProfesorAsignatura.setIdProfesor(idProfesorNew);
            }
            detalleProfesorAsignatura = em.merge(detalleProfesorAsignatura);
            if (idAsignaturaOld != null && !idAsignaturaOld.equals(idAsignaturaNew)) {
                idAsignaturaOld.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignatura);
                idAsignaturaOld = em.merge(idAsignaturaOld);
            }
            if (idAsignaturaNew != null && !idAsignaturaNew.equals(idAsignaturaOld)) {
                idAsignaturaNew.getDetalleProfesorAsignaturaCollection().add(detalleProfesorAsignatura);
                idAsignaturaNew = em.merge(idAsignaturaNew);
            }
            if (idProfesorOld != null && !idProfesorOld.equals(idProfesorNew)) {
                idProfesorOld.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignatura);
                idProfesorOld = em.merge(idProfesorOld);
            }
            if (idProfesorNew != null && !idProfesorNew.equals(idProfesorOld)) {
                idProfesorNew.getDetalleProfesorAsignaturaCollection().add(detalleProfesorAsignatura);
                idProfesorNew = em.merge(idProfesorNew);
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
                Integer id = detalleProfesorAsignatura.getIdDetalle();
                if (findDetalleProfesorAsignatura(id) == null) {
                    throw new NonexistentEntityException("The detalleProfesorAsignatura with id " + id + " no longer exists.");
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
            DetalleProfesorAsignatura detalleProfesorAsignatura;
            try {
                detalleProfesorAsignatura = em.getReference(DetalleProfesorAsignatura.class, id);
                detalleProfesorAsignatura.getIdDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleProfesorAsignatura with id " + id + " no longer exists.", enfe);
            }
            Asignatura idAsignatura = detalleProfesorAsignatura.getIdAsignatura();
            if (idAsignatura != null) {
                idAsignatura.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignatura);
                idAsignatura = em.merge(idAsignatura);
            }
            Profesor idProfesor = detalleProfesorAsignatura.getIdProfesor();
            if (idProfesor != null) {
                idProfesor.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignatura);
                idProfesor = em.merge(idProfesor);
            }
            em.remove(detalleProfesorAsignatura);
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

    public List<DetalleProfesorAsignatura> findDetalleProfesorAsignaturaEntities() {
        return findDetalleProfesorAsignaturaEntities(true, -1, -1);
    }

    public List<DetalleProfesorAsignatura> findDetalleProfesorAsignaturaEntities(int maxResults, int firstResult) {
        return findDetalleProfesorAsignaturaEntities(false, maxResults, firstResult);
    }

    private List<DetalleProfesorAsignatura> findDetalleProfesorAsignaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleProfesorAsignatura.class));
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

    public DetalleProfesorAsignatura findDetalleProfesorAsignatura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleProfesorAsignatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleProfesorAsignaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleProfesorAsignatura> rt = cq.from(DetalleProfesorAsignatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
