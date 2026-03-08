package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProjetService implements IDao<Projet> {

    @Override
    public void create(Projet projet) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(projet);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Projet projet) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(projet);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Projet projet) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Projet p = em.find(Projet.class, projet.getId());
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public Projet findById(int id) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        Projet p = em.find(Projet.class, id);
        em.close();
        return p;
    }

    @Override
    public List<Projet> findAll() {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        List<Projet> projets = em.createQuery("SELECT p FROM Projet p", Projet.class).getResultList();
        em.close();
        return projets;
    }

    public List<Tache> getTachesPlanifiees(int projetId) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        TypedQuery<Tache> query = em.createQuery(
                "SELECT t FROM Tache t WHERE t.projet.id = :id", Tache.class);
        query.setParameter("id", projetId);

        List<Tache> list = query.getResultList();
        em.close();

        return list;
    }

    public void afficherTachesRealisees(Projet projet) {

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");



        List<EmployeTache> list = em.createQuery(
                        "SELECT et FROM EmployeTache et WHERE et.tache.projet.id = :id",
                        EmployeTache.class)
                .setParameter("id", projet.getId())
                .getResultList();

        System.out.println("Projet : " + projet.getId() +
                " Nom : " + projet.getNom() +
                " Date début : " + sdf.format(projet.getDateDebut()));

        System.out.println("Liste des tâches:");
        System.out.printf("%-5s %-20s %-20s %-20s%n",
                "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");

        for (EmployeTache et : list) {

            String d1 = et.getDateDebutReelle() != null
                    ? sdf.format(et.getDateDebutReelle())
                    : "-";

            String d2 = et.getDateFinReelle() != null
                    ? sdf.format(et.getDateFinReelle())
                    : "-";

            System.out.printf("%-5d %-20s %-20s %-20s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    d1,
                    d2);
        }

        em.close();
    }
}