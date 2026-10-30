package Service;

import DAO.ReservationDAO;
import DAO.VehiculeDAO;
import Exceptions.DAOExceptions;
import Exceptions.RegleGestionException;
import Modele.Flotte;
import Modele.Vehicule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VehiculeService {


    private static final Logger log = LogManager.getLogger(VehiculeService.class);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private VehiculeDAO vehiculeDAO;
    private ReservationDAO reservationDAO;
    private Flotte flotte;

    public VehiculeService(Flotte flotte, ReservationDAO reservationDAO, VehiculeDAO vehiculeDAO) {
        this.flotte = flotte;
        this.reservationDAO = reservationDAO;
        this.vehiculeDAO = vehiculeDAO;
        flotte.charger(vehiculeDAO.lister());
    }

    public void ajouterVehicule(Vehicule vehicule) {
        flotte.ajouter(vehicule);
        executor.submit(() -> {
            try {
                vehiculeDAO.creer(vehicule);
                log.info("Véhicule enregistré : {}", vehicule.getImmatriculation());
            } catch (DAOExceptions e) {
                log.error("Échec de l'enregistrement", e);
            }
        });
    }

    /**
     * Retire un véhicule de la flotte.
     * @param immat l'immatriculation du véhicule.
     * @throws RegleGestionException si l'immatriculation ne correspond à aucun véhicule.
     */
    public void retirerVehicule(String immat){
        // Vérifier que le véhicule existe
        Vehicule vehicule = rechercherParImmatriculation(immat);

        // Vérifier que le véhicule n'a pas de réservation en cours
        if(reservationDAO.estEnDeplacement(vehicule.getId())) throw new RegleGestionException("Impossible de retirer le véhicule : réservation en cours");

        // Execution du retrait
        flotte.retirer(vehicule);
        executor.submit(() -> {
            try {
                vehiculeDAO.supprimer(vehicule.getId());
                log.info("Véhicule retiré : {}", vehicule.getImmatriculation());
            } catch (DAOExceptions e) {
                log.error("Échec du retrait du véhicule", e);
            }
        });
    }

    /**
     * Retourne la liste des véhicules triés par marque et modèle.
     * @return une liste des véhicules triés par marque et modèle
     */
    public List<Vehicule> trierParMarqueEtModele(){
        List<Vehicule> vehicules = getFlotte();

        vehicules.sort(Comparator.comparing(Vehicule::getMarque)
                .thenComparing(Vehicule::getModele));

        return vehicules;
    }

    /**
     * Retournes la liste des véhicules triés par date de mise en circulation.
     * @return une liste des véhicules triés par date de mise en circulation.
     */
    public List<Vehicule> trierParDateMiseCirculation(){
        List<Vehicule> vehicules = getFlotte();

        vehicules.sort(Comparator.comparing(Vehicule::getDate_mise_circulation));

        return vehicules;
    }

    /**
     * Retournes la liste des véhicules triés par kilométrage décroissant.
     * @return une liste des véhicules triés par kilométrage décroissant.
     */
    public List<Vehicule> trierParKmDecroissant(){
        List<Vehicule> vehicules = getFlotte();

        vehicules.sort(Comparator.comparing(Vehicule::getKilometrage).reversed());

        return vehicules;
    }



    /**
     * Retourne le véhicule correspondant à l'immatriculation.
     * @param immat l'immatriculation du véhicule recherché.
     * @return le véhicule correspondant.
     * @throws RegleGestionException si l'immatriculation ne correspond à aucun véhicule.
     */
    public Vehicule rechercherParImmatriculation(String immat){
        Vehicule vehicule = vehiculeDAO.trouverParImmatriculation(immat);
        if (vehicule == null) throw new RegleGestionException("L'immatriculation ne correspond à aucun véhicule de la flotte");
        return vehicule;
    }

    /**
     * Retourne la flotte de véhicule.
     * @return la liste des véhicules de la flotte.
     * @throws RegleGestionException s'il n'y a aucun véhicule.
     */
    public List<Vehicule> getFlotte(){
        List<Vehicule> flotte = this.flotte.getVehicules();
        if(flotte.isEmpty()) throw new RegleGestionException("Le flotte ne contient aucun véhicule");
        return flotte;
    }

    /**
     * Ferme l'executor, à appeler par le Main à la fermeture du programme.
     * @throws InterruptedException
     */
    public void fermer() throws InterruptedException {
        executor.shutdown();                                                // n'accepte plus de nouvelles tâches
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();                                         // force l'arrêt au bout de 10s
        }
    }
}
