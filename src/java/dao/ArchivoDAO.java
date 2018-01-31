/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.ArchivoObj;
import bobjects.Usuario;
import database.Transacciones;
import java.util.ArrayList;
import utils.MyDate;

/**
 *
 * @author Alejandro
 */
public class ArchivoDAO {

    private Transacciones transacciones;

    public ArchivoDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public ArchivoObj initArchivo(int idArchivo) {
        ArchivoObj archivo = new ArchivoObj(idArchivo);
        ArrayList<ArrayList<String>> dataArchivo = transacciones.getArchivo("" + idArchivo);
        if (dataArchivo != null && dataArchivo.size() > 0) {
            ArrayList<String> datos = dataArchivo.get(0);
            //archivo.setTipo(null);
            archivo.setNombreTipo(datos.get(1));
            archivo.setDescripcionTipo(datos.get(2));
            archivo.setNombre(datos.get(3));
            archivo.setExtension(datos.get(4));
            archivo.setPath(datos.get(5));
            archivo.setChecksum(datos.get(6));
            archivo.setDescription(datos.get(7));
            String tmpDate = datos.get(8);
            MyDate fecha = new MyDate(tmpDate);
            if (tmpDate != null && tmpDate.length() > 0) {
                fecha.splitSQLStandarDate();
            }else{
                fecha = new MyDate("0000-00-00 00:00");
                fecha.splitSQLStandarDate();
            }
            archivo.setDate(fecha);
            archivo.setAlcance(datos.get(9));
            archivo.setEditor(datos.get(10));
            archivo.setDerechos(datos.get(11));
            archivo.setOrigen(datos.get(12));
            archivo.setTags(datos.get(13));
            archivo.setTipo(datos.get(14));
            String tmpSize = datos.get(15);
            try {
                archivo.setSize(Long.parseLong(tmpSize));
            } catch (NumberFormatException nfe) {
                archivo.setSize(-1);
            }
            ArrayList<ArrayList<String>> usuarios = transacciones.getArchivosByUser("" + idArchivo);
            if (usuarios != null) {
                for (ArrayList<String> user : usuarios) {
                    try {
                        Usuario u = new Usuario(Integer.parseInt(user.get(0)));
                        u.setNombres(user.get(1));
                        u.setApellidos(user.get(2));
                        u.setAcciones(user.get(3));
                        u.setComentarios(user.get(4));
                        archivo.addUser(u);
                    } catch (NumberFormatException nfe) {

                    }
                }
            }
            return archivo;
        } else {
            return null;
        }
    }
}
