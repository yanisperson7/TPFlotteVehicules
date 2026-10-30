package DAO;

import Modele.Salarie;
import Modele.Vehicule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalarieDAOImpl implements SalarieDAO {
    private final String tableSalarie="salarie";
    private final String matriculeCOL="matricule";
    private final String nomCOL = "nom";
    private final String prenomCOL = "prenom";

    @Override
    public void creer(Salarie s) {
        String query = "INSERT INTO " + tableSalarie + " ("+ matriculeCOL + ", " + nomCOL + ", " + prenomCOL + ") values(?, ?, ?)";

        try {
            PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query);
            ps.setString(1, s.getMatricule());
            ps.setString(2, s.getNom());
            ps.setString(3, s.getPrenom());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Salarie trouverParMatricule(String matricule) {
        Salarie salarie = null;
        String query = "SELECT* FROM " + tableSalarie + " WHERE " + this.matriculeCOL + " LIKE ?";

        try(PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setString(1, matricule);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mat = rs.getString(this.matriculeCOL);
                String nom = rs.getString(this.nomCOL);
                String prenom = rs.getString(this.prenomCOL);

                salarie = new Salarie(matricule, nom, prenom);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return salarie;
    }

    @Override
    public List<Salarie> lister() {
        String query = "SELECT* FROM " + tableSalarie;

        List<Salarie> listeSalarie = new ArrayList<>();

        try(PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mat = rs.getString(this.matriculeCOL);
                String nom = rs.getString(this.nomCOL);
                String prenom = rs.getString(this.prenomCOL);

                Salarie salarie = new Salarie(mat, nom, prenom);
                listeSalarie.add(salarie);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return listeSalarie;
    }
}
