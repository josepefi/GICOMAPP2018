/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobjects;

/**
 * Este objeto es la abstracción de un punto representado en google maps. Para
 * este fin se necesitan parámetros para definir el ícono que llevará el punto,
 * así como la etiqueta y las coordenadas. La idea es que este objeto sea usado
 * como response en un arreglo, y así actualizar coordenadas en mapas
 *
 * @author Alejandro
 */
public class PuntoMapa {
    public static String ICONO_DEF = "images/icons/agua.png";
    public static String ICONO_AGUA = "images/icons/agua.png";
    public static String ICONO_SEDIMENTO = "images/icons/sedimento.png";
    public static String ICONO_AGUA_SED = "images/icons/aguaSed.png";
    public static String ICONO_METAGENOMAS = "images/icons/metageno.png";
    private String etiqueta;
    private String icon;
    private float latitud;
    private float longitud;

    public PuntoMapa(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

}
