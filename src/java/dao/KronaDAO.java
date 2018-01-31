/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bobjects.ArchivoObj;
import database.Transacciones;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class KronaDAO {

    private Transacciones transacciones;

    public Transacciones getTransacciones() {
        return transacciones;
    }

    public KronaDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    public void setTransacciones(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Método que se encarga de hubicar un archivo krona, dado un ID de un
     * marcador
     *
     * @param idMarcador id del marcador
     * @return String con todo el contenido del archivo
     * @deprecated Se crea el archivo readKrona el cual es univrsal para
     * marcadors y metagenomas. Se espera una migrción para la parte de
     * amplicones
     */
    public String readKronaFile(int idMarcador) {
        try {
            // BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Alejandro\\Documents\\Projects\\pemex\\Amplicones\\kronas\\B8_FON_1\\krona.html"));
            //  BufferedReader reader = new BufferedReader(new FileReader("/home/glassfish/glassfish4/glassfish/domains/domain1/applications/CIGOMAPP/kronas/krona100.html"));
            String dataPath = transacciones.getPath2Krona(idMarcador, ArchivoObj.TIPO_KRN);
            if (dataPath.length() > 0) {
                BufferedReader reader = new BufferedReader(new FileReader(dataPath));
                StringBuilder kronaHTML = new StringBuilder();
                String linea;
                String ls = System.getProperty("line.separator");
                while ((linea = reader.readLine()) != null) {
                    kronaHTML.append(linea).append(ls);
                }
                reader.close();
                return kronaHTML.toString();

            } else {
                return "ERROR";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KronaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            Logger.getLogger(KronaDAO.class.getName()).log(Level.SEVERE, null, ioe);
        }
        return "ERROR";
    }

    /**
     * Este método evoluciona de readKronaFile, el cual sólo sirva para
     * marcadores, este nuevo método sirve tanto para metagenomas como
     * marcadores
     *
     * @param idKrona ID del meta o del marc
     * @param src fuente marcador o metagenoma
     * @return
     */
    public String readKrona(int idKrona, String src) {
        try {
            // BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Alejandro\\Documents\\Projects\\pemex\\Amplicones\\kronas\\B8_FON_1\\krona.html"));
            //  BufferedReader reader = new BufferedReader(new FileReader("/home/glassfish/glassfish4/glassfish/domains/domain1/applications/CIGOMAPP/kronas/krona100.html"));
            String dataPath = transacciones.getKronaPath(idKrona, ArchivoObj.TIPO_KRN, src,"krona_p.html");
            if (dataPath.length() > 0) {
                BufferedReader reader = new BufferedReader(new FileReader(dataPath));
                StringBuilder kronaHTML = new StringBuilder();
                String linea;
                String ls = System.getProperty("line.separator");
                while ((linea = reader.readLine()) != null) {
                    kronaHTML.append(linea).append(ls);
                }
                reader.close();
                return kronaHTML.toString();

            } else {
                return "ERROR";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KronaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            Logger.getLogger(KronaDAO.class.getName()).log(Level.SEVERE, null, ioe);
        }
        return "ERROR";
    }

}
