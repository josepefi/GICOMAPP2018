/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.Muestra;
import database.Transacciones;
import java.util.ArrayList;
/**
 *
 * @author Jose Pefi
 */
public class MuestraDAO {
    
    private Transacciones transacciones;

    public MuestraDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }
    
    /**
     * Este método se encarga de inicializar una muestra en base a su ID
     *
     * @param userId
     * @return
     */
    public Muestra initMuestraObject(int idMuestra) {
        ArrayList<ArrayList> muestraDetails = transacciones.getMuestra(idMuestra);
       
       if (muestraDetails != null && muestraDetails.size() > 0) {
            Muestra muestra = null;
            int i = 0;
            ArrayList<String> rec = muestraDetails.get(0);
            for (String val : rec) {
                if (val != null) {
                    switch (i) {
                        case 0:
                            muestra = new Muestra(Integer.parseInt(val));
                            break;
                        case 1:
                            muestra.setEtiqueta(val);
                            break;
                        case 2:
                            muestra.setContenedor(val);
                            break;
                        case 3:
                            muestra.setSamp_size(val);
                            break;
                        case 4:
                            muestra.setprotocolo(val);
                            break;
                        case 5:
                            muestra.setNotas(val);
                            break;
                        case 6:
                            muestra.setreal_to_oxy(val);
                            break;
                        case 7:
                            muestra.setcontenedor_temp(val);
                            break;
                    }
                }
                i++;
            }
            return muestra;
        }
        else{
        return null;
        }
    }
        /**
     * Este método se encarga de generar un String[] con los detalles de un
     * punto de muestreo dada una muestra. Es usado para construir el archivo de
     * entrada qu lee R para generar mapas de abundancia
     *
     * @param idMuestra
     * @return un array de string con todos los datos necesarion para el archivo
     * que se genera 
     */
    public String[] getEstacionByMuestra(String idMuestra) {
        ArrayList<ArrayList<String>> puntos = transacciones.getEstacionCoordsForMuestra(idMuestra);
        String[] detallesPunto = new String[4];
        if (puntos != null && puntos.size() > 0) {
            ArrayList<String> detalle = puntos.get(0);
            detallesPunto[0] = detalle.get(0);//ID
            detallesPunto[1] = detalle.get(1);//ESTACION
            detallesPunto[2] = detalle.get(2);//LAT
            detallesPunto[3] = detalle.get(3);//LON
            return detallesPunto;
        } else {
            return null;
        }
    }
}
