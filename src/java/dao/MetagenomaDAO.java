/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.ArchivoObj;
import bobjects.AssemblyObj;
import bobjects.LibraryObj;
import bobjects.Metagenoma;
import bobjects.StatsObj;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class MetagenomaDAO {

    private Transacciones transacciones;

    public MetagenomaDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Este método trae la información de los metagenomas a ser desplegada en la
     * página de Blast
     *
     * @param where
     * @return
     */
    public ArrayList<ArrayList<String>> getMetagenomasBlast(String where) {
        ArrayList<ArrayList<String>> metagenomas = transacciones.getMetagenomasBlast(where);
        if (metagenomas != null && !metagenomas.isEmpty()) {
            return metagenomas;
        } else {
            return null;
        }
    }

    /**
     * Método para inicializar un metagenoma
     *
     * @param idM el id del metagenoma a inicializar
     * @return
     */
    public Metagenoma initMetagenoma(int idM) {
        Metagenoma metagenoma = new Metagenoma(idM);
        ArrayList<ArrayList> metagenomaAL = transacciones.getNewMetagenoma("" + idM);
        if (metagenomaAL != null && metagenomaAL.size() > 0) {
            ArrayList<String> meta = metagenomaAL.get(0);
            if (meta.size() < 16) {
                return null;
            }
            try {
                metagenoma.setIdmuestra(Integer.parseInt(meta.get(0)));
                metagenoma.setEtiquetaMuestra(meta.get(1));
            } catch (NumberFormatException nfe) {
                metagenoma.setIdmuestra(-1);
                metagenoma.setEtiquetaMuestra("Muestra desconocida");
            }
            metagenoma.setTipoMuestra(meta.get(2));
            metagenoma.setProfundidad(meta.get(3));
            metagenoma.setName(meta.get(4));
            metagenoma.setDesc(meta.get(5));
            metagenoma.setCultivo(meta.get(6));
            metagenoma.setProcesamiento(meta.get(7));
            metagenoma.setAnalisis(meta.get(8));            
            try {
                metagenoma.setLatitud(Double.parseDouble(meta.get(9)));
            } catch (NumberFormatException nfe) {
                metagenoma.setLatitud(-1);
            }
            try {
                metagenoma.setLongitud(Double.parseDouble(meta.get(10)));
            } catch (NumberFormatException nfe) {
                metagenoma.setLongitud(-1);
            }
            metagenoma.setTipoSecuenciacion(meta.get(11));
            metagenoma.setDescTipoSecuenciacion(meta.get(12));
            metagenoma.setEquipoSecuenciacion(meta.get(13));
            metagenoma.setCentro_secuenciacion(meta.get(14));
            metagenoma.setCantidad_dna(meta.get(15));
            metagenoma.setClean_up_kit(meta.get(16));
            metagenoma.setClean_up_method(meta.get(17));            
            metagenoma.setComentarios(meta.get(18));
            metagenoma.setEstacion(meta.get(26));
            int stats;
            try {
                stats = Integer.parseInt(meta.get(19));                
            } catch (NumberFormatException nfe) {
                stats = -1;
            }
            metagenoma.setIdStats(stats);
            StatsDAO sdao = new StatsDAO(transacciones);
            StatsObj s = sdao.initStats(stats);
            metagenoma.setStats(s);
            //EnsambleObj ensamble
            int statsAss;
            try {
                statsAss = Integer.parseInt(meta.get(20));
            } catch (NumberFormatException nfe) {
                statsAss = -1;
            }
            AssemblyDAO adao = new AssemblyDAO(transacciones);
            AssemblyObj ensamble = adao.initAssembly(statsAss);
            metagenoma.setEnsamble(ensamble);
            //EnsambleObj ensamble
            int idLib;
            try {
                idLib = Integer.parseInt(meta.get(21));
            } catch (NumberFormatException nfe) {
                idLib = -1;
            }
            LibraryDAO ldao = new LibraryDAO(transacciones);
            LibraryObj library = ldao.initLibrary(idLib);
            metagenoma.setLibreria(library);
            metagenoma.setCite(meta.get(22));
             try {
                metagenoma.setGen_num_total(Integer.parseInt(meta.get(23)));
            } catch (NumberFormatException nfe) {
                metagenoma.setGen_num_total(-1);
            }            
            if(meta.get(24).equals("1")){
                metagenoma.setIsTranscriptoma(true);
            }            
            metagenoma.setCondicionesTranscriptoma(meta.get(24));
            ArrayList<ArrayList<String>> ids = transacciones.getArchivosMetagenoma(""+idM);
            ArchivoDAO archivoDAO =  new ArchivoDAO(transacciones);
            if (ids != null) {
                for (ArrayList<String> id : ids) {
                    try {
                        ArchivoObj arch = archivoDAO.initArchivo(Integer.parseInt(id.get(0)));
                        if (arch != null) {
                            metagenoma.addArchivo(arch);
                        }
                    } catch (NumberFormatException nfe) {

                    }
                }
            }
        }
        return metagenoma;

    }
}
