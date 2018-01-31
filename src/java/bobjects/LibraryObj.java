/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bobjects;

/**
 *
 * @author Alejandro
 */
public class LibraryObj {
    private int idLibreria;
    private String fuente="";//library src GENOMIC, METAGENOMIC, TRANSCRIPTOMIC..
    private String seleccion = "";//library selection PCR, RANDOM, etc
    private String layout=""; //Configuración de la llibrería PAIRDE, SINGLE, etc
    private String vector="";//library vector
    private String screen=""; //library enriched, screening, etc
    private String metodo=""; //Descripcion o referencia al protocolo estandar para construcción de la librería

    public int getIdLibreria() {
        return idLibreria;
    }

    public void setIdLibreria(int idLibreria) {
        this.idLibreria = idLibreria;
    }

    public LibraryObj(int idLibreria) {
        this.idLibreria = idLibreria;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(String seleccion) {
        this.seleccion = seleccion;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
    
    
    
}
