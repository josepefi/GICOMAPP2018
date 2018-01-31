/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package job;

/**
 *
 * @author Alejandro
 */
public class BlastResult {
   private String query ;
   private String target;
   private String target_type;//nc aa 5p 3p
   private String identity;
   private String source;//producto genéticco del target: metagenoma o genoma
   private String target_definition;
   private String target_org;
   private String prot_id;
   private String eval;
   private String gen_id;//id del target
   private String bit_score;
   private String query_from;
   private String query_to;
   private String target_from;
   private String target_to;
   private String taxa;
   private String idMuestra;
    public String getQuery_from() {
        return query_from;
    }

    public String getIdMuestra() {
        return idMuestra;
    }

    public void setIdMuestra(String idMuestra) {
        this.idMuestra = idMuestra;
    }

    public void setQuery_from(String query_from) {
        this.query_from = query_from;
    }

    public String getQuery_to() {
        return query_to;
    }

    public void setQuery_to(String query_to) {
        this.query_to = query_to;
    }

    public String getTarget_from() {
        return target_from;
    }

    public void setTarget_from(String target_from) {
        this.target_from = target_from;
    }

    public String getTarget_to() {
        return target_to;
    }

    public void setTarget_to(String target_to) {
        this.target_to = target_to;
    }

    public String getTaxa() {
        return taxa;
    }

    public void setTaxa(String taxa) {
        this.taxa = taxa;
    }

    public BlastResult(String query) {
        this.query = query;
    }

    public String getBit_score() {
        return bit_score;
    }

    public void setBit_score(String bit_score) {
        this.bit_score = bit_score;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getTarget_type() {
        return target_type;
    }

    public void setTarget_type(String target_type) {
        this.target_type = target_type;
    }

    public String getTarget_definition() {
        return target_definition;
    }

    public void setTarget_definition(String target_definition) {
        this.target_definition = target_definition;
    }

    public String getTarget_org() {
        return target_org;
    }

    public void setTarget_org(String target_org) {
        this.target_org = target_org;
    }

    public String getProt_id() {
        return prot_id;
    }

    public void setProt_id(String prot_id) {
        this.prot_id = prot_id;
    }

    public BlastResult() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String producto) {
        this.source = producto;
    }

    public String getEval() {
        return eval;
    }

    public void setEval(String eval) {
        this.eval = eval;
    }

    public String getGen_id() {
        return gen_id;
    }

    public void setGen_id(String gen_id) {
        this.gen_id = gen_id;
    }
   
}
