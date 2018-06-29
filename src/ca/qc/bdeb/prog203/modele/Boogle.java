package ca.qc.bdeb.prog203.modele;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Modèle
 *
 * @author 1666876
 */
public final class Boogle extends Observable {

    /**
     * Différentes raisons pour lesquelles on voudrait que les Observers
     * s'updatent
     */
    public enum TypeUpdate {

        TIMER, //Lorsqu'une seconde passe
        GRILLE, //Lorsqu'on réinitialise la grille
        LETTRE, //Lorsqu'on sélectionne une lettre
        MOT, //Lorsqu'on confirme ou annule un mot
        FIN //Lorsqu'il ne reste plus de temps
    }

    private int points;
    private Mot motActuel;
    private Case[][] grille;
    private final ArrayList<Mot> mots;
    private ArrayList<String> dictionnaire;
    private final Random random;
    private Timer timer;
    private int secondesRestantes;
    private BufferedReader lecture;
    private PrintWriter ecriture;
    private int dimension;

    public final static int[] DIMENSIONS = {4, 5, 6, 7};

    public final static int DIMENSION_PAR_DEFAUT = DIMENSIONS[0];

    public final static int TEMPS_PAR_DEFAUT = 180;

    /**
     * Constructeur par défaut
     */
    public Boogle() {

        dimension = DIMENSION_PAR_DEFAUT;
        random = new Random();
        grille = new Case[dimension][dimension];
        mots = new ArrayList<>();
        motActuel = new Mot();
        secondesRestantes = TEMPS_PAR_DEFAUT;
        points = 0;
        timer();

        importDictionnaire();
        initialiserGrille();

    }

    /**
     * Initialise la grille des cases aléatoires
     */
    public void initialiserGrille() {

        for (Case[] rangee : grille) {
            for (int i = 0; i < grille.length; i++) {
                rangee[i] = randomCase();
            }
        }

    }

    /**
     * Reinitialise la grille de jeu ainsi que les mots et les points du joueur
     *
     * @param dimension Dimension désiré de la grille
     */
    public void reinitialiserBoogle(int dimension) {

        this.dimension = dimension;
        this.grille = new Case[dimension][dimension];
        initialiserGrille();

        this.mots.clear();
        this.motActuel.effacerMot();

        this.secondesRestantes = TEMPS_PAR_DEFAUT;
        points = 0;

        timer.cancel();
        timer();

        setChanged();
        notifyObservers(TypeUpdate.GRILLE);

    }

    /**
     * Comme reinitialiserBoogle(int dimension) mais on utilise la dernière
     * dimension du jeu
     */
    public void reinitialiserBoogle() {

        this.grille = new Case[dimension][dimension];
        initialiserGrille();

        this.mots.clear();
        this.motActuel.effacerMot();

        this.secondesRestantes = TEMPS_PAR_DEFAUT;
        points = 0;

        timer.cancel();
        timer();

        setChanged();
        notifyObservers(TypeUpdate.GRILLE);

    }

    /**
     * Si elle est sélectionnable, sélectionne une case averti les cases
     * adjacentes
     *
     * @param cellule Adresse mémoire de la Case qu'on désire sélectionner
     * @param x Coordonné en x de la Case dans la grille de jeu
     * @param y Coordonné en y de la Case dans la grille de jeu
     */
    public void selectionnerCase(Case cellule, int x, int y) {

        if (cellule.isSelectable()) {

            this.motActuel.ajouterLettre(cellule);
            cellule.setSelected(true);
            this.updateSelectable(x, y);

            setChanged();
            notifyObservers(TypeUpdate.LETTRE);
        }

    }

    /**
     * Annule la saisie d'un mot
     */
    public void annulerMot() {
        this.motActuel.effacerMot();
        clearGrille();

        setChanged();
        notifyObservers(TypeUpdate.MOT);
    }

    /**
     * Ajoute motActuel à la liste de mots et calcule ses points
     */
    public void ajouterMotListe() {

        this.motActuel.calculerPoints();
        this.mots.add(motActuel);
        calculerPoints();

        motActuel = new Mot();
        clearGrille();
        setChanged();
        notifyObservers(TypeUpdate.MOT);

    }

    /**
     * Ajoute(ou pas) un mot au dictionnaire, dans la liste de mot et dans le
     * fichier .txt
     *
     * @param ajouter Si true on ajoute le mot, si false on ne l'ajoute pas
     */
    public void ajouterMotDictionnaire(boolean ajouter) {
        if (ajouter) {

            this.dictionnaire.add(this.motActuel.toString().toLowerCase());

            ajouterMotListe();

        } else {
            this.motActuel.effacerMot();

            clearGrille();
            setChanged();
            notifyObservers(TypeUpdate.MOT);
        }

    }

