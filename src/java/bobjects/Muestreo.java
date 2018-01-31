package bobjects;

import java.util.ArrayList;
import utils.MyCoord;
import utils.MyDate;

/**
 * Este objeto representa un muestreo, lance o arrastre realizado en alguna
 * estación en particular
 *
 * @author Alejandro
 */
public class Muestreo {

    //tipos de muestreos
    public static int M_AGUAS_SOMERAS = 1;
    public static int M_AGUAS_PROFUNDAS = 2;
    public static int M_SEDIMENTO = 3;
    public static int M_ZOOPLANCTON = 4;
    public static int M_FITOPLANCTON = 4;
    //tipos de muestras - matriz o env_packaage (MIGS)
    public static int MATRIZ_AGUA = 1;
    public static int MATRIZ_SEDIMENTO = 2;
    //TIPOS profundidades
    public static String PROF_MAX_F = "Max. Fluoresc-";
    public static String PROF_MIN_O = "Min. O2";
    public static String PROF_MIL_M = "1000 m";
    public static String PROF_FONDO = "Fondo";
    private int idMuestreo = 0;
    private String idCampana = "";//agregue
    private String idEstacion = "";//agregue
    private int idDerrotero = -1; //id del derrotero o idCE (Crucero Estación)    
    private int idTipoMuestreo = -1;
    private int idTipoMuestra = -1;
    private String etiqueta = "";
    private MyDate fechaInicial;
    private MyDate fechaFinal;
    private MyCoord latitud_estacion = new MyCoord("0");//agregue
    private MyCoord longitud_estacion = new MyCoord("0");//agregue
    private MyCoord latitud_r = new MyCoord("0");//real
    private MyCoord longitud_r = new MyCoord("0");//real
    private MyCoord latitud_a = new MyCoord("0");//ajustada --> requerimiento CIGOM preguntar a lex si las tenemos  // puede fungir como final
    private MyCoord longitud_a = new MyCoord("0");//ajustada --> requerimiento CIGOM preguntar a lex si las tenemos //puede fungir como final
    private String protocolo = "";
    private String comentarios = "";
    private String lance = "";
    private Term bioma;
    private Term env_feature;
    private Term env_material;
    private String tamano = "ND"; //cantidad y unidades
    private double profundidad = -1;
    private String tipo_prof = "";
    private ArrayList<Instrumento> instrumentos;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Muestra> muestras;
    private ArrayList<Medicion> mediciones;
    private double distanciaEstacion;//distancia que hay entre la estación y el lugar de la toma de muestra

    public Muestreo() {
        instrumentos = new ArrayList<Instrumento>();
        usuarios = new ArrayList<Usuario>();
        muestras = new ArrayList<Muestra>();
        mediciones = new ArrayList<Medicion>();
    }

    public double getDistanciaEstacion() {
        return distanciaEstacion;
    }

    public void setDistanciaEstacion(double distanciaEstacion) {
        this.distanciaEstacion = distanciaEstacion;
    }

    public Muestreo(int idMuestreo) {
        this.idMuestreo = idMuestreo;
        instrumentos = new ArrayList<Instrumento>();
        usuarios = new ArrayList<Usuario>();
        muestras = new ArrayList<Muestra>();
        mediciones = new ArrayList<Medicion>();
    }

    public void addNewInstrumento(Instrumento instrumento) {
        this.instrumentos.add(instrumento);
    }

    public void addNewUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public void addNewMuestra(Muestra muestra) {
        this.muestras.add(muestra);
    }

    public void addNewMedicion(Medicion medicion) {
        this.mediciones.add(medicion);
    }

    public ArrayList<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(ArrayList<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    public int getIdMuestreo() {
        return idMuestreo;
    }

    public void setIdMuestreo(int idMuestreo) {
        this.idMuestreo = idMuestreo;
    }

    public String getIdCampana() {
        return idCampana;
    }

    public void setIdCampana(String idCampana) {
        this.idCampana = idCampana;
    }

    public String getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(String idEstacion) {
        this.idEstacion = idEstacion;
    }

    public int getIdDerrotero() {
        return idDerrotero;
    }

    public void setIdDerrotero(int idDerrotero) {
        this.idDerrotero = idDerrotero;
    }

    public int getIdTipoMuestreo() {
        return idTipoMuestreo;
    }

    public void setIdTipoMuestreo(int idTipoMuestreo) {
        this.idTipoMuestreo = idTipoMuestreo;
    }

    public MyDate getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(MyDate fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

   

    public ArrayList<Muestra> getMuestras() {
        return muestras;
    }

    public void setMuestras(ArrayList<Muestra> muestras) {
        this.muestras = muestras;
    }

    public MyDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(MyDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public MyCoord getLatitud_r() {
        return latitud_r;
    }

    public void setLatitud_r(MyCoord latitud_r) {
        this.latitud_r = latitud_r;
    }

    public MyCoord getLongitud_r() {
        return longitud_r;
    }

    public void setLongitud_r(MyCoord logitud_r) {
        this.longitud_r = logitud_r;
    }

    public MyCoord getLatitud_estacion() {
        return latitud_estacion;
    }

    public void setLatitud_estacion(MyCoord latitud_estacion) {
        this.latitud_estacion = latitud_estacion;
    }

    public MyCoord getLongitud_estacion() {
        return longitud_estacion;
    }

    public void setLongitud_estacion(MyCoord longitud_estacion) {
        this.longitud_estacion = longitud_estacion;
    }   

    public MyCoord getLatitud_a() {
        return latitud_a;
    }

    public void setLatitud_a(MyCoord latitud_a) {
        this.latitud_a = latitud_a;
    }

    public MyCoord getLongitud_a() {
        return longitud_a;
    }

    public void setLongitud_a(MyCoord logitud_a) {
        this.longitud_a = logitud_a;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getLance() {
        return lance;
    }

    public void setLance(String lance) {
        this.lance = lance;
    }

    public int getIdTipoMuestra() {
        return idTipoMuestra;
    }

    public void setIdTipoMuestra(int idTipoMuestra) {
        this.idTipoMuestra = idTipoMuestra;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Term getBioma() {
        return bioma;
    }

    public void setBioma(Term bioma) {
        this.bioma = bioma;
    }

    public Term getEnv_feature() {
        return env_feature;
    }

    public void setEnv_feature(Term env_feature) {
        this.env_feature = env_feature;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public Term getEnv_material() {
        return env_material;
    }

    public void setEnv_material(Term env_material) {
        this.env_material = env_material;
    }

    public double getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(double profundidad) {
        this.profundidad = profundidad;
    }

    public String getTipo_prof() {
        return tipo_prof;
    }

    public void setTipo_prof(String tipo_prof) {
        this.tipo_prof = tipo_prof;
    }

    public ArrayList<Instrumento> getInstrumentos() {
        return instrumentos;
    }

    public void setInstrumentos(ArrayList<Instrumento> instrumentos) {
        this.instrumentos = instrumentos;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
