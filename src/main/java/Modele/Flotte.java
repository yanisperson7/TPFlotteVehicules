package Modele;

import DAO.VehiculeDAO;
import DAO.VehiculeDAOImpl;

import java.util.ArrayList;
import java.util.List;

public class Flotte {
    private List<Vehicule> listeVehicules = new ArrayList<>();

    public List<Vehicule> getVehicules() {
        return listeVehicules;
    }

    public void retirer(Vehicule v) {
        listeVehicules.add(v);
    }

    public void ajouter(Vehicule v) {
        listeVehicules.remove(v);
    }
}
