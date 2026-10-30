package Service;

import Exceptions.RegleGestionException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Path;

public class AuthService {
    private String path;
    private JsonMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
    private JsonNode root = mapper.readTree(Path.of("user.json"));
    private int tentativeRestante;

    public AuthService(String path) {
        this.path = path;
        this.tentativeRestante = 3;
    }

    public boolean login(String utilisateur, String motDePasse){
        if(tentativeRestante>1){
            String motDePasseBase = (root.get("login").asString().equals(utilisateur)) ? root.get("motDePasseHash").asString():"";

            if(motDePasse.isEmpty()){
                tentativeRestante--;
                return false;
            }

            BCrypt.Result result = BCrypt.verifyer().verify(motDePasse.toCharArray(), motDePasseBase);

            if(result.verified) return true;
            else{
                tentativeRestante--;
                return false;
            }
        }else{
            throw new RegleGestionException("Identifiants incorrects. Nombre maximal de tentatives atteint. \nFermeture de l'application.");
        }

    }

    public int getTentativeRestante(){
        return this.tentativeRestante;
    }

}
