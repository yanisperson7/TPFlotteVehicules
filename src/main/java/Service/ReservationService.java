package Service;

import DAO.ReservationDAO;
import DAO.VehiculeDAO;
import Exceptions.RegleGestionException;
import Modele.HistoriqueReservations;
import Modele.Reservation;
import Modele.Salarie;
import Modele.Vehicule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class ReservationService {
    private static final Logger log = LogManager.getLogger(ReservationService.class);
    private List<HistoriqueReservations<Salarie, Timestamp, Reservation>> historiques = new ArrayList<>();
    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;

    public ReservationService(VehiculeDAO vehiculeDAO, ReservationDAO reservationDAO) {
        this.vehiculeDAO = vehiculeDAO;
        this.reservationDAO = reservationDAO;
    }




    public void enregistrerRetour(String immatriculation, int kmRetour) {
        Vehicule v = vehiculeDAO.trouverParImmatriculation(immatriculation);
        // Règles de gestion
        if (v == null) {
            throw new RegleGestionException("véhicule inconnu");
        }
        if (!reservationDAO.estEnDeplacement(v.getId())) {
            throw new RegleGestionException("ce véhicule n'est pas en déplacement");
        }
        if (kmRetour < v.getKilometrage()) {
            throw new RegleGestionException("kilométrage inférieur au compteur actuel");
        }

        // Persistance : deux DAO pour une seule action
        //reservationDAO.cloturer(v.getId(), new Timestamp(System.currentTimeMillis()), kmRetour);
        v.setKilometrage(kmRetour);
        vehiculeDAO.modifier(v);

        log.info("Retour de {} à {} km", immatriculation, kmRetour);
    }
}
