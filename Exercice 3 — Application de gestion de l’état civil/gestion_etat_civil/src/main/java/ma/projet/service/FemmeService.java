package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;


public class FemmeService implements IDao<Femme> {


    @Override
    public boolean create(Femme f) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(f);
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
    public boolean delete(Femme f) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(session.contains(f) ? f : session.merge(f));
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
    public boolean update(Femme f) {
        Session session = HibernateUtil.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(f);
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
    public Femme findById(int id) {
        Session session = HibernateUtil.getSession();
        try {
            return session.get(Femme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Femme> findAll() {
        Session session = HibernateUtil.getSession();
        try {
            return session.createQuery("FROM Femme f ORDER BY f.nom", Femme.class).list();
        } finally {
            session.close();
        }
    }


    public Femme getFemmeLaPlusAgee() {
        Session session = HibernateUtil.getSession();
        try {
            List<Femme> result = session.createQuery(
                "FROM Femme f ORDER BY f.dateNaissance ASC", Femme.class)
                .setMaxResults(1)
                .list();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            session.close();
        }
    }


    public int getNbrEnfantsEntreDates(Femme femme, Date dateDebut, Date dateFin) {
        Session session = HibernateUtil.getSession();
        try {
            NativeQuery<Object> q = session.getNamedNativeQuery("Femme.nbrEnfantsEntreDates");
            q.setParameter("femmeId",   femme.getId());
            q.setParameter("dateDebut", dateDebut);
            q.setParameter("dateFin",   dateFin);
            Object result = q.uniqueResult();
            return result == null ? 0 : ((Number) result).intValue();
        } finally {
            session.close();
        }
    }


    public List<Femme> getFemmesMarieesAuMoinsDeux() {
        Session session = HibernateUtil.getSession();
        try {
            return session.createNamedQuery("Femme.femmesMarieesPlusDeDeux", Femme.class).list();
        } finally {
            session.close();
        }
    }


    public long getNbrHommesMariesAQuatreFemmes(Date dateDebut, Date dateFin) {
        Session session = HibernateUtil.getSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Mariage> root = cq.from(Mariage.class);

            cq.select(cb.countDistinct(root.get("mari")));

            Predicate periodePred = cb.and(
                cb.greaterThanOrEqualTo(root.<Date>get("dateDebut"), dateDebut),
                cb.lessThanOrEqualTo(root.<Date>get("dateDebut"), dateFin)
            );

            // Sous-requête : ne garder que les maris ayant exactement 4 épouses
            Subquery<Long> sub = cq.subquery(Long.class);
            Root<Mariage> subRoot = sub.from(Mariage.class);
            sub.select(cb.count(subRoot));
            sub.where(
                cb.equal(subRoot.get("mari"), root.get("mari")),
                cb.greaterThanOrEqualTo(subRoot.<Date>get("dateDebut"), dateDebut),
                cb.lessThanOrEqualTo(subRoot.<Date>get("dateDebut"), dateFin)
            );

            cq.where(periodePred, cb.equal(sub, 4L));

            Long count = session.createQuery(cq).uniqueResult();
            return count == null ? 0 : count;
        } finally {
            session.close();
        }
    }
}
