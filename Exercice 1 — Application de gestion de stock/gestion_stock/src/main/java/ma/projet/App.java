package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class App {

    private static final CategorieService categorieService = new CategorieService();
    private static final ProduitService produitService = new ProduitService();
    private static final CommandeService commandeService = new CommandeService();
    private static final LigneCommandeService ligneService = new LigneCommandeService();


    public static void main(String[] args) throws Exception {

        System.out.println("APPLICATION GESTION DE STOCK ");

        initialiserDonnees();

        ListeTousLesProduits();
        ProduitsByCategorie();
        ProduitsEntreDates();
        ProduitsParCommande(1);
        PrixSuperieur100();

        HibernateUtil.shutdown();
        System.out.println("FIN DES TESTS");
    }

    //initialisation des donnees

    private static void initialiserDonnees() throws Exception {
        System.out.println("Initialisation des donnees");

        // Categories
        Categorie catInfo = new Categorie("INFO", "Informatique");
        Categorie catPeri = new Categorie("PERI", "Peripheriques");
        categorieService.create(catInfo);
        categorieService.create(catPeri);
        System.out.println("  Categories : " + catInfo.getLibelle() + ", " + catPeri.getLibelle());

        // Produits
        Produit p1 = new Produit("ES12", 120, catInfo);
        Produit p2 = new Produit("ZR85", 100, catInfo);
        Produit p3 = new Produit("EE85", 200, catPeri);
        Produit p4 = new Produit("AB50",  50, catPeri);
        Produit p5 = new Produit("XY99", 350, catInfo);
        produitService.create(p1);
        produitService.create(p2);
        produitService.create(p3);
        produitService.create(p4);
        produitService.create(p5);
        System.out.println("  5 produits crees");

        // Commandes
        Commande cmd1 = new Commande(new Date(113, 2, 14));
        Commande cmd2 = new Commande(new Date(123, 5, 20));
        commandeService.create(cmd1);
        commandeService.create(cmd2);
        System.out.println("  Commandes : cmd1 (14/03/2013), cmd2 (20/06/2023)");

        // Lignes de commande
        ligneService.create(new LigneCommandeProduit(7,  cmd1, p1));
        ligneService.create(new LigneCommandeProduit(14, cmd1, p2));
        ligneService.create(new LigneCommandeProduit(5,  cmd1, p3));
        ligneService.create(new LigneCommandeProduit(3,  cmd2, p4));
        ligneService.create(new LigneCommandeProduit(2,  cmd2, p5));
        System.out.println("  Lignes de commande creees");
    }


    //liste de tous les produits

    private static void ListeTousLesProduits() {
        System.out.println("Test 1 — Liste de tous les produits");

        List<Produit> produits = produitService.findAll();
        System.out.printf("  %-5s %-12s %8s   %-15s%n", "ID", "Reference", "Prix", "Categorie");
        for (Produit p : produits) {
            System.out.printf("  %-5d %-12s %5.0f DH   %-15s%n",
                    p.getId(),
                    p.getReference(),
                    p.getPrix(),
                    p.getCategorie().getLibelle());
        }
        System.out.println();
    }


    //produits par categorie

    private static void ProduitsByCategorie() {
        System.out.println("Test 2 — Produits par categorie");

        for (Categorie cat : categorieService.findAll()) {
            System.out.println("  Categorie : " + cat.getLibelle() + " [" + cat.getCode() + "]");
            List<Produit> produits = produitService.findByCategorie(cat);
            for (Produit p : produits) {
                System.out.printf("    -> %-8s  %6.0f DH%n", p.getReference(), p.getPrix());
            }
        }
        System.out.println();
    }


    //  produits commandes entre deux dates

    private static void ProduitsEntreDates() throws Exception {
        System.out.println("Test 3 — Produits commandes entre deux dates");

        Date d1 = new Date(113, 0, 1);
        Date d2 = new Date(113, 11, 31);
        System.out.println("  Periode : 01/01/2013 -> 31/12/2013");

        List<Produit> produits = produitService.findProduitsCommandesEntreDates(d1, d2);
        for (Produit p : produits) {
            System.out.printf("    -> %-8s  %6.0f DH%n", p.getReference(), p.getPrix());
        }
        System.out.println();
    }


    //  detail d'une commande donnee

    private static void ProduitsParCommande(int commandeId) {
        System.out.println("Test 4 — Produits de la commande n° " + commandeId);
        produitService.afficherProduitsParCommande(commandeId);
        System.out.println();
    }


    //  prix > 100 DH

    private static void PrixSuperieur100() {
        System.out.println("Test 5 — Produits prix > 100 DH");

        List<Produit> produits = produitService.findProduitsPrixSuperieur100();
        System.out.printf("  %-12s %8s%n", "Reference", "Prix");
        for (Produit p : produits) {
            System.out.printf("  %-12s %5.0f DH%n", p.getReference(), p.getPrix());
        }
        System.out.println();
    }

}
