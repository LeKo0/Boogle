package ca.qc.bdeb.prog203.modele;

/**
 * Objet Mot
 * @author 1666876
 */
public class Mot {

    private final StringBuilder mot;
    private int points;

    /**
     * Constructeur par défaut
     */
    public Mot() {

        this.mot = new StringBuilder();

    }

    /**
     * Constructeur qui clone le mot qu'on lui met en paramètre
     * @param mot
     */
    public Mot(Mot mot) {

        this.mot = new StringBuilder();
        this.mot.append(mot.toString());
        this.points = mot.getPoints();

    }

    /**
     * Ajoute une lettre au mot
     * @param cellule Case contenant la lettre à ajouter
     */
    public void ajouterLettre(Case cellule) {

        this.mot.append(cellule.toString());

    }

    /**
     * Efface le mot
     */
    public void effacerMot() {

        //Efface de zéro jusqu'à la fin
        this.mot.delete(0, mot.length());
    }

    /**
     * Calcule la valeur du mot
     */
    public void calculerPoints() {

        switch (mot.length()) {
            case 1:
            case 2:
                points = 0;
                break;
            case 3:
                points = 1;
                break;
            case 4:
                points = 1;
                break;
            case 5:
                points = 2;
                break;
            case 6:
                points = 3;
                break;
            case 7:
                points = 5;
                break;
            default:
                points = 11;
                break;
        }

    }

    public int getPoints() {
        return this.points;
    }

    public boolean isEmpty() {
        return this.mot.length() == 0;
    }

    @Override
    public String toString() {
        return this.mot.toString();

    }

}
