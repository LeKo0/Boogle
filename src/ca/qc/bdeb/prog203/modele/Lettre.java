package ca.qc.bdeb.prog203.modele;

/**
 * Liste des différentes lettres avec leur pondérations respectives
 * @author 1666876
 */
public enum Lettre {


    A(8.1),
    B(0.9),
    C(3.4),
    D(3.7),
    E(17.1),
    F(1.1),
    G(0.9),
    H(0.7),
    I(7.8),
    J(0.5),
    K(0.1),
    L(5.5),
    M(3.0),
    N(7.1),
    O(5.3),
    P(3.0),
    QU(1.4),
    R(6.6),
    S(7.8),
    T(7.2),
    U(6.3),
    V(1.6),
    W(0.1),
    X(0.4),
    Y(0.3),
    Z(0.1);

        private final double ponderation;

        Lettre(double ponderation) {
            this.ponderation = ponderation;
        }
        
    public double getPonderation(){
            return ponderation;
        }
    
}
