import Controleur.FlotteController;
import DAO.*;
import Modele.Flotte;
import Modele.VehiculeRecord;
import Service.VehiculeService;
import Vue.MenuConsole;

import java.sql.Connection;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        MenuConsole menuConsole = new MenuConsole();
        Flotte flotte = new Flotte();
        ReservationDAO reservationDAO = new ReservationDAOImpl();
        VehiculeDAO vehiculeDAO = new VehiculeDAOImpl();
        flotte = new Flotte();
        VehiculeService vehiculeService = new VehiculeService(flotte,reservationDAO,vehiculeDAO);
        FlotteController flotteController = new FlotteController(vehiculeService);

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

            }

            choix = menuConsole.afficherMenu();

        }

    }
}