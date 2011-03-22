package fr.xebia.usiquizz.core.persistence;


import java.util.Comparator;

public class Joueur {
    private int score;
    private String lastName;
    private String firstName;
    private String email;
    private String sessionId;

    public Joueur(int score, String lastName, String firstName, String email, String sessionId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.sessionId = sessionId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }

    public String getSessionId() {
        return sessionId;
    }

    public static class JoueurComparator implements Comparator<Joueur> {

        @Override
        public int compare(Joueur o1, Joueur o2) {
            int comp;
            if ((comp = o1.getScore() - o2.getScore()) == 0) {
                if ((comp = o1.getEmail().compareTo(o2.getEmail())) == 0) {
                    if ((comp = o1.getLastName().compareTo(o2.getLastName())) == 0) {
                        return o1.getFirstName().compareTo(o2.getFirstName());
                    }
                }
            }
            return comp;
        }
    }


}