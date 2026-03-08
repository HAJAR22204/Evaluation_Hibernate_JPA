package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeService implements IDao<Employe> {

    @Override
    public void create(Employe e) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(e);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Employe e) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(e);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Employe e) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        try {
            em.getTransaction().begin();

            Employe emp = em.find(Employe.class, e.getId());

            if (emp != null)
                em.remove(emp);

            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Employe findById(int id) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        Employe e = em.find(Employe.class, id);
        em.close();

        return e;
    }

    @Override
    public List<Employe> findAll() {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        List<Employe> list =
                em.createQuery("SELECT e FROM Employe e", Employe.class).getResultList();

        em.close();

        return list;
    }

    public List<Tache> getTachesRealisees(int employeId) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        TypedQuery<Tache> query = em.createQuery(
                "SELECT et.tache FROM EmployeTache et WHERE et.employe.id=:id",
                Tache.class);

        query.setParameter("id", employeId);

        List<Tache> list = query.getResultList();

        em.close();

        return list;
    }

    public List<Projet> getProjetsGeres(int employeId) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        TypedQuery<Projet> query = em.createQuery(
                "SELECT p FROM Projet p WHERE p.chefDeProjet.id=:id",
                Projet.class);

        query.setParameter("id", employeId);

        List<Projet> list = query.getResultList();

        em.close();

        return list;
    }
}