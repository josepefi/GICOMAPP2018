/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.ArchivoObj;
import bobjects.AssemblyObj;
import bobjects.Genoma;
import bobjects.LibraryObj;
import bobjects.NCBINode;
import bobjects.StatsObj;
import java.util.ArrayList;
import database.Transacciones;

/**
 *
 * @author Alejandro
 */
public class GenomaDAO {
    
    private Transacciones transacciones;
    
    public GenomaDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Método para inicializar un metagenoma
     *
     * @param idG el id del genoma a inicializar
     * @return
     */
    public Genoma initGenoma(int idG) {
        Genoma genoma = new Genoma(idG);
        ArrayList<ArrayList> genomaAL = transacciones.getGenoma("" + idG);
        if (genomaAL != null && genomaAL.size() > 0) {
            ArrayList<String> geno = genomaAL.get(0);            
            try {
                genoma.setIdmuestra(Integer.parseInt(geno.get(0)));
                genoma.setEtiquetaMuestra(geno.get(1));
            } catch (NumberFormatException nfe) {
                genoma.setIdmuestra(-1);
                genoma.setEtiquetaMuestra("Muestra desconocida");
            }
            genoma.setName(geno.get(2));
            genoma.setDesc(geno.get(3));
            genoma.setTax_id(geno.get(4));
            
            ArrayList<ArrayList> linaje = transacciones.getLinaje(geno.get(4));
            
            if (linaje != null && linaje.size() > 0) {
                NCBINode ncbi = null; //= new NCBINode(geno.get(4));
                int i = 0;
                ArrayList<String> rec = linaje.get(0);
                for (String val : rec) {
                    if (val != null) {
                        switch (i) {
                            case 0:
                                ncbi = new NCBINode(val);
                                break;
                            case 1:
                                ncbi.setKingdom(val);
                                break;                            
                            case 2:
                                ncbi.setPhylum(val);
                                break;
                            case 3:
                                ncbi.setClas(val);
                                break;
                            case 4:
                                ncbi.setOrden(val);
                                break;
                            case 5:
                                ncbi.setFamily(val);
                                break;
                            case 6:
                                ncbi.setGenus(val);
                                break;
                            case 7:
                                ncbi.setSpecies(val);
                                break;
                            case 8:
                                ncbi.setSubspecies(val);
                                break;
                        }
                    }
                    i++;
                }
                genoma.setTax(ncbi);
            }
            genoma.setStrain(geno.get(5));
            genoma.setCrecimiento(geno.get(6));
            genoma.setVersion(geno.get(7));
            try {
                genoma.setLatitud(Double.parseDouble(geno.get(8)));
            } catch (NumberFormatException nfe) {
                genoma.setLatitud(-1);
            }
            try {
                genoma.setLongitud(Double.parseDouble(geno.get(9)));
            } catch (NumberFormatException nfe) {
                genoma.setLongitud(-1);
            }
            genoma.setCantidad_dna(geno.get(10));
            genoma.setClean_up_kit(geno.get(11));
            genoma.setClean_up_method(geno.get(12));
            genoma.setAnalisis(geno.get(13));
            genoma.setRef_anot(geno.get(14));
            genoma.setFinishing_strategy(geno.get(15));
            genoma.setProcesamiento(geno.get(16));
            genoma.setReferencia(geno.get(17));
            try {
                genoma.setGen_num_total(Integer.parseInt(geno.get(18)));
            } catch (NumberFormatException nfe) {
                genoma.setGen_num_total(-1);
            }
            genoma.setTipoSecuenciacion(geno.get(19));
            genoma.setDescTipoSecuenciacion(geno.get(20));
            genoma.setEquipoSecuenciacion(geno.get(21));
            genoma.setComentarios(geno.get(22));
            genoma.setRespaldo_org(geno.get(23));
            //estadísticas
            int stats;
            try {
                stats = Integer.parseInt(geno.get(24));
            } catch (NumberFormatException nfe) {
                stats = -1;
            }
            StatsDAO sdao = new StatsDAO(transacciones);
            StatsObj s = sdao.initStats(stats);
            genoma.setStats(s);
            //libreria
            int lib;
            try {
                lib = Integer.parseInt(geno.get(25));
            } catch (NumberFormatException nfe) {
                lib = -1;
            }
            LibraryDAO ldao = new LibraryDAO(transacciones);
            LibraryObj libreria = ldao.initLibrary(lib);
            genoma.setLibreria(libreria);
            //EnsambleObj ensamble
            int statsAss;
            try {
                statsAss = Integer.parseInt(geno.get(26));
            } catch (NumberFormatException nfe) {
                statsAss = -1;
            }
            AssemblyDAO adao = new AssemblyDAO(transacciones);
            AssemblyObj ensamble = adao.initAssembly(statsAss);
            genoma.setEnsamble(ensamble);
            
            if (geno.get(27).equals("1")) {
                genoma.setIsTranscriptoma(true);
            }            
            genoma.setCondicionesTranscriptoma(geno.get(28));
            genoma.setEstacion(geno.get(29));
            genoma.setProfundidad(geno.get(30));
            genoma.setTipoMuestra(geno.get(31));
            genoma.setCentro_secuenciacion(geno.get(32));
            ArrayList<ArrayList<String>> ids = transacciones.getArchivosGenoma("" + idG);
            ArchivoDAO archivoDAO = new ArchivoDAO(transacciones);
            if (ids != null) {
                for (ArrayList<String> id : ids) {
                    try {
                        ArchivoObj arch = archivoDAO.initArchivo(Integer.parseInt(id.get(0)));
                        if (arch != null) {
                            genoma.addArchivo(arch);
                        }
                    } catch (NumberFormatException nfe) {
                        
                    }
                }
            }
            
        }        
        return genoma;        
    }

    /**
     * Este método trae la información de los genomas a ser desplegada en la
     * página de Blast
     *
     * @param where
     * @return
     */
    public ArrayList<ArrayList<String>> getGenomasBlast(String where) {
        ArrayList<ArrayList<String>> genomas = transacciones.getGenomasBlast(where);
        if (genomas != null && !genomas.isEmpty()) {
            return genomas;
        } else {
            return null;
        }
    }
    
    public ArrayList<Genoma> getGenomas(String filtro) {
        ArrayList<ArrayList<String>> genos = transacciones.getGenomas(filtro);
        ArrayList<Genoma> genomas = new ArrayList<>();
        if (genos != null && !genos.isEmpty()) {
            for (ArrayList<String> genoma : genos) {
                Genoma g = new Genoma();
                g.setIdgenoma(Integer.parseInt(genoma.get(0)));
                g.setIdmuestra(Integer.parseInt(genoma.get(1)));
                g.setIdtipo_secuenciacion(Integer.parseInt(genoma.get(2)));
                g.setIdSecuenciador(Integer.parseInt(genoma.get(3)));
                g.setTax(new NCBINode(genoma.get(4)));
                g.setName(genoma.get(5));
                g.setDesc(genoma.get(6));
                g.setStrain(genoma.get(7));
                g.setCrecimiento(genoma.get(8));
                //a seguir implementando
            }
            return genomas;
        } else {
            return null;
        }
    }
    
}
