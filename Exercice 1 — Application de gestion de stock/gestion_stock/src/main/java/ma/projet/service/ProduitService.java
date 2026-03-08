package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit produit) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(produit);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur create Produit : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Produit produit) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produit managed = em.contains(produit) ? produit : em.merge(produit);
            em.remove(managed);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur delete Produit : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Produit produit) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(produit);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("Erreur update Produit : " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Produit findById(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Produit.class, id);
        } catch (Exception e) {
            System.err.println("Erreur findById Produit : " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Produit> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Produit p JOIN FETCH p.categorie",
                    Produit.class
            ).getResultList();        } catch (Exception e) {
            System.err.println("Erreur findAll Produit : " + e.getMessage());
            return new ArrayList<Produit>();
        } finally {
            em.close();
        }
    }

    //liste des produits par categorie

    public List<Produit> findByCategorie(Categorie categorie) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Produit> q = em.createQuery(
                "SELECT p FROM Produit p WHERE p.categorie = :cat", Produit.class);
            q.setParameter("cat", categorie);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur findByCategorie : " + e.getMessage());
            return new ArrayList<Produit>();
        } finally {
            em.close();
        }
    }

    //liste des produits commandes entre deux dates

    public List<Produit> findProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Produit> q = em.createQuery(
                "SELECT DISTINCT lcp.produit FROM LigneCommandeProduit lcp " +
                "WHERE lcp.commande.date BETWEEN :d1 AND :d2", Produit.class);
            q.setParameter("d1", dateDebut);
            q.setParameter("d2", dateFin);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur findProduitsCommandesEntreDates : " + e.getMessage());
            return new ArrayList<Produit>();
        } finally {
            em.close();
        }
    }

    //liste des produits commandes dans une commande

    public void afficherProduitsParCommande(int commandeId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                "SELECT lcp.commande.id, lcp.commande.date, " +
                "       lcp.produit.reference, lcp.produit.prix, lcp.quantite " +
                "FROM LigneCommandeProduit lcp " +
                "WHERE lcp.commande.id = :cid", Object[].class)
                .setParameter("cid", commandeId)
                .getResultList();

            if (rows.isEmpty()) {
                System.out.println("  Aucun produit pour la commande " + commandeId);
                return;
            }

            Object[] first = rows.get(0);
            System.out.println("Commande : " + first[0] + "     Date : " + first[1]);
            System.out.println("Liste des produits :");
            System.out.printf("%-12s %-10s %-10s%n", "Reference", "Prix", "Quantite");
            for (Object[] row : rows) {
                System.out.printf("%-12s %-10s %-10s%n",
                    row[2],
                    (int)(float)(Float) row[3] + " DH",
                    row[4]);
            }
        } catch (Exception e) {
            System.err.println("Erreur afficherProduitsParCommande : " + e.getMessage());
        } finally {
            em.close();
        }
    }

    //liste des produits dont le prix est supérieur à 100 DH

    public List<Produit> findProduitsPrixSuperieur100() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createNamedQuery("Produit.prixSuperieur100", Produit.class).getResultList();
        } catch (Exception e) {
            System.err.println("Erreur findProduitsPrixSuperieur100 : " + e.getMessage());
            return new ArrayList<Produit>();
        } finally {
            em.close();
        }
    }
}
