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
public class StatsObj {
    private int idStats;
    private String reads="";
    private String bases="";
    private String long_avg="";
    private String gc_prc="";
    private String qc_avg="";
    private String ns_prc="";
    private String q20="";
    private String q30="";
    private String combined_prc="";

    public StatsObj(int idStats) {
        this.idStats = idStats;
    }

    public int getIdStats() {
        return idStats;
    }

    public void setIdStats(int idStats) {
        this.idStats = idStats;
    }

    public String getReads() {
        return reads;
    }

    public void setReads(String reads) {
        this.reads = reads;
    }

    public String getBases() {
        return bases;
    }

    public void setBases(String bases) {
        this.bases = bases;
    }

    public String getLong_avg() {
        return long_avg;
    }

    public void setLong_avg(String long_avg) {
        this.long_avg = long_avg;
    }

    public String getGc_prc() {
        return gc_prc;
    }

    public void setGc_prc(String gc_prc) {
        this.gc_prc = gc_prc;
    }

    public String getQc_avg() {
        return qc_avg;
    }

    public void setQc_avg(String qc_avg) {
        this.qc_avg = qc_avg;
    }

    public String getNs_prc() {
        return ns_prc;
    }

    public void setNs_prc(String ns_prc) {
        this.ns_prc = ns_prc;
    }

    public String getQ20() {
        return q20;
    }

    public void setQ20(String q20) {
        this.q20 = q20;
    }

    public String getQ30() {
        return q30;
    }

    public void setQ30(String q30) {
        this.q30 = q30;
    }

    public String getCombined_prc() {
        return combined_prc;
    }

    public void setCombined_prc(String combined_prc) {
        this.combined_prc = combined_prc;
    }

      
}
