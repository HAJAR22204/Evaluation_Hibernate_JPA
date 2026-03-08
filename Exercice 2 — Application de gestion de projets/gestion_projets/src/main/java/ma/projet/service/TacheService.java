package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class TacheService implements IDao<Tache> {

    @Override
    public void create(Tache t) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Tache t) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Tache t) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Tache tache = em.find(Tache.class, t.getId());

            if (tache != null)
                em.remove(tache);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Tache findById(int id) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        Tache t = em.find(Tache.class, id);
        em.close();

        return t;
    }

    @Override
    public List<Tache> findAll() {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        List<Tache> list = em.createQuery(
                "SELECT t FROM Tache t", Tache.class).getResultList();

        em.close();

        return list;
    }

    public List<Tache> getTachesParPrixSuperieur(double prix) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        TypedQuery<Tache> query =
                em.createNamedQuery("Tache.findByPrixSuperieur", Tache.class);

        query.setParameter("prix", prix);

        List<Tache> list = query.getResultList();

        em.close();

        return list;
    }

    public List<EmployeTache> getTachesRealiseeEntreDates(Date d1, Date d2) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        TypedQuery<EmployeTache> query =
                em.createNamedQuery("EmployeTache.findRealiseeEntreDates",
                        EmployeTache.class);

        query.setParameter("dateDebut", d1);
        query.setParameter("dateFin", d2);

        List<EmployeTache> list = query.getResultList();

        em.close();

        return list;
    }
}