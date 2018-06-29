package ca.qc.bdeb.prog203.vue;

import ca.qc.bdeb.prog203.modele.Case;
import javax.swing.JButton;

/**
 * JButton personnalisé pour pouvoir avoir comme attribut sa Case et ses coordonnés
 * @author 1666876
 */
public class Bouton extends JButton {
    
    private final int x;
    private final int y;
    private final Case cellule;

    /**
     * Constructeur par défaut
     * @param cellule
     * @param x
     * @param y
     * @param text
     */
    public Bouton(Case cellule, int x, int y, String text) {
        super(text);
        this.x = x;
        this.y = y;
        this.cellule = cellule;
    }


    public int getPosX() {
        return x;
    }

    public int getPosY() {
        return y;
    }

    public Case getCellule() {
        return cellule;
    }
    
    
    
    
    
    

    
    
    
    
}
