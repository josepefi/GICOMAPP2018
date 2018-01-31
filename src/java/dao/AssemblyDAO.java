/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.AssemblyObj;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class AssemblyDAO {

    private Transacciones transacciones;

    public Transacciones getTransacciones() {
        return transacciones;
    }

    public AssemblyDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public void setTransacciones(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Metodo que se encarga de inicializar un objeto de tipo ensamble
     *
     * @param idEnsamble
     * @return
     */
    public AssemblyObj initAssembly(int idEnsamble) {
        AssemblyObj ensamble = new AssemblyObj(idEnsamble);
        if (idEnsamble != -1) {
            ArrayList<ArrayList> ensambleAL = transacciones.getEnsambleByID("" + idEnsamble);
            if (ensambleAL != null && ensambleAL.size() > 0) {
                ArrayList<String> ass = ensambleAL.get(0);
                ensamble.setEnsamblador(ass.get(0));
                ensamble.setComentarios(ass.get(1));
                try {
                    ensamble.setContigs(Integer.parseInt(ass.get(2)));
                } catch (NumberFormatException nfe) {
                    ensamble.setContigs(-1);
                }
                try {
                    ensamble.setLongestContig(Integer.parseInt(ass.get(3)));
                } catch (NumberFormatException nfe) {
                    ensamble.setLongestContig(-1);
                }
                try {
                    ensamble.setAvgContig(Double.parseDouble(ass.get(4)));
                } catch (NumberFormatException nfe) {
                    ensamble.setAvgContig(-1);
                }
                ensamble.setN5090(ass.get(5));
                try {
                    ensamble.setLecturasMapeadas(Double.parseDouble(ass.get(6)));
                } catch (NumberFormatException nfe) {
                    ensamble.setLecturasMapeadas(-1);
                }

            }
        }
        return ensamble;

    }

}
