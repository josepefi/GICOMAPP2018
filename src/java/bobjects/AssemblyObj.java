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
public class AssemblyObj {
    int idEnsamble;
    String ensamblador="";
    String comentarios="";
    int contigs = -1;
    int longestContig = -1;
    double avgContig = -1;
    String n5090="";
    double lecturasMapeadas = -1;

    public AssemblyObj(int idEnsamble) {
        this.idEnsamble = idEnsamble;
    }

    public int getIdEnsamble() {
        return idEnsamble;
    }

    public void setIdEnsamble(int idEnsamble) {
        this.idEnsamble = idEnsamble;
    }

    public String getEnsamblador() {
        return ensamblador;
    }

    public void setEnsamblador(String ensamblador) {
        this.ensamblador = ensamblador;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getContigs() {
        return contigs;
    }

    public void setContigs(int contigs) {
        this.contigs = contigs;
    }

    public int getLongestContig() {
        return longestContig;
    }

    public void setLongestContig(int longestContig) {
        this.longestContig = longestContig;
    }

    public double getAvgContig() {
        return avgContig;
    }

    public void setAvgContig(double avgContig) {
        this.avgContig = avgContig;
    }

    public String getN5090() {
        return n5090;
    }

    public void setN5090(String n5090) {
        this.n5090 = n5090;
    }

    public double getLecturasMapeadas() {
        return lecturasMapeadas;
    }

    public void setLecturasMapeadas(double lecturasMapeadas) {
        this.lecturasMapeadas = lecturasMapeadas;
    }
    
    
    
    
}
