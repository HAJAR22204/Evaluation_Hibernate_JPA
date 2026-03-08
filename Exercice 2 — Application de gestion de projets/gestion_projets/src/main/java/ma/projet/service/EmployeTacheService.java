package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class EmployeTacheService implements IDao<EmployeTache> {

    @Override
    public void create(EmployeTache employeTache) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(employeTache);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    @Override
    public void update(EmployeTache employeTache) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(employeTache);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    @Override
    public void delete(EmployeTache employeTache) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            EmployeTache managed = em.find(EmployeTache.class, employeTache.getId());
            if (managed != null) em.remove(managed);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally { em.close(); }
    }

    @Override
    public EmployeTache findById(int id) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try { return em.find(EmployeTache.class, id); } finally { em.close(); }
    }

    @Override
    public List<EmployeTache> findAll() {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT et FROM EmployeTache et", EmployeTache.class).getResultList();
        } finally { em.close(); }
    }
}
