package DAO;

import Modele.Vehicule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAOImpl implements VehiculeDAO{

    private String table = "vehicule";
    private String id = "id";
    private String immatriculation = "immatriculation";
    private String marque="marque";
    private String modele = "modele";
    private String date_mise_circulation = "date_mise_circulation";
    private String kilometrage = "kilometrage";

    @Override
    public void creer(Vehicule vehicule) {
        String query =
                "INSERT INTO " + table + " ("+
                        immatriculation + ", " +
                        marque + ", " +
                        modele + ", " +
                        date_mise_circulation + ", " +
                        kilometrage + ") " +
                "VALUES (?, ? , ?, ?, ?)";
        try {
            PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query);
            ps.setString(1,vehicule.getImmatriculation());
            ps.setString(2,vehicule.getMarque());
            ps.setString(3,vehicule.getModele());
            ps.setDate(4,java.sql.Date.valueOf(vehicule.getDate_mise_circulation()));
            ps.setInt(5,vehicule.getKilometrage());

            ps.execute();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Vehicule trouverParId(int id) {
        return null;
    }

    @Override
    public Vehicule trouverParImmatriculation(String immat) {
        return null;
    }

    @Override
    public List<Vehicule> lister() {
        String query = "SELECT * FROM " + table;
        List<Vehicule> vehicules = new ArrayList<>();

        try {
            PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt(this.id);
                String immatriculation = rs.getString(this.immatriculation);
                String marque = rs.getString(this.marque);
                String modele = rs.getString(this.modele);
                LocalDate date_mise_circulation = rs.getDate(this.date_mise_circulation).toLocalDate();
                int kilometrage = rs.getInt(this.kilometrage);

                Vehicule vehicule = new Vehicule(id,immatriculation,marque,modele,date_mise_circulation,kilometrage);
                vehicules.add(vehicule);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        return vehicules;
    }

    @Override
    public void modifier(Vehicule vehicule) {

    }

    @Override
    public void supprimer(int id) {

    }
}
