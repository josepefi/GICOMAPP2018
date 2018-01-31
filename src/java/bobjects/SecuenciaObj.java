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
public class SecuenciaObj {
    String secuencia;
    String TipoSecuencia;
    String genId;



    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getTipoSecuencia() {
        return TipoSecuencia;
    }

    public void setTipoSecuencia(String TipoSecuencia) {
        this.TipoSecuencia = TipoSecuencia;
    }

    public String getGenId() {
        return genId;
    }

    public void setGenId(String genId) {
        this.genId = genId;
    }
    public SecuenciaObj(String genId, String TipoSecuencia) {
            this.genId = genId;
            this.TipoSecuencia= TipoSecuencia;
            
    }
}
