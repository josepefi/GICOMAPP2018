/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobjects;

/**
 *
 * @author Jose Pefi
 */
public class PFAMObj {
    
    String acc;
    String clan;
    String Id_pfam;
    String pfam_deff;
    String evalue;
    String from;
    String to;

   public PFAMObj(String acc) {
        this.acc = acc;
    }

    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    

    public void PFAMObj(String acc){
        this.acc = acc;
    }
    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getClan() {
        return clan;
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public String getId_pfam() {
        return Id_pfam;
    }

    public void setId_pfam(String Id_pfam) {
        this.Id_pfam = Id_pfam;
    }

    public String getPfam_deff() {
        return pfam_deff;
    }

    public void setPfam_deff(String pfam_deff) {
        this.pfam_deff = pfam_deff;
    }

    public String getEvalue() {
        return evalue;
    }

    public void setEvalue(String evalue) {
        this.evalue = evalue;
    }
    
    
    
    
}
