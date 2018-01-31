/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.Transacciones;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class TaxaDAO {

    private Transacciones transacciones;
    private long conteos = 0;
    private boolean soloDegradadores = false;

    public TaxaDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public boolean isSoloDegradadores() {
        return soloDegradadores;
    }

    public void setSoloDegradadores(boolean soloDegradadores) {
        this.soloDegradadores = soloDegradadores;
    }

    public Transacciones getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public long getConteos() {
        return conteos;
    }

    public void setConteos(long conteos) {
        this.conteos = conteos;
    }

    /**
     * Este método se encarga de hacer la búsqueda de taxones de acuerdo a un
     * rango y su nombre.
     *
     * @param rank el rango del taxón: class,
     * @param name
     * @param taxID
     * @return regresa un ArrayList<ArrayList<String>> la cual contien e toda la
     * información para generar la tabla. Cada sublista es un registro y
     * contiene los siguientes datos en el mismo orden que se
     * presenta:idmarcador, mar_name,idmuestra, etiqueta, tipoMuestra,
     * tipoProfundidad, profundidad, numero de secs
     */
    public ArrayList<ArrayList<String>> getConteosByTaxName(String rank, String name, String analisis) {
        List<String> rangos = Arrays.asList("kingdom", "superkingdom", "subkingdom", "superphylum", "phylum", "subphylum", "superclass", "infraclass", "class", "subclass", "parvorder", "superorder", "infraorder", "order", "suborder", "superfamily", "family", "subfamily", "tribe", "subtribe", "genus", "subgenus", "species", "species group", "species subgroup", "subspecies", "forma", "varietas", "no rank");
        if (!rangos.contains(rank)) {
            return null;
        } else {
            if (rank.equals("superkingdom")) {
                rank = "kingdom";
            } else if (rank.equals("order")) {
                rank = "orden";
            } else if (rank.equals("species group")) {
                rank = "species_group";
            } else if (rank.equals("species subgroup")) {
                rank = "species_subgroup";
            } else if (rank.equals("no rank")) {
                rank = "no_rank";
            }
        }
        ArrayList<ArrayList<String>> tabla = new ArrayList();
        //ArrayList<ArrayList> marcadoresCounts = transacciones.getConteosMarcadorPorTaxon(rank, name);
        ArrayList<ArrayList> marcadoresCounts = transacciones.getConteosMarcadorPorTaxonOptimized(rank, name, analisis);
        /*if(rank.equals("no rank")){
         marcadoresCounts = transacciones.getConteosMarcadorPorTaxon(rank, name);
         }else{
         marcadoresCounts = transacciones.getConteosMarcadorPorTaxonNoRank();
         }*/
        conteos = 0;
        DecimalFormat df = new DecimalFormat("#.######");

        for (ArrayList<String> marcador : marcadoresCounts) {
            ArrayList<String> registro = new ArrayList();
            String idMarcador = marcador.get(0);
            int counts = Integer.parseInt(marcador.get(1));
            conteos += counts;
            registro.add(idMarcador);
            ArrayList<ArrayList> detalles = transacciones.getDetallesMuestraMarcador(idMarcador);
            if (detalles.size() > 0) {
                ArrayList<String> detalle = detalles.get(0);
                registro.add(detalle.get(0));//nombre marcador
                registro.add(detalle.get(1));//idMuestra
                registro.add(detalle.get(2));//etiqueta
                registro.add(detalle.get(3));//tipo_muestra
                registro.add(detalle.get(4));//tipo_prof
                registro.add(detalle.get(5));//profundidad
                registro.add("" + counts);
                int secsMarcador = transacciones.getSecuenciasByMarcador(idMarcador);
                registro.add("" + df.format(((double) counts / secsMarcador) * 100));
                //registros
            } else {
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
            }
            tabla.add(registro);
        }
        return tabla;
    }

    /**
     * *
     * Igual que getConteosByTaxName pero este es para shotguns
     *
     * @param rank ver especificaciones getConteosByTaxName
     * @param name ver especificaciones getConteosByTaxName
     * @return
     */
    public ArrayList<ArrayList<String>> getConteosByTaxNameShotgun(String rank, String name) {
        List<String> rangos = Arrays.asList("kingdom", "superkingdom", "subkingdom", "superphylum", "phylum", "subphylum", "superclass", "infraclass", "class", "subclass", "parvorder", "superorder", "infraorder", "order", "suborder", "superfamily", "family", "subfamily", "tribe", "subtribe", "genus", "subgenus", "species", "species group", "species subgroup", "subspecies", "forma", "varietas", "no rank");
        if (!rangos.contains(rank)) {
            return null;
        } else {
            if (rank.equals("superkingdom")) {
                rank = "kingdom";
            } else if (rank.equals("order")) {
                rank = "orden";
            } else if (rank.equals("species group")) {
                rank = "species_group";
            } else if (rank.equals("species subgroup")) {
                rank = "species_subgroup";
            } else if (rank.equals("no rank")) {
                rank = "no_rank";
            }
        }
        ArrayList<ArrayList<String>> tabla = new ArrayList();
        //ArrayList<ArrayList> marcadoresCounts = transacciones.getConteosMarcadorPorTaxon(rank, name);
        ArrayList<ArrayList> marcadoresCounts = transacciones.getConteosMetagenomaPorTaxonOptimized(rank, name);
        /*if(rank.equals("no rank")){
         marcadoresCounts = transacciones.getConteosMarcadorPorTaxon(rank, name);
         }else{
         marcadoresCounts = transacciones.getConteosMarcadorPorTaxonNoRank();
         }*/
        conteos = 0;
        DecimalFormat df = new DecimalFormat("#.######");

        for (ArrayList<String> marcador : marcadoresCounts) {
            ArrayList<String> registro = new ArrayList();
            String idMarcador = marcador.get(0);
            int counts = Integer.parseInt(marcador.get(1));
            conteos += counts;
            registro.add(idMarcador);
            ArrayList<ArrayList> detalles = transacciones.getDetallesMuestraMetagenomaa(idMarcador);
            if (detalles.size() > 0) {
                ArrayList<String> detalle = detalles.get(0);
                registro.add(detalle.get(0));//nombre marcador
                registro.add(detalle.get(1));//idMuestra
                registro.add(detalle.get(2));//etiqueta
                registro.add(detalle.get(3));//tipo_muestra
                registro.add(detalle.get(4));//tipo_prof
                registro.add(detalle.get(5));//profundidad
                registro.add("" + counts);
                int secsMetagenoma = transacciones.getSecuenciasByMetagenoma(idMarcador);
                registro.add("" + df.format(((double) counts / secsMetagenoma) * 100));
                //registros
            } else {
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
                registro.add("");
            }
            tabla.add(registro);
        }
        return tabla;
    }

    /**
     * Crea los archivos para el script de R que genera las gráficas
     *
     * @param tablaAbundancia. Es la tabla que se obtiene de
     * getConteosByTaxaName
     * @param dataDir el workingDir o directorio de datos donde se gaurdan los
     * archivos, se espera que venga con el ultimo "/"
     * @return un array list con la lista de los nombres de los archivos ya
     * creados. SOLO NOMBRE!
     */
    public ArrayList<String> createArchivosAbundancia(ArrayList<ArrayList<String>> tablaAbundancia, String dataDir, boolean includeNumCampania) {
        StringBuilder sed = new StringBuilder();
        StringBuilder max = new StringBuilder();
        StringBuilder min = new StringBuilder();
        StringBuilder mil = new StringBuilder();
        StringBuilder fon = new StringBuilder();
        ArrayList<String> archivos = new ArrayList();
        for (ArrayList<String> data : tablaAbundancia) {
            String estacion[] = getEstacionByMuestra(data.get(2));
            if (includeNumCampania) {
                String numCampania = "";
                try {
                    numCampania = data.get(3).split("-")[1];//<-etiqueta muestra eje: MMF-01-B05-ROSETA-FON.millipore; SOG-02-S63-NUCLEADOR-SED.nucleos
                    numCampania = "-" + Integer.parseInt(numCampania);
                } catch (Exception e) {
                    numCampania = "";
                }
                estacion[1] = estacion[1] + numCampania;
            }
            if (data.get(5).toLowerCase().contains("sed")) {
                sed.append(estacion[1]);//
                sed.append("\t").append(estacion[2]);
                sed.append("\t").append(estacion[3]);
                sed.append("\t").append(data.get(7));
                try {
                    double tmp = Double.parseDouble(data.get(8));
                    sed.append("\t").append(tmp).append("\n");
                } catch (NumberFormatException nfe) {
                    sed.append("\t").append(0.0001).append("\n");
                }
            } else if (data.get(5).toLowerCase().contains("max")) {
                max.append(estacion[1]);//
                max.append("\t").append(estacion[2]);
                max.append("\t").append(estacion[3]);
                max.append("\t").append(data.get(7));
                try {
                    double tmp = Double.parseDouble(data.get(8));
                    max.append("\t").append(tmp).append("\n");
                } catch (NumberFormatException nfe) {
                    max.append("\t").append(0.0001).append("\n");
                }
            } else if (data.get(5).toLowerCase().contains("min")) {
                min.append(estacion[1]);//
                min.append("\t").append(estacion[2]);
                min.append("\t").append(estacion[3]);
                min.append("\t").append(data.get(7));
                try {
                    double tmp = Double.parseDouble(data.get(8));
                    min.append("\t").append(tmp).append("\n");
                } catch (NumberFormatException nfe) {
                    min.append("\t").append(0.0001).append("\n");
                }
            } else if (data.get(5).toLowerCase().contains("mil")) {
                mil.append(estacion[1]);//                
                mil.append("\t").append(estacion[2]);
                mil.append("\t").append(estacion[3]);
                mil.append("\t").append(data.get(7));
                try {
                    double tmp = Double.parseDouble(data.get(8));
                    mil.append("\t").append(tmp).append("\n");
                } catch (NumberFormatException nfe) {
                    mil.append("\t").append(0.0001).append("\n");
                }
            } else if (data.get(5).toLowerCase().contains("fon")) {
                fon.append(estacion[1]);//
                fon.append("\t").append(estacion[2]);
                fon.append("\t").append(estacion[3]);
                fon.append("\t").append(data.get(7));
                try {
                    double tmp = Double.parseDouble(data.get(8));
                    fon.append("\t").append(tmp).append("\n");
                } catch (NumberFormatException nfe) {
                    fon.append("\t").append(0.0001).append("\n");
                }
            }
        }
        String time = ("" + System.currentTimeMillis()).substring(5);
        String fileName;
        if (sed.length() > 0) {
            fileName = dataDir + "SED_" + time + ".txt";
            if (writeFile(sed, fileName)) {
                archivos.add("SED_" + time + ".txt");
            }
        }
        if (min.length() > 0) {
            fileName = dataDir + "MIN_" + time + ".txt";
            if (writeFile(min, fileName)) {
                archivos.add("MIN_" + time + ".txt");
            }
        }
        if (max.length() > 0) {
            fileName = dataDir + "MAX_" + time + ".txt";
            if (writeFile(max, fileName)) {
                archivos.add("MAX_" + time + ".txt");
            }
        }
        if (fon.length() > 0) {
            fileName = dataDir + "FON_" + time + ".txt";
            if (writeFile(fon, fileName)) {
                archivos.add("FON_" + time + ".txt");
            }
        }
        if (mil.length() > 0) {
            fileName = dataDir + "MIL_" + time + ".txt";
            if (writeFile(mil, fileName)) {
                archivos.add("MIL_" + time + ".txt");
            }
        }
        return archivos;
    }

    /**
     * Se encarga de crear un archivo
     *
     * @param data los datos a escribir en el archivo
     * @param fileName el nombre del archivo donde se escriben
     * @return true si logra crearlo
     */
    public boolean writeFile(StringBuilder data, String fileName) {
        StringBuilder header = new StringBuilder("estacion\tlatitud\tlongitud\tsecuencias\tabundancia\n");
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(header.toString());
            writer.write(data.toString());
            writer.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(TaxaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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

    /**
     * Este método se encarga de generar los campos que tienen que ser
     * seleccionados en el query para armar la matriz en base al nivel de corte
     * seleccionado
     *
     * @param nivel el nivel de corte: kingdom, phylum, class, orden, etc...
     * @return parte de la sentencia SQL que va al query
     */
    public String getSQLFieldsForMatrixByLabel(String nivel, String sep) {
        if (nivel.toLowerCase().equals("kingdom")) {
            return " DISTINCT (kingdom)";
        } else if (nivel.toLowerCase().equals("phylum")) {
            return " DISTINCT (CONCAT(kingdom ,'" + sep + "', phylum ))";
        } else if (nivel.toLowerCase().equals("class")) {
            return " DISTINCT (CONCAT(kingdom ,'" + sep + "', phylum,'" + sep + "',class ))";
        } else if (nivel.toLowerCase().equals("orden")) {
            return " DISTINCT (CONCAT(kingdom ,'" + sep + "', phylum,'" + sep + "',class,'" + sep + "',orden))";
        } else if (nivel.toLowerCase().equals("family")) {
            return " DISTINCT (CONCAT(kingdom ,'" + sep + "', phylum,'" + sep + "',class,'" + sep + "',orden,'" + sep + "',family))";
        } else if (nivel.toLowerCase().equals("genus")) {
            return " DISTINCT (CONCAT(kingdom ,'" + sep + "', phylum,'" + sep + "',class,'" + sep + "',orden,'" + sep + "',family,'" + sep + "',genus))";
        } else if (nivel.toLowerCase().equals("species")) {
            return " DISTINCT (CONCAT(kingdom ,'" + sep + "', phylum,'" + sep + "',class,'" + sep + "',orden,'" + sep + "',family,'" + sep + "',genus,'" + sep + "',species))";
        } else {
            return "";
        }
    }

    /**
     * Crea una cabecera para los archivos que se generen como matrices de
     * abundancia
     *
     * @param nivel nivel de corte
     * @return
     */
    public String getFlatFieldsForMatrixByLabel(String nivel) {
        if (nivel.toLowerCase().equals("kingdom")) {
            return "Reino";
        } else if (nivel.toLowerCase().equals("phylum")) {
            return "Reino\tFilum";
        } else if (nivel.toLowerCase().equals("class")) {
            return "Reino\tFilum\tClase";
        } else if (nivel.toLowerCase().equals("orden")) {
            return "Reino\tFilum\tClase\tOrden";
        } else if (nivel.toLowerCase().equals("family")) {
            return "Reino\tFilum\tClase\tOrden\tFamilia";
        } else if (nivel.toLowerCase().equals("genus")) {
            return "Reino\tFilum\tClase\tOrden\tFamilia\tGenero";
        } else if (nivel.toLowerCase().equals("species")) {
            return "Reino\tFilum\tClase\tOrden\tFamilia\tGenero\tEspecie";
        } else {
            return "";
        }
    }

    /**
     * Creau Un arrayList de strings, donde cada elemento es un nivel
     * taxonómico, acorde al corte. Este método será útil si se decide imprimir
     * la matriz en la página web, ya que de aquí podrían salir las cabceras
     * para la tabla
     *
     * @param nivel nivel de corte
     * @return
     */
    public ArrayList<String> getHeaderForMatrixByLabel(String nivel) {
        ArrayList<String> niveles = new ArrayList<>();
        if (nivel.toLowerCase().equals("kingdom")) {
            niveles.add("Kingdom");
        } else if (nivel.toLowerCase().equals("phylum")) {
            niveles.add("Kingdom");
            niveles.add("Phylum");
        } else if (nivel.toLowerCase().equals("class")) {
            niveles.add("Kingdom");
            niveles.add("Phylum");
            niveles.add("Class");
        } else if (nivel.toLowerCase().equals("orden")) {
            niveles.add("Kingdom");
            niveles.add("Phylum");
            niveles.add("Class");
            niveles.add("Order");
        } else if (nivel.toLowerCase().equals("family")) {
            niveles.add("Kingdom");
            niveles.add("Phylum");
            niveles.add("Class");
            niveles.add("Order");
            niveles.add("Family");
        } else if (nivel.toLowerCase().equals("genus")) {
            niveles.add("Kingdom");
            niveles.add("Phylum");
            niveles.add("Class");
            niveles.add("Order");
            niveles.add("Family");
            niveles.add("Genus");
        } else if (nivel.toLowerCase().equals("species")) {
            niveles.add("Kingdom");
            niveles.add("Phylum");
            niveles.add("Class");
            niveles.add("Order");
            niveles.add("Family");
            niveles.add("Genus");
            niveles.add("Species");
        } else {
            return null;
        }
        return niveles;
    }

    /**
     * Este método es el método principal en la creación de la matriz de
     * abundancia de TOOOODOS los amplicones mas los filtros que se vayan
     * agregando en la página
     *
     * @param nivel nivel de taxonomía seleccionado por el usuario
     * @param trimName es por si se requiere el nombre completo del marcador
     * como ser: AMP-SED-SOG02-S39.1, AMP-MAX-MMF1-A1.1 o trimed, en cuyo caso
     * quita las prmeras tres letras y el guión
     * @param profundidades filtro para las profundidades, tipos seprados por
     * "-"
     * @param campanas filtro para las campanas, ids separados por "-"
     * @return
     */
    public String generaMatrizFiltrosHTML(String nivel, boolean trimName, String profundidades, String campanas, String provincias, String orgName, boolean norm, boolean toHTML) {
        String separator = "\t";
        String camposSQL = getSQLFieldsForMatrixByLabel(nivel, ";");
        StringBuilder matrix = new StringBuilder("");
        StringBuilder where = new StringBuilder(""); //este where es usado para los taxones
        StringBuilder whereMarcadores = new StringBuilder("");//este where es usado para los marcadores
        boolean isFirst = true;
        HashMap<String, Integer> conteosXmarcador = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.####");
        StringBuilder whereMuestras = new StringBuilder("");
        if (profundidades != null && profundidades.length() > 0) {
            String profs[] = profundidades.split("-");
            for (String prof : profs) {
                if (isFirst) {
                    whereMuestras.append(" tipo_profundidad IN('").append(prof.trim()).append("'");
                    isFirst = false;
                } else {
                    whereMuestras.append(", '").append(prof.trim()).append("'");
                }
            }
            whereMuestras.append(") ");
        }
        isFirst = true;
        if (campanas != null && campanas.length() > 0) {
            String camps[] = campanas.split("-");
            for (String camp : camps) {
                if (isFirst) {
                    if (whereMuestras.length() > 1) {
                        whereMuestras.append(" AND ");
                    }
                    whereMuestras.append(" idCampana IN(").append(camp.trim());
                    isFirst = false;
                } else {
                    whereMuestras.append(", ").append(camp.trim());
                }
            }
            whereMuestras.append(") ");
        }
        isFirst = true;
        if (provincias != null && provincias.length() > 0) {
            String provs[] = provincias.split("-");
            for (String prov : provs) {
                if (isFirst) {
                    if (whereMuestras.length() > 1) {
                        whereMuestras.append(" AND ");
                    }
                    whereMuestras.append(" provincia IN(").append(prov.trim());
                    isFirst = false;
                } else {
                    whereMuestras.append(", ").append(prov.trim());
                }
            }
            whereMuestras.append(") ");
        }
        if (whereMuestras.length() > 1) {
            isFirst = true;
            ArrayList<ArrayList> muestras = transacciones.getDistinctMuestras(whereMuestras.toString());
            if (muestras != null && muestras.size() > 0) {
                for (ArrayList<String> muestra : muestras) {
                    if (isFirst) {
                        where.append(" WHERE idMuestra IN(").append(muestra.get(0));
                        whereMarcadores.append(" INNER JOIN muestra ON muestra.idmuestra = marcador.idmuestra WHERE idMuestra IN(").append(muestra.get(0));
                        isFirst = false;
                    } else {
                        where.append(", ").append(muestra.get(0));
                        whereMarcadores.append(", ").append(muestra.get(0));
                    }
                }
                where.append(")");
                whereMarcadores.append(")");
            }
        }
        StringBuilder whereTaxones = new StringBuilder("");
        if (soloDegradadores) {
            if (where.length() > 1) {
                whereTaxones.append(" AND ");
            } else {
                whereTaxones.append(" WHERE ");
            }
            whereTaxones.append(" degradadora = 1 ");
        }
        ArrayList<ArrayList> taxones = transacciones.getDistinctTaxones(camposSQL, where.toString() + whereTaxones.toString());
        ArrayList<ArrayList> marcadores = transacciones.getMarcadores(where.toString());
        ArrayList<String> listaTaxones = new ArrayList<>();
        if (taxones != null && marcadores != null && taxones.size() > 0 && marcadores.size() > 0) {
            if (toHTML) {
                matrix.append("<thead><tr><th>Taxonomía</th>");
            } else {
                matrix.append("TAXONOMY");
            }
            for (ArrayList<String> marcador : marcadores) {
                String name = marcador.get(1);
                if (trimName) {
                    name = name.indexOf("-") != -1 ? name.substring(name.indexOf("-") + 1) : name;
                }
                if (toHTML) {
                    matrix.append("<th>").append(name).append("</th>");
                } else {
                    matrix.append(separator).append(name);
                }
            }
            if (toHTML) {
                matrix.append("</tr></thead><tbody id='taxobody'>");
            } else {
                matrix.append(System.getProperty("line.separator"));
            }

            for (ArrayList<String> taxon : taxones) {
                //String tax = taxon.get(0).replaceAll(";+",";");
                String taxonTmp = "";
                if (orgName.toLowerCase().equals("taxon")) {
                    String levels[] = taxon.get(0).split(";");
                    //taxonTmp = levels[levels.length - 1];
                    taxonTmp = levels.length > 0 ? levels[levels.length - 1] : taxon.get(0);
                } else if (orgName.toLowerCase().equals("filo")) {
                    taxonTmp = taxon.get(0);
                } else {//NCBI!
                    taxonTmp = taxon.get(1);
                }
                if (listaTaxones.contains(taxonTmp)) {
                    taxonTmp = taxonTmp.trim() + "_" + transacciones.getRankByTaxID(taxon.get(1));
                }
                listaTaxones.add(taxonTmp);
                if (toHTML) {
                    matrix.append("<tr style=\"text-align: left;\" class=\"meta\">");
                    matrix.append("<td>").append(taxonTmp).append("</td>");
                } else {
                    matrix.append(taxonTmp);//concatena todo el nombre                    
                }
                for (ArrayList<String> marcador : marcadores) {
                    int counts = transacciones.getTaxaCountByMarcador(marcador.get(0), taxon.get(1));
                    if (norm) {//normalizar los datos

                        if (conteosXmarcador.get(marcador.get(0)) == null) {
                            conteosXmarcador.put(marcador.get(0), new Integer(transacciones.getSecuenciasByMarcador(marcador.get(0))));
                        }
                        //double c = counts / transacciones.getSecuenciasByMarcador(marcador.get(0));
                        double val = ((double) counts / conteosXmarcador.get(marcador.get(0))) * 100;
                        if (toHTML) {
                            matrix.append("<td>").append(df.format(val)).append("</td>");
                        } else {
                            matrix.append(separator).append(df.format(val));
                        }
                    } else {
                        if (toHTML) {
                            matrix.append("<td>").append(counts).append("</td>");
                        } else {
                            matrix.append(separator).append(counts);
                        }
                    }
                }
                if (toHTML) {
                    matrix.append("</tr>");
                } else {
                    matrix.append(System.getProperty("line.separator"));
                }
            }
            matrix.append("</tbody>");
        }

        return matrix.toString();
    }

    public String generaMatrizFiltros(String nivel, boolean trimName, String profundidades, String campanas, String orgName, boolean norm) {
        String separator = "\t";
        String camposSQL = getSQLFieldsForMatrixByLabel(nivel, ";");
        StringBuilder matrix = new StringBuilder("");
        StringBuilder where = new StringBuilder(""); //este where es usado para los taxones
        StringBuilder whereMarcadores = new StringBuilder("");//este where es usado para los marcadores
        boolean isFirst = true;
        HashMap<String, Integer> conteosXmarcador = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.####");
        StringBuilder whereMuestras = new StringBuilder("");
        if (profundidades != null && profundidades.length() > 0) {
            String profs[] = profundidades.split("-");
            for (String prof : profs) {
                if (isFirst) {
                    whereMuestras.append(" tipo_profundidad IN('").append(prof.trim()).append("'");
                    isFirst = false;
                } else {
                    whereMuestras.append(", '").append(prof.trim()).append("'");
                }
            }
            whereMuestras.append(") ");
        }
        isFirst = true;
        if (campanas != null && campanas.length() > 0) {
            String camps[] = campanas.split("-");
            for (String camp : camps) {
                if (isFirst) {
                    if (whereMuestras.length() > 1) {
                        whereMuestras.append(" AND ");
                    }
                    whereMuestras.append(" idCampana IN(").append(camp.trim());
                    isFirst = false;
                } else {
                    whereMuestras.append(", ").append(camp.trim());
                }
            }
            whereMuestras.append(") ");
        }
        if (whereMuestras.length() > 1) {
            isFirst = true;
            ArrayList<ArrayList> muestras = transacciones.getDistinctMuestras(whereMuestras.toString());
            if (muestras != null && muestras.size() > 0) {
                for (ArrayList<String> muestra : muestras) {
                    if (isFirst) {
                        where.append(" WHERE idMuestra IN(").append(muestra.get(0));
                        whereMarcadores.append(" INNER JOIN muestra ON muestra.idmuestra = marcador.idmuestra WHERE idMuestra IN(").append(muestra.get(0));
                        isFirst = false;
                    } else {
                        where.append(", ").append(muestra.get(0));
                        whereMarcadores.append(", ").append(muestra.get(0));
                    }
                }
                where.append(")");
                whereMarcadores.append(")");
            }
        }
        ArrayList<ArrayList> taxones = transacciones.getDistinctTaxones(camposSQL, where.toString());
        ArrayList<ArrayList> marcadores = transacciones.getMarcadores(where.toString());
        ArrayList<String> listaTaxones = new ArrayList<>();
        if (taxones != null && marcadores != null && taxones.size() > 0 && marcadores.size() > 0) {
            matrix.append("TAXONOMY");
            for (ArrayList<String> marcador : marcadores) {
                String name = marcador.get(1);
                if (trimName) {
                    name = name.indexOf("-") != -1 ? name.substring(name.indexOf("-") + 1) : name;
                }
                matrix.append(separator).append(name);
            }
            matrix.append(System.getProperty("line.separator"));
            for (ArrayList<String> taxon : taxones) {
                //String tax = taxon.get(0).replaceAll(";+",";");
                String taxonTmp = "";
                if (orgName.toLowerCase().equals("taxon")) {
                    String levels[] = taxon.get(0).split(";");
                    //taxonTmp = levels[levels.length - 1];
                    taxonTmp = levels.length > 0 ? levels[levels.length - 1] : taxon.get(0);
                } else if (orgName.toLowerCase().equals("filo")) {
                    taxonTmp = taxon.get(0);
                } else {//NCBI!
                    taxonTmp = taxon.get(1);
                }
                if (listaTaxones.contains(taxonTmp)) {
                    taxonTmp = taxonTmp.trim() + "_" + transacciones.getRankByTaxID(taxon.get(1));
                }
                listaTaxones.add(taxonTmp);
                matrix.append(taxonTmp);//concatena todo el nombre                    
                for (ArrayList<String> marcador : marcadores) {
                    int counts = transacciones.getTaxaCountByMarcador(marcador.get(0), taxon.get(1));
                    if (norm) {//normalizar los datos

                        if (conteosXmarcador.get(marcador.get(0)) == null) {
                            conteosXmarcador.put(marcador.get(0), new Integer(transacciones.getSecuenciasByMarcador(marcador.get(0))));
                        }
                        //double c = counts / transacciones.getSecuenciasByMarcador(marcador.get(0));
                        double val = ((double) counts / conteosXmarcador.get(marcador.get(0))) * 100;
                        matrix.append(separator).append(df.format(val));
                    } else {
                        matrix.append(separator).append(counts);
                    }
                }
                matrix.append(System.getProperty("line.separator"));
            }
        }
        return matrix.toString();
    }

    public String generaMatrizFiltrosXtraPoledHTML(String nivel, boolean trimName, String profundidades, String campanas, String provincias, String orgName, boolean norm, boolean toHTML) {
        String separator = "\t";
        String camposSQL = getSQLFieldsForMatrixByLabel(nivel, ";");
        StringBuilder matrix = new StringBuilder("");
        StringBuilder where = new StringBuilder(""); //este where es usado para los taxones
        StringBuilder whereMarcadores = new StringBuilder("");//este where es usado para los marcadores
        boolean isFirst = true;
        HashMap<String, Integer> conteosXmarcador = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.####");
        StringBuilder whereMuestras = new StringBuilder("");
        if (profundidades != null && profundidades.length() > 0) {
            String profs[] = profundidades.split("-");
            for (String prof : profs) {
                if (isFirst) {
                    whereMuestras.append(" tipo_profundidad IN('").append(prof.trim()).append("'");
                    isFirst = false;
                } else {
                    whereMuestras.append(", '").append(prof.trim()).append("'");
                }
            }
            whereMuestras.append(") ");
        }
        isFirst = true;
        if (campanas != null && campanas.length() > 0) {
            String camps[] = campanas.split("-");
            for (String camp : camps) {
                if (isFirst) {
                    if (whereMuestras.length() > 1) {
                        whereMuestras.append(" AND ");
                    }
                    whereMuestras.append(" idCampana IN(").append(camp.trim());
                    isFirst = false;
                } else {
                    whereMuestras.append(", ").append(camp.trim());
                }
            }
            whereMuestras.append(") ");
        }
        isFirst = true;
        if (provincias != null && provincias.length() > 0) {
            String provs[] = provincias.split("-");
            for (String prov : provs) {
                if (isFirst) {
                    if (whereMuestras.length() > 1) {
                        whereMuestras.append(" AND ");
                    }
                    whereMuestras.append(" provincia IN(").append(prov.trim());
                    isFirst = false;
                } else {
                    whereMuestras.append(", ").append(prov.trim());
                }
            }
            whereMuestras.append(") ");
        }
        if (whereMuestras.length() > 1) {
            isFirst = true;
            ArrayList<ArrayList> muestras = transacciones.getDistinctMuestras(whereMuestras.toString());
            if (muestras != null && muestras.size() > 0) {
                for (ArrayList<String> muestra : muestras) {
                    if (isFirst) {
                        where.append(" WHERE idMuestra IN(").append(muestra.get(0));
                        whereMarcadores.append(" INNER JOIN muestra ON muestra.idmuestra = marcador.idmuestra WHERE idMuestra IN(").append(muestra.get(0));
                        isFirst = false;
                    } else {
                        where.append(", ").append(muestra.get(0));
                        whereMarcadores.append(", ").append(muestra.get(0));
                    }
                }
                where.append(")");
                whereMarcadores.append(")");
            }
        }

        StringBuilder whereTaxones = new StringBuilder("");
        if (soloDegradadores) {
            if (where.length() > 1) {
                whereTaxones.append(" AND ");
            } else {
                whereTaxones.append(" WHERE ");
            }
            whereTaxones.append(" degradadora = 1 ");
        }
        ArrayList<ArrayList> taxones = transacciones.getDistinctTaxones(camposSQL, where.toString() + whereTaxones);
        ArrayList<ArrayList> marcadores = transacciones.getMarcadores(where.toString());
        ArrayList<String> listaTaxones = new ArrayList<>();
        if (taxones != null && marcadores != null && taxones.size() > 0 && marcadores.size() > 0) {
            if (toHTML) {
                matrix.append("<thead><tr><th>Muestra</th>");
            } else {
                matrix.append("MUESTRA");
            }
            for (ArrayList<String> taxon : taxones) {
                //String tax = taxon.get(0).replaceAll(";+",";");
                String taxonTmp = "";
                if (orgName.toLowerCase().equals("taxon")) {
                    String levels[] = taxon.get(0).split(";");
                    taxonTmp = levels.length > 0 ? levels[levels.length - 1] : taxon.get(0);
                } else if (orgName.toLowerCase().equals("filo")) {
                    taxonTmp = taxon.get(0);
                } else {//NCBI!
                    taxonTmp = taxon.get(1);
                }
                if (listaTaxones.contains(taxonTmp)) {
                    taxonTmp = taxonTmp.trim() + "_" + transacciones.getRankByTaxID(taxon.get(1));
                }
                listaTaxones.add(taxonTmp);
                if (toHTML) {
                    matrix.append("<th>").append(taxonTmp).append("</th>");
                } else {
                    matrix.append(separator).append(taxonTmp);
                }
            }
            if (toHTML) {
                matrix.append("</tr><tbody id='taxobody'>");
            } else {
                matrix.append(System.getProperty("line.separator"));
            }

            for (ArrayList<String> marcador : marcadores) {
                String name = marcador.get(1);
                if (trimName) {
                    name = name.indexOf("-") != -1 ? name.substring(name.indexOf("-") + 1) : name;
                }
                if (toHTML) {
                    matrix.append("<tr style=\"text-align: left;\" class=\"meta\">");
                    matrix.append("<td>").append(name).append("</td>");
                } else {
                    matrix.append(name);//concatena todo el nombre                    
                }
                // matrix.append(name);

                for (ArrayList<String> taxon : taxones) {
                    int counts = transacciones.getTaxaCountByMarcador(marcador.get(0), taxon.get(1));
                    if (norm) {//normalizar los datos
                        if (conteosXmarcador.get(marcador.get(0)) == null) {
                            conteosXmarcador.put(marcador.get(0), new Integer(transacciones.getSecuenciasByMarcador(marcador.get(0))));
                        }
                        //double c = counts / transacciones.getSecuenciasByMarcador(marcador.get(0));
                        double val = ((double) counts / conteosXmarcador.get(marcador.get(0))) * 100;
                        if (toHTML) {
                            matrix.append("<td>").append(df.format(val)).append("</td>");
                        } else {
                            matrix.append(separator).append(df.format(val));
                        }

                    } else {
                        if (toHTML) {
                            matrix.append("<td>").append(df.format(counts)).append("</td>");
                        } else {
                            matrix.append(separator).append(df.format(counts));
                        }
                    }
                }
                if (toHTML) {
                    matrix.append("</tr>");
                } else {
                    matrix.append(System.getProperty("line.separator"));
                }
            }
            matrix.append("</tbody>");
        }
        return matrix.toString();
    }

    public String generaTablaDegradadores() {
        ArrayList<ArrayList> degradadores = transacciones.getDegradadorasByGenus();
        StringBuilder htmlTable = new StringBuilder("");
        if (degradadores != null) {
            htmlTable.append("<table class=\"table table-striped\" id=\"lista-organismo\">").append("<thead>").append("<tr align=\"center\">");
            htmlTable.append("<th>").append("Tax ID").append("</th>");
            htmlTable.append("<th>").append("Reino").append("</th>");
            htmlTable.append("<th>").append("Philum").append("</th>");
            htmlTable.append("<th>").append("Clase").append("</th>");
            htmlTable.append("<th>").append("Orden").append("</th>");
            htmlTable.append("<th>").append("Familia").append("</th>");
            htmlTable.append("<th>").append("Género").append("</th>");
            htmlTable.append("</tr>\n").append("<tbody>");
            for (ArrayList<String> organismo : degradadores) {
                htmlTable.append("<tr>");
                htmlTable.append("<td>").append(organismo.get(6)).append("</td>");
                htmlTable.append("<td>").append(organismo.get(0)).append("</td>");
                htmlTable.append("<td>").append(organismo.get(1)).append("</td>");
                htmlTable.append("<td>").append(organismo.get(2)).append("</td>");
                htmlTable.append("<td>").append(organismo.get(3)).append("</td>");
                htmlTable.append("<td>").append(organismo.get(4)).append("</td>");
                htmlTable.append("<td>").append(organismo.get(5)).append("</td>");
                htmlTable.append("</tr>");
            }
            htmlTable.append("</tbody>").append("</table>");
        }
        return htmlTable.toString();
    }

    /**
     *
     * @param nivel
     * @param idMarcadores
     * @param withHeader
     * @return
     */
    public String generaMatrizAbundancia(String nivel, String idsFuente, String orgName, boolean withHeader, boolean norm, boolean toFile, boolean isAmplicon, String idanalisis) {
        String camposSQL = getSQLFieldsForMatrixByLabel(nivel, "\t");
        String header = getFlatFieldsForMatrixByLabel(nivel);
        StringBuilder matrix = new StringBuilder("");
        ArrayList<ArrayList> matrix_data;
        StringBuilder whereTaxones = new StringBuilder("");
        if (soloDegradadores) {
            whereTaxones.append(" AND degradadora = 1 ");
        }
        if (isAmplicon) {
            matrix_data = transacciones.getMatrizPorMarcadoresNew(camposSQL, idsFuente, idanalisis, whereTaxones.toString());
            if (matrix_data == null || matrix_data.size() < 1) {
                if (idanalisis.equals("2")) {
                    idanalisis = "1";
                } else {
                    idanalisis = "2";
                }
                matrix_data = transacciones.getMatrizPorMarcadoresNew(camposSQL, idsFuente, idanalisis, whereTaxones.toString());
            }
        } else {
            matrix_data = transacciones.getMatrizPorMetagenoma(camposSQL, idsFuente, whereTaxones.toString());

        }
        ArrayList<String> listaTaxones = new ArrayList<>();
        if (matrix_data != null && !matrix_data.isEmpty()) {
            if (!toFile) {
                matrix.append("<thead><tr>");
                if (orgName.toLowerCase().equals("filo")) {
                    String levels[] = header.split("\t");
                    for (String level : levels) {
                        matrix.append("<th>").append(level).append("</th>");
                    }
                    matrix.append("<th>Abundancia</th></tr></thead><tbody id='taxobody'>");
                } else {
                    matrix.append("<th>Taxonomía</th><th>Abundancia</th></tr></thead><tbody id='taxobody'>");
                }
            } else {
                if (withHeader) {
                    matrix.append(header).append("\t").append("Abundancia").append("\n");
                }
            }
            /*  if (withHeader && orgName.toLowerCase().equals("filo")) {
             if (!toFile) {
             String levels[] = header.split("\t");
             for (String level : levels) {
             matrix.append("<th>").append(level).append("</th>");
             }
             matrix.append("<th>Abundancia</th></tr></thead><tbody id='taxobody'>");
             } else {
             matrix.append(header).append("\t").append("Abundancia").append("\n");
             }
             } else {
             if (!toFile) {
             matrix.append("<th>Taxonomía</th><th>Abundancia</th></tr></thead><tbody id='taxobody'>");
             }
             }*/
            //esto solo sirve xa un marcador, si se quiere implementar para multiples marcadores hay que cambiar o corregir el método
            int secuenciasMarcador = 1;
            DecimalFormat df = new DecimalFormat("#.######");
            if (norm) {
                if (isAmplicon) {
                    secuenciasMarcador = transacciones.getSecuenciasByMarcador(idsFuente);
                } else {
                    secuenciasMarcador = transacciones.getSecuenciasByMetagenoma(idsFuente);
                }
            }
            for (ArrayList<String> taxon : matrix_data) {
                if (taxon.get(0).trim().length() > 0) {
                    String taxonTmp = "";
                    if (orgName.toLowerCase().equals("taxon")) {
                        String levels[] = taxon.get(0).split("\t");
                        //taxonTmp = levels[levels.length - 1];
                        taxonTmp = levels.length > 0 ? levels[levels.length - 1] : taxon.get(0);
                    } else if (orgName.toLowerCase().equals("filo")) {
                        taxonTmp = taxon.get(0);
                    } else {//NCBI!
                        taxonTmp = taxon.get(2);
                    }

                    if (listaTaxones.contains(taxonTmp)) {
                        taxonTmp = taxonTmp.trim() + "_" + transacciones.getRankByTaxID(taxon.get(1));
                    }

                    listaTaxones.add(taxonTmp);
                    if (norm) {
                        try {
                            double val = Double.parseDouble(taxon.get(1));
                            //df.format(val/secuenciasMarcador);
                            if (!toFile) {
                                if (orgName.toLowerCase().equals("filo")) {
                                    StringTokenizer st = new StringTokenizer(taxonTmp, "\t", true);
                                    matrix.append("<tr style=\"text-align: left;\" class=\"meta\">");
                                    int i = 0;
                                    while (st.hasMoreTokens()) {
                                        String tok = st.nextToken();
                                        if (i % 2 == 0 && tok.equals("\t")) {
                                            matrix.append("<td>no_rank</td>");
                                            i++;
                                        } else if (i % 2 == 0 && !tok.equals("\t")) {
                                            matrix.append("<td>").append(tok).append("</td>");
                                        }
                                        i++;
                                    }
                                    if (taxonTmp.endsWith("\t")) {
                                        matrix.append("<td>no_rank</td>");
                                    }
                                    matrix.append("<td>").append(df.format((val / secuenciasMarcador) * 100)).append("</td></tr>");
                                } else {
                                    matrix.append("<tr style=\"text-align: left;\" class=\"meta\"><td>").append(taxonTmp).append("</td>").append("<td>").append(df.format((val / secuenciasMarcador) * 100)).append("</td></tr>");
                                }
                            } else {
                                matrix.append(taxonTmp).append("\t").append(df.format((val / secuenciasMarcador) * 100)).append("\n");
                            }
                        } catch (NumberFormatException nfe) {
                            System.err.println("Error al crear matriz de abundancia - parse double: " + taxon.get(1) + " taxon: " + taxon.get(0) + " ids: " + secuenciasMarcador);
                            matrix.append(taxonTmp).append("\t").append("0").append("\n");
                        }
                    } else {
                        if (!toFile) {
                            if (orgName.toLowerCase().equals("filo")) {
                                StringTokenizer st = new StringTokenizer(taxonTmp, "\t", true);
                                matrix.append("<tr style=\"text-align: left;\" class=\"meta\">");
                                int i = 0;
                                while (st.hasMoreTokens()) {
                                    String tok = st.nextToken();
                                    if (i % 2 == 0 && tok.equals("\t")) {
                                        matrix.append("<td>no_rank</td>");
                                        i++;
                                    } else if (i % 2 == 0 && !tok.equals("\t")) {
                                        matrix.append("<td>").append(tok).append("</td>");
                                    }
                                    i++;
                                }
                                if (taxonTmp.endsWith("\t")) {
                                    matrix.append("<td>no_rank</td>");
                                }
                                matrix.append("<td>").append(taxon.get(1)).append("</td></tr>");
                            } else {
                                matrix.append("<tr style=\"text-align: left;\" class=\"meta\"><td>").append(taxonTmp).append("</td>").append("<td>").append(taxon.get(1)).append("</td></tr>");
                            }
                        } else {
                            matrix.append(taxonTmp).append("\t").append(taxon.get(1)).append("\n");
                        }
                    }
                }
            }
            if (!toFile) {
                matrix.append("</tbody>");
            }
        }
        return matrix.toString();

    }
}
