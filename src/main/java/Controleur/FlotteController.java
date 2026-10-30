package Controleur;

import Exceptions.RegleGestionException;
import Modele.Vehicule;
import Modele.VehiculeRecord;
import Service.VehiculeService;

import java.util.ArrayList;
import java.util.List;

public class FlotteController {
    VehiculeService vehiculeService;

    public FlotteController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    public String afficherFlotte(){
        String res = "";

        try {
            List<Vehicule> flotte = vehiculeService.getFlotte();

            res += "+-----------+----------+-----------+------------+-----------+----------------+\n";
            res += String.format("| %-10.10s| %-9.9s| %-10.10s| %-11.11s| %-10.10s| %-15.15s|\n", "Immat.", "Marque", "Modèle", "Mise circ", "Km", "Etat");
            for (Vehicule v : flotte){
                res+=String.format("| %-10.10s| %-9.9s| %-10.10s| %-11.11s| %-10.10s| %-15.15s|\n", v.getImmatriculation(), v.getMarque(), v.getModele(), v.getDate_mise_circulation(), v.getKilometrage(), "ETAT");;
            }
            res += "+-----------+----------+-----------+------------+-----------+----------------+\n";
        }catch (RegleGestionException e){
            return e.getMessage();
        }

        return res;

    }

    public String ajouterVehicule(VehiculeRecord vehiculeRecord){

        try {
            Vehicule vehicule = new Vehicule(
                    1,
                    vehiculeRecord.immat(),
                    vehiculeRecord.marque(),
                    vehiculeRecord.modele(),
                    vehiculeRecord.dateMiseCirculation(),
                    vehiculeRecord.kilometrage()

            );
            vehiculeService.ajouterVehicule(vehicule);
        } catch (RegleGestionException e) {
            return e.getMessage();
        }

        return "Le véhicule a été ajouté";
    }

    public String retirerVehicule(String immat){
        try {
            vehiculeService.retirerVehicule(immat);
        }catch (RegleGestionException e){
            System.out.println(e.getMessage());
        }

        return "Le véhicule a été retiré";
    }

    public  String rechercherVehicule(String immat){
        try {
            String res = "";
            Vehicule vehicule = vehiculeService.rechercherParImmatriculation(immat);
            res += vehicule.getImmatriculation() + " " + vehicule.getMarque() + " " + vehicule.getModele() + " " +
                    " Mise en circulation depuis le " + vehicule.getDate_mise_circulation() + ", " + vehicule.getKilometrage() + " km\n";
            res += "Etat : ...";
            return res;
        }catch (RegleGestionException e){
            return e.getMessage();
        }

    }

    public String trierFlotte(int tri){
        List<Vehicule> vehicules = new ArrayList<>();
        String res = "";
        switch(tri){
            case 1:
                vehicules = vehiculeService.trierParMarqueEtModele();
                break;
            case 2:
                vehicules = vehiculeService.trierParDateMiseCirculation();
                break;
            case 3:
                vehicules = vehiculeService.trierParKmDecroissant();
                break;
        }
        for (Vehicule vehicule: vehicules){
            res += vehicule.getImmatriculation() + " " + vehicule.getMarque() + " " + vehicule.getModele() + " " + vehicule.getKilometrage() +" km\n";
        }

        return res;
    }
}
