package DAO;

import Exceptions.DAOExceptions;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DAOFactory {
    private String url;
    private String login;
    private String password;
    private static DAOFactory instance;

    private DAOFactory() throws ClassNotFoundException, DAOExceptions {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try {
            Properties props = new Properties();
            InputStream in = getClass().getClassLoader().getResourceAsStream("dao.properties");
            props.load(in);

            url = props.getProperty("url");
            login = props.getProperty("user");
            password = props.getProperty("password");

            Class.forName(props.getProperty("driver"));
        } catch (Exception e) {
            throw new DAOExceptions("Erreur de configuration");
        }
    }

    public static DAOFactory getInstance() throws ClassNotFoundException {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public Connection getConnection() throws DAOExceptions {
        try {
            return DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            throw new DAOExceptions("Connexion impossible");
        }
    }
}
