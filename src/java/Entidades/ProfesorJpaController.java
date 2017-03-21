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
public class ProfesorJpaController implements Serializable {

    public ProfesorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesor profesor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (profesor.getDetallesProfesornivelFormativoCollection() == null) {
            profesor.setDetallesProfesornivelFormativoCollection(new ArrayList<DetallesProfesornivelFormativo>());
        }
        if (profesor.getDetalleProfesorAsignaturaCollection() == null) {
            profesor.setDetalleProfesorAsignaturaCollection(new ArrayList<DetalleProfesorAsignatura>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DetallesProfesornivelFormativo> attachedDetallesProfesornivelFormativoCollection = new ArrayList<DetallesProfesornivelFormativo>();
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach : profesor.getDetallesProfesornivelFormativoCollection()) {
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach = em.getReference(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach.getClass(), detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach.getIdDetalle());
                attachedDetallesProfesornivelFormativoCollection.add(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativoToAttach);
            }
            profesor.setDetallesProfesornivelFormativoCollection(attachedDetallesProfesornivelFormativoCollection);
            Collection<DetalleProfesorAsignatura> attachedDetalleProfesorAsignaturaCollection = new ArrayList<DetalleProfesorAsignatura>();
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach : profesor.getDetalleProfesorAsignaturaCollection()) {
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach = em.getReference(detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach.getClass(), detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach.getIdDetalle());
                attachedDetalleProfesorAsignaturaCollection.add(detalleProfesorAsignaturaCollectionDetalleProfesorAsignaturaToAttach);
            }
            profesor.setDetalleProfesorAsignaturaCollection(attachedDetalleProfesorAsignaturaCollection);
            em.persist(profesor);
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo : profesor.getDetallesProfesornivelFormativoCollection()) {
                Profesor oldIdProfesorOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.getIdProfesor();
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.setIdProfesor(profesor);
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
                if (oldIdProfesorOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo != null) {
                    oldIdProfesorOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
                    oldIdProfesorOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = em.merge(oldIdProfesorOfDetallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
                }
            }
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura : profesor.getDetalleProfesorAsignaturaCollection()) {
                Profesor oldIdProfesorOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.getIdProfesor();
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.setIdProfesor(profesor);
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
                if (oldIdProfesorOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura != null) {
                    oldIdProfesorOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
                    oldIdProfesorOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = em.merge(oldIdProfesorOfDetalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProfesor(profesor.getIdProfesor()) != null) {
                throw new PreexistingEntityException("Profesor " + profesor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesor profesor) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Profesor persistentProfesor = em.find(Profesor.class, profesor.getIdProfesor());
            Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollectionOld = persistentProfesor.getDetallesProfesornivelFormativoCollection();
            Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollectionNew = profesor.getDetallesProfesornivelFormativoCollection();
            Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollectionOld = persistentProfesor.getDetalleProfesorAsignaturaCollection();
            Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollectionNew = profesor.getDetalleProfesorAsignaturaCollection();
            Collection<DetallesProfesornivelFormativo> attachedDetallesProfesornivelFormativoCollectionNew = new ArrayList<DetallesProfesornivelFormativo>();
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach : detallesProfesornivelFormativoCollectionNew) {
                detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach = em.getReference(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach.getClass(), detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach.getIdDetalle());
                attachedDetallesProfesornivelFormativoCollectionNew.add(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativoToAttach);
            }
            detallesProfesornivelFormativoCollectionNew = attachedDetallesProfesornivelFormativoCollectionNew;
            profesor.setDetallesProfesornivelFormativoCollection(detallesProfesornivelFormativoCollectionNew);
            Collection<DetalleProfesorAsignatura> attachedDetalleProfesorAsignaturaCollectionNew = new ArrayList<DetalleProfesorAsignatura>();
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach : detalleProfesorAsignaturaCollectionNew) {
                detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach = em.getReference(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach.getClass(), detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach.getIdDetalle());
                attachedDetalleProfesorAsignaturaCollectionNew.add(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignaturaToAttach);
            }
            detalleProfesorAsignaturaCollectionNew = attachedDetalleProfesorAsignaturaCollectionNew;
            profesor.setDetalleProfesorAsignaturaCollection(detalleProfesorAsignaturaCollectionNew);
            profesor = em.merge(profesor);
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo : detallesProfesornivelFormativoCollectionOld) {
                if (!detallesProfesornivelFormativoCollectionNew.contains(detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo)) {
                    detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo.setIdProfesor(null);
                    detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionOldDetallesProfesornivelFormativo);
                }
            }
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo : detallesProfesornivelFormativoCollectionNew) {
                if (!detallesProfesornivelFormativoCollectionOld.contains(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo)) {
                    Profesor oldIdProfesorOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo = detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.getIdProfesor();
                    detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.setIdProfesor(profesor);
                    detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo);
                    if (oldIdProfesorOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo != null && !oldIdProfesorOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.equals(profesor)) {
                        oldIdProfesorOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo.getDetallesProfesornivelFormativoCollection().remove(detallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo);
                        oldIdProfesorOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo = em.merge(oldIdProfesorOfDetallesProfesornivelFormativoCollectionNewDetallesProfesornivelFormativo);
                    }
                }
            }
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura : detalleProfesorAsignaturaCollectionOld) {
                if (!detalleProfesorAsignaturaCollectionNew.contains(detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura)) {
                    detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura.setIdProfesor(null);
                    detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionOldDetalleProfesorAsignatura);
                }
            }
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura : detalleProfesorAsignaturaCollectionNew) {
                if (!detalleProfesorAsignaturaCollectionOld.contains(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura)) {
                    Profesor oldIdProfesorOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura = detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.getIdProfesor();
                    detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.setIdProfesor(profesor);
                    detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura);
                    if (oldIdProfesorOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura != null && !oldIdProfesorOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.equals(profesor)) {
                        oldIdProfesorOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura.getDetalleProfesorAsignaturaCollection().remove(detalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura);
                        oldIdProfesorOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura = em.merge(oldIdProfesorOfDetalleProfesorAsignaturaCollectionNewDetalleProfesorAsignatura);
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
                Integer id = profesor.getIdProfesor();
                if (findProfesor(id) == null) {
                    throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.");
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
            Profesor profesor;
            try {
                profesor = em.getReference(Profesor.class, id);
                profesor.getIdProfesor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.", enfe);
            }
            Collection<DetallesProfesornivelFormativo> detallesProfesornivelFormativoCollection = profesor.getDetallesProfesornivelFormativoCollection();
            for (DetallesProfesornivelFormativo detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo : detallesProfesornivelFormativoCollection) {
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo.setIdProfesor(null);
                detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo = em.merge(detallesProfesornivelFormativoCollectionDetallesProfesornivelFormativo);
            }
            Collection<DetalleProfesorAsignatura> detalleProfesorAsignaturaCollection = profesor.getDetalleProfesorAsignaturaCollection();
            for (DetalleProfesorAsignatura detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura : detalleProfesorAsignaturaCollection) {
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura.setIdProfesor(null);
                detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura = em.merge(detalleProfesorAsignaturaCollectionDetalleProfesorAsignatura);
            }
            em.remove(profesor);
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

    public List<Profesor> findProfesorEntities() {
        return findProfesorEntities(true, -1, -1);
    }

    public List<Profesor> findProfesorEntities(int maxResults, int firstResult) {
        return findProfesorEntities(false, maxResults, firstResult);
    }

    private List<Profesor> findProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesor.class));
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

    public Profesor findProfesor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesor> rt = cq.from(Profesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
