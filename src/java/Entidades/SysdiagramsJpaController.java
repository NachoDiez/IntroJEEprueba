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
public class SysdiagramsJpaController implements Serializable {

    public SysdiagramsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sysdiagrams sysdiagrams) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(sysdiagrams);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSysdiagrams(sysdiagrams.getDiagramId()) != null) {
                throw new PreexistingEntityException("Sysdiagrams " + sysdiagrams + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sysdiagrams sysdiagrams) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            sysdiagrams = em.merge(sysdiagrams);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sysdiagrams.getDiagramId();
                if (findSysdiagrams(id) == null) {
                    throw new NonexistentEntityException("The sysdiagrams with id " + id + " no longer exists.");
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
            Sysdiagrams sysdiagrams;
            try {
                sysdiagrams = em.getReference(Sysdiagrams.class, id);
                sysdiagrams.getDiagramId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sysdiagrams with id " + id + " no longer exists.", enfe);
            }
            em.remove(sysdiagrams);
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

    public List<Sysdiagrams> findSysdiagramsEntities() {
        return findSysdiagramsEntities(true, -1, -1);
    }

    public List<Sysdiagrams> findSysdiagramsEntities(int maxResults, int firstResult) {
        return findSysdiagramsEntities(false, maxResults, firstResult);
    }

    private List<Sysdiagrams> findSysdiagramsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sysdiagrams.class));
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

    public Sysdiagrams findSysdiagrams(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sysdiagrams.class, id);
        } finally {
            em.close();
        }
    }

    public int getSysdiagramsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sysdiagrams> rt = cq.from(Sysdiagrams.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
