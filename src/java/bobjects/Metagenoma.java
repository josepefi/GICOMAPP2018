/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobjects;

import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class Metagenoma {

    private int idmetagenoma;
    //datos de la muestra
    private int idmuestra;
    private String etiquetaMuestra = "";
    private double latitud;
    private double longitud;
    private String profundidad = "";
    private String estacion = "";
    private String tipoMuestra = "";
    //secuenciacion
    private int idtipo_secuenciacion = -1;
    private String tipoSecuenciacion = "";
    private String descTipoSecuenciacion = "";
    //secuenciador
    private int idSecuenciador;
    private String equipoSecuenciacion = "";//
    String centro_secuenciacion = "";
    private String name = "";//nombre metagenoma
    private String desc = "";//descripcion  metagenoma
    private String comentarios = "";
    //libreria    
    LibraryObj libreria;
    String cite="";
    private int gen_num_total;
    private String cantidad_dna = "";
    private String cultivo = "";
    String raw_data_path = "";
    String proc_data_path = "";
    String analisis = "";
    String clean_up_kit = "";
    String clean_up_method = "";
    String procesamiento = ""; //procesamiento de la muestra (después de obtenida)
    int idStats;
    StatsObj stats;
    boolean visible = true;
   
    private AssemblyObj ensamble;
    ArrayList<ArchivoObj> archivos = new ArrayList<>();
    boolean isTranscriptoma = false;
    String condicionesTranscriptoma = "";

    public String getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(String tipoMuestra) {
        
        this.tipoMuestra = tipoMuestra;
        if(etiquetaMuestra.contains("-MIL")){
            tipoMuestra += " mil metros";
        }else if(etiquetaMuestra.contains("-MAX")){
            tipoMuestra += " máximo fluorescencia";
        }else if(etiquetaMuestra.contains("-FON")){
            tipoMuestra += " fondo";
        }else if(etiquetaMuestra.contains("-MIN")){
            tipoMuestra += " mínimo de oxígeno";
        }
    }

    public StatsObj getStats() {
        return stats;
    }

    public ArrayList<ArchivoObj> getArchivos() {
        return archivos;
    }

    public void setArchivos(ArrayList<ArchivoObj> archivos) {
        this.archivos = archivos;
    }

    public void addArchivo(ArchivoObj archivo) {
        archivos.add(archivo);
    }

    public AssemblyObj getEnsamble() {
        return ensamble;
    }

    public void setEnsamble(AssemblyObj ensamble) {
        this.ensamble = ensamble;
    }

    public void setStats(StatsObj stats) {
        this.stats = stats;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

   
    public String getTipoSecuenciacion() {
        return tipoSecuenciacion;
    }

    public void setTipoSecuenciacion(String tipoSecuenciacion) {
        this.tipoSecuenciacion = tipoSecuenciacion;
    }

    public String getDescTipoSecuenciacion() {
        return descTipoSecuenciacion;
    }

    public void setDescTipoSecuenciacion(String descTipoSecuenciacion) {
        this.descTipoSecuenciacion = descTipoSecuenciacion;
    }

    public String getEquipoSecuenciacion() {
        return equipoSecuenciacion;
    }

    public void setEquipoSecuenciacion(String equipoSecuenciacion) {
        this.equipoSecuenciacion = equipoSecuenciacion;
    }

   
    public String getCultivo() {
        return cultivo;
    }

    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    
    public String getEtiquetaMuestra() {
        return etiquetaMuestra;
    }

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }

    public void setEtiquetaMuestra(String etiquetaMuestra) {
        this.etiquetaMuestra = etiquetaMuestra;
    }

    public Metagenoma(int idmetagenoma) {
        this.idmetagenoma = idmetagenoma;
    }

    public int getIdmetagenoma() {
        return idmetagenoma;
    }

    public void setIdmetagenoma(int idmetagenoma) {
        this.idmetagenoma = idmetagenoma;
    }

    public int getIdmuestra() {
        return idmuestra;
    }

    public void setIdmuestra(int idmuestra) {
        this.idmuestra = idmuestra;
    }

    public int getIdtipo_secuenciacion() {
        return idtipo_secuenciacion;
    }

    public void setIdtipo_secuenciacion(int idtipo_secuenciacion) {
        this.idtipo_secuenciacion = idtipo_secuenciacion;
    }

    public int getIdSecuenciador() {
        return idSecuenciador;
    }

    public void setIdSecuenciador(int idSecuenciador) {
        this.idSecuenciador = idSecuenciador;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getCentro_secuenciacion() {
        return centro_secuenciacion;
    }

    public void setCentro_secuenciacion(String centro_secuenciacion) {
        this.centro_secuenciacion = centro_secuenciacion;
    }

    public LibraryObj getLibreria() {
        return libreria;
    }

    public void setLibreria(LibraryObj libreria) {
        this.libreria = libreria;
    }

    public int getGen_num_total() {
        return gen_num_total;
    }

    public void setGen_num_total(int gen_num_total) {
        this.gen_num_total = gen_num_total;
    }

    public String getRaw_data_path() {
        return raw_data_path;
    }

    public void setRaw_data_path(String raw_data_path) {
        this.raw_data_path = raw_data_path;
    }

    public String getProc_data_path() {
        return proc_data_path;
    }

    public void setProc_data_path(String proc_data_path) {
        this.proc_data_path = proc_data_path;
    }

    public String getAnalisis() {
        return analisis;
    }

    public void setAnalisis(String analisis) {
        this.analisis = analisis;
    }

    public String getClean_up_kit() {
        return clean_up_kit;
    }

    public void setClean_up_kit(String clean_up_kit) {
        this.clean_up_kit = clean_up_kit;
    }

    public String getClean_up_method() {
        return clean_up_method;
    }

    public void setClean_up_method(String clean_up_method) {
        this.clean_up_method = clean_up_method;
    }

    public String getProcesamiento() {
        return procesamiento;
    }

    public void setProcesamiento(String procesamiento) {
        this.procesamiento = procesamiento;
    }

    public int getIdStats() {
        return idStats;
    }

    public void setIdStats(int idStats) {
        this.idStats = idStats;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isTranscriptoma() {
        return isTranscriptoma;
    }

    public void setIsTranscriptoma(boolean isTranscriptoma) {
        this.isTranscriptoma = isTranscriptoma;
    }

    public String getCondicionesTranscriptoma() {
        return condicionesTranscriptoma;
    }

    public void setCondicionesTranscriptoma(String condicionesTranscriptoma) {
        this.condicionesTranscriptoma = condicionesTranscriptoma;
    }

   
   

    public String getCantidad_dna() {
        return cantidad_dna;
    }

    public void setCantidad_dna(String cantidad_dna) {
        this.cantidad_dna = cantidad_dna;
    }

}
