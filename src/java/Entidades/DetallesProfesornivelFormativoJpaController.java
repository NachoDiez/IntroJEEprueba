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
public class DetallesProfesornivelFormativoJpaController implements Serializable {

    public DetallesProfesornivelFormativoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetallesProfesornivelFormativo detallesProfesornivelFormativo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            NivelFormativo idNivelFormativo = detallesProfesornivelFormativo.getIdNivelFormativo();
            if (idNivelFormativo != null) {
                idNivelFormativo = em.getReference(idNivelFormativo.getClass(), idNivelFormativo.getIdNivelFormativo());
                detallesProfesornivelFormativo.setIdNivelFormativo(idNivelFormativo);
            }
            Profesor idProfesor = detallesProfesornivelFormativo.getIdProfesor();
            if (idProfesor != null) {
                idProfesor = em.getReference(idProfesor.getClass(), idProfesor.getIdProfesor());
                detallesProfesornivelFormativo.setIdProfesor(idProfesor);
            }
            em.persist(detallesProfesornivelFormativo);
            if (idNivelFormativo != null) {
                idNivelFormativo.getDetallesProfesornivelFormativoCollection().add(detallesProfesornivelFormativo);
                idNivelFormativo = em.merge(idNivelFormativo);
            }
            if (idProfesor != null) {
                idProfesor.getDetallesProfesornivelFormativoCollection().add(detallesProfesornivelFormativo);
                idProfesor = em.merge(idProfesor);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetallesProfesornivelFormativo(detallesProfesornivelFormativo.getIdDetalle()) != null) {
                throw new PreexistingEntityException("DetallesProfesornivelFormativo " + detallesProfesornivelFormativo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetallesProfesornivelFormativo detallesProfesornivelFormativo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetallesProfesornivelFormativo persistentDetallesProfesornivelFormativo = em.find(DetallesProfesornivelFormativo.class, detallesProfesornivelFormativo.getIdDetalle());
            NivelFormativo idNivelFormativoOld = persistentDetallesProfesornivelFormativo.getIdNivelFormativo();
            NivelFormativo idNivelFormativoNew = detallesProfesornivelFormativo.getIdNivelFormativo();
            Profesor idProfesorOld = persistentDetallesProfesornivelFormativo.getIdProfesor();
            Profesor idProfesorNew = detallesProfesornivelFormativo.getIdProfesor();
            if (idNivelFormativoNew != null) {
                idNivelFormativoNew = em.getReference(idNivelFormativoNew.getClass(), idNivelFormativoNew.getIdNivelFormativo());
                detallesProfesornivelFormativo.setIdNivelFormativo(idNivelFormativoNew);
            }
            if (idProfesorNew != null) {
                idProfesorNew = em.getReference(idProfesorNew.getClass(), idProfesorNew.getIdProfesor());
                detallesProfesornivelFormativo.setIdProfesor(idProfesorNew);
            }
            detallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativo);
            if (idNivelFormativoOld != null && !idNivelFormativoOld.equals(idNivelFormativoNew)) {
                idNivelFormativoOld.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativo);
                idNivelFormativoOld = em.merge(idNivelFormativoOld);
            }
            if (idNivelFormativoNew != null && !idNivelFormativoNew.equals(idNivelFormativoOld)) {
                idNivelFormativoNew.getDetallesProfesornivelFormativoCollection().add(detallesProfesornivelFormativo);
                idNivelFormativoNew = em.merge(idNivelFormativoNew);
            }
            if (idProfesorOld != null && !idProfesorOld.equals(idProfesorNew)) {
                idProfesorOld.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativo);
                idProfesorOld = em.merge(idProfesorOld);
            }
            if (idProfesorNew != null && !idProfesorNew.equals(idProfesorOld)) {
                idProfesorNew.getDetallesProfesornivelFormativoCollection().add(detallesProfesornivelFormativo);
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
                Integer id = detallesProfesornivelFormativo.getIdDetalle();
                if (findDetallesProfesornivelFormativo(id) == null) {
                    throw new NonexistentEntityException("The detallesProfesornivelFormativo with id " + id + " no longer exists.");
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
            DetallesProfesornivelFormativo detallesProfesornivelFormativo;
            try {
                detallesProfesornivelFormativo = em.getReference(DetallesProfesornivelFormativo.class, id);
                detallesProfesornivelFormativo.getIdDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallesProfesornivelFormativo with id " + id + " no longer exists.", enfe);
            }
            NivelFormativo idNivelFormativo = detallesProfesornivelFormativo.getIdNivelFormativo();
            if (idNivelFormativo != null) {
                idNivelFormativo.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativo);
                idNivelFormativo = em.merge(idNivelFormativo);
            }
            Profesor idProfesor = detallesProfesornivelFormativo.getIdProfesor();
            if (idProfesor != null) {
                idProfesor.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativo);
                idProfesor = em.merge(idProfesor);
            }
            em.remove(detallesProfesornivelFormativo);
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

    public List<DetallesProfesornivelFormativo> findDetallesProfesornivelFormativoEntities() {
        return findDetallesProfesornivelFormativoEntities(true, -1, -1);
    }

    public List<DetallesProfesornivelFormativo> findDetallesProfesornivelFormativoEntities(int maxResults, int firstResult) {
        return findDetallesProfesornivelFormativoEntities(false, maxResults, firstResult);
    }

    private List<DetallesProfesornivelFormativo> findDetallesProfesornivelFormativoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetallesProfesornivelFormativo.class));
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

    public DetallesProfesornivelFormativo findDetallesProfesornivelFormativo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetallesProfesornivelFormativo.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallesProfesornivelFormativoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetallesProfesornivelFormativo> rt = cq.from(DetallesProfesornivelFormativo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
