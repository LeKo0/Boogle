package ca.qc.bdeb.prog203.vue;

import static ca.qc.bdeb.prog203.modele.Boogle.TypeUpdate.FIN;
import ca.qc.bdeb.prog203.modele.Boogle;
import ca.qc.bdeb.prog203.modele.Mot;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**
 * Vue
 *
 * @author 1666876
 */
public final class Fenetre extends JFrame implements Observer {

    private final Boogle boggle;

    private String min, sec;

    private Bouton[][] btnGrille;
    private JPanel pnlGrille, pnlInterface;
    private JMenu mnuFichier, mnuAide, mnuOptions;
    private JMenuBar menuBar;
    private JMenuItem mnuNouvellePartie, mnuQuitter, mnuAPropos;
    private JRadioButtonMenuItem[] rbmDimensions;
    private JLabel lblMotActuel, lblTimer, lblPoints;
    private JButton btnConfirmer, btnAnnuler;
    private JTextArea txaListeMots;
    private JScrollPane scpMots;
    private ButtonGroup btgDimensions;

    public static int HAUTEUR = 525;

    public static int LARGEUR = 750;

    /**
     * Constructeur par défaut. Appelle toutes les méthodes d'initialisation
     *
     * @param boggle Objet Boogle avec lequel on travaille
     */
    public Fenetre(Boogle boggle) {

        this.boggle = boggle;
        this.boggle.addObserver(this);

        initialiserFenetre();
        initialiserMenuBar();
        initialiserGrille();
        initialiserInterface();

        setVisible(true);

    }

    /**
     * Initialise la Fenetre(JFrame)
     */
    private void initialiserFenetre() {

        this.pnlGrille = new JPanel();
        this.pnlInterface = new JPanel();

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {

                if (JOptionPane.showConfirmDialog(Fenetre.this, "Etes vous sure de vouloir quitter?", "Voulez-vous quitter?", JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    boggle.writeDictionnaire(); //Save le fichier
                    System.exit(0);
                }
            }
        });
        this.setTitle("Jeu de Boogle");
        this.setSize(LARGEUR, HAUTEUR);
        this.setLayout(new GridLayout(1, 2));
        this.setMinimumSize(new Dimension(LARGEUR, HAUTEUR));

