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
public class Muestra {

    private int idMuestra = 0;//es auto increment en la BD
    private int idMuestreo = -1;
    private double profundidad = -1;
    private String etiqueta = "";
    private String contenedor="";//isolation growth conditions
    private String process=""; //samp_mat_process
    private String protocolo="";//
    private String notas=""; //comentarios
    private String samp_size="ND";//
    private String real_to_oxy="";//
    private String contenedor_temp="";//
    private ArrayList<Medicion> mediciones;
    private ArrayList<Usuario> usuarios;

    public Muestra(int idMuestra) {
        this.idMuestra = idMuestra;
    }

    public String getSamp_size() {
        return samp_size;
    }

    public void setSamp_size(String samp_size) {
        this.samp_size = samp_size;
    }
    
    public String getprotocolo() {
        return protocolo;
    }

    public void setprotocolo(String protocolo) {
        this.protocolo = protocolo;
    }
    
    public String getreal_to_oxy() {
        return real_to_oxy;
    }

    public void setreal_to_oxy(String real_to_oxy) {
        this.real_to_oxy = real_to_oxy;
    }
    public String getcontenedor_temp() {
        return contenedor_temp;
    }

    public void setcontenedor_temp(String contenedor_temp) {
        this.contenedor_temp = contenedor_temp;
    }

    public Muestra(int idMuestra, int idMuestreo) {
        this.idMuestreo = idMuestreo;
        this.idMuestra = idMuestra;
    }

    public int getIdMuestra() {
        return idMuestra;
    }

    public void setIdMuestra(int idMuestra) {
        this.idMuestra = idMuestra;
    }

    public int getIdMuestreo() {
        return idMuestreo;
    }

    public void setIdMuestreo(int idMuestreo) {
        this.idMuestreo = idMuestreo;
    }

    public double getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(double profundidad) {
        this.profundidad = profundidad;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getContenedor() {
        return contenedor;
    }

    public void setContenedor(String contenedor) {
        this.contenedor = contenedor;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public ArrayList<Medicion> getMediciones() {
        return mediciones;
    }

    public void setMediciones(ArrayList<Medicion> mediciones) {
        this.mediciones = mediciones;
    }

    public void addMedecion(Medicion medicion) {
        this.mediciones.add(medicion);
    }

    public void addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
