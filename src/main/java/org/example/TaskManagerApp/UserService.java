package org.example.TaskManagerApp;

import org.mindrot.jbcrypt.BCrypt;

/*
* TaskManagerin luokka, jossa määritellään salasanan hashaukseen käytettävät metodit.
*  -Suojataan salasana käyttämällä BCrypt-hash salausta, ja käytetään salaukseen 12 kierrosta.
*  -Varmistetaan hashatun salasanan ja oikean salasanan yhteensopivuus käyttämällä BCryptin checkpw-mnetodia.
 */
public class UserService {

    /*
    * Metodi, jolla hashataan salasana käyttämällä BCrypt-salauslogiikkaa. Käytetään salauksessa 12 kierrosta, koska se
    * on suositeltu määrä kierroksia niin, että suojaus on tehokas mutta myös suhteellisen nopea.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Metodi, jolla oikean salasanan ja hashatun salasanan yhteensopivuutta vertaillaan.
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
