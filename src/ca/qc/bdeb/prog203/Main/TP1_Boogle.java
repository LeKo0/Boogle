/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog203.Main;

import ca.qc.bdeb.prog203.modele.Boogle;
import ca.qc.bdeb.prog203.vue.Fenetre;

public class TP1_Boogle {

    public static void main(String[] args) {

        Boogle modele = new Boogle();
        Fenetre vue = new Fenetre(modele);

    }

}
