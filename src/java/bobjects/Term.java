/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobjects;

/**
 * Esta clase representa un término dentro de una ontología
 *
 * @author Alejandro
 */
public class Term {

    private int idOntologia;
    private String ontologia = "";
    private String idTerm;
    private String name = "";
    private String definition = "";
    private String is_a = "";
    private String name_space = "";
    private String relationship = "";
    private String is_obsolete = "";
    private String replaced_by = "";
    private String comments = "";
    private String url = "";

    public Term(String idTerm) {
        this.idTerm = idTerm;
        if (idTerm.equals("-1") || idTerm.length() == 0) {
            idOntologia = -1;
            ontologia = "";            
            name = "No data";
            definition = "";
            is_a = "";
            name_space = "";
            relationship = "";
            is_obsolete = "";
            replaced_by = "";
            comments = "";
            url = "";
        }

    }

    public int getIdOntologia() {
        return idOntologia;
    }

    public void setIdOntologia(int idOntologia) {
        this.idOntologia = idOntologia;
    }

    public String getOntologia() {
        return ontologia;
    }

    public void setOntologia(String ontologia) {
        this.ontologia = ontologia;
    }

    public String getIdTerm() {
        return idTerm;
    }

    public void setIdTerm(String idTerm) {
        this.idTerm = idTerm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getIs_a() {
        return is_a;
    }

    public void setIs_a(String is_a) {
        this.is_a = is_a;
    }

    public String getName_space() {
        return name_space;
    }

    public void setName_space(String name_space) {
        this.name_space = name_space;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getIs_obsolete() {
        return is_obsolete;
    }

    public void setIs_obsolete(String is_obsolete) {
        this.is_obsolete = is_obsolete;
    }

    public String getReplaced_by() {
        return replaced_by;
    }

    public void setReplaced_by(String replaced_by) {
        this.replaced_by = replaced_by;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
