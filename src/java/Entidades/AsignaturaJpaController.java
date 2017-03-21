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
public class AsignaturaJpaController implements Serializable {

    public AsignaturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignatura asignatura) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (asignatura.getDetalleCursoAsignaturaCollection() == null) {
            asignatura.setDetalleCursoAsignaturaCollection(new ArrayList<DetalleCursoAsignatura>());
        }
        if (asignatura.getDetalleProfesorAsignaturaCollection() == null) {
            asignatura.setDetalleProfesorAsignaturaCollection(new ArrayList<DetalleProfesorAsignatura>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DetalleCursoAsignatura> attachedDetalleCursoAsignaturaCollection = new ArrayList<DetalleCursoAsignatura>();
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach : asignatura.getDetalleCursoAsignaturaCollection()) {
                detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach = em.getReference(detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach.getClass(), detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach.getIdDetalle());
                attachedDetalleCursoAsignaturaCollection.add(detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach);
            }
            asignatura.setDetalleCursoAsignaturaCollection(attachedDetalleCursoAsignaturaCollection);
            Collection<DetalleProfesorAsignatura> attachedDetalleProfesorAsignaturaCollection = new ArrayList<DetalleProfesorAsignatura>();
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach : asignatura.getDetalleProfesorAsignaturaCollection()) {
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach = em.getReference(detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach.getClass(), detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach.getIdDetalle());
                attachedDetalleProfesorAsignaturaCollection.add(detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach);
            }
            asignatura.setDetalleProfesorAsignaturaCollection(attachedDetalleProfesorAsignaturaCollection);
            em.persist(asignatura);
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionDetalleCursoAsignatura : asignatura.getDetalleCursoAsignaturaCollection()) {
                Asignatura oldIdAsignaturaOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura = detalleCursoAsignaturaCollectionDetalleCursoAsignatura.getIdAsignatura();
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura.setIdAsignatura(asignatura);
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionDetalleCursoAsignatura);
                if (oldIdAsignaturaOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura != null) {
                    oldIdAsignaturaOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignaturaCollectionDetalleCursoAsignatura);
                    oldIdAsignaturaOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura = em.merge(oldIdAsignaturaOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura);
                }
            }
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura : asignatura.getDetalleProfesorAsignaturaCollection()) {
                Asignatura oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.getIdAsignatura();
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.setIdAsignatura(asignatura);
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
                if (oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura != null) {
                    oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
                    oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = em.merge(oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAsignatura(asignatura.getIdAsignatura()) != null) {
                throw new PreexistingEntityException("Asignatura " + asignatura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignatura asignatura) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Asignatura persistentAsignatura = em.find(Asignatura.class, asignatura.getIdAsignatura());
            Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollectionOld = persistentAsignatura.getDetalleCursoAsignaturaCollection();
            Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollectionNew = asignatura.getDetalleCursoAsignaturaCollection();
            Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollectionOld = persistentAsignatura.getDetalleProfesorAsignaturaCollection();
            Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollectionNew = asignatura.getDetalleProfesorAsignaturaCollection();
            Collection<DetalleCursoAsignatura> attachedDetalleCursoAsignaturaCollectionNew = new ArrayList<DetalleCursoAsignatura>();
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach : detalleCursoAsignaturaCollectionNew) {
                detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach = em.getReference(detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach.getClass(), detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach.getIdDetalle());
                attachedDetalleCursoAsignaturaCollectionNew.add(detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach);
            }
            detalleCursoAsignaturaCollectionNew = attachedDetalleCursoAsignaturaCollectionNew;
            asignatura.setDetalleCursoAsignaturaCollection(detalleCursoAsignaturaCollectionNew);
            Collection<DetalleProfesorAsignatura> attachedDetalleProfesorAsignaturaCollectionNew = new ArrayList<DetalleProfesorAsignatura>();
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach : detalleProfesorAsignaturaCollectionNew) {
                detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach = em.getReference(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach.getClass(), detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach.getIdDetalle());
                attachedDetalleProfesorAsignaturaCollectionNew.add(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach);
            }
            detalleProfesorAsignaturaCollectionNew = attachedDetalleProfesorAsignaturaCollectionNew;
            asignatura.setDetalleProfesorAsignaturaCollection(detalleProfesorAsignaturaCollectionNew);
            asignatura = em.merge(asignatura);
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura : detalleCursoAsignaturaCollectionOld) {
                if (!detalleCursoAsignaturaCollectionNew.contains(detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura)) {
                    detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura.setIdAsignatura(null);
                    detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura);
                }
            }
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura : detalleCursoAsignaturaCollectionNew) {
                if (!detalleCursoAsignaturaCollectionOld.contains(detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura)) {
                    Asignatura oldIdAsignaturaOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura = detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.getIdAsignatura();
                    detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.setIdAsignatura(asignatura);
                    detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura);
                    if (oldIdAsignaturaOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura != null && !oldIdAsignaturaOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.equals(asignatura)) {
                        oldIdAsignaturaOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura);
                        oldIdAsignaturaOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura = em.merge(oldIdAsignaturaOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura);
                    }
                }
            }
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura : detalleProfesorAsignaturaCollectionOld) {
                if (!detalleProfesorAsignaturaCollectionNew.contains(detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura)) {
                    detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura.setIdAsignatura(null);
                    detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura);
                }
            }
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura : detalleProfesorAsignaturaCollectionNew) {
                if (!detalleProfesorAsignaturaCollectionOld.contains(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura)) {
                    Asignatura oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura = detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.getIdAsignatura();
                    detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.setIdAsignatura(asignatura);
                    detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura);
                    if (oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura != null && !oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.equals(asignatura)) {
                        oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura);
                        oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura = em.merge(oldIdAsignaturaOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura);
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
                Integer id = asignatura.getIdAsignatura();
                if (findAsignatura(id) == null) {
                    throw new NonexistentEntityException("The asignatura with id " + id + " no longer exists.");
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
            Asignatura asignatura;
            try {
                asignatura = em.getReference(Asignatura.class, id);
                asignatura.getIdAsignatura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignatura with id " + id + " no longer exists.", enfe);
            }
            Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollection = asignatura.getDetalleCursoAsignaturaCollection();
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionDetalleCursoAsignatura : detalleCursoAsignaturaCollection) {
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura.setIdAsignatura(null);
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionDetalleCursoAsignatura);
            }
            Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollection = asignatura.getDetalleProfesorAsignaturaCollection();
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura : detalleProfesorAsignaturaCollection) {
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.setIdAsignatura(null);
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
            }
            em.remove(asignatura);
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

    public List<Asignatura> findAsignaturaEntities() {
        return findAsignaturaEntities(true, -1, -1);
    }

    public List<Asignatura> findAsignaturaEntities(int maxResults, int firstResult) {
        return findAsignaturaEntities(false, maxResults, firstResult);
    }

    private List<Asignatura> findAsignaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignatura.class));
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

    public Asignatura findAsignatura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignatura> rt = cq.from(Asignatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
