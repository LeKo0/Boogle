package ca.qc.bdeb.prog203.modele;

/**
 * Objet Case
 * @author 1666876
 */
public class Case {

    private boolean selectable;
    private boolean selected;
    private final Lettre lettre;

    /**
     * Constructeur par défut
     * @param lettre
     */
    public Case(Lettre lettre) {

        this.selectable = true;
        this.selected = false;
        this.lettre = lettre;
    }

    public void setSelected(Boolean iSSelected) {
        this.selected = iSSelected;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public String toString() {
        return this.lettre.toString();
    }
}
