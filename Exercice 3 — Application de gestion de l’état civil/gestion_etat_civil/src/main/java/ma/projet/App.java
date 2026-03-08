package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class App {

    public static void main(String[] args) {

        FemmeService fs = new FemmeService();
        HommeService hs = new HommeService();
        MariageService ms = new MariageService();

        // 10 femmes
        System.out.println("  Création des femmes et des hommes");

        Femme f1  = new Femme("RAMI","Salima","0600000001","Rabat",d(1970,6,15));
        Femme f2  = new Femme("ALI","Amal","0600000002","Casablanca",d(1975,3,20));
        Femme f3  = new Femme("ALAOUI","Wafa","0600000003","Fes",d(1980,11,10));
        Femme f4  = new Femme("ALAMI","Karima","0600000004","Meknes",d(1968,1,5));
        Femme f5  = new Femme("BENALI","Fatima","0600000005","Marrakech",d(1972,8,22));
        Femme f6  = new Femme("AMINE","Houda","0600000006","Agadir",d(1983,9,30));
        Femme f7  = new Femme("TAZI","Nadia","0600000007","Tanger",d(1978,2,14));
        Femme f8  = new Femme("IDRISSI","Zineb","0600000008","Oujda",d(1990,7,1));
        Femme f9  = new Femme("FILALI","Khadija","0600000009","Settat",d(1965,4,18));
        Femme f10 = new Femme("CHRAIBI","Loubna","0600000010","Safi",d(1985,12,25));

        fs.create(f1); fs.create(f2); fs.create(f3); fs.create(f4); fs.create(f5);
        fs.create(f6); fs.create(f7); fs.create(f8); fs.create(f9); fs.create(f10);

        System.out.println("10 femmes créées.");

        // 5 hommes
        Homme h1 = new Homme("SAID","Safi","0611111111","Rabat",d(1965,5,10));
        Homme h2 = new Homme("KADIRI","Omar","0622222222","Casablanca",d(1960,3,3));
        Homme h3 = new Homme("BENNIS","Youssef","0633333333","Fes",d(1972,7,25));
        Homme h4 = new Homme("LAHLOU","Hassan","0644444444","Marrakech",d(1968,11,11));
        Homme h5 = new Homme("ZIRARI","Mehdi","0655555555","Agadir",d(1975,9,19));

        hs.create(h1); hs.create(h2); hs.create(h3); hs.create(h4); hs.create(h5);

        System.out.println("5 hommes créés.\n");

        // mariages
        System.out.println("  Création des mariages");

        ms.create(new Mariage(d(1989,9,3),d(1990,9,3),0,h1,f4));
        ms.create(new Mariage(d(1990,9,3),null,4,h1,f1));
        ms.create(new Mariage(d(1995,9,3),null,2,h1,f2));
        ms.create(new Mariage(d(2000,11,4),null,3,h1,f3));

        ms.create(new Mariage(d(1995,1,1),null,2,h2,f5));
        ms.create(new Mariage(d(1996,6,1),null,1,h2,f6));
        ms.create(new Mariage(d(1998,1,1),null,3,h2,f7));
        ms.create(new Mariage(d(2000,3,1),null,0,h2,f8));

        ms.create(new Mariage(d(2000,5,10),null,1,h3,f9));
        ms.create(new Mariage(d(2005,12,12),null,2,h3,f10));

        ms.create(new Mariage(d(1988,1,1),d(1990,1,1),0,h4,f1));
        ms.create(new Mariage(d(1992,5,5),null,2,h4,f9));

        System.out.println("Mariages créés.\n");

        // liste femmes
        System.out.println("liste des femmes");
        List<Femme> femmes = fs.findAll();

        for(int i=0;i<femmes.size();i++){
            Femme f=femmes.get(i);
            System.out.println((i+1)+". "+f.getPrenom().toUpperCase()+" "+f.getNom().toUpperCase());
        }

        System.out.println("Femme la plus âgée");
        Femme plusAgee = fs.getFemmeLaPlusAgee();
        System.out.println("La femme la plus âgée : "+plusAgee.getPrenom()+" "+plusAgee.getNom());

        System.out.println("Épouses de Safi SAID (entre 1989 et 2005)");
        List<Femme> epouses = hs.getEpousesEntreDates(h1,d(1989,1,1),d(2005,12,31));
        for(Femme f:epouses){
            System.out.println(f.getPrenom()+" "+f.getNom());
        }

        System.out.println(" Nombre d'enfants de Salima RAMI (1989-2001)");
        int nbr = fs.getNbrEnfantsEntreDates(f1,d(1989,1,1),d(2001,12,31));
        System.out.println("Nombre d'enfants : "+nbr);

        System.out.println("Femmes mariées au moins deux fois");
        List<Femme> list = fs.getFemmesMarieesAuMoinsDeux();
        for(Femme f:list){
            System.out.println(f.getPrenom()+" "+f.getNom());
        }

        System.out.println("Hommes mariés à 4 femmes (1990-2005)");
        long nb = fs.getNbrHommesMariesAQuatreFemmes(d(1990,1,1),d(2005,12,31));
        System.out.println(nb);

        System.out.println(" Mariages détaillés de Safi SAID");
        hs.afficherMariagesDetailles(h1);

        System.out.println("\n Fin des tests ");
    }

    private static Date d(int year,int month,int day){
        Calendar c=Calendar.getInstance();
        c.set(year,month-1,day);
        return c.getTime();
    }
}