    /**
     * Désélectionne toutes les cases et les rends sélectionnable
     */
    public void clearGrille() {
        for (Case[] rangee : grille) {
            for (Case cellule : rangee) {
                cellule.setSelected(false);
                cellule.setSelectable(true);
            }
        }
    }

    /**
     * Rends sélectionnable seulement les cases qui sont adjacentes au
     * coordonnées données
     *
     * @param x
     * @param y
     */
    public void updateSelectable(int x, int y) {

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille.length; j++) {

                if (!grille[i][j].isSelected()
                        && ((i == x && j == y + 1)
                        || (i == x && j == y - 1)
                        || (i == x + 1 && j == y)
                        || (i == x - 1 && j == y)
                        || (i == x + 1 && j == y + 1)
                        || (i == x - 1 && j == y - 1)
                        || (i == x - 1 && j == y + 1)
                        || (i == x + 1 && j == y - 1))) {

                    grille[i][j].setSelectable(true);

                } else {
                    grille[i][j].setSelectable(false);
                }

            }

        }

    }

    /**
     * Met en marche le timer et vérifie si il reste du temps
     */
    public void timer() {

        timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                secondesRestantes--;
                if (secondesRestantes == 0) {

                    timer.cancel();
                    setChanged();
                    notifyObservers(TypeUpdate.FIN);

                } else {

                    setChanged();
                    notifyObservers(TypeUpdate.TIMER);
                }

            }

        }, 1000, 1000); //Exécute run() chaque 1000ms

    }

    /**
     * Importe le dictionnaire en ArrayList<> à partir d'un document texte
     */
    public void importDictionnaire() {

        //Déclarations
        String ligne;
        dictionnaire = new ArrayList<>();

        try {

            lecture = new BufferedReader(new FileReader("liste_francais.txt"));

            while ((ligne = lecture.readLine()) != null) {

                dictionnaire.add(ligne);

            }

        } catch (FileNotFoundException ex) {
            System.out.println("Fichier introuvable"); //Ne devrait jamais se rendre ici        
        } catch (IOException ex) {
            System.out.println("Erreur de lecture du fichier"); //Ne devrait jamais se rendre ici
        }
    }

    /**
     * Calcule le total des points associés aux mots dans la liste
     */
    public void calculerPoints() {

        //Recalcule à partir de 0 à chaque fois
        points = 0;

        for (Mot mot : this.mots) {
            points += mot.getPoints();
        }

    }

    /**
     * Crée une nouvelle case aléatoire selon la pondération des lettres
     *
     * @return Une Case aléatoire
     */
    public Case randomCase() {

        //Initialise un nombre aléatoire entre 0 et 100 (double)
        double p = random.nextDouble() * 100;
        //Probabilité cumulative est la somme des probabilité
        double probabiliteCumulative = 0.0;

        for (Lettre value : Lettre.values()) {

            //On additionne les probabilités unes par unes
            probabiliteCumulative += value.getPonderation();

            //Jusqu'à ce qu'on trouve à quel intervale le nombre aléatoire appartiens
            if (p <= probabiliteCumulative) {
                return new Case(value);
            }
        }
        return null; //N'est pas supposé de se rendre ici

    }

    /**
     * Confirme si un mot se trouve est valide ou non
     *
     * @return 1 si le mot est trop court 2 si le mot n'est pas dans le
     * dictionnaire 3 si le mot est déjà dans la liste 0 si le mot est valide
     */
    public int confirmerMot() {

        if (this.motActuel.toString().length() < 3) {
            return 1;
        } else if (!this.dictionnaire.contains(this.motActuel.toString().toLowerCase())) {
            return 2;
        } else if (this.listeMotsString().contains(motActuel.toString())) {
            return 3;
        }
        return 0;

    }

    /**
     * Ecrit ArrayList<Mot> mots dans le fichier
     */
    public void writeDictionnaire() {
        
        

        try {
            ecriture = new PrintWriter(new BufferedWriter(new FileWriter("liste_francais.txt", true)));

            for (String mot : this.dictionnaire) {
                ecriture.println();
                ecriture.print(mot.toLowerCase());
            }

            ecriture.close();

        } catch (IOException ex) {
            System.out.println("Erreur dans l'écriture du fichier");
        }

    }

    /**
     * Convertit la liste de Mot en liste de String
     *
     * @return mots en String
     */
    public ArrayList<String> listeMotsString() {

        ArrayList<String> motsString = new ArrayList<>();

        for (Mot mot : this.mots) {
            motsString.add(mot.toString());
        }

        return motsString;

    }

    public ArrayList<Mot> getMots() {
        return mots;
    }

    public Mot getMotActuel() {
        return motActuel;
    }

    public int getPoints() {
        return points;
    }

    public int getSecondesRestantes() {
        return secondesRestantes;
    }

    public Case[][] getGrille() {
        return grille;
    }

}
