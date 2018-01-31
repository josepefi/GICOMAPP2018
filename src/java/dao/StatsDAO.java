/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.StatsObj;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class StatsDAO {

    private Transacciones transacciones;

    public StatsDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public StatsObj initStats(int idStats) {
        StatsObj stats = new StatsObj(idStats);
        ArrayList<ArrayList<String>> dataStats = transacciones.getStats(""+idStats);
        if(dataStats != null && dataStats.size()>0){
             //sólo se espera un elemento
            ArrayList<String> datos = dataStats.get(0);
            stats.setReads(datos.get(0));
            stats.setBases(datos.get(1));
            stats.setLong_avg(datos.get(2));
            stats.setGc_prc(datos.get(3));
            stats.setQc_avg(datos.get(4));
            stats.setNs_prc(datos.get(5));
            stats.setQ20(datos.get(6));
            stats.setQ30(datos.get(7));
            stats.setCombined_prc(datos.get(8));
            return stats;
        }else{
            stats.setReads("-1");
            stats.setBases("-1");
            stats.setLong_avg("-1");
            stats.setGc_prc("-1");
            stats.setQc_avg("-1");
            stats.setNs_prc("-1");
            stats.setQ20("-1");
            stats.setQ30("-1");
            stats.setCombined_prc("-1");
            return stats;
        }
    }
}
