/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.PCRObj;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class PcrDAO {

    private Transacciones transacciones;

    public PcrDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public PCRObj initPCR(int idPCR) {
        PCRObj pcr = new PCRObj(idPCR);
        ArrayList<ArrayList<String>> pcrData = transacciones.getPCR("" + idPCR);
        if (pcrData != null && pcrData.size() > 0) {
            ArrayList<String> data = pcrData.get(0);
            pcr.setFw_primer(data.get(0));
            pcr.setRv_primer(data.get(1));
            pcr.setPrimerRef(data.get(2));            
            pcr.setComentarios(data.get(3));
            String condiciones = data.get(4);
            pcr.setPcr_cond(condiciones);
            return pcr;
        }else{
            return null;
        }
    }
}
