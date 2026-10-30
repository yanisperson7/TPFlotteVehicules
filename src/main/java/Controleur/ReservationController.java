package Controleur;

import Exceptions.RegleGestionException;
import Modele.HistoriqueReservations;
import Modele.Reservation;
import Modele.Salarie;
import Modele.SalarieRecord;
import Service.ReservationService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public String creerSalarie(SalarieRecord salarieRecord) {
        try {
            Salarie salarie = new Salarie(
                    salarieRecord.matricule(),
                    salarieRecord.nom(),
                    salarieRecord.prenom()
            );
            reservationService.creerSalarie(salarie);
            return "Salarié " + salarie.getMatricule() + " - "
                    + salarie.getPrenom() + " " + salarie.getNom() + " créé.";

        } catch (RegleGestionException e) {
            return e.getMessage();
        }
    }

    public String enregistrerDepart(String matricule, String immatriculation) {
        try {
            reservationService.enregistrerDepart(matricule, immatriculation);
            return "Le départ a été enregistré";
        } catch (RegleGestionException e) {
            return "Le départ a été refusé : " + e.getMessage();
        }

    }

    public String enregistrerRetour(String immatriculation, int kmRetour) {
        try {
            reservationService.enregistrerRetour(immatriculation, kmRetour);
            return "Le retour a été enregistré";
        } catch (RegleGestionException e) {
            return "Le retour a été refusé : " + e.getMessage();
        }
    }

    public List<Reservation> vehiculeEnDeplacement() {
        try {
            return reservationService.vehiculesEnDeplacement();
        } catch (RegleGestionException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public HistoriqueReservations<Salarie, Timestamp, Reservation> historiqueSalarie(String matricule) {
        try {
            return reservationService.historiqueSalarie(matricule);
        } catch (RegleGestionException e) {
            System.out.println("L'historique n'a pas pu être chargé : " + e.getMessage());
            return null;
        }
    }
}
