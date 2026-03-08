package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeService implements IDao<LigneCommandeProduit> {

    @Override
    public boolean create(LigneCommandeProduit ligne) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ligne);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur create LigneCommandeProduit : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(LigneCommandeProduit ligne) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LigneCommandeProduit managed = em.contains(ligne) ? ligne : em.merge(ligne);
            em.remove(managed);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur delete LigneCommandeProduit : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(LigneCommandeProduit ligne) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ligne);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur update LigneCommandeProduit : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public LigneCommandeProduit findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(LigneCommandeProduit.class, id);
        } catch (Exception e) {
            System.err.println("Erreur findById LigneCommandeProduit : " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<LigneCommandeProduit> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM LigneCommandeProduit l", LigneCommandeProduit.class).getResultList();
        } catch (Exception e) {
            System.err.println("Erreur findAll LigneCommandeProduit : " + e.getMessage());
            return new ArrayList<LigneCommandeProduit>();
        } finally {
            em.close();
        }
    }
}
