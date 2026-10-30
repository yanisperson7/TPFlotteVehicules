package Controleur;

import Modele.UtilisateurRecord;
import Service.AuthService;

public class AuthController {
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Connecte l'utilisateur à son compte.
     * @param utilisateurRecord le couple utilisateur / mot de passe.
     * @return true si les informations de connexion sont bonnes, false le cas échéant.
     */
    public boolean Login(UtilisateurRecord utilisateurRecord){

        String utilisateur = utilisateurRecord.utilisateur();
        String motDePasse = utilisateurRecord.motDePasse();

        if(!authService.login(utilisateur,motDePasse)){
            return false;
        }

        return true;
    }

    /**
     * Retourne le nombre de tentatives restantes
     * @return une chaine de caractère informant du nombre de tentatives restantes.
     */
    public String afficherTentativesRestantes(){
        int tentativeRestantes = authService.getTentativeRestante();
        if(tentativeRestantes>0)
            return "Identifiants incorrects. Tentatives restantes : " + tentativeRestantes;
        else return "Identifiants incorrects. Nombre maximal de tentatives atteint. \nFermeture de l'application.";
    }
}
