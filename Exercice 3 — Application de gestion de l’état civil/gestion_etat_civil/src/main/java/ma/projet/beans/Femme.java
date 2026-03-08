package ma.projet.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "FEMME")
@DiscriminatorValue("FEMME")
@NamedQueries({
    @NamedQuery(
        name  = "Femme.femmesMarieesPlusDeDeux",
        query = "SELECT f FROM Femme f JOIN f.mariages m "
              + "GROUP BY f HAVING COUNT(m) >= 2"
    )
})
@NamedNativeQueries({
    @NamedNativeQuery(
        name            = "Femme.nbrEnfantsEntreDates",
        query           = "SELECT SUM(m.NBR_ENFANT) FROM MARIAGE m "
                        + "WHERE m.FEMME_ID = :femmeId "
                        + "  AND m.DATE_DEBUT >= :dateDebut "
                        + "  AND m.DATE_DEBUT <= :dateFin"
    )
})
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mariage> mariages = new ArrayList<>();


    public Femme() {}

    public Femme(String nom, String prenom, String telephone,
                 String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }


    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
