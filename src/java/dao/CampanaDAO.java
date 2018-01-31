/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.PuntoMapa;
import bobjects.RegistroResumen;
import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class CampanaDAO {

    private Transacciones transacciones;

    public CampanaDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Este método trae todas las campañas existenes ordenadas por fecha de
     * ejecución
     *
     * @return un arreglo bidimencional con los datos idcampana | siglas |
     * nombre
     */
    public ArrayList<ArrayList<String>> getCampanas() {
        ArrayList<ArrayList<String>> campanas = transacciones.getAllCampanas();
        return campanas;
    }

    public ArrayList<ArrayList<String>> getCampanasId(int idCampana) {
        ArrayList<ArrayList<String>> campanasid = transacciones.getAllCampanasId(idCampana);
        return campanasid;
    }

    /**
     * Trae el ID de la campaña mas reciente.
     *
     * @return
     */
    public int lastIDCampana() {
        return transacciones.getLastIDCampana();
    }

    /**
     * Este método se encarga de traer todos las estaciones muestreadas en una
     * campaña y en base a eso contruye un arreglo de PuntoMapa el cual es un
     * objeto que tiene los atributos necessarios para ser representado en
     * gmaps.
     *
     * @param idCampana La campaña en cuestión
     * @param puntosEstaciones si es true el query busca las coordenadas
     * mediante la estación de referencia, lo que garantiiza puntos únicos, pero
     * no es 100% atinado con el lugar exacto de la muestra. Si es false trae
     * los puntos de la muestra, lo que incluso puede generar estaciones
     * repetidas, ya que en una estación pueden existir mas de un lance y es
     * elancce puede tener diferentes coordenadas
     * @return el arreglo con todos los puntos, donde cada punto representa una
     * estacción.
     */
    public ArrayList<PuntoMapa> getEstacionesCampana(int idCampana, boolean puntosEstaciones) {
        ArrayList<PuntoMapa> estaciones = new ArrayList<>();
        ArrayList<ArrayList<String>> estacionesMuestreadas = null;
        if (puntosEstaciones) {
            estacionesMuestreadas = transacciones.getEstacionesMuestreadasFromCampana2Map(idCampana);
        } else {
            estacionesMuestreadas = transacciones.getPuntosMuestreadosFromCampana2Map(idCampana);
        }
        for (ArrayList<String> estacion : estacionesMuestreadas) {
            String idEstacion = estacion.get(0);
            String nombreEstacion = estacion.get(1);
            String lat_tmp = estacion.get(2);
            String lon_tmp = estacion.get(3);
            int idEstacionN;
            float lat;
            float lon;
            try {
                idEstacionN = Integer.parseInt(idEstacion);
                lat = Float.parseFloat(lat_tmp);
                lon = Float.parseFloat(lon_tmp);

                PuntoMapa punto = new PuntoMapa(nombreEstacion);
                punto.setLatitud(lat);
                punto.setLongitud(lon);
                int muestreosAgua = transacciones.getConteoMuestreosCampanaEstacionTipoMuestra(idCampana, idEstacionN, 1);
                int muestreosSedimentos = transacciones.getConteoMuestreosCampanaEstacionTipoMuestra(idCampana, idEstacionN, 2);
                if (muestreosAgua > 0 && muestreosSedimentos > 0) {
                    punto.setIcon(PuntoMapa.ICONO_AGUA_SED);
                } else if (muestreosAgua > 0) {
                    punto.setIcon(PuntoMapa.ICONO_AGUA);
                } else if (muestreosSedimentos > 0) {
                    punto.setIcon(PuntoMapa.ICONO_SEDIMENTO);
                } else {
                    punto.setIcon(PuntoMapa.ICONO_DEF);
                }
                estaciones.add(punto);
            } catch (NumberFormatException nfe) {
                System.err.println("Error parseando coordenadas o id campana: " + idCampana + " Estacion " + idEstacion + "lat: " + lat_tmp + "long: " + lon_tmp);
            }

        }
        return estaciones;
    }

    public ArrayList<ArrayList<String>> getResumenCampanaByProducto(int idCampana) {
        ArrayList<ArrayList<String>> resumen = new ArrayList<>();
        ArrayList<ArrayList<String>> resumenMarcadores = transacciones.getMarcadoresByCampana(idCampana);
        ArrayList<ArrayList<String>> resumenMetagenomas = transacciones.getMetagenomasByCampana(idCampana);
        ArrayList<ArrayList<String>> resumenGenomas = transacciones.getGenomasByCampana(idCampana);
        if (resumenMarcadores != null) {
            llenaResultadosProductosGenomicosByCampana(resumenMarcadores, resumen, "AMPLICONES");
        }
        if (resumenMetagenomas != null) {
            llenaResultadosProductosGenomicosByCampana(resumenMetagenomas, resumen, "METAGENOMA");
        }
        if (resumenGenomas != null) {
            llenaResultadosProductosGenomicosByCampana(resumenGenomas, resumen, "GENOMA");
        }
        return resumen;

    }

    /**
     * Este método se encarga de llenar la información resultante de los querys
     * para la tabla de resumen por producto genético. Se espera que cada
     * registro venga con estacion.idestacion, estacion_nombre, idproducto,
     * nombre_producto, muestra.idmuestra, muestra.etiqueta, tipo_profundidad
     * muestreo.profundidad
     *
     * @param resumen
     * @param registros
     * @param producto
     */
    public void llenaResultadosProductosGenomicosByCampana(ArrayList<ArrayList<String>> resumen, ArrayList<ArrayList<String>> registros, String producto) {

        for (ArrayList<String> elemento : resumen) {
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(elemento.get(0));//idestacion
            tmpArray.add(elemento.get(1));//nombre estacion
            if (producto.toUpperCase().equals("METAGENOMA")) {
                tmpArray.add("METAGENOMA");//producto
                tmpArray.add("showMetagenoma?idMetagenoma=" + elemento.get(2));//link
            } else if (producto.toUpperCase().equals("GENOMA")) {
                tmpArray.add("GENOMA");//producto
                tmpArray.add("showGenoma?idGenoma=" + elemento.get(2));//link
            } else if (producto.toUpperCase().equals("AMPLICONES")) {
                tmpArray.add("AMPLICON");//producto
                tmpArray.add("showMarcador?idMarcador=" + elemento.get(2));//link
            } else {
                tmpArray.add(producto);//producto
                tmpArray.add("");//link
            }
            tmpArray.add(elemento.get(3));//NOMBRE
            tmpArray.add(elemento.get(4));//IDMUESTRA
            tmpArray.add(elemento.get(5));//ETIQUETA MUESTRA
            tmpArray.add(elemento.get(6));//TIPO PROFUNDIDAD
            tmpArray.add(elemento.get(7));//PROFUNDIDA MUESTREO
            registros.add(tmpArray);
        }
    }

    /**
     * Este método se encarga de traer toda la información necesaria para llenar
     * la tabla de resumen de campaña en el menú de inicio.
     *
     * @param idCampana el id de la campaña de la que queremos el resumen
     * @return regresa un arreglo bidimencional (que representa a tabla) con las
     * columnas en el siguiente orden: idEstacion | nombreEstacion |
     * #muestras_agua | #muestras sedimento
     */
    public ArrayList<ArrayList<String>> getResumenCampana(int idCampana) {
        ArrayList<ArrayList<String>> estacionesMuestreadas = transacciones.getEstacionesMuestreadasFromCampana(idCampana);
        ArrayList<ArrayList<String>> resumen = new ArrayList<>();
        for (ArrayList<String> estacion : estacionesMuestreadas) {
            ArrayList<String> record = new ArrayList<>();
            String idEstacion = estacion.get(0);
            String nombreEstacion = estacion.get(1);
            record.add(idEstacion);
            record.add(nombreEstacion);
            int idEstacionN;
            try {
                idEstacionN = Integer.parseInt(idEstacion);
            } catch (NumberFormatException nfe) {
                idEstacionN = -1;
            }
            int muestreosAgua = transacciones.getConteoMuestreosCampanaEstacionTipoMuestra(idCampana, idEstacionN, 1);
            int muestreosSedimentos = transacciones.getConteoMuestreosCampanaEstacionTipoMuestra(idCampana, idEstacionN, 2);
            int metagenomas = transacciones.getConteoMetagenomasPorEstacionCampana(idCampana, idEstacionN);
            int amplicones = transacciones.getConteoAmpliconPorEstacionCampana(idCampana, idEstacionN);
            int genomas = transacciones.getConteoGenomaPorEstacionCampana(idCampana, idEstacionN);
            record.add("" + muestreosAgua);
            record.add("" + muestreosSedimentos);
            record.add("" + metagenomas);
            record.add("" + amplicones);
            record.add("" + genomas);
            resumen.add(record);
        }
        return resumen;

    }

    /**
     * Este método trae toda la información del resumen de campaña mostrada en
     * home del usuario, es similar al método getResumenCampana, pero este hace
     * uso de un objeto RegistroResumen, el cual trae información para presentar
     * la información de otra forma Es importante notar que hay un campo HC que
     * es el numero de elementos de secuencias, este es = a 3 y equivale a
     * amplicones, metagenomas y genomas para cada tipo de profundidad
     *
     * @param idCampana
     * @return
     */
    public ArrayList<RegistroResumen> getResumenCampanaRegistro(int idCampana) {
        ArrayList<ArrayList<String>> estacionesMuestreadas = transacciones.getEstacionesMuestreadasFromCampana(idCampana);
        ArrayList<RegistroResumen> resumen = new ArrayList<>();
        for (ArrayList<String> estacion : estacionesMuestreadas) {
            //ArrayList<String> record = new ArrayList<>();
            String idEstacion = estacion.get(0);
            String nombreEstacion = estacion.get(1);
            int idEstacionN;
            try {
                idEstacionN = Integer.parseInt(idEstacion);
            } catch (NumberFormatException nfe) {
                idEstacionN = -1;
            }
            //3 es el numero de elementos de secuencias para la tabla de cada celda por cada profundidad: amplicon, metagenoma, genomas
            RegistroResumen registro = new RegistroResumen(idEstacionN, 3);
            registro.setEstacion(nombreEstacion);
            String profundidades[] = {"MAX_F", "MIN_O", "MIL", "FONDO", "SED"};
            for (int i = 0; i < profundidades.length; i++) {
                int muestreo = transacciones.getConteoMuestreosCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]);
                if (i == 0) {
                    if (muestreo > 0) {
                        registro.setMaxF(true);
                        //amplicones metagenoma genoma
                        registro.setValueSecMax(0, transacciones.getConteoAmpliconesCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecMax(1, transacciones.getConteoMetagenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecMax(2, transacciones.getConteoGenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                    }
                } else if (i == 1) {
                    if (muestreo > 0) {
                        registro.setMinO(true);
                        //amplicones metagenoma genoma
                        registro.setValueSecMin(0, transacciones.getConteoAmpliconesCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecMin(1, transacciones.getConteoMetagenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecMin(2, transacciones.getConteoGenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                    }
                } else if (i == 2) {
                    if (muestreo > 0) {
                        registro.setMil(true);
                        //amplicones metagenoma genoma
                        registro.setValueSecMil(0, transacciones.getConteoAmpliconesCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecMil(1, transacciones.getConteoMetagenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecMil(2, transacciones.getConteoGenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                    }
                } else if (i == 3) {
                    if (muestreo > 0) {
                        registro.setFondo(true);
                        //amplicones metagenoma genoma
                        registro.setValueSecFondo(0, transacciones.getConteoAmpliconesCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecFondo(1, transacciones.getConteoMetagenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecFondo(2, transacciones.getConteoGenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                    }
                } else if (i == 4) {
                    if (muestreo > 0) {
                        registro.setSedimento(true);
                        //amplicones metagenoma genoma
                        registro.setValueSecSedimento(0, transacciones.getConteoAmpliconesCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecSedimento(1, transacciones.getConteoMetagenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                        registro.setValueSecSedimento(2, transacciones.getConteoGenomasCampanaEstacionTipoProfundidad(idCampana, idEstacionN, profundidades[i]));
                    }
                }
            }

            resumen.add(registro);
        }
        return resumen;

    }
}
