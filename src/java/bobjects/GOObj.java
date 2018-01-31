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
public class GOObj {
    String id_GO;
    String go_name;
    String namespace;
    String url;
    String definition;
    
    public GOObj(String acc) {
        this.id_GO = id_GO;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String ursl) {
        this.url = ursl;
    }

    
    
    public String getId_GO() {
        return id_GO;
    }

    public void setId_GO(String id_GO) {
        this.id_GO = id_GO;
    }

    public String getGo_name() {
        return go_name;
    }

    public void setGo_name(String go_name) {
        this.go_name = go_name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
    
    
}
