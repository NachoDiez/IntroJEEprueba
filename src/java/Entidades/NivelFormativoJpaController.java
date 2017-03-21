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
public class NivelFormativoJpaController implements Serializable {

    public NivelFormativoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NivelFormativo nivelFormativo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (nivelFormativo.getDetallesProfesornivelFormativoCollection() == null) {
            nivelFormativo.setDetallesProfesornivelFormativoCollection(new ArrayList<DetallesProfesornivelFormativo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DetallesProfesornivelFormativo> attachedDetallesProfesornivelFormativoCollection = new ArrayList<DetallesProfesornivelFormativo>();
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach : nivelFormativo.getDetallesProfesornivelFormativoCollection()) {
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach = em.getReference(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach.getClass(), detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach.getIdDetalle());
                attachedDetallesProfesornivelFormativoCollection.add(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach);
            }
            nivelFormativo.setDetallesProfesornivelFormativoCollection(attachedDetallesProfesornivelFormativoCollection);
            em.persist(nivelFormativo);
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo : nivelFormativo.getDetallesProfesornivelFormativoCollection()) {
                NivelFormativo oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.getIdNivelFormativo();
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.setIdNivelFormativo(nivelFormativo);
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
                if (oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo != null) {
                    oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
                    oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = em.merge(oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findNivelFormativo(nivelFormativo.getIdNivelFormativo()) != null) {
                throw new PreexistingEntityException("NivelFormativo " + nivelFormativo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NivelFormativo nivelFormativo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            NivelFormativo persistentNivelFormativo = em.find(NivelFormativo.class, nivelFormativo.getIdNivelFormativo());
            Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollectionOld = persistentNivelFormativo.getDetallesProfesornivelFormativoCollection();
            Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollectionNew = nivelFormativo.getDetallesProfesornivelFormativoCollection();
            Collection<DetallesProfesornivelFormativo> attachedDetallesProfesornivelFormativoCollectionNew = new ArrayList<DetallesProfesornivelFormativo>();
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach : detallesProfesornivelFormativoCollectionNew) {
                detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach = em.getReference(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach.getClass(), detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach.getIdDetalle());
                attachedDetallesProfesornivelFormativoCollectionNew.add(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach);
            }
            detallesProfesornivelFormativoCollectionNew = attachedDetallesProfesornivelFormativoCollectionNew;
            nivelFormativo.setDetallesProfesornivelFormativoCollection(detallesProfesornivelFormativoCollectionNew);
            nivelFormativo = em.merge(nivelFormativo);
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo : detallesProfesornivelFormativoCollectionOld) {
                if (!detallesProfesornivelFormativoCollectionNew.contains(detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo)) {
                    detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo.setIdNivelFormativo(null);
                    detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo);
                }
            }
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo : detallesProfesornivelFormativoCollectionNew) {
                if (!detallesProfesornivelFormativoCollectionOld.contains(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo)) {
                    NivelFormativo oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo = detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.getIdNivelFormativo();
                    detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.setIdNivelFormativo(nivelFormativo);
                    detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo);
                    if (oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo != null && !oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.equals(nivelFormativo)) {
                        oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo);
                        oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo = em.merge(oldIdNivelFormativoOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo);
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
                Integer id = nivelFormativo.getIdNivelFormativo();
                if (findNivelFormativo(id) == null) {
                    throw new NonexistentEntityException("The nivelFormativo with id " + id + " no longer exists.");
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
            NivelFormativo nivelFormativo;
            try {
                nivelFormativo = em.getReference(NivelFormativo.class, id);
                nivelFormativo.getIdNivelFormativo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivelFormativo with id " + id + " no longer exists.", enfe);
            }
            Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollection = nivelFormativo.getDetallesProfesornivelFormativoCollection();
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo : detallesProfesornivelFormativoCollection) {
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.setIdNivelFormativo(null);
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
            }
            em.remove(nivelFormativo);
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

    public List<NivelFormativo> findNivelFormativoEntities() {
        return findNivelFormativoEntities(true, -1, -1);
    }

    public List<NivelFormativo> findNivelFormativoEntities(int maxResults, int firstResult) {
        return findNivelFormativoEntities(false, maxResults, firstResult);
    }

    private List<NivelFormativo> findNivelFormativoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NivelFormativo.class));
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

    public NivelFormativo findNivelFormativo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NivelFormativo.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivelFormativoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NivelFormativo> rt = cq.from(NivelFormativo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
