package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "MARIAGE")
public class Mariage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_DEBUT", nullable = false)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_FIN")
    private Date dateFin;          // null  ⟹ mariage en cours

    @Column(name = "NBR_ENFANT")
    private int nbrEnfant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOMME_ID")
    private Homme mari;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEMME_ID")
    private Femme femme;


    public Mariage() {}

    public Mariage(Date dateDebut, Date dateFin, int nbrEnfant, Homme mari, Femme femme) {
        this.dateDebut  = dateDebut;
        this.dateFin    = dateFin;
        this.nbrEnfant  = nbrEnfant;
        this.mari       = mari;
        this.femme      = femme;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbrEnfant() {
        return nbrEnfant;
    }

    public void setNbrEnfant(int nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }

    public Homme getMari() {
        return mari;
    }

    public void setMari(Homme mari) {
        this.mari = mari;
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
    }
}
