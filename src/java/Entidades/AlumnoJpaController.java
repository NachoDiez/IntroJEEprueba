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
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author admin
 */
public class AlumnoJpaController implements Serializable {

    public AlumnoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (alumno.getDetallesCursoAlumnoCollection() == null) {
            alumno.setDetallesCursoAlumnoCollection(new ArrayList<DetallesCursoAlumno>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DetallesCursoAlumno> attachedDetallesCursoAlumnoCollection = new ArrayList<DetallesCursoAlumno>();
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach : alumno.getDetallesCursoAlumnoCollection()) {
                detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach = em.getReference(detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach.getClass(), detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach.getIdDetalleCurso());
                attachedDetallesCursoAlumnoCollection.add(detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach);
            }
            alumno.setDetallesCursoAlumnoCollection(attachedDetallesCursoAlumnoCollection);
            em.persist(alumno);
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionDetallesCursoAlumno : alumno.getDetallesCursoAlumnoCollection()) {
                Alumno oldIdAlumnoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno = detallesCursoAlumnoCollectionDetallesCursoAlumno.getIdAlumno();
                detallesCursoAlumnoCollectionDetallesCursoAlumno.setIdAlumno(alumno);
                detallesCursoAlumnoCollectionDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionDetallesCursoAlumno);
                if (oldIdAlumnoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno != null) {
                    oldIdAlumnoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumnoCollectionDetallesCursoAlumno);
                    oldIdAlumnoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno = em.merge(oldIdAlumnoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAlumno(alumno.getIdAlumno()) != null) {
                throw new PreexistingEntityException("Alumno " + alumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getIdAlumno());
            Collection<DetallesCursoAlumno> detallesCursoAlumnoCollectionOld = persistentAlumno.getDetallesCursoAlumnoCollection();
            Collection<DetallesCursoAlumno> detallesCursoAlumnoCollectionNew = alumno.getDetallesCursoAlumnoCollection();
            Collection<DetallesCursoAlumno> attachedDetallesCursoAlumnoCollectionNew = new ArrayList<DetallesCursoAlumno>();
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach : detallesCursoAlumnoCollectionNew) {
                detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach = em.getReference(detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach.getClass(), detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach.getIdDetalleCurso());
                attachedDetallesCursoAlumnoCollectionNew.add(detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach);
            }
            detallesCursoAlumnoCollectionNew = attachedDetallesCursoAlumnoCollectionNew;
            alumno.setDetallesCursoAlumnoCollection(detallesCursoAlumnoCollectionNew);
            alumno = em.merge(alumno);
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionOldDetallesCursoAlumno : detallesCursoAlumnoCollectionOld) {
                if (!detallesCursoAlumnoCollectionNew.contains(detallesCursoAlumnoCollectionOldDetallesCursoAlumno)) {
                    detallesCursoAlumnoCollectionOldDetallesCursoAlumno.setIdAlumno(null);
                    detallesCursoAlumnoCollectionOldDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionOldDetallesCursoAlumno);
                }
            }
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionNewDetallesCursoAlumno : detallesCursoAlumnoCollectionNew) {
                if (!detallesCursoAlumnoCollectionOld.contains(detallesCursoAlumnoCollectionNewDetallesCursoAlumno)) {
                    Alumno oldIdAlumnoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno = detallesCursoAlumnoCollectionNewDetallesCursoAlumno.getIdAlumno();
                    detallesCursoAlumnoCollectionNewDetallesCursoAlumno.setIdAlumno(alumno);
                    detallesCursoAlumnoCollectionNewDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionNewDetallesCursoAlumno);
                    if (oldIdAlumnoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno != null && !oldIdAlumnoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno.equals(alumno)) {
                        oldIdAlumnoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumnoCollectionNewDetallesCursoAlumno);
                        oldIdAlumnoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno = em.merge(oldIdAlumnoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno);
                    }
                }
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
                Integer id = alumno.getIdAlumno();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
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
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getIdAlumno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            Collection<DetallesCursoAlumno> detallesCursoAlumnoCollection = alumno.getDetallesCursoAlumnoCollection();
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionDetallesCursoAlumno : detallesCursoAlumnoCollection) {
                detallesCursoAlumnoCollectionDetallesCursoAlumno.setIdAlumno(null);
                detallesCursoAlumnoCollectionDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionDetallesCursoAlumno);
            }
            em.remove(alumno);
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

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
