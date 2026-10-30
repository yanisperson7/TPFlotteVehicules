package Vue;

import Modele.SalarieRecord;
import Modele.UtilisateurRecord;
import Modele.Vehicule;
import Modele.VehiculeRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuConsole {
    private Scanner scanner = new Scanner(System.in);

    public void demarrage(){
        System.out.println("=============================================");
        System.out.println("  GESTION DE LA FLOTTE DE VÉHICULES");
        System.out.println("=============================================");
    }

    public UtilisateurRecord connexion(){
        String utilisateur = saisieString("Login : ");
        String motDePasse = saisieString("Mot de passe : ");

        return new UtilisateurRecord(utilisateur,motDePasse);
    }

    public int afficherMenu(){
        System.out.println("\n============== MENU PRINCIPAL ==============\n");
        System.out.println("VÉHICULES");
        System.out.println("1. Afficher la flotte");
        System.out.println("2. Ajouter un véhicule");
        System.out.println("3. Retirer un véhicule");
        System.out.println("4. Rechercher par immatriculation");
        System.out.println("5. Trier la flotte");
        System.out.println("SALARIÉS ET RÉSERVATIONS");
        System.out.println("6. Créer un salarié");
        System.out.println("7. Enregistrer un départ");
        System.out.println("8. Enregistrer un retour");
        System.out.println("9. Véhicules en déplacement");
        System.out.println("10. Historique d'un salarié");
        System.out.println("ÉTATS ET EXPORTS");
        System.out.println("11. Générer l'état PDF et l'envoyer par courriel");
        System.out.println("12. Exporter salariés et réservations (AES)");
        System.out.println("13. Lire un fichier exporté");
        System.out.println("0. Quitter");
        System.out.println("============================================");
        System.out.println("Votre Choix :");

        int choix = -1;
        boolean sortie = false;
        while(!sortie){
            try {
                choix = scanner.nextInt();
                if(choix >= 0 && choix <= 13) sortie = true;
                else System.out.println(choix + " ne fais pas partie des options");
            }catch (InputMismatchException e){
                System.out.println("Veuillez entrer un entier supérieur ou égale à 0");
            }
        }
        scanner.nextLine();
        return choix;
    }

    public void afficher(String res){

        System.out.println(res);

    }

    public VehiculeRecord ajouterVehicule(){

        System.out.println("AJOUT D'UN VÉHICULE");

        String immat = saisieImmat("Immatriculation (AA-123-AA) : ");
        String marque = saisieString("Marque :");
        String modele = saisieString("Modèle :");
        LocalDate date = saisieDate("Mise en circulation (jj/mm/aaaa) : ");
        int kilometrage = saisieInt("Kilométrage :");

        return new VehiculeRecord(immat,marque,modele,date,kilometrage);

    }

    public String retirerVehicule(){

        String immat = saisieImmat("Immatriculation du véhicule à retrier (AB-123-CD) : ");

        return immat;
    }

    public String rechercherVehicule(){

        String immat = saisieImmat("Immatriculation du véhicule à retrier (AB-123-CD) : ");

        return immat;
    }

    public int trierFlotte(){
        boolean saisieValide = false;
        int choix;

        do {
            System.out.println("1. Par marque, puis modèle");
            System.out.println("2. Par date de mise en circulation (plus ancien d'abord)");
            System.out.println("3. Par kilométrage décroissant");
            choix = saisieInt("Tri : ");

            if(choix>=1 && choix<=3) saisieValide = true;
            else System.out.println("Veuillez saisir un nomnbre entier entre 1 et 3");
        }while(!saisieValide);

        return choix;
    }

    public SalarieRecord creerSalarie() {
        System.out.println("CRÉATION D'UN SALARIÉ");

        String matricule = saisieString("Matricule (6 chiffres) : ");
        String nom = saisieString("Nom : ");
        String prenom = saisieString("Prénom : ");

        System.out.println("Récapitulatif");
        System.out.println("  Matricule : " + matricule);
        System.out.println("  Nom       : " + nom);
        System.out.println("  Prénom    : " + prenom);

        String confirmation = saisieString("Confirmer la création ? (o/n) : ");

        if (!confirmation.equalsIgnoreCase("o")) {
            System.out.println("Création annulée.");
            return null;
        }

        return new SalarieRecord(matricule, nom, prenom);
    }

    /*public String enregistrerDepart() {
        System.out.println("DÉPART D'UN VÉHICULE");

        String matricule = saisieString("Matricule du salarié : ");
        String nomPrenom = saisieString("Salarié : ");
        String immat = saisieImmat("Immatriculation : ");

        /*String matricule = saisieString("Matricule du salarié : ");
        String immat = saisieImmat("Immatriculation : ");

        return reservationController.enregistrerDepart(matricule, immat);

    }*/

    // UTIL
    public LocalDate saisieDate(String msg) {
        LocalDate valeur = null;
        boolean saisieValide;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        do {
            System.out.print(msg);
            String saisie = scanner.nextLine().trim();
            try {
                valeur = LocalDate.parse(saisie, format);
                saisieValide = true;
            } catch (DateTimeParseException e) {
                System.out.println("\u001B[31mVeuillez entrer une date valide (jj/mm/aaaa)\u001B[0m");
                saisieValide = false;
            }
        } while (!saisieValide);

        return valeur;
    }

    public int saisieInt(String msg) {
        int valeur = 0;
        boolean saisieValide;

        do {
            System.out.print(msg);
            String saisie = scanner.nextLine().trim();
            try {
                valeur = Integer.parseInt(saisie);
                saisieValide = true;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mVeuillez entrer un nombre entier valide\u001B[0m");
                saisieValide = false;
            }
        } while (!saisieValide);

        return valeur;
    }

    public String saisieString(String msg) {
        String saisie;
        boolean saisieValide;

        do {
            System.out.print(msg);
            saisie = scanner.nextLine().trim();
            saisieValide = !saisie.isEmpty();
            if (!saisieValide) {
                System.out.println("\u001B[31mLa saisie ne peut pas être vide\u001B[0m");
            }
        } while (!saisieValide);

        return saisie;
    }

    public String saisieImmat(String msg) {
        String saisie;
        boolean saisieValide;

        do {
            System.out.print(msg);
            saisie = scanner.nextLine().trim();

            if(!saisie.matches("^[A-Za-z]{2}-[0-9]{3}-[A-Za-z]{2}$")) saisieValide = false ;
            else saisieValide = true;

            if (!saisieValide) {
                System.out.println("\u001B[31mL'immatriculation doit-être au format AB-123-CD\u001B[0m");
            }
        } while (!saisieValide);

        return saisie;
    }
}















