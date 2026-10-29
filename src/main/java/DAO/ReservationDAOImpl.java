package DAO;

import Modele.Reservation;
import Modele.Salarie;
import Modele.Vehicule;

import java.security.Timestamp;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {
    private final String tableReservation="reservation";
    private final String id="id";
    private final String matricule="matricule";
    private final String idVehicule="id_vehicule";
    private final String dateDepart="date_depart";
    private final String dateRetour="date_retour";
    private final String kmRetour="km_retour";

    @Override
    public void creer(Reservation r) {

    }

    @Override
    public void cloturer(int idVehicule, Timestamp dateRetour, int kmRetour) {

    }

    @Override
    public boolean estEnDeplacement(int idVehicule) {
        return false;
    }

    @Override
    public boolean salarieEnDeplacement(String matricule) {
        return false;
    }

    @Override
    public List<Reservation> listerEnCours() {
        return List.of();
    }

    @Override
    public List<Reservation> listerParSalarie(String matricule) {
        return List.of();
    }

    @Override
    public List<Reservation> lister() {
        return List.of();
    }
}
