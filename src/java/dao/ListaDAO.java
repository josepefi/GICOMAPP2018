/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class ListaDAO {

    private Transacciones transacciones;

    public ListaDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public ArrayList<ArrayList> getLista(String lista, String where) {
        ArrayList<ArrayList> datos = null;
        if (lista.equals("1")) {
            datos = transacciones.getListaMarcadores(where);
        } else if (lista.equals("2")) {
            datos = transacciones.getListaMetagenomas(where);
        } else if (lista.equals("3")) {
            
            datos = transacciones.getListaGenomas(where);
        }else if(lista.equals("4")){
            datos = transacciones.getListaMuestra(where);
        }
        else if(lista.equals("5")){
            datos = transacciones.getListaMetGe(where);
        }
        return datos;

    }
    public ArrayList<ArrayList> getListaGenomas(String where) {
            ArrayList<ArrayList> datosG = transacciones.getListaMetGe(where) ;
            
            return datosG;
        }
    }


