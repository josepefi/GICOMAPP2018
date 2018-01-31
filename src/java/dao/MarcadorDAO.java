/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.ArchivoObj;
import bobjects.Marcador;
import bobjects.StatsObj;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class MarcadorDAO {

    private Transacciones transacciones;

    public MarcadorDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Este método se encarga de inicializar un Marcador
     *
     * @param idMarcador el id del marcador a inicializar
     * @return marcador inicializado o null si no existe el id
     */
    public Marcador initMarcador(String idMarcador) {
        Marcador marcador = new Marcador(idMarcador);
        PcrDAO pcr = new PcrDAO(transacciones);
        LibraryDAO lib = new LibraryDAO(transacciones);
        ArchivoDAO archivoDAO = new ArchivoDAO(transacciones);
        StatsDAO stats = new StatsDAO(transacciones);
        ArrayList<ArrayList<String>> datosMarcador = transacciones.getNewMarcador(idMarcador);
        if (datosMarcador != null && datosMarcador.size() > 0) {
            //sólo se espera un elemento
            ArrayList<String> datos = datosMarcador.get(0);
            marcador.setIdMuestra(datos.get(1));
            marcador.setEtiquetaMuestra(datos.get(2));
            marcador.setIdTipoMarcador(datos.get(3));
            marcador.setGenes(datos.get(4));
            marcador.setSubFragment(datos.get(5));
            marcador.setIdTipoSec(datos.get(6));
            marcador.setNombreTipoSecuenciacion(datos.get(7));
            marcador.setDescripcionTipoSecuenciacion(datos.get(8));
            marcador.setIdSecuenciador(datos.get(9));
            marcador.setMarca(datos.get(10));
            marcador.setModelo(datos.get(11));
            marcador.setCentro_secuenciacion(datos.get(12));
            marcador.setIdPcr(datos.get(13));
            try {
                int idPCR = Integer.parseInt(datos.get(13));
                marcador.setPcr(pcr.initPCR(idPCR));
            } catch (NumberFormatException nfe) {
                marcador.setPcr(null);
            }
            marcador.setIdPcr(datos.get(13));
            try {
                int idLib = Integer.parseInt(datos.get(14));
                marcador.setLibreria(lib.initLibrary(idLib));
            } catch (NumberFormatException nfe) {
                marcador.setLibreria(null);
            }
            marcador.setIdStats(datos.get(15));
            try {
                int tmp = Integer.parseInt(datos.get(15));
                marcador.setStats(stats.initStats(tmp));
            } catch (NumberFormatException nfe) {
                marcador.setStats(new StatsObj(-1));
            }
            marcador.setMarc_name(datos.get(16));
            marcador.setMarc_desc(datos.get(17));
            marcador.setRaw_data_path(datos.get(18));
            marcador.setProc_data_path(datos.get(19));
            marcador.setAnalisis(datos.get(20));
            marcador.setClean_up_kit(datos.get(21));
            marcador.setClean_up_method(datos.get(22));
            marcador.setVolumen(datos.get(23));
            marcador.setComentarios(datos.get(24));
            marcador.setCite(datos.get(25));
            try {
                int tmp = Integer.parseInt(datos.get(26));
                marcador.setSeq_num_total(tmp);
            } catch (NumberFormatException nfe) {
                marcador.setSeq_num_total(-1);
            }
            marcador.setProcesamiento(datos.get(27));
            marcador.setEstacion(datos.get(28));
            marcador.setLatitud(datos.get(29));
            marcador.setLongitud(datos.get(30));
            marcador.setProfundidad(datos.get(31));
            marcador.setTipoMuestra(datos.get(32));
            ArrayList<ArrayList<String>> ids = transacciones.getArchivosMarcador(idMarcador);
            if (ids != null) {
                for (ArrayList<String> id : ids) {
                    try {
                        ArchivoObj arch = archivoDAO.initArchivo(Integer.parseInt(id.get(0)));
                        if (arch != null) {
                            marcador.addArchivo(arch);
                        }
                    } catch (NumberFormatException nfe) {

                    }
                }
            }
            return marcador;
        } else {
            return null;
        }

    }
}
