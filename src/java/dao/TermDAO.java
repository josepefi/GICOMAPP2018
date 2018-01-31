/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.Term;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class TermDAO {

    private Transacciones transacciones;

    public TermDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Este método inicializa un nuevo término de alguna ontología
     *
     * @param idTerm
     * @return
     */
    public Term initTerm(String idTerm) {
        Term term = new Term(idTerm);
        if (idTerm != null && idTerm.length() > 0) {
            ArrayList<ArrayList> termAL = transacciones.getTermData(idTerm);            
            if (!termAL.isEmpty()) {
                ArrayList<String> t = termAL.get(0);
                try {
                    term.setIdOntologia(Integer.parseInt(t.get(0)));
                } catch (NumberFormatException nfe) {
                    term.setIdOntologia(-1);
                }
                term.setOntologia(t.get(1));
                term.setName(t.get(2));
                term.setDefinition(t.get(3));
                term.setIs_a(t.get(4));
                term.setName_space(t.get(5));
                term.setRelationship(t.get(6));
                term.setIs_obsolete(t.get(7));
                term.setReplaced_by(t.get(8));
                term.setUrl(t.get(9));
                term.setComments(t.get(10));
                return term;
            } else {
                return term;
            }
        } else {
            return term;
        }
    }
}
