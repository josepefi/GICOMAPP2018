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
public class Genoma {

    private int idgenoma;
    private String etiquetaMuestra = "";
    private int idmuestra;
    private String tax_id = "";
    private int idtipo_secuenciacion;
    private int idSecuenciador;
    
    private String profundidad = "";
    private String estacion = "";
    private String tipoMuestra = "";
    String centro_secuenciacion = "";
    private String tipoSecuenciacion = "";
    private String descTipoSecuenciacion = "";
    private String equipoSecuenciacion = "";
    private NCBINode tax;
    private String name = "";//nombre del genoma
    private String desc = "";//desc del genoma
    private String strain = "";//cepa
    private String crecimiento = "";//
    private int replicones = 1;
    private String referencia = "";//cite en la bd
    private String comentarios = "";
    //libreria    
    private LibraryObj libreria;
    /* private String library_selection;
     private String library_layout;
     private String library_screen;
     */
    private double latitud;
    private double longitud;
    private String cantidad_dna = "";
    private String clean_up_method = "";
    private String clean_up_kit = "";
    private String ref_anot = "";//organismo de referencia contra el que se anotó/mapeó 
    private String finishing_strategy;
    private String version;
    private String procesamiento = "";
    private String analisis = "";
    private int gen_num_total;
    private StatsObj stats;
    private AssemblyObj ensamble;
    ArrayList<ArchivoObj> archivos = new ArrayList<>();
    boolean isTranscriptoma = false;
    String condicionesTranscriptoma = "";
    String respaldo_org;//donde se encuentra la cepa aislada.

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getCentro_secuenciacion() {
        return centro_secuenciacion;
    }

    public void setCentro_secuenciacion(String centro_secuenciacion) {
        this.centro_secuenciacion = centro_secuenciacion;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(String tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public AssemblyObj getEnsamble() {
        return ensamble;
    }

    public String getAnalisis() {
        return analisis;
    }

    public void setAnalisis(String analisis) {
        this.analisis = analisis;
    }

    public void setEnsamble(AssemblyObj ensamble) {
        this.ensamble = ensamble;
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

    public LibraryObj getLibreria() {
        return libreria;
    }

    public void setLibreria(LibraryObj libreria) {
        this.libreria = libreria;
    }

    public String getClean_up_method() {
        return clean_up_method;
    }

    public void setClean_up_method(String clean_up_method) {
        this.clean_up_method = clean_up_method;
    }

    public String getClean_up_kit() {
        return clean_up_kit;
    }

    public void setClean_up_kit(String clean_up_kit) {
        this.clean_up_kit = clean_up_kit;
    }

    public String getRef_anot() {
        return ref_anot;
    }

    public void setRef_anot(String ref_anot) {
        this.ref_anot = ref_anot;
    }

    public String getProcesamiento() {
        return procesamiento;
    }

    public void setProcesamiento(String procesamiento) {
        this.procesamiento = procesamiento;
    }

    public int getGen_num_total() {
        return gen_num_total;
    }

    public void setGen_num_total(int gen_num_total) {
        this.gen_num_total = gen_num_total;
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

    public String getRespaldo_org() {
        return respaldo_org;
    }

    public void setRespaldo_org(String respaldo_org) {
        this.respaldo_org = respaldo_org;
    }

    public StatsObj getStats() {
        return stats;
    }

    public void setStats(StatsObj stats) {
        this.stats = stats;
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

    public String getCantidad_dna() {
        return cantidad_dna;
    }

    public void setCantidad_dna(String cantidad_dna) {
        this.cantidad_dna = cantidad_dna;
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

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getIdgenoma() {
        return idgenoma;
    }

    public Genoma(int idgenoma) {
        this.idgenoma = idgenoma;
    }

    public String getEtiquetaMuestra() {
        return etiquetaMuestra;
    }

    public void setEtiquetaMuestra(String etiquetaMuestra) {
        this.etiquetaMuestra = etiquetaMuestra;
    }

    public Genoma() {
    }

    public void setIdgenoma(int idgenoma) {
        this.idgenoma = idgenoma;
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

    public NCBINode getTax() {
        return tax;
    }

    public void setTax(NCBINode tax) {
        this.tax = tax;
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

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getCrecimiento() {
        return crecimiento;
    }

    public void setCrecimiento(String crecimiento) {
        this.crecimiento = crecimiento;
    }

    public int getReplicones() {
        return replicones;
    }

    public void setReplicones(int replicones) {
        this.replicones = replicones;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getFinishing_strategy() {
        return finishing_strategy;
    }

    public void setFinishing_strategy(String finishing_strategy) {
        this.finishing_strategy = finishing_strategy;
    }

}
