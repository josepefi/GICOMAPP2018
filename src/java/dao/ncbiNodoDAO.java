/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.NCBINode;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Jose Pefi
 */
public class ncbiNodoDAO {

    Transacciones transacciones;

    public ncbiNodoDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public NCBINode busqueda(String name) {
        ArrayList<ArrayList> ncbiDetails = transacciones.buscaNodosByName(name, false);

        if (ncbiDetails != null && ncbiDetails.size() > 0) {
            NCBINode ncbi = null;
            int i = 0;
            ArrayList<String> rec = ncbiDetails.get(0);
            for (String val : rec) {
                if (val != null) {
                    switch (i) {
                        case 0:
                            ncbi = new NCBINode(val);
                        case 1:
                            ncbi.setName(val);
                        case 2:
                            ncbi.setRank(val);
                    }
                }
                i++;
            }
            return ncbi;
        } else {
            return null;
        }
    }
}
