package Modele;

import java.util.HashMap;

public class HistoriqueReservations<S, K, V> {
    private S salarie;
    private HashMap<K, V> reservations = new HashMap<>();

    public HistoriqueReservations(S salarie) {
        this.salarie = salarie;
    }

    public S getSalarie() {
        return salarie;
    }

    public void setSalarie(S salarie) {
        this.salarie = salarie;
    }

    public void ajouter(K cle, V valeur) {
        reservations.put(cle, valeur);
    }

    public HashMap<K, V> getReservations() {
        return reservations;
    }
}

//