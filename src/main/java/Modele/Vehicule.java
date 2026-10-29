package Modele;

import java.time.LocalDate;

public class Vehicule {
    private int id;
    private String immatriculation;
    private String marque;
    private String modele;
    private LocalDate date_mise_circulation;
    private int kilometrage;

    public Vehicule(int id, String immatriculation, String marque, String modele, LocalDate date_mise_circulation, int kilometrage) {
        this.id = id;
        setImmatriculation(immatriculation);
        this.marque = marque;
        this.modele = modele;
        this.date_mise_circulation = date_mise_circulation;
        setKilometrage(kilometrage);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        boolean estValide = immatriculation.matches("^[A-Za-z]{2}-[0-9]{3}-[A-Za-z]{2}$");
        if(!estValide) throw new IllegalArgumentException("L'immatriculation doit-être au format AB-123-CD");
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public LocalDate getDate_mise_circulation() {
        return date_mise_circulation;
    }

    public void setDate_mise_circulation(LocalDate date_mise_circulation) {
        this.date_mise_circulation = date_mise_circulation;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(int kilometrage) {
        if(kilometrage<0) throw new IllegalArgumentException("Le kilométrage doit-être un entier positif");
        this.kilometrage = kilometrage;
    }

    @Override
    public String toString() {
        return "Vehicule{" +
                "id=" + id +
                ", immatriculation='" + immatriculation + '\'' +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", date_mise_ciruclation='" + date_mise_circulation + '\'' +
                ", kilometrage=" + kilometrage +
                '}';
    }
}
