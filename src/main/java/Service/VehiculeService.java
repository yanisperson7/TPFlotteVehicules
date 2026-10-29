package Service;

import DAO.ReservationDAO;
import DAO.VehiculeDAO;
import Exceptions.DAOExceptions;
import Modele.Flotte;
import Modele.Vehicule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        flotte.ajouter(vehicule);                   // mise à jour immédiate en mémoire

        // 2. Soumettre : l'écriture en base se fait en arrière-plan
        executor.submit(() -> {
            try {
                VehiculeDAO.creer(vehicule);
                log.info("Véhicule enregistré : {}", vehicule.getImmatriculation());
            } catch (DAOExceptions e) {
                log.error("Échec de l'enregistrement", e);
            }
        });
    }

    // 3. Arrêter : appelé par Main quand le gestionnaire quitte l'application
    public void fermer() throws InterruptedException {
        executor.shutdown();                                                // n'accepte plus de nouvelles tâches
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();                                         // force l'arrêt au bout de 10s
        }
    }
}
