/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.Instrumento;
import bobjects.Medicion;
import bobjects.Muestra;
import bobjects.Muestreo;
import bobjects.Usuario;
import bobjects.Variable;
import database.Transacciones;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MyCoord;
import java.util.Date;
import java.util.Locale;
import utils.MyDate;

/**
 *
 * @author Alejandro
 */
public class MuestreoDAO {

    private Transacciones transacciones;

    public MuestreoDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Crea el SQL o inserta directamente en la BD todo lo necesario para dar de
     * alta una nueva muestra. TBD Reemplazar param outFile por algún writer
     * para no abrir y cerrar el archivo por cada registro!
     *
     * @param muestreo el muestreo - registro
     * @param toFile - true se escribe en archivo, false se escribe directamente
     * en la BD
     * @param outFile si toFile = true -> outFile se usa para el path absoluto
     * del archivo, a ser reemplazado por un writer
     * @param append si es archivo y append es true --> concatena al archivo,
     * sino reescribe
     * @param addInstrumentos si true -> agrega los instrumentos relacionados a
     * la muestreo
     * @param addUsuarios si true -> agrega los usuarios relacionados a la
     * muestreo
     * @param addMuestras si true -> agrega las muestras relacionados a la
     * muestreo
     * @return
     */
    public String almacenaMuestreo(Muestreo muestreo, boolean toFile, String outFile, boolean append, boolean addInstrumentos, boolean addUsuarios, boolean addMuestras) {
        //String query = "INSERT INTO Muestreo (`idMuestreo`, `idCE`, `idTipoMuestreo`, 
        //`idTipoMuestra`, `etiqueta`, `fecha_i`, `fecha_f`, `latitud_r`, `longitud_r`, 
        //`protocolo`, `comentarios`, `latitud_a`, `longitud_a`, `lance`, `bioma`, `env_feature`, 
        //`env_material`, `tamano`, `profundidad`, `tipo_profundidad`) VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);"
        String log = "";
        String fechaInicial = "";
        if (muestreo.getFechaInicial() == null) {
            fechaInicial = "";
        } else {
            fechaInicial = muestreo.getFechaInicial().toSQLString(true);
        }
        String fechaFinal = "";
        if (muestreo.getFechaFinal() == null) {
            fechaFinal = "";
        } else {
            fechaFinal = muestreo.getFechaFinal().toSQLString(true);
        }
        String query = "INSERT INTO muestreo  (idMuestreo, idCE, idTipoMuestreo,"
                + "idTipoMuestra, etiqueta, fecha_i, fecha_f, latitud_r, longitud_r, "
                + "protocolo, comentarios, latitud_a, longitud_a, lance, bioma, env_feature,"
                + "env_material, tamano,profundidad, tipo_profundidad) VALUES "
                + "(" + muestreo.getIdMuestreo() + "," + muestreo.getIdDerrotero() + "," + muestreo.getIdTipoMuestreo()
                + "," + muestreo.getIdTipoMuestra() + ",'" + muestreo.getEtiqueta() + "','" + fechaInicial
                + "','" + fechaFinal + "'," + muestreo.getLatitud_r().getCoords()
                + "," + muestreo.getLongitud_r().getCoords() + ",'" + muestreo.getProtocolo() + "','" + muestreo.getComentarios()
                + "'," + muestreo.getLatitud_a().getCoords() + "," + muestreo.getLongitud_a().getCoords() + ",'" + muestreo.getLance()
                + "','" + muestreo.getBioma() + "', '" + muestreo.getEnv_feature() + "','" + muestreo.getEnv_material()
                + "','" + muestreo.getTamano() + "'," + muestreo.getProfundidad() + ",'" + muestreo.getTipo_prof() + "')";
        FileWriter writer = null;
        if (toFile) {
            try {
                writer = new FileWriter(outFile, append);
            } catch (IOException ex) {
                Logger.getLogger(MuestreoDAO.class.getName()).log(Level.SEVERE, null, ex);
                log += "Error accesando archivo: " + outFile + "\n";
            }
        }
        if (!toFile) {
            if (!transacciones.insertaQuery(query)) {
                log += "Error insertando muestreo: " + muestreo.getIdMuestreo() + " - " + query + "\n";
            }
        } else {
            try {
                writer.write(query + ";\n");
            } catch (IOException ex) {
                log = "Error I/O escribiendo archivo: " + outFile + "\n" + query + "\n";
            }
        }
        //MEDICIONES
        for (Medicion medicion : muestreo.getMediciones()) {
            String queryMedicion = "INSERT INTO muestreo_variable(idMuestreo, idVariable,"
                    + "idOrden, idMetodo, medicion_t1, comentarios) VALUES ("
                    + muestreo.getIdMuestreo() + "," + medicion.getIdVariable() + ","
                    + medicion.getOrden() + "," + medicion.getIdMetodoMedida() + ", '" + medicion.getMedicion_t1() + "','" + medicion.getComentarios() + "')";
            if (!toFile) {
                if (!transacciones.insertaQuery(queryMedicion)) {
                    log += "Error insertando relación muestreo-variable: "
                            + muestreo.getIdMuestreo() + "(idmuestreo) - " + medicion.getIdVariable() + "(idVariable) - " + query + "\n";
                }
            } else {
                try {
                    writer.write(queryMedicion + ";\n");
                } catch (IOException ex) {
                    log = "Error I/O escribiendo archivo: " + outFile + "\n" + query + "\n";
                }
            }
        }
        if (addInstrumentos) {
            for (Instrumento instrumento : muestreo.getInstrumentos()) {
                String queryInstrumento = "INSERT INTO muestreo_instrumento(idMuestreo, "
                        + "idInstrumento, cantidad, comentarios) VALUES ("
                        + muestreo.getIdMuestreo() + "," + instrumento.getIdInsrumento() + ",'"
                        + instrumento.getCantidad() + "','" + instrumento.getComentarios() + "')";
                if (!toFile) {
                    if (!transacciones.insertaQuery(queryInstrumento)) {
                        log += "Error insertando relación muestreo-instrumento: "
                                + muestreo.getIdMuestreo() + "(idmuestreo) - " + instrumento.getIdInsrumento() + "(idInst) - " + query + "\n";
                    }
                } else {
                    try {
                        writer.write(queryInstrumento + ";\n");
                    } catch (IOException ex) {
                        log = "Error I/O escribiendo archivo: " + outFile + "\n" + query + "\n";
                    }
                }
            }
        }
        if (addUsuarios) {
            for (Usuario usuario : muestreo.getUsuarios()) {
                String queryUsuario = "INSERT INTO muestreo_Usuario(idMuestreo, "
                        + "idUsuario, acciones) VALUES ("
                        + muestreo.getIdMuestreo() + "," + usuario.getIdUsuario() + ",'"
                        + usuario.getAcciones() + "')";
                if (!toFile) {
                    if (!transacciones.insertaQuery(queryUsuario)) {
                        log += "Error insertando relación muestreo-usuarios: "
                                + muestreo.getIdMuestreo() + "(idmuestreo) - " + usuario.getIdUsuario() + "(idUser) - " + query + "\n";
                    }
                } else {
                    try {
                        writer.write(queryUsuario + ";\n");
                    } catch (IOException ex) {
                        log = "Error I/O escribiendo archivo: " + outFile + "\n" + query + "\n";
                    }
                }
            }
        }
        if (addMuestras) {
            for (Muestra muestra : muestreo.getMuestras()) {
                String queryMuestra = "INSERT INTO muestra "
                        + "(idMuestra, idMuestreo, profundidad, etiqueta, contenedor, tamano, protocolo, notas) "
                        + "VALUES (" + muestra.getIdMuestra() + ", " + muestra.getIdMuestreo() + ", "
                        + muestra.getProfundidad() + ", '" + muestra.getEtiqueta() + "', '"
                        + muestra.getContenedor() + "', '" + muestra.getSamp_size() + "', '" + muestra.getProcess() + "','" + muestra.getNotas() + "')";
                if (!toFile) {
                    if (!transacciones.insertaQuery(queryMuestra)) {
                        log += "Error insertando muestra - muestreo: "
                                + muestra.getIdMuestra() + " - " + muestra.getIdMuestreo() + "(idmuestreo) - " + query + "\n";
                    }
                } else {
                    try {
                        writer.write(queryMuestra + ";\n");
                    } catch (IOException ex) {
                        log = "Error I/O escribiendo archivo: " + outFile + "\n" + query + "\n";
                    }
                }
                //MEDICIONES
                for (Medicion medicion : muestreo.getMediciones()) {
                    String queryMedicion = "INSERT INTO muestra_valor(idMuestra, idVariable,"
                            + "orden, idMetodo, medicion_t1, medicion_t2, medicion_t3, comentarios) VALUES ("
                            + muestra.getIdMuestra() + "," + medicion.getIdVariable() + ","
                            + medicion.getOrden() + "," + medicion.getIdMetodoMedida() + ",'" + medicion.getMedicion_t1() + "','','','" + medicion.getComentarios() + "')";
                    if (!toFile) {
                        if (!transacciones.insertaQuery(queryMedicion)) {
                            log += "Error insertando relación muestra_valor: "
                                    + muestra.getIdMuestra() + "(idmuestra) - " + medicion.getIdVariable() + "(idVariable) - " + query + "\n";
                        }
                    } else {
                        try {
                            writer.write(queryMedicion + ";\n");
                        } catch (IOException ex) {
                            log = "Error I/O escribiendo archivo: " + outFile + "\n" + query + "\n";
                        }
                    }
                }
            }
        }
        if (toFile) {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(MuestreoDAO.class.getName()).log(Level.SEVERE, null, ex);
                log += "Error cerrando archivo: " + outFile + "\n";
            }
        }
        return log;
    }

    /**
     * Inicializar objeto muestreo a partir de la muestra
     *
     * @param idm id de la muestra
     * @return objeto muestreo con lo necesario para desplegar la info en
     * funcion de la muestra
     */
    public Muestreo initMuestreoFromMuestra(int idm) {
        ArrayList<ArrayList> muestreoDetalles = transacciones.getMuestreo(idm);
        ArrayList<ArrayList> medicionesDetalles = transacciones.getVariables(idm);
        TermDAO tDAO = new TermDAO(transacciones);
        Muestreo muestreo = null; //new Muestreo(idm);
        if (muestreoDetalles != null && muestreoDetalles.size() > 0) {
            ArrayList<String> rec = muestreoDetalles.get(0);
            int i = 0;
            for (String val : rec) {
                if (val != null) {
                    switch (i) {
                        case 0:
                            muestreo = new Muestreo(Integer.parseInt(val));
                            break;
                        case 1:
                            muestreo.setEtiqueta(val);
                            break;
                        case 2:
                            muestreo.setIdCampana(val);
                            break;
                        case 3:
                            muestreo.setIdEstacion(val);
                            break;
                        case 4:
                            muestreo.setBioma(tDAO.initTerm(val));
                            break;
                        case 5:
                            muestreo.setEnv_material(tDAO.initTerm(val));
                            break;
                        case 6:
                            muestreo.setEnv_feature(tDAO.initTerm(val));
                            break;
                        case 7:
                            muestreo.setProtocolo(val);
                            break;
                        case 8:
                            muestreo.setComentarios(val);
                            break;   
                        case 9:
                            MyCoord lat = new MyCoord(val);
                            lat.computeGradosDecimales();
                            muestreo.setLatitud_r(lat);
                            break;
                        //en la vista
                        case 10:
                            MyCoord lon = new MyCoord(val);
                            lon.computeGradosDecimales();
                            muestreo.setLongitud_r(lon);
                            break;
                        case 11:
                            MyCoord latEstacion = new MyCoord(val);
                            latEstacion.computeGradosDecimales();
                            muestreo.setLatitud_estacion(latEstacion);
                            break;
                        case 12:
                            MyCoord lonEstacion = new MyCoord(val);
                            lonEstacion.computeGradosDecimales();
                            muestreo.setLongitud_estacion(lonEstacion);
                            break;
                        case 13:
                            MyDate fi = new MyDate(val);
                            fi.splitSQLStandarDate();
                            muestreo.setFechaInicial(fi);
                            break;
                        case 14:
                            MyDate ff = new MyDate(val);
                            ff.splitSQLStandarDate();
                            muestreo.setFechaFinal(ff);
                            break;
                        case 15:
                            muestreo.setTipo_prof(val);
                            break;
                        case 16:
                            muestreo.setProfundidad(Double.parseDouble(val));
                            break;
                    }
                }
                i++;
            }
            muestreo.setDistanciaEstacion(MyCoord.distance(muestreo.getLatitud_r(), muestreo.getLongitud_r(), muestreo.getLatitud_estacion(), muestreo.getLongitud_estacion(), "K", 4));
            Medicion medicion = null;
            for (ArrayList<String> m : medicionesDetalles) {
                String idMuestreo = m.get(0);
                String idvariable = m.get(1);
                String nombre_web = m.get(2);
                String medicion_t1 = m.get(3);
                String unidades = m.get(4);

                medicion = new Medicion(Integer.parseInt(idvariable));
                medicion.setMedicion_t1(medicion_t1);
                medicion.setIdVariable(Integer.parseInt(idvariable));
                medicion.setNombre(nombre_web);
                medicion.setUnidades(unidades);
                medicion.setIdMuestra(muestreo.getIdMuestreo());
                muestreo.addNewMedicion(medicion);
            }
            return muestreo;
        } else {
            return null;
        }
    }
}
