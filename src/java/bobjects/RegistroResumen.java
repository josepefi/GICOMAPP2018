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
public class RegistroResumen {

    private String estacion;//nobre de la estacion
    private int idEstacion;
    private boolean maxF = false;
    private boolean minO = false;
    private boolean mil = false;
    private boolean fondo = false;
    private boolean sedimento = false;
    private int[] secuenciasMax;
    private int[] secuenciasMin;
    private int[] secuenciasMil;
    private int[] secuenciasFondo;
    private int[] secuenciasSedimento;

    public RegistroResumen(int idEstacion, int secNum) {
        this.idEstacion = idEstacion;
        secuenciasMax = new int[secNum];
        secuenciasMin = new int[secNum];
        secuenciasMil = new int[secNum];
        secuenciasFondo = new int[secNum];
        secuenciasSedimento = new int[secNum];
        for (int i = 0; i < secNum; i++) {
            secuenciasMax[i] = 0;
            secuenciasMin[i] = 0;
            secuenciasMil[i] = 0;
            secuenciasFondo[i] = 0;
            secuenciasSedimento[i] = 0;
        }
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public int getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(int idEstacion) {
        this.idEstacion = idEstacion;
    }

    public int[] getSecuenciasMin() {
        return secuenciasMin;
    }

    public void setSecuenciasMin(int[] secuenciasMin) {
        this.secuenciasMin = secuenciasMin;
    }

    public int[] getSecuenciasMil() {
        return secuenciasMil;
    }

    public void setSecuenciasMil(int[] secuenciasMil) {
        this.secuenciasMil = secuenciasMil;
    }

    public void setValueSecMax(int i, int val) {
        secuenciasMax[i] = val;
    }

    public void setValueSecMin(int i, int val) {
        secuenciasMin[i] = val;
    }

    public void setValueSecMil(int i, int val) {
        secuenciasMil[i] = val;
    }

    public void setValueSecFondo(int i, int val) {
        secuenciasFondo[i] = val;
    }

    public void setValueSecSedimento(int i, int val) {
        secuenciasSedimento[i] = val;
    }

    public boolean tieneFondo() {
        return fondo;
    }

    public void setFondo(boolean fondo) {
        this.fondo = fondo;
    }

    public boolean tieneMaxF() {
        return maxF;
    }

    public void setMaxF(boolean maxF) {
        this.maxF = maxF;
    }

    public boolean tieneMinO() {
        return minO;
    }

    public void setMinO(boolean minO) {
        this.minO = minO;
    }

    public boolean tieneMil() {
        return mil;
    }

    public void setMil(boolean mil) {
        this.mil = mil;
    }

    public boolean tieneSedimento() {
        return sedimento;
    }

    public void setSedimento(boolean sedimento) {
        this.sedimento = sedimento;
    }

    public int[] getSecuenciasMax() {
        return secuenciasMax;
    }

    public void setSecuenciasMax(int[] secuenciasMax) {
        this.secuenciasMax = secuenciasMax;
    }

    public int[] getSecuenciasFondo() {
        return secuenciasFondo;
    }

    public void setSecuenciasFondo(int[] secuenciasFondo) {
        this.secuenciasFondo = secuenciasFondo;
    }

    public int[] getSecuenciasSedimento() {
        return secuenciasSedimento;
    }

    public void setSecuenciasSedimento(int[] secuenciasSedimento) {
        this.secuenciasSedimento = secuenciasSedimento;
    }

}
