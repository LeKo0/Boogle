package ca.qc.bdeb.prog203.vue;

import javax.swing.JRadioButtonMenuItem;

/**
 * JRadioButtonMenuItem personnalisé pour avoir en paramètre sa dimension associé
 * @author 1666876
 */
public class RadioButtonDimension extends JRadioButtonMenuItem {
    
    private final int dimensionIndex;

    /**
     * Constructeur par défaut
     * @param dimensionIndex
     * @param text
     */
    public RadioButtonDimension(int dimensionIndex, String text) {
        super(text);
        this.dimensionIndex = dimensionIndex;
    }

    public int getDimensionIndex() {
        return dimensionIndex;
    }
    
  

    
    
    
    
}
