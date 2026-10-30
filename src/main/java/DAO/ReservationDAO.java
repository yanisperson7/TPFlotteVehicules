package DAO;

import Modele.Reservation;

import java.sql.Timestamp;
import java.util.List;

public interface ReservationDAO {
    void creer(Reservation r);
    void cloturer(int idVehicule, Timestamp dateRetour, int kmRetour);
    boolean estEnDeplacement(int idVehicule);
    boolean salarieEnDeplacement(String matricule);
    List<Reservation> listerEnCours();
    List<Reservation> listerParSalarie(String matricule);
    List<Reservation> lister();
}