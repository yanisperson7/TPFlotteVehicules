package DAO;

import Modele.Reservation;
import Modele.Salarie;
import Modele.Vehicule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {
    private final String tableReservation="reservation";
    private final String idCOL="id";
    private final String matriculeCOL = "matricule";
    private final String idVehiculeCOL = "id_vehicule";
    private final String dateDepartCOL = "date_depart";
    private final String dateRetourCOL = "date_retour";
    private final String kmRetourCOL="km_retour";

    @Override
    public void creer(Reservation r) {
        String query = "INSERT INTO " + tableReservation +" ("+ matriculeCOL + ", "+ idVehiculeCOL + ", "+ dateDepartCOL + ") values(?,?,?)";

        try {
            PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query);
            ps.setString(1, r.getSalarie().getMatricule());
            ps.setInt(2, r.getVehicule().getId());
            ps.setTimestamp(3, r.getDateDepart());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cloturer(int idVehicule, Timestamp dateRetour, int kmRetour) {
        String query = "UPDATE " + tableReservation
                + " SET " + dateRetourCOL + " = ?, " + kmRetourCOL + " = ?"
                + " WHERE " + idVehiculeCOL + " = ? AND " + dateRetourCOL + " IS NULL";

        try {
            PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query);
            ps.setTimestamp(1, dateRetour);
            ps.setInt(2, kmRetour);
            ps.setInt(3, idVehicule);
            ps.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erreur lors de la clôture de la réservation", e);
        }
    }

    @Override
    public boolean estEnDeplacement(int idVehicule) {
        String query = "SELECT COUNT(*) FROM " + tableReservation + " WHERE " + idVehiculeCOL + "=? " + " AND " + dateRetourCOL + " IS NULL ";

        try {
            Connection c = DAOFactory.getInstance().getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, idVehicule);
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean resultat = rs.getInt(1) > 0;
            c.close();
            return resultat;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erreur estEnDeplacement", e);
        }
    }

    @Override
    public boolean salarieEnDeplacement(String matricule) {
        String query = "SELECT COUNT(*) FROM " + tableReservation + " WHERE " + matriculeCOL + " = ?"
                + " AND " + dateRetourCOL + " IS NULL";

        try {
            Connection c = DAOFactory.getInstance().getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, matricule);
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean resultat = rs.getInt(1) > 0;
            c.close();
            return resultat;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erreur salarieEnDeplacement", e);
        }
    }

    @Override
    public List<Reservation> listerEnCours() {
        List<Reservation> listeReservation = new ArrayList<>();
        String query = "SELECT r.id as idReservation, r.date_depart, r.date_retour, r.km_retour,\n" +
                "       s.matricule, s.nom, s.prenom,\n" +
                "       v.id AS idVehicule, v.immatriculation, v.marque, v.modele,\n" +
                "       v.date_mise_circulation, v.kilometrage\n" +
                "FROM reservation r\n" +
                "INNER JOIN salarie s ON r.matricule = s.matricule\n" +
                "INNER JOIN vehicule v ON r.id_vehicule = v.id\n" +
                "WHERE r.date_retour IS NULL";

        try {
            Connection c = DAOFactory.getInstance().getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Salarie s = new Salarie(rs.getString("matricule"),
                        rs.getString("nom"),
                        rs.getString("prenom"));

                Vehicule v = new Vehicule(rs.getInt("idVehicule"),
                        rs.getString("immatriculation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getDate("date_mise_circulation").toLocalDate(),
                        rs.getInt("kilometrage"));

                Reservation r = new Reservation(s, v, rs.getTimestamp("date_depart"));
                listeReservation.add(r);
            }

            c.close();
            return listeReservation;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erreur", e);
        }
    }

    @Override
    public List<Reservation> listerParSalarie(String matricule) {
        List<Reservation> listeReservation = new ArrayList<>();
        String query = """
        SELECT r.id AS idReservation, r.date_depart, r.date_retour, r.km_retour,
               s.matricule, s.nom, s.prenom,
               v.id AS idVehicule, v.immatriculation, v.marque, v.modele,
               v.date_mise_circulation, v.kilometrage
        FROM reservation r
        INNER JOIN salarie s ON r.matricule = s.matricule
        INNER JOIN vehicule v ON r.id_vehicule = v.id
        WHERE r.matricule = ?
        ORDER BY r.date_depart DESC
        """;

        try {
            Connection c = DAOFactory.getInstance().getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, matricule);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Salarie s = new Salarie(rs.getString("matricule"),
                        rs.getString("nom"),
                        rs.getString("prenom"));

                Vehicule v = new Vehicule(rs.getInt("idVehicule"),
                        rs.getString("immatriculation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getDate("date_mise_circulation").toLocalDate(),
                        rs.getInt("kilometrage"));

                Reservation r = new Reservation(rs.getInt("idReservation"), s, v, rs.getTimestamp("date_depart"),
                        rs.getTimestamp("date_retour"),
                        rs.getInt("km_retour"));
                listeReservation.add(r);
            }

            c.close();
            return listeReservation;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lister par salarié : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservation> lister() {
        String query = "SELECT r.id AS idReservation, r.date_depart, r.date_retour, r.km_retour, s.matricule, s.nom, s.prenom, v.id AS idVehicule, v.immatriculation, v.marque, v.modele, v.date_mise_circulation, v.kilometrage\n" +
                "FROM reservation r \n" +
                "INNER JOIN salarie s ON r.matricule = s.matricule\n" +
                "INNER JOIN vehicule v ON r.id_vehicule = v.id\n" +
                "ORDER BY r.date_depart";

        List<Reservation> listReservations = new ArrayList<>();

        try {
            Connection c = DAOFactory.getInstance().getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Salarie s = new Salarie(rs.getString("matricule"),
                        rs.getString("nom"),
                        rs.getString("prenom"));

                Vehicule v = new Vehicule(rs.getInt("idVehicule"),
                        rs.getString("immatriculation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getDate("date_mise_circulation").toLocalDate(),
                        rs.getInt("kilometrage"));

                Reservation r = new Reservation(rs.getInt("idReservation"), s, v,
                        rs.getTimestamp("date_depart"),
                        rs.getTimestamp("date_retour"),
                        rs.getInt("km_retour"));
                listReservations.add(r);
            }

            c.close();
            return listReservations;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lister : " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
