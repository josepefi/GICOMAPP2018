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
public class PCRObj {

    int idPCR;
    String fw_primer = "";
    String rv_primer = "";
    String primerRef = "";
   // String clean_up_kit="";
    // String clean_up_method="";
    String comentarios = "";
   // ArrayList<String[]> pcr_cond = new ArrayList<>();
    String pcr_cond = "";

    public PCRObj(int idPCR) {
        this.idPCR = idPCR;
    }

    public int getIdPCR() {
        return idPCR;
    }

    public String getPrimerRef() {
        return primerRef;
    }

    public void setPrimerRef(String primerRef) {
        this.primerRef = primerRef;
    }

    public void setIdPCR(int idPCR) {
        this.idPCR = idPCR;
    }

    public String getFw_primer() {
        return fw_primer;
    }

    public void setFw_primer(String fw_primer) {
        this.fw_primer = fw_primer;
    }

    public String getRv_primer() {
        return rv_primer;
    }

    public void setRv_primer(String rv_primer) {
        this.rv_primer = rv_primer;
    }

  
    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getPcr_cond() {
        return pcr_cond;
    }

    public void setPcr_cond(String pcr_cond) {
        this.pcr_cond = pcr_cond;
    }

   /* public void addCondition(String[] condition) {
        this.pcr_cond.add(condition);
    }*/

}
