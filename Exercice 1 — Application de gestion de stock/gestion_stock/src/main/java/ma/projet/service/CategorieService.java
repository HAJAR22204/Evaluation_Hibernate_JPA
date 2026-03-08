package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements IDao<Categorie> {

    @Override
    public boolean create(Categorie categorie) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(categorie);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur create Categorie : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Categorie categorie) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Categorie managed = em.contains(categorie) ? categorie : em.merge(categorie);
            em.remove(managed);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur delete Categorie : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Categorie categorie) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(categorie);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur update Categorie : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Categorie findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Categorie.class, id);
        } catch (Exception e) {
            System.err.println("Erreur findById Categorie : " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categorie> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
        } catch (Exception e) {
            System.err.println("Erreur findAll Categorie : " + e.getMessage());
            return new ArrayList<Categorie>();
        } finally {
            em.close();
        }
    }
}