        this.add(pnlGrille);
        this.add(pnlInterface);

    }

    /**
     * Initialise la barre de menu et tout les évènements qui y sont relié
     */
    private void initialiserMenuBar() {

        //Déclarations
        menuBar = new JMenuBar();
        mnuFichier = new JMenu("Fichier");
        mnuAide = new JMenu("Aide");
        btgDimensions = new ButtonGroup();
        rbmDimensions = new RadioButtonDimension[4]; //4 est le nombre de choix de dimensions possible       

        //Disposition
        menuBar.add(mnuFichier);
        menuBar.add(mnuAide);

        mnuNouvellePartie = new JMenuItem("Nouvelle partie");
        mnuOptions = new JMenu("Options");
        mnuQuitter = new JMenuItem("Quitter");
        mnuAPropos = new JMenuItem("A propos");

        mnuFichier.add(mnuNouvellePartie);
        mnuFichier.add(mnuOptions);
        mnuFichier.addSeparator();
        mnuFichier.add(mnuQuitter);
        mnuAide.add(mnuAPropos);

        for (int i = 0; i < 4; i++) {
            rbmDimensions[i] = new RadioButtonDimension(i, Integer.toString(i + 4) + "x" + Integer.toString(i + 4));
            btgDimensions.add(rbmDimensions[i]);
            this.mnuOptions.add(rbmDimensions[i]);
        }
        rbmDimensions[Boogle.DIMENSION_PAR_DEFAUT - 4].setSelected(true); //Bouton coché par défaut

        this.setJMenuBar(menuBar);

        //Évènements
        mnuNouvellePartie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(Fenetre.this, "Voulez-vous vraiment commencer une nouvelle partie?") == JOptionPane.YES_OPTION) {
                    boggle.reinitialiserBoogle();
                    enableFrame(true);
                }
            }
        });

        mnuQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (JOptionPane.showConfirmDialog(Fenetre.this, "Etes vous sure de vouloir quitter?", "Voulez-vous quitter?", JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    boggle.writeDictionnaire(); //Save le fichier
                    System.exit(0);
                }
            }
        });

        mnuAPropos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Fenetre.this, "Léo Gagnon \n Dimanche 15 octobre 2017", "A propos", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        for (int i = 0; i < Boogle.DIMENSIONS.length; i++) {
            rbmDimensions[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    int dimension = Boogle.DIMENSIONS[((RadioButtonDimension) e.getSource()).getDimensionIndex()];

                    if (JOptionPane.showConfirmDialog(Fenetre.this, "Etes vous sur de vouloir créer une nouvelle partie en  " + dimension + " x " + dimension + "?") == JOptionPane.YES_OPTION) {
                        rbmDimensions[dimension - 4].setSelected(true);
                        boggle.reinitialiserBoogle(dimension);
                        enableFrame(true);

                    }

                }
            });
        }

    }

    /**
     * Initialise la partie interface d'utilisation (autre que la grille) et
     * tout les évènements qui y sont reliés
     */
    private void initialiserInterface() {

        //Initialisation variables GridBadLayout
        pnlInterface.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Declarations
        lblMotActuel = new JLabel(boggle.getMotActuel().toString());
        lblTimer = new JLabel(((boggle.getSecondesRestantes() % 3600) / 60) + ":"
                + boggle.getSecondesRestantes() % 60);
        btnConfirmer = new JButton("Confirmer mot");
        btnAnnuler = new JButton("Annuler mot");
        txaListeMots = new JTextArea();
        scpMots = new JScrollPane();
        lblPoints = new JLabel("Points : " + boggle.getPoints());

        //Réglages 
        scpMots.setViewportView(txaListeMots);
        scpMots.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scpMots.setPreferredSize(new Dimension(100, 30));
        txaListeMots.setEditable(false);

        lblMotActuel.setHorizontalAlignment(JLabel.CENTER);
        lblMotActuel.setOpaque(true);
        lblMotActuel.setPreferredSize(new Dimension(100, 20));
        lblMotActuel.setFont(new Font("Serif", Font.PLAIN, 20));

        lblTimer.setHorizontalAlignment(JLabel.CENTER);
        lblTimer.setOpaque(true);
        lblTimer.setPreferredSize(new Dimension(100, 20));
        lblTimer.setFont(new Font("SansSerif", Font.BOLD, 100));

        lblPoints.setHorizontalAlignment(JLabel.LEFT);
        lblPoints.setOpaque(true);
        lblPoints.setPreferredSize(new Dimension(100, 20));
        lblPoints.setFont(new Font("SansSerif", Font.BOLD, 25));

        //Disposition
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        pnlInterface.add(lblMotActuel, c);

        c.gridy = 1;
        c.gridwidth = 1;
        pnlInterface.add(btnConfirmer, c);
        c.gridx = 1;
        pnlInterface.add(btnAnnuler, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        pnlInterface.add(scpMots, c);

        c.gridx = 1;
        pnlInterface.add(lblPoints, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        pnlInterface.add(lblTimer, c);

        //Évènements
        btnAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boggle.annulerMot();
            }
        });

        btnConfirmer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmer = boggle.confirmerMot();
                switch (confirmer) {
                    case 1:

                        JOptionPane.showMessageDialog(Fenetre.this, "Votre mot doit avoir au moin 3 lettres", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:

                        switch (JOptionPane.showConfirmDialog(Fenetre.this, "Le mot que vous avez composé n'est pas dans le dictionnaire, voulez-vous l'ajouter?")) {

                            case JOptionPane.YES_OPTION:

                                boggle.ajouterMotDictionnaire(true);
                                break;
                            case JOptionPane.NO_OPTION:

                                boggle.ajouterMotDictionnaire(false);
                                break;
                            case JOptionPane.CANCEL_OPTION:
                            //Ne rien faire car cela veux dire qu'il s'est trompé de bouton

                        }

                        break;
                    case 3:

                        JOptionPane.showMessageDialog(Fenetre.this, "Vous ne pouvez pas composer le même mot à deux reprise", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
                        break;
                    default: //confirmer == 0
                        boggle.ajouterMotListe();
                        break;
                }
            }
        });

    }

    /**
     * Initialise l'interface de la grille de jeu et tout les évènements qui y
     * sont reliés
     */
    private void initialiserGrille() {

        //Déclaration
        int dimension = this.boggle.getGrille().length;
        this.btnGrille = new Bouton[dimension][dimension];

        //Reinitialisation de la grille
        this.pnlGrille.removeAll();
        this.pnlGrille.setLayout(new GridLayout(dimension, dimension));

        //Réglages des boutons et évènements
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {

                Bouton boutonTemp; //Espace temporaire pour configurer le bouton

                this.pnlGrille.add(boutonTemp = new Bouton(boggle.getGrille()[i][j], i, j, boggle.getGrille()[i][j].toString())); //Création du bouton et ajout dans le JPanel
                this.btnGrille[i][j] = boutonTemp; //Enregistrement du bouton dans la grille de JButton
                this.btnGrille[i][j].setBackground(Color.WHITE); //Couleur par défault

                boutonTemp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        boggle.selectionnerCase(((Bouton) e.getSource()).getCellule(),
                                ((Bouton) e.getSource()).getPosX(),
                                ((Bouton) e.getSource()).getPosY());

                    }
                });

            }
        }

        //Au cas où enableFrame(false)
        pnlGrille.revalidate();
        pnlGrille.repaint();

    }

    /**
     * Met à jour l'affichage du temps
     */
    private void majTimer() {

        min = Integer.toString((boggle.getSecondesRestantes() % 3600) / 60);
        sec = Integer.toString(boggle.getSecondesRestantes() % 60);

        if (sec.length() == 1) {
            sec = "0" + sec;
        }

        this.lblTimer.setText(min + ":" + sec);

    }

    /**
     * Met à jour l'affichage de la liste de mots
     */
    private void majListeMot() {

        this.txaListeMots.setText(null);
        for (Mot mot : boggle.getMots()) {
            this.txaListeMots.append(mot.toString() + "\n");
        }

    }

    /**
     * Met à jour l'affichage de la grille de boutons
     */
    private void majGrilleBoutons() {

        for (int i = 0; i < boggle.getGrille().length; i++) {
            for (int j = 0; j < boggle.getGrille().length; j++) {
                if (boggle.getGrille()[i][j].isSelected()) {
                    btnGrille[i][j].setBackground(Color.LIGHT_GRAY); //Couleur bouton sélectionné
                } else {
                    btnGrille[i][j].setBackground(Color.WHITE); //Couleur bouton non-sélectionné
                }
            }

        }

    }

    /**
     * Met à jour l'affichage du mot actuel
     */
    private void majMotActuel() {

        this.lblMotActuel.setText(boggle.getMotActuel().toString());
    }

    /**
     * Met à jour l'affichage des points
     */
    private void majPoints() {

        this.lblPoints.setText("Points : " + boggle.getPoints());

    }

    /**
     * Active ou désactive tout les Component du frame excepté le JMenuBar
     *
     * @param enable si true on active, si false on désactive
     */
    private void enableFrame(boolean enable) {

        for (Component component : pnlGrille.getComponents()) {
            component.setEnabled(enable);
        }

        for (Component component : pnlInterface.getComponents()) {
            component.setEnabled(enable);
        }

    }

    /**
     * Actions à exécuter lorsque la partie est finie
     */
    private void finPartie() {

        this.lblTimer.setText("FIN");
        enableFrame(false);
        JOptionPane.showMessageDialog(Fenetre.this, "Fin de la partie\n" + "Nombre de mots : " + boggle.getMots().size() + "\n" + "Points : " + boggle.getPoints());

    }

    /**
     * Met à jour l'interface graphique en fonction des donnés du modèle
     * (boggle). Selon le type de mise à jour, effectue différentes actions
     *
     * @param o Le modèle (implements Obsevable)
     * @param arg Le paramètre envoyé par le modèle (dans ce cas-ci un
     * Boogle.TypeUpdate)
     */
    @Override
    public void update(Observable o, Object arg) {

        Boogle.TypeUpdate typeUpdate = (Boogle.TypeUpdate) arg;

        switch (typeUpdate) {
            case TIMER:
                this.majTimer();
                break;
            case GRILLE:
                this.majListeMot();
                this.initialiserGrille();
                this.majMotActuel();
                this.majPoints();
                break;
            case LETTRE:
                this.majMotActuel();
                this.majGrilleBoutons();
                break;
            case MOT:
                this.majMotActuel();
                this.majPoints();
                this.majGrilleBoutons();
                this.majListeMot();
                break;
            case FIN:
                this.finPartie();
                break;
        }

    }

}
