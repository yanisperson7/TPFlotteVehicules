import Controleur.AuthController;
import Controleur.FlotteController;
import Controleur.ReservationController;
import DAO.*;
import Exceptions.RegleGestionException;
import Modele.Flotte;
import Modele.Reservation;
import Service.AuthService;
import Service.ReservationService;
import Service.VehiculeService;
import Vue.MenuConsole;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Flotte flotte = new Flotte();

        VehiculeDAO vehiculeDAO = new VehiculeDAOImpl();
        ReservationDAO reservationDAO = new ReservationDAOImpl();
        SalarieDAO salarieDAO = new SalarieDAOImpl();

        VehiculeService vehiculeService = new VehiculeService(flotte,reservationDAO,vehiculeDAO);
        ReservationService reservationService = new ReservationService(vehiculeDAO, reservationDAO, salarieDAO);
        FlotteController flotteController = new FlotteController(vehiculeService);
        ReservationController reservationController = new ReservationController(reservationService);
        AuthService authService = new AuthService("user.json");
        AuthController authController = new AuthController(authService);

        MenuConsole menuConsole = new MenuConsole();

        menuConsole.demarrage();

        boolean login=false;

        try {
            while(!login){
                login = authController.Login(menuConsole.connexion());
                if(!login) menuConsole.afficher(authController.afficherTentativesRestantes());
            }
        }catch (RegleGestionException e){
            menuConsole.afficher(e.getMessage());
            return;
        }

        int choix = menuConsole.afficherMenu();
        while(choix != 0){

            switch(choix){
                case 1: // Afficher la flotte
                    menuConsole.afficher(flotteController.afficherFlotte());
                    break;

                case 2: // Ajouter un véhicule
                    menuConsole.afficher(flotteController.ajouterVehicule(menuConsole.ajouterVehicule()));
                    break;

                case 3: // Retirer un véhicule
                    menuConsole.afficher(flotteController.retirerVehicule(menuConsole.retirerVehicule()));
                    break;

                case 4: // Rechercher un véhicule
                    menuConsole.afficher(flotteController.rechercherVehicule(menuConsole.rechercherVehicule()));
                    break;

                case 5: // Trier un véhicule
                    menuConsole.afficher(flotteController.trierFlotte(menuConsole.trierFlotte()));
                    break;

                case 6: // Créer un salarié
                    menuConsole.afficher(reservationController.creerSalarie(menuConsole.creerSalarie()));
                    break;

                case 7: // Enregistrer un départ
                    String[] infosDepart = menuConsole.enregistrerDepart();
                    String resultatDepart = reservationController.enregistrerDepart(infosDepart[0], infosDepart[1]);
                    menuConsole.afficher(resultatDepart);
                    break;

                case 8: // Enregistrer un retour
                    String[] infosRetour = menuConsole.enregistrerRetour();
                    String resultatRetour = reservationController.enregistrerRetour(infosRetour[0], Integer.parseInt(infosRetour[1]));
                    menuConsole.afficher(resultatRetour);
                    break;

                case 9: // Véhicules en déplacement
                    List<Reservation> enDeplacement = reservationController.vehiculeEnDeplacement();
                    menuConsole.vehiculesEnDeplacement(enDeplacement);
                    break;

            }

            choix = menuConsole.afficherMenu();

        }



    }
}