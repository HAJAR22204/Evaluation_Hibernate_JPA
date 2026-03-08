package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class App {

    public static void main(String[] args) {

        ProjetService projetService = new ProjetService();
        EmployeService employeService = new EmployeService();
        TacheService tacheService = new TacheService();
        EmployeTacheService employeTacheService = new EmployeTacheService();

        System.out.println("Application de Gestion de Projets");

        // création des employés
        System.out.println("Création des employés");

        Employe emp1 = new Employe("Alami", "Mohamed", "0661-000001");
        Employe emp2 = new Employe("Benali", "Fatima", "0661-000002");
        Employe emp3 = new Employe("Chraibi", "Ahmed", "0661-000003");

        employeService.create(emp1);
        employeService.create(emp2);
        employeService.create(emp3);

        // création des projets
        System.out.println("Création des projets");

        Projet projet1 = new Projet(
                "Gestion de stock",
                new Date(113,0,14),
                new Date(113,5,30)
        );
        projet1.setChefDeProjet(emp1);

        Projet projet2 = new Projet(
                "Gestion RH",
                new Date(114,2,1),
                new Date(114,11,31)
        );
        projet2.setChefDeProjet(emp1);

        projetService.create(projet1);
        projetService.create(projet2);

        // création des tâches
        System.out.println("Création des tâches");

        Tache tache1 = new Tache("Analyse",
                new Date(113,1,1),
                new Date(113,1,28),
                1200.0,
                projet1);

        Tache tache2 = new Tache("Conception",
                new Date(113,2,1),
                new Date(113,2,31),
                800.0,
                projet1);

        Tache tache3 = new Tache("Développement",
                new Date(113,3,1),
                new Date(113,3,30),
                2500.0,
                projet1);

        Tache tache4 = new Tache("Tests",
                new Date(113,4,1),
                new Date(113,4,31),
                500.0,
                projet1);

        Tache tache5 = new Tache("Cahier des charges",
                new Date(114,2,1),
                new Date(114,2,31),
                1500.0,
                projet2);

        tacheService.create(tache1);
        tacheService.create(tache2);
        tacheService.create(tache3);
        tacheService.create(tache4);
        tacheService.create(tache5);

        // affectation des tâches
        System.out.println("Affectation des tâches");

        EmployeTache et1 = new EmployeTache(
                emp2, tache1,
                new Date(113,1,10),
                new Date(113,1,20)
        );

        EmployeTache et2 = new EmployeTache(
                emp2, tache2,
                new Date(113,2,10),
                new Date(113,2,15)
        );

        EmployeTache et3 = new EmployeTache(
                emp3, tache3,
                new Date(113,3,10),
                new Date(113,3,25)
        );

        EmployeTache et4 = new EmployeTache(
                emp2, tache4,
                null, null
        );

        EmployeTache et5 = new EmployeTache(
                emp1, tache5,
                new Date(114,2,5),
                new Date(114,2,25)
        );

        employeTacheService.create(et1);
        employeTacheService.create(et2);
        employeTacheService.create(et3);
        employeTacheService.create(et4);
        employeTacheService.create(et5);


        System.out.println("\nAffichage des tâches réalisées :");
        projetService.afficherTachesRealisees(projet1);

        // tâches planifiées
        System.out.println("\nTâches planifiées pour : " + projet1.getNom());

        List<Tache> planifiees = projetService.getTachesPlanifiees(projet1.getId());

        for (Tache t : planifiees)
            System.out.println(t.getNom() + " | Prix : " + t.getPrix());

        // tâches réalisées par un employé
        System.out.println("\nTâches réalisées par : " + emp2.getNom());

        List<Tache> tachesEmp = employeService.getTachesRealisees(emp2.getId());

        for (Tache t : tachesEmp)
            System.out.println(t.getNom());

        // projets gérés
        System.out.println("\nProjets gérés par : " + emp1.getNom());

        List<Projet> projets = employeService.getProjetsGeres(emp1.getId());

        for (Projet p : projets)
            System.out.println(p.getNom());

        // tâches prix > 1000
        System.out.println("\nTâches avec prix > 1000");

        List<Tache> cheres = tacheService.getTachesParPrixSuperieur(1000);

        for (Tache t : cheres)
            System.out.println(t.getNom() + " | " + t.getPrix());

        // tâches réalisées entre dates
        System.out.println("\nTâches réalisées entre deux dates");

        Date d1 = new Date(113,1,1);
        Date d2 = new Date(113,3,30);

        List<EmployeTache> ets = tacheService.getTachesRealiseeEntreDates(d1, d2);

        for (EmployeTache et : ets)
            System.out.println(et.getTache().getNom() + " | " + et.getEmploye().getNom());

        // liste des employés
        System.out.println("\nListe des employés");

        for (Employe e : employeService.findAll())
            System.out.println(e.getNom() + " " + e.getPrenom() + " | Tel : " + e.getTelephone());

        System.out.println("\nFIN DES TESTS");

        HibernateUtil.close();
    }
}