/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.LibraryObj;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class LibraryDAO {

    private Transacciones transacciones;

    public Transacciones getTransacciones() {
        return transacciones;
    }

    public LibraryDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public void setTransacciones(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public LibraryObj initLibrary(int idLibreria) {

        LibraryObj libreria = new LibraryObj(idLibreria);
        if (idLibreria != -1) {
            ArrayList<ArrayList> datosLib = transacciones.getLibreria(""+idLibreria);
            if(datosLib != null && datosLib.size()>0){
                ArrayList<String> data = datosLib.get(0);
                libreria.setFuente(data.get(0));
                libreria.setSeleccion(data.get(1));
                libreria.setLayout(data.get(2));
                libreria.setVector(data.get(3));
                libreria.setScreen(data.get(4));
                libreria.setMetodo(data.get(5));
            }
        }
        return libreria;
    }
}
