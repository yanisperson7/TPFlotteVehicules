package Modele;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Reservation {
    private int id;
    private Salarie salarie;
    private Vehicule vehicule;
    private Timestamp dateDepart;
    private Timestamp dateRetour;
    private int kmRetour;

    public Reservation(int id, Salarie salarie, Vehicule vehicule, Timestamp dateDepart) {
        this.id = id;
        this.salarie = salarie;
        this.vehicule = vehicule;
        this.dateDepart = dateDepart;
    }

    public Reservation(int id, Timestamp dateRetour, int kmRetour) {
        this.id = id;
        this.dateRetour = dateRetour;
        this.kmRetour = kmRetour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Salarie getSalarie() {
        return salarie;
    }

    public void setSalarie(Salarie salarie) {
        this.salarie = salarie;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Timestamp getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Timestamp dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Timestamp getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Timestamp dateRetour) {
        this.dateRetour = dateRetour;
    }

    public int getKmRetour() {
        return kmRetour;
    }

    public void setKmRetour(int kmRetour) {
        this.kmRetour = kmRetour;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", salarie=" + salarie +
                ", vehicule=" + vehicule +
                ", dateDepart=" + dateDepart +
                ", dateRetour=" + dateRetour +
                ", kmRetour=" + kmRetour +
                '}';
    }

    public boolean estEnCours() {
        return dateRetour == null;
    }
}
