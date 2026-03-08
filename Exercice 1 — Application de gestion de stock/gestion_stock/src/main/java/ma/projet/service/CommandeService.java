package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements IDao<Commande> {

    @Override
    public boolean create(Commande commande) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(commande);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur create Commande : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Commande commande) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Commande managed = em.contains(commande) ? commande : em.merge(commande);
            em.remove(managed);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur delete Commande : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Commande commande) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(commande);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur update Commande : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Commande findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Commande.class, id);
        } catch (Exception e) {
            System.err.println("Erreur findById Commande : " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Commande> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Commande c", Commande.class).getResultList();
        } catch (Exception e) {
            System.err.println("Erreur findAll Commande : " + e.getMessage());
            return new ArrayList<Commande>();
        } finally {
            em.close();
        }
    }
}
