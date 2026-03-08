package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "PERSONNE")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE_PERSONNE")
public abstract class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "NOM", nullable = false)
    protected String nom;

    @Column(name = "PRENOM", nullable = false)
    protected String prenom;

    @Column(name = "TELEPHONE")
    protected String telephone;

    @Column(name = "ADRESSE")
    protected String adresse;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_NAISSANCE")
    protected Date dateNaissance;


    public Personne() {}

    public Personne(String nom, String prenom, String telephone,
                    String adresse, Date dateNaissance) {
        this.nom           = nom;
        this.prenom        = prenom;
        this.telephone     = telephone;
        this.adresse       = adresse;
        this.dateNaissance = dateNaissance;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
