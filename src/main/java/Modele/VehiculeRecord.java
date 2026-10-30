package Modele;

import java.time.LocalDate;

public record VehiculeRecord(String immat, String marque, String modele, LocalDate dateMiseCirculation, int kilometrage ) {
}
