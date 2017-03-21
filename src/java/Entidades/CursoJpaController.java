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
public class CursoJpaController implements Serializable {

    public CursoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Curso curso) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (curso.getDetallesCursoProfesorCollection() == null) {
            curso.setDetallesCursoProfesorCollection(new ArrayList<DetallesCursoProfesor>());
        }
        if (curso.getDetalleCursoAsignaturaCollection() == null) {
            curso.setDetalleCursoAsignaturaCollection(new ArrayList<DetalleCursoAsignatura>());
        }
        if (curso.getDetallesCursoAlumnoCollection() == null) {
            curso.setDetallesCursoAlumnoCollection(new ArrayList<DetallesCursoAlumno>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DetallesCursoProfesor> attachedDetallesCursoProfesorCollection = new ArrayList<DetallesCursoProfesor>();
            for (DetallesCursoProfesor detallesCursoProfesorCollectionDetallesCursoProfesorToAttach : curso.getDetallesCursoProfesorCollection()) {
                detallesCursoProfesorCollectionDetallesCursoProfesorToAttach = em.getReference(detallesCursoProfesorCollectionDetallesCursoProfesorToAttach.getClass(), detallesCursoProfesorCollectionDetallesCursoProfesorToAttach.getIdDetalleCurso2());
                attachedDetallesCursoProfesorCollection.add(detallesCursoProfesorCollectionDetallesCursoProfesorToAttach);
            }
            curso.setDetallesCursoProfesorCollection(attachedDetallesCursoProfesorCollection);
            Collection<DetalleCursoAsignatura> attachedDetalleCursoAsignaturaCollection = new ArrayList<DetalleCursoAsignatura>();
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach : curso.getDetalleCursoAsignaturaCollection()) {
                detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach = em.getReference(detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach.getClass(), detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach.getIdDetalle());
                attachedDetalleCursoAsignaturaCollection.add(detalleCursoAsignaturaCollectionDetalleCursoAsignaturaToAttach);
            }
            curso.setDetalleCursoAsignaturaCollection(attachedDetalleCursoAsignaturaCollection);
            Collection<DetallesCursoAlumno> attachedDetallesCursoAlumnoCollection = new ArrayList<DetallesCursoAlumno>();
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach : curso.getDetallesCursoAlumnoCollection()) {
                detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach = em.getReference(detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach.getClass(), detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach.getIdDetalleCurso());
                attachedDetallesCursoAlumnoCollection.add(detallesCursoAlumnoCollectionDetallesCursoAlumnoToAttach);
            }
            curso.setDetallesCursoAlumnoCollection(attachedDetallesCursoAlumnoCollection);
            em.persist(curso);
            for (DetallesCursoProfesor detallesCursoProfesorCollectionDetallesCursoProfesor : curso.getDetallesCursoProfesorCollection()) {
                Curso oldIdCursoOfDetallesCursoProfesorCollectionDetallesCursoProfesor = detallesCursoProfesorCollectionDetallesCursoProfesor.getIdCurso();
                detallesCursoProfesorCollectionDetallesCursoProfesor.setIdCurso(curso);
                detallesCursoProfesorCollectionDetallesCursoProfesor = em.merge(detallesCursoProfesorCollectionDetallesCursoProfesor);
                if (oldIdCursoOfDetallesCursoProfesorCollectionDetallesCursoProfesor != null) {
                    oldIdCursoOfDetallesCursoProfesorCollectionDetallesCursoProfesor.getDetallesCursoProfesorCollection().remove(detallesCursoProfesorCollectionDetallesCursoProfesor);
                    oldIdCursoOfDetallesCursoProfesorCollectionDetallesCursoProfesor = em.merge(oldIdCursoOfDetallesCursoProfesorCollectionDetallesCursoProfesor);
                }
            }
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionDetalleCursoAsignatura : curso.getDetalleCursoAsignaturaCollection()) {
                Curso oldIdCursoOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura = detalleCursoAsignaturaCollectionDetalleCursoAsignatura.getIdCurso();
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura.setIdCurso(curso);
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionDetalleCursoAsignatura);
                if (oldIdCursoOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura != null) {
                    oldIdCursoOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignaturaCollectionDetalleCursoAsignatura);
                    oldIdCursoOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura = em.merge(oldIdCursoOfDetalleCursoAsignaturaCollectionDetalleCursoAsignatura);
                }
            }
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionDetallesCursoAlumno : curso.getDetallesCursoAlumnoCollection()) {
                Curso oldIdCursoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno = detallesCursoAlumnoCollectionDetallesCursoAlumno.getIdCurso();
                detallesCursoAlumnoCollectionDetallesCursoAlumno.setIdCurso(curso);
                detallesCursoAlumnoCollectionDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionDetallesCursoAlumno);
                if (oldIdCursoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno != null) {
                    oldIdCursoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumnoCollectionDetallesCursoAlumno);
                    oldIdCursoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno = em.merge(oldIdCursoOfDetallesCursoAlumnoCollectionDetallesCursoAlumno);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCurso(curso.getIdCurso()) != null) {
                throw new PreexistingEntityException("Curso " + curso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Curso persistentCurso = em.find(Curso.class, curso.getIdCurso());
            Collection<DetallesCursoProfesor> detallesCursoProfesorCollectionOld = persistentCurso.getDetallesCursoProfesorCollection();
            Collection<DetallesCursoProfesor> detallesCursoProfesorCollectionNew = curso.getDetallesCursoProfesorCollection();
            Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollectionOld = persistentCurso.getDetalleCursoAsignaturaCollection();
            Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollectionNew = curso.getDetalleCursoAsignaturaCollection();
            Collection<DetallesCursoAlumno> detallesCursoAlumnoCollectionOld = persistentCurso.getDetallesCursoAlumnoCollection();
            Collection<DetallesCursoAlumno> detallesCursoAlumnoCollectionNew = curso.getDetallesCursoAlumnoCollection();
            Collection<DetallesCursoProfesor> attachedDetallesCursoProfesorCollectionNew = new ArrayList<DetallesCursoProfesor>();
            for (DetallesCursoProfesor detallesCursoProfesorCollectionNewDetallesCursoProfesorToAttach : detallesCursoProfesorCollectionNew) {
                detallesCursoProfesorCollectionNewDetallesCursoProfesorToAttach = em.getReference(detallesCursoProfesorCollectionNewDetallesCursoProfesorToAttach.getClass(), detallesCursoProfesorCollectionNewDetallesCursoProfesorToAttach.getIdDetalleCurso2());
                attachedDetallesCursoProfesorCollectionNew.add(detallesCursoProfesorCollectionNewDetallesCursoProfesorToAttach);
            }
            detallesCursoProfesorCollectionNew = attachedDetallesCursoProfesorCollectionNew;
            curso.setDetallesCursoProfesorCollection(detallesCursoProfesorCollectionNew);
            Collection<DetalleCursoAsignatura> attachedDetalleCursoAsignaturaCollectionNew = new ArrayList<DetalleCursoAsignatura>();
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach : detalleCursoAsignaturaCollectionNew) {
                detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach = em.getReference(detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach.getClass(), detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach.getIdDetalle());
                attachedDetalleCursoAsignaturaCollectionNew.add(detalleCursoAsignaturaCollectionNewDetalleCursoAsignaturaToAttach);
            }
            detalleCursoAsignaturaCollectionNew = attachedDetalleCursoAsignaturaCollectionNew;
            curso.setDetalleCursoAsignaturaCollection(detalleCursoAsignaturaCollectionNew);
            Collection<DetallesCursoAlumno> attachedDetallesCursoAlumnoCollectionNew = new ArrayList<DetallesCursoAlumno>();
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach : detallesCursoAlumnoCollectionNew) {
                detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach = em.getReference(detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach.getClass(), detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach.getIdDetalleCurso());
                attachedDetallesCursoAlumnoCollectionNew.add(detallesCursoAlumnoCollectionNewDetallesCursoAlumnoToAttach);
            }
            detallesCursoAlumnoCollectionNew = attachedDetallesCursoAlumnoCollectionNew;
            curso.setDetallesCursoAlumnoCollection(detallesCursoAlumnoCollectionNew);
            curso = em.merge(curso);
            for (DetallesCursoProfesor detallesCursoProfesorCollectionOldDetallesCursoProfesor : detallesCursoProfesorCollectionOld) {
                if (!detallesCursoProfesorCollectionNew.contains(detallesCursoProfesorCollectionOldDetallesCursoProfesor)) {
                    detallesCursoProfesorCollectionOldDetallesCursoProfesor.setIdCurso(null);
                    detallesCursoProfesorCollectionOldDetallesCursoProfesor = em.merge(detallesCursoProfesorCollectionOldDetallesCursoProfesor);
                }
            }
            for (DetallesCursoProfesor detallesCursoProfesorCollectionNewDetallesCursoProfesor : detallesCursoProfesorCollectionNew) {
                if (!detallesCursoProfesorCollectionOld.contains(detallesCursoProfesorCollectionNewDetallesCursoProfesor)) {
                    Curso oldIdCursoOfDetallesCursoProfesorCollectionNewDetallesCursoProfesor = detallesCursoProfesorCollectionNewDetallesCursoProfesor.getIdCurso();
                    detallesCursoProfesorCollectionNewDetallesCursoProfesor.setIdCurso(curso);
                    detallesCursoProfesorCollectionNewDetallesCursoProfesor = em.merge(detallesCursoProfesorCollectionNewDetallesCursoProfesor);
                    if (oldIdCursoOfDetallesCursoProfesorCollectionNewDetallesCursoProfesor != null && !oldIdCursoOfDetallesCursoProfesorCollectionNewDetallesCursoProfesor.equals(curso)) {
                        oldIdCursoOfDetallesCursoProfesorCollectionNewDetallesCursoProfesor.getDetallesCursoProfesorCollection().remove(detallesCursoProfesorCollectionNewDetallesCursoProfesor);
                        oldIdCursoOfDetallesCursoProfesorCollectionNewDetallesCursoProfesor = em.merge(oldIdCursoOfDetallesCursoProfesorCollectionNewDetallesCursoProfesor);
                    }
                }
            }
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura : detalleCursoAsignaturaCollectionOld) {
                if (!detalleCursoAsignaturaCollectionNew.contains(detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura)) {
                    detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura.setIdCurso(null);
                    detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionOldDetalleCursoAsignatura);
                }
            }
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura : detalleCursoAsignaturaCollectionNew) {
                if (!detalleCursoAsignaturaCollectionOld.contains(detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura)) {
                    Curso oldIdCursoOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura = detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.getIdCurso();
                    detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.setIdCurso(curso);
                    detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura);
                    if (oldIdCursoOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura != null && !oldIdCursoOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.equals(curso)) {
                        oldIdCursoOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura.getDetalleCursoAsignaturaCollection().remove(detalleCursoAsignaturaCollectionNewDetalleCursoAsignatura);
                        oldIdCursoOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura = em.merge(oldIdCursoOfDetalleCursoAsignaturaCollectionNewDetalleCursoAsignatura);
                    }
                }
            }
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionOldDetallesCursoAlumno : detallesCursoAlumnoCollectionOld) {
                if (!detallesCursoAlumnoCollectionNew.contains(detallesCursoAlumnoCollectionOldDetallesCursoAlumno)) {
                    detallesCursoAlumnoCollectionOldDetallesCursoAlumno.setIdCurso(null);
                    detallesCursoAlumnoCollectionOldDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionOldDetallesCursoAlumno);
                }
            }
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionNewDetallesCursoAlumno : detallesCursoAlumnoCollectionNew) {
                if (!detallesCursoAlumnoCollectionOld.contains(detallesCursoAlumnoCollectionNewDetallesCursoAlumno)) {
                    Curso oldIdCursoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno = detallesCursoAlumnoCollectionNewDetallesCursoAlumno.getIdCurso();
                    detallesCursoAlumnoCollectionNewDetallesCursoAlumno.setIdCurso(curso);
                    detallesCursoAlumnoCollectionNewDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionNewDetallesCursoAlumno);
                    if (oldIdCursoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno != null && !oldIdCursoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno.equals(curso)) {
                        oldIdCursoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno.getDetallesCursoAlumnoCollection().remove(detallesCursoAlumnoCollectionNewDetallesCursoAlumno);
                        oldIdCursoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno = em.merge(oldIdCursoOfDetallesCursoAlumnoCollectionNewDetallesCursoAlumno);
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
                Integer id = curso.getIdCurso();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getIdCurso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            Collection<DetallesCursoProfesor> detallesCursoProfesorCollection = curso.getDetallesCursoProfesorCollection();
            for (DetallesCursoProfesor detallesCursoProfesorCollectionDetallesCursoProfesor : detallesCursoProfesorCollection) {
                detallesCursoProfesorCollectionDetallesCursoProfesor.setIdCurso(null);
                detallesCursoProfesorCollectionDetallesCursoProfesor = em.merge(detallesCursoProfesorCollectionDetallesCursoProfesor);
            }
            Collection<DetalleCursoAsignatura> detalleCursoAsignaturaCollection = curso.getDetalleCursoAsignaturaCollection();
            for (DetalleCursoAsignatura detalleCursoAsignaturaCollectionDetalleCursoAsignatura : detalleCursoAsignaturaCollection) {
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura.setIdCurso(null);
                detalleCursoAsignaturaCollectionDetalleCursoAsignatura = em.merge(detalleCursoAsignaturaCollectionDetalleCursoAsignatura);
            }
            Collection<DetallesCursoAlumno> detallesCursoAlumnoCollection = curso.getDetallesCursoAlumnoCollection();
            for (DetallesCursoAlumno detallesCursoAlumnoCollectionDetallesCursoAlumno : detallesCursoAlumnoCollection) {
                detallesCursoAlumnoCollectionDetallesCursoAlumno.setIdCurso(null);
                detallesCursoAlumnoCollectionDetallesCursoAlumno = em.merge(detallesCursoAlumnoCollectionDetallesCursoAlumno);
            }
            em.remove(curso);
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

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
