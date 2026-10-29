package DAO;

import Modele.Vehicule;

import java.util.List;

public interface VehiculeDAO {

    void creer(Vehicule vehicule); // renseigne l'id généré
    Vehicule trouverParId(int id);
    Vehicule trouverParImmatriculation(String immat);
    List<Vehicule> lister();
    void modifier(Vehicule vehicule);
    void supprimer(int id);

}
