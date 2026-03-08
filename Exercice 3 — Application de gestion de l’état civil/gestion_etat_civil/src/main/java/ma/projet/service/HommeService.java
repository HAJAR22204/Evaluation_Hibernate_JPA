package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme h) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(h);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Homme h) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(session.contains(h) ? h : session.merge(h));
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Homme h) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(h);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Homme findById(int id) {
        Session session = HibernateUtil.getSession();
        try {
            return session.get(Homme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Homme> findAll() {
        Session session = HibernateUtil.getSession();
        try {
            return session.createQuery("FROM Homme", Homme.class).list();
        } finally {
            session.close();
        }
    }


    public List<Femme> getEpousesEntreDates(Homme homme, Date dateDebut, Date dateFin) {
        Session session = HibernateUtil.getSession();
        try {
            Query<Femme> q = session.createQuery(
                "SELECT m.femme FROM Mariage m " +
                "WHERE m.mari = :homme " +
                "  AND m.dateDebut >= :debut " +
                "  AND m.dateDebut <= :fin",
                Femme.class);
            q.setParameter("homme", homme);
            q.setParameter("debut", dateDebut);
            q.setParameter("fin",   dateFin);
            return q.list();
        } finally {
            session.close();
        }
    }


    public void afficherMariagesDetailles(Homme homme) {
        Session session = HibernateUtil.getSession();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            List<Mariage> mariages = session.createQuery(
                "FROM Mariage m WHERE m.mari = :homme ORDER BY m.dateDebut",
                Mariage.class)
                .setParameter("homme", homme)
                .list();

            System.out.println("Nom : " + homme.getPrenom().toUpperCase()
                             + " " + homme.getNom().toUpperCase());

            System.out.println("Mariages En Cours :");
            int idx = 1;
            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    System.out.printf("%d. Femme : %-15s  Date Début : %s    Nbr Enfants : %d%n",
                        idx++,
                        (m.getFemme().getPrenom() + " " + m.getFemme().getNom()).toUpperCase(),
                        sdf.format(m.getDateDebut()),
                        m.getNbrEnfant());
                }
            }

            System.out.println("\nMariages échoués :");
            idx = 1;
            for (Mariage m : mariages) {
                if (m.getDateFin() != null) {
                    System.out.printf("%d. Femme : %-15s  Date Début : %s    Date Fin : %s    Nbr Enfants : %d%n",
                        idx++,
                        (m.getFemme().getPrenom() + " " + m.getFemme().getNom()).toUpperCase(),
                        sdf.format(m.getDateDebut()),
                        sdf.format(m.getDateFin()),
                        m.getNbrEnfant());
                }
            }
        } finally {
            session.close();
        }
    }
}
