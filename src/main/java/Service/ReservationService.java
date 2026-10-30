package Service;

import DAO.ReservationDAO;
import DAO.SalarieDAO;
import DAO.VehiculeDAO;
import Exceptions.RegleGestionException;
import Modele.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class ReservationService {
    private static final Logger log = LogManager.getLogger(ReservationService.class);
    private final List<HistoriqueReservations<Salarie, Timestamp, Reservation>> historiques = new ArrayList<>();
    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;
    private final SalarieDAO salarieDAO;
    private Flotte flotte;

    public ReservationService(VehiculeDAO vehiculeDAO, ReservationDAO reservationDAO, SalarieDAO salarieDAO) {
        this.vehiculeDAO = vehiculeDAO;
        this.reservationDAO = reservationDAO;
        this.salarieDAO = salarieDAO;
    }

    private HistoriqueReservations<Salarie, Timestamp, Reservation> trouverHistorique(String matricule) {
        for (HistoriqueReservations<Salarie, Timestamp, Reservation> h : historiques) {
            if (h.getSalarie().getMatricule().equals(matricule)) {
                return h;
            }
        }
        return null;
    }

    public void creerSalarie(Salarie salarie) {
        // Règles de gestion
        if (salarieDAO.trouverParMatricule(salarie.getMatricule()) != null) {
            throw new RegleGestionException("Le matricule " + salarie.getMatricule() + " existe déjà");
        }
        salarieDAO.creer(salarie);
    }

    public void enregistrerDepart(String matricule, String immatriculation) {
        // Règles de gestion
        Salarie s = salarieDAO.trouverParMatricule(matricule);
        if (s == null) {
            throw new RegleGestionException("salarié inconnu");
        }

        Vehicule v = vehiculeDAO.trouverParImmatriculation(immatriculation);
        if (v == null) {
            throw new RegleGestionException("véhicule inconnu");
        }

        if (reservationDAO.estEnDeplacement(v.getId())) {
            throw new RegleGestionException("Ce véhicule " + v.getImmatriculation() + " est déjà en déplacement");
        }

        if (reservationDAO.salarieEnDeplacement(s.getMatricule())) {
            throw new RegleGestionException(s.getPrenom() + " " + s.getNom() + " a déjà un véhicule en déplacement");
        }

        Reservation r = new Reservation(s, v, new Timestamp(System.currentTimeMillis()));
        reservationDAO.creer(r);

        HistoriqueReservations<Salarie, Timestamp, Reservation> h = trouverHistorique(s.getMatricule());
        if (h == null) {
            h = new HistoriqueReservations<>(s);
            historiques.add(h);
        }
        h.ajouter(r.getDateDepart(), r);
        log.info("Départ de {} avec {}", s.getMatricule(), v.getImmatriculation());
    }

    public void enregistrerRetour(String immatriculation, int kmRetour) {

        // Règles de gestion
        Vehicule v = vehiculeDAO.trouverParImmatriculation(immatriculation);
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
        reservationDAO.cloturer(v.getId(), new Timestamp(System.currentTimeMillis()), kmRetour);
        v.setKilometrage(kmRetour);
        vehiculeDAO.modifier(v);

        log.info("Retour de {} à {} km", immatriculation, kmRetour);
    }

    public List<Reservation> vehiculesEnDeplacement() {
        return reservationDAO.listerEnCours();
    }

    public HistoriqueReservations<Salarie, Timestamp, Reservation> historiqueSalarie(String matricule) {

        // Règles de gestion
        Salarie s = salarieDAO.trouverParMatricule(matricule);
        if (s == null) {
            throw new RegleGestionException("salarié inconnu");
        }

        HistoriqueReservations<Salarie, Timestamp, Reservation> historique = new HistoriqueReservations<>(s);

        for (Reservation r : reservationDAO.listerParSalarie(matricule)) {
            historique.ajouter(r.getDateDepart(), r);
        }
        return historique;
    }

    public void chargerHistoriques() {
        historiques.clear();
        for (Reservation r : reservationDAO.lister()) {
            HistoriqueReservations<Salarie, Timestamp, Reservation> h = trouverHistorique(r.getSalarie().getMatricule());
            if (h == null) {
                h = new HistoriqueReservations<>(r.getSalarie());
                historiques.add(h);
            }
            h.ajouter(r.getDateDepart(), r);
        }
    }
}
