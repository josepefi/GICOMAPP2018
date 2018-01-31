/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 *
 * @author Alejandro
 */
public class MyCoord {

    String coordenadas;
    int grados;
    int minutos;
    float segundos;
    float gradosDecimales;

    public MyCoord(String coordenadas) {
        this.coordenadas = coordenadas;

    }

    public float getGradosDecimales() {
        return gradosDecimales;
    }
     public float getGradosDecimales(int round) {
         String format = "#.";
         for(int i=0; i < round; i++){
             format+="#";
         }
         DecimalFormat df = new DecimalFormat(format);      
         return Float.valueOf(df.format(gradosDecimales));
    }

    public void setGradosDecimales(float gradosDecimales) {
        this.gradosDecimales = gradosDecimales;
    }

    public void computeGradosDecimales() {
        try {
            gradosDecimales = Float.parseFloat(coordenadas);
        } catch (NumberFormatException nfe) {
            gradosDecimales = (grados + ((float) minutos + ((float) segundos / 60)) / 60);
        }
    }

    /**
     * Código para obtener la distancia entre dos puntos. Código adaptado de:
     * //http://www.geodatasource.com/developers/java
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param unit
     * @return por defaul esta en M millas N = milla naúticas
     */
    public static double distance(MyCoord lat1, MyCoord lon1, MyCoord lat2, MyCoord lon2, String unit, int round) {
        double long1 = Math.abs(lon1.getGradosDecimales());
        double long2 = Math.abs(lon2.getGradosDecimales());
        double lati1 = Math.abs(lat1.getGradosDecimales());
        double lati2 = Math.abs(lat2.getGradosDecimales());
        if((long1-long2)==0&&(lati1-lati2)==0){
            return 0;
        }
     //   double theta = lon1.getGradosDecimales() - lon2.getGradosDecimales();
        //   double dist = Math.sin(deg2rad(lat1.getGradosDecimales())) * Math.sin(deg2rad(lat2.getGradosDecimales())) + Math.cos(deg2rad(lat1.getGradosDecimales())) * Math.cos(deg2rad(lat2.getGradosDecimales())) * Math.cos(deg2rad(theta));
        double theta = long1 - long2;
        double dist = Math.sin(deg2rad(lati1)) * Math.sin(deg2rad(lati2)) + Math.cos(deg2rad(lati1)) * Math.cos(deg2rad(lati2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        } else if (unit == "m") {
            dist = dist * 1.609344 * 1000;
        }
         String format = "#.";
         for(int i=0; i < round; i++){
             format+="#";
         }
         DecimalFormat df = new DecimalFormat(format);      
         return Double.valueOf(df.format(dist));
       // return (dist);
    }
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public boolean parseCoordsLGMS() {
        //  N 26º 84 817 / W 102º 08 58.4 coordenadas pueden venir asi
        StringTokenizer st = new StringTokenizer(coordenadas);
//descomentar para hectofiles!!       
// String latLong = st.nextToken();
        String gTmp;
        try {
            gTmp = st.nextToken();
            gTmp = gTmp.substring(0, gTmp.length() - 1);
            grados = Integer.parseInt(gTmp.trim());
            minutos = Integer.parseInt(st.nextToken());
            segundos = Float.parseFloat(st.nextToken());
        } catch (NumberFormatException nfe) {
            System.out.println("Error NFE parseCoordsLGMS(): " + coordenadas);
            return false;

        } catch (NoSuchElementException nsee) {
            System.out.println("Error nsee parseCoordsLGMS(): " + coordenadas);
            return false;
        }
        computeGradosDecimales();
        return true;

    }

    /**
     * Parseo de cordenadas para archivos de yunuen
     *
     * @return
     */
    public boolean parseCoordsGMS() {
        //  26º 84' 817N /  102º 08' 58.4''W coordenadas pueden venir asi
        StringTokenizer st = new StringTokenizer(coordenadas, "'�°WN ");
//descomentar para hectofiles!!       
// String latLong = st.nextToken();
        String gTmp;
        double coords = 0;
        try {
            grados = Integer.parseInt(st.nextToken().trim());
            minutos = Integer.parseInt(st.nextToken());
            segundos = Float.parseFloat(st.nextToken());
            coords = (grados) + ((double) minutos + (segundos / 60)) / 60;
            coordenadas = "" + coords;

        } catch (NumberFormatException nfe) {
            System.out.println("Error NFE parseCoordsLGMS(): " + coordenadas);
            return false;

        } catch (NoSuchElementException nsee) {
            System.out.println("Error nsee parseCoordsLGMS(): " + coordenadas);
            return false;
        }
        computeGradosDecimales();
        return true;

    }

    /**
     * Parseo de cordenadas para cualquier tipo de dato
     *
     * @return
     */
    public boolean parseAnyCoordGMS() {
        //  26º 84' 817N /  102º 08' 58.4''W coordenadas pueden venir asi
        //35° 55.116'
        StringTokenizer st = new StringTokenizer(coordenadas, "'�°WNSEO ");
//descomentar para hectofiles!!       
// String latLong = st.nextToken();
        String gTmp;
        double coords = 0;
        try {

            int tokens = st.countTokens();
            if (tokens == 1) {//solo tenemmos grados 
                double g = Double.parseDouble(st.nextToken().trim());
                grados = (int) g;
                g = (g - grados) * 60; //la parte fraccion la volvemos a minutos
                minutos = (int) g;
                g = (g - minutos) * 60; //la parte fraccion la volvemos segundos
                segundos = (float) g;
            } else if (tokens == 2) { //tenemos grados ° y minutos                
                grados = Integer.parseInt(st.nextToken().trim());
                double m = Double.parseDouble(st.nextToken());
                minutos = (int) m;
                m = (m - minutos) * 60; //la parte fraccion la volvemos segundos
                segundos = (float) m;
            } else if (tokens == 3) {
                grados = Integer.parseInt(st.nextToken().trim());
                minutos = Integer.parseInt(st.nextToken().trim());
                segundos = Float.parseFloat(st.nextToken().trim());
                //  coords = (grados) + ((double) minutos + (segundos / 60)) / 60;
                //  coordenadas = "" + coords;
            } else {
                int tok = 0;
                while (st.hasMoreTokens()) {
                    tok++;
                    System.out.print("Token: " + tok + " " + st.nextToken());
                }
            }
            // minutos = Integer.parseInt(st.nextToken());
            // segundos = Float.parseFloat(st.nextToken());
            coords = (grados) + ((double) minutos + (segundos / 60)) / 60;
            coordenadas = "" + coords;

        } catch (NumberFormatException nfe) {
            System.out.println("Error NFE parseCoordsLGMS(): " + coordenadas);
            return false;

        } catch (NoSuchElementException nsee) {
            System.out.println("Error nsee parseCoordsLGMS(): " + coordenadas);
            return false;
        }
        computeGradosDecimales();
        return true;

    }

    /**
     * Este metodo convierte el atributo ccooords a float por el momento solo
     * sirve cuando el atributo vine dado por la cordenada en grados que es
     * cuando se utiliza tambien el metodo parseCoordsGtoGMS para parsear a g m
     * s
     *
     * @return
     */
    public float getCoords() {
        float coords_toF = 0;
        try {
            coords_toF = Float.parseFloat(coordenadas);
        } catch (NumberFormatException nfe) {
            System.out.println("ERROR parsing coords");
            return 0;
        }
        return coords_toF;
    }

    public void parseCoordsGtoGMS() {
        try {
            double coord = Double.parseDouble(coordenadas);
            grados = (int) coord;
            double tmpMinutos = (Math.abs(coord) - Math.abs(grados)) * 60;
            minutos = (int) tmpMinutos;
            segundos = ((float) tmpMinutos - minutos) * 60;
        } catch (Exception e) {

        }
    }

    public boolean parseCoordsGMSL() {
        if (coordenadas.equals("x")) {
            grados = 0;
            minutos = 0;
            segundos = 0;
            return true;
        }
        //  N 26º 84' 817'' / W 102º 08' 58.4'' coordenadas pueden venir asi
        String tmpCoord = coordenadas.replaceAll("[^\\d. ]", "");
        StringTokenizer st = new StringTokenizer(tmpCoord);
//descomentar para hectofiles!!       
// String latLong = st.nextToken();
        //  String gTmp;
        try {
            //     gTmp = st.nextToken();
            //    gTmp = gTmp.substring(0, gTmp.length()-1);
            grados = Integer.parseInt(st.nextToken());
            minutos = Integer.parseInt(st.nextToken());
            segundos = Float.parseFloat(st.nextToken());
        } catch (NumberFormatException nfe) {
            System.out.println("Error NFE parseCoordsLGMS(): " + coordenadas);
            return false;

        } catch (NoSuchElementException nsee) {
            System.out.println("Error nsee parseCoordsLGMS(): " + coordenadas);
            return false;
        }
        return true;

    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public int getGrados() {
        return grados;
    }

    public void setGrados(int grados) {
        this.grados = grados;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public float getSegundos() {
        return segundos;
    }

    public String getSegundosFormated() {
        NumberFormat formatter = new DecimalFormat("###.####");
        return formatter.format(segundos);
    }

    public void setSegundos(float segundos) {
        this.segundos = segundos;
    }

}
