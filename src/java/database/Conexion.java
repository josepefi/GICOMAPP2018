/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import java.sql.*;
import java.util.*;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    
    private Map<String,List<String>> hTabla;
    private String strNull="0";
    private Connection connection; //para establecer conexión
    private Statement statement;
    private String user;
    private String con;
    private ResultSet resultSet; //para traer la información obtenida por ResultSetMetaData
    private ArrayList<String> titulos;/// = new ArrayList<String>();
    private ArrayList<ArrayList> tabla;// = new ArrayList<ArrayList>(50000);;
    public ResultSetMetaData rsmd;
    public int estatus;
    public boolean seConecto;
    public String IP;
    PreparedStatement stmt = null;
    private String log;
    private PreparedStatement geneDetails; //;= "select sp.uniprot_id, uniprot_acc, prot_name,ncbi_node.name,tipo_muestra,eval from gen left join gen_swiss_prot as gsp on gsp.gen_id = gen.gen_id left join swiss_prot as sp on sp.uniprot_id = gsp.uniprot_id left join ncbi_node on sp.ncbi_tax_id = ncbi_node.tax_id inner join metagenoma on metagenoma.idmetagenoma = gen.idmetagenoma inner join muestra on muestra.idmuestra = metagenoma.idmuestra inner join muestreo on muestreo.idmuestreo = muestra.idmuestreo inner join tipo_muestra on tipo_muestra.idtipomuestra = muestreo.idtipomuestra where gen.gen_id = ?";
    private PreparedStatement metaGeneDetails;

    public Connection getConnection() {
        return connection;
    }

    public void compilePreparedGene() {
        String query = "select sp.uniprot_id, uniprot_acc, prot_name,ncbi_node.name,tipo_muestra,eval from gen left join gen_swiss_prot as gsp on gsp.gen_id = gen.gen_id left join swiss_prot as sp on sp.uniprot_id = gsp.uniprot_id left join ncbi_node on sp.ncbi_tax_id = ncbi_node.tax_id inner join metagenoma on metagenoma.idmetagenoma = gen.idmetagenoma inner join muestra on muestra.idmuestra = metagenoma.idmuestra inner join muestreo on muestreo.idmuestreo = muestra.idmuestreo inner join tipo_muestra on tipo_muestra.idtipomuestra = muestreo.idtipomuestra where gen.gen_id = ? order by eval";

        String query2 = "select sp.uniprot_id, uniprot_acc, prot_name,ncbi_node.name,tipo_muestra,eval from gen left join gen_swiss_prot as gsp on gsp.gen_id = gen.gen_id left join swiss_prot as sp on sp.uniprot_id = gsp.uniprot_id left join ncbi_node on sp.ncbi_tax_id = ncbi_node.tax_id inner join genoma on genoma.idgenoma = gen.idgenoma inner join muestra on muestra.idmuestra = genoma.idmuestra inner join muestreo on muestreo.idmuestreo = muestra.idmuestreo inner join tipo_muestra on tipo_muestra.idtipomuestra = muestreo.idtipomuestra where gen.gen_id = ? order by eval";

        try {
            geneDetails = connection.prepareStatement(query2);
            metaGeneDetails = connection.prepareStatement(query);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo para ejecutar SQL coomands de tipo SELECT basicamente
     *
     * @param query Query a ejecutar
     */
    public void executePreparedGene(String gen_id) {
        try {
            geneDetails.setString(1, gen_id);
            //statement = connection.createStatement();
            resultSet = geneDetails.executeQuery();
            //resultSet = statement.executeQuery(query); //ponemos el resultado de la ejecución
            //en un ResultSet

            llenaResultados(resultSet);
        } catch (SQLException sqlex) {
            System.err.println("Error de query con gen: " + gen_id);
            sqlex.printStackTrace();
        }
    }

    public void executePreparedMetaGene(String gen_id) {
        try {
            metaGeneDetails.setString(1, gen_id);
            //statement = connection.createStatement();
            resultSet = metaGeneDetails.executeQuery();
            //resultSet = statement.executeQuery(query); //ponemos el resultado de la ejecución
            //en un ResultSet

            llenaResultados(resultSet);
        } catch (SQLException sqlex) {
            System.err.println("Error de query con gen: " + gen_id);
            sqlex.printStackTrace();
        }
    }

    public void closePreparedGene() {

        try {
            geneDetails.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    /**
     *
     * @param db String El nombre de la base de datos
     * @param ip String La dirección IP donde se encuentra la base de datos
     */
    public Conexion(String db, String ip, String user, String pass) {
        IP = ip;
        String manejador = //da de alta la base de datos en el sistema operativo
                // "jdbc:mysql://" + IP + ":3306/" + db + "?user=root&=";
               // "jdbc:mysql://" + IP + ":3306/" + db + "?user=" + user + "&=";
               //  "jdbc:mysql://" + IP + ":3306/" + db + "?zeroDateTimeBehavior=convertToNull";
                "jdbc:mysql://" + IP + ":3306/" + db + "?zeroDateTimeBehavior=convertToNull&noAccessToProcedureBodies=true";
        try {
            Class.forName("com.mysql.jdbc.Driver"); //controlador de msqlpara base de datos
            //  connection = DriverManager.getConnection(manejador, "root", "AMORPHIS"); //genera la conexión
            connection = DriverManager.getConnection(manejador, user, pass);
            seConecto = true;

        } catch (Exception e) {
            e.printStackTrace();
            String stack = "";
            seConecto = false;
            for (int i = 0; i < e.getStackTrace().length; i++) {
                stack += ((StackTraceElement[]) e.getStackTrace())[i].getClassName()
                        + " --> "
                        + ((StackTraceElement[]) e.getStackTrace())[i].getLineNumber()
                        + "\n";
            }
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                        "error.log")));
                out.println(stack);
                out.close();
            } catch (Exception ex) {
            }

        }
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public ArrayList<ArrayList> getTabla() {
        return tabla;
    }

    /**
     * Ejecuta instrucciones SQL proncipalmente de tipo SELECT
     *
     * @param query
     * @return message Mensaje de error por si falla algo
     */
    public String executeStatementToFile(String query) {
        try {
            statement = connection.createStatement();
            titulos = new ArrayList<String>();
            //Filas = new Vector();
            tabla = new ArrayList<ArrayList>();
            resultSet = statement.executeQuery(query); //ponemos el resultado de la ejecución
            //en un ResultSet

            //llenaResultados(resultSet);
            statement.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return "sin conexion";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "sin conexion";
        }
        return "";
    }

    public ArrayList<String> getTitulos() {
        return titulos;
    }

    /**
     * metodo para ejecutar peticiones de tipo insert, update, delete.
     *
     * @param peticion String
     * @return boolean
     */
    public boolean queryUpdate(String peticion) { //recive el query a ejecutarse
        try {
            statement = connection.createStatement(); //ingresa a la bd
            estatus = statement.executeUpdate(peticion); //ejecuta la petición
            if (estatus != 0) { //ejecutar un query regresa una bandera, si esta es igual a 1
                //significa que se realizó con exito
                // JOptionPane.showMessageDialog(null, "Operacion exitosa");
                return true;
            } else {
                // JOptionPane.showMessageDialog(null,
                //    "Se detecto un error\n por favor verifique los datos");
                return false;
            }
        } catch (MySQLSyntaxErrorException mysqlex) {
            //donde se genera la excepción
            // mysqlex.printStackTrace();
            //  JOptionPane.showMessageDialog(null, "Error\n " + sqle.toString());
            log = "Error de sintaxis: " + peticion;
            return false;
        } catch (SQLException sqle) { // si el query estaba mal formulado es aca
            //donde se genera la excepción
            // sqle.printStackTrace();
            log = "SQL Exp: " + peticion;
            //  JOptionPane.showMessageDialog(null, "Error\n " + sqle.toString());
            return false;
        } catch (Exception ex) {
            log = "Exp en query: " + peticion;
            log += "\n" + ex.getLocalizedMessage();
            //  JOptionPane.showMessageDialog(null, "Error\n " + sqle.toString());
            return false;
        }

    }

    public boolean queryUpdate2(String peticion) throws SQLException { //recive el query a ejecutarse
        try {
            statement = connection.createStatement(); //ingresa a la bd
            estatus = statement.executeUpdate(peticion); //ejecuta la petición
            if (estatus != 0) { //ejecutar un query regresa una bandera, si esta es igual a 1
                //significa que se realizó con exito
                // JOptionPane.showMessageDialog(null, "Operacion exitosa");
                return true;
            } else {
                // JOptionPane.showMessageDialog(null,
                //    "Se detecto un error\n por favor verifique los datos");
                return false;
            }
        } catch (SQLException sqle) { // si el query estaba mal formulado es aca
            //donde se genera la excepción
            //  sqle.printStackTrace();
            //  JOptionPane.showMessageDialog(null, "Error\n " + sqle.toString());
            return false;
        }

    }

    /**
     * metodo para ejecutar peticiones de tipo insert, update, delete. Pero
     * obtiene el ID de la llave generada
     *
     * @param peticion String
     * @return boolean
     */
    public int queryUpdateWithKey(String query) { //recive el query a ejecutarse
        try {
            statement = connection.createStatement(); //ingresa a la bd
            // estatus = statement.executeUpdate(peticion); //ejecuta la petición
            //  Statement st = conn.createStatement();
            statement.execute(query,
                    Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = statement.getGeneratedKeys();
            int id = -1;
            while (keys.next()) {
                id = keys.getInt(1);
            }
            return id;
        } catch (SQLException sqle) { // si el query estaba mal formulado es aca
            //donde se genera la excepción
            sqle.printStackTrace();
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, sqle);
            //  JOptionPane.showMessageDialog(null, "Error\n " + sqle.toString());
            return -1;
        } catch (Exception e) { // si el query estaba mal formulado es aca
            e.printStackTrace();
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
            return -1;
        }

    }

    public String preparedUpdate(String query, ArrayList<String> values, Vector ints, Vector floats) throws SQLException { //recive el query a ejecutarse

        try {
            stmt = connection.prepareStatement(query);
            //statement = connection.createStatement(); //ingresa a la bd
            // estatus = statement.executeUpdate(peticion); //ejecuta la petición
            int cont = 0;
            for (String value : values) {
                if (ints.contains("" + cont)) {
                    try {
                        stmt.setInt((cont + 1), Integer.parseInt(value));
                    } catch (NumberFormatException nfe) {
                        System.out.println("Error parse Int" + values.toString());
                        stmt.setInt((cont + 1), 0);
                        return "ERROR INT TYPE READ VALUE (NFE) = " + value + "\nRecord:" + cont + " value set to 0";

                    }
                } else if (floats.contains("" + cont)) {
                    try {
                        stmt.setFloat((cont + 1), Float.parseFloat(value));
                    } catch (NumberFormatException nfe) {
                        System.out.println("Error parse float" + values.toString());
                        stmt.setInt((cont + 1), 0);
                        return "ERROR FLOAT TYPE READ VALUE (NFE) = " + value + "\nRecord:" + cont + " value set to 0";
                    }
                } else {
                    stmt.setString((cont + 1), value);
                }
                cont++;
            }
            estatus = stmt.executeUpdate();
            if (estatus != 0) { //ejecutar un query regresa una bandera, si esta es igual a 1
                //significa que se realizó con exito
                // JOptionPane.showMessageDialog(null, "Operacion exitosa");
                return "OK";
            } else {
                // JOptionPane.showMessageDialog(null,
                //    "Se detecto un error\n por favor verifique los datos");
                return "NOT INSERTED " + cont;
            }
        } catch (SQLException sqle) { // si el query estaba mal formulado es aca
            //donde se genera la excepción
            //  sqle.printStackTrace();
            //  JOptionPane.showMessageDialog(null, "Error\n " + sqle.toString());
            return "SQL EXCPETION " + sqle.getMessage();
        }

    }
    String tuplo[];
    //  int cont = 0;

    public String preparedUpdateSB(String query, StringBuilder values, Vector ints, Vector floats) throws SQLException { //recive el query a ejecutarse

        try {
            stmt = connection.prepareStatement(query);
            //statement = connection.createStatement(); //ingresa a la bd
            // estatus = statement.executeUpdate(peticion); //ejecuta la petición
            // cont = 0;
            tuplo = values.toString().split("\t");

            //for (String value : tuplo) {
            for (int i = 0; i < tuplo.length; i++) {

                if (ints.contains("" + i)) {
                    try {
                        if (tuplo[i].toLowerCase().equals("null")) {
                            stmt.setNull((i + 1), java.sql.Types.INTEGER);
                        } else {
                            stmt.setInt((i + 1), Integer.parseInt(tuplo[i]/*value*/));
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Error parse Int" + values.toString());
                        stmt.setInt((i + 1), 0);
                        return "ERROR INT TYPE READ VALUE (NFE) = " + tuplo[i]/*value*/ + "\nRecord:" + i + " value set to 0";

                    }
                } else if (floats.contains("" + i)) {
                    try {
                        if (tuplo[i].toLowerCase().equals("null")) {
                            stmt.setNull((i + 1), java.sql.Types.FLOAT);
                        } else {
                            stmt.setFloat((i + 1), Float.parseFloat(tuplo[i]/*value*/));
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Error parse float" + values.toString());
                        stmt.setInt((i + 1), 0);

                        return "ERROR FLOAT TYPE READ VALUE (NFE) = " + tuplo[i]/*value*/ + "\nRecord:" + i + " value set to 0";
                    }
                } else {
                    if (tuplo[i].toLowerCase().equals("null")) {
                        stmt.setNull((i + 1), java.sql.Types.VARCHAR);
                    } else {
                        stmt.setString((i + 1), tuplo[i]/*value*/);
                    }
                }
            }
            //i++;

            estatus = stmt.executeUpdate();
            if (estatus != 0) { //ejecutar un query regresa una bandera, si esta es igual a 1
                //significa que se realizó con exito
                // JOptionPane.showMessageDialog(null, "Operacion exitosa");
                return "OK";
            } else {
                // JOptionPane.showMessageDialog(null,
                //    "Se detecto un error\n por favor verifique los datos");
                return "NOT INSERTED " /*+ cont*/;
            }
        } catch (SQLException sqle) { // si el query estaba mal formulado es aca
            //donde se genera la excepción
            //  sqle.printStackTrace();
            //  JOptionPane.showMessageDialog(null, "Error\n " + sqle.toString());
            return "SQL EXCPETION " + sqle.getMessage();
        }

    }

    /**
     * Ejecuta instrucciones SQL proncipalmente de tipo SELECT
     *
     * @param query
     * @return message Mensaje de error por si falla algo
     */
    public String executeStatement(String query) {
        try {
            statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);

            /*
             statement= connection.createStatement(
             ResultSet.TYPE_SCROLL_INSENSITIVE, 
             ResultSet.CONCUR_READ_ONLY);
             statement.setFetchSize(Integer.MIN_VALUE);
             */
            // resultSet = new ResultSet();
            //resultSet = statement.executeQuery(query); //ponemos el resultado de la ejecución
            //en un ResultSet
            llenaResultados(statement.executeQuery(query));
            statement.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return "sin conexion";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "sin conexion";
        }
        return "";
    }

    public String executeStatementSB(String query, Transacciones t, String insertMethod, Vector ints, Vector floats, String tabla, String campos, String values) {
        try {
            statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(Integer.MIN_VALUE);
            /*
             statement= connection.createStatement(
             ResultSet.TYPE_SCROLL_INSENSITIVE, 
             ResultSet.CONCUR_READ_ONLY);
             statement.setFetchSize(Integer.MIN_VALUE);
             */
            // resultSet = new ResultSet();
            //resultSet = statement.executeQuery(query); //ponemos el resultado de la ejecución
            //en un ResultSet

            String res = llenaResultadosSB(statement.executeQuery(query), t, insertMethod, ints, floats, tabla, campos, values);
            statement.close();
            return res;
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return "sin conexion";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "sin conexion";
        }
        //return "";
    }

    /**
     * //rgresa la bandera de estatus
     *
     * @return int
     */
    public int getEstatus() {
        return estatus;
    }

    /**
     * Metodo para ejecutar SQL coomands de tipo SELECT basicamente
     *
     * @param query Query a ejecutar
     */
    public void executePreparedS(String query) {
        PreparedStatement pState;
        try {
            pState = connection.prepareStatement(query);
            //statement = connection.createStatement();
            resultSet = pState.executeQuery();
            //resultSet = statement.executeQuery(query); //ponemos el resultado de la ejecución
            //en un ResultSet

            llenaResultados(resultSet);
            pState.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    /**
     * Este método es invocado por el método getTable y se encarga de llenar los
     * ArrayLists con la información obtenida del query ejecutado en el método
     * que lo invocó
     *
     * @param rs ResultSet Viene con los datos generados por la petición
     * @throws SQLException
     */
    private void llenaResultados(ResultSet rs) throws SQLException {
        // rs.setFetchSize(10000);
        //posiciona el primer dato
        boolean moreRecords = rs.next();

        // si no hay datos es aca donde se desplegara el mensaje
        titulos = new ArrayList<String>();
        //titulos.clear();

        //Filas = new Vector();
        tabla = new ArrayList<ArrayList>();
        //tabla.clear();
        if (!moreRecords) { //si no hay mas datos termina

            return;
        }

        try {
            // obtiene el título de las columnas
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                //Columnas.addElement(rsmd.getColumnName(i)); //pone el nombre a las columnas de la tabla
                titulos.add(rsmd.getColumnName(i));
            }
            do {
                //Filas.addElement(getNextRow(rs, rsmd)); //al vector filas los datos
                tabla.add(getNextTuplo(rs, rsmd));
            } while (rs.next()); //mientras existan registros

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
    Class[] parametertypes = new Class[]{StringBuilder.class, Vector.class, Vector.class, Integer.class};
    Object params[] = new Object[4];//{null, ints, floats, cont};
    Method m = null;
    // StringBuilder newRes = new StringBuilder();
    //String tempRes = "";

    private String llenaResultadosSB(ResultSet rs, Transacciones t, String insertMethod, Vector ints, Vector floats, String table, String campos, String values) {
        try {
            boolean moreRecords = rs.next();
            if (!moreRecords) { //si no hay mas datos termina

                return "END";
            }

            rsmd = rs.getMetaData();
            //      newRes.delete(0, newRes.length());
            int cont = 1;
            params[1] = ints;
            params[2] = floats;
            params[3] = cont;
            m = null;
            /*try {
             m = t.getClass().getMethod(insertMethod, parametertypes);
             } catch (NoSuchMethodException ex) {
             Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
             } catch (SecurityException ex) {
             Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
             }*/

            do {
                //params[0] = getNextTuploSB(rs, rsmd);
                try {
                    //  m.invoke(t, params);
                    //   tempRes = ((String) m.invoke(t, params));

                    // if (!tempRes.equals("OK")) {
                    //   newRes.append(tempRes).append("\n");
                    // }
                    // t.insertPreparedPhyloDistSB(getNextTuploSB(rs, rsmd), ints, floats, cont);
                    //   t.genericInsert(getNextTuploSB(rs, rsmd), ints, floats, cont, table, campos, values);
                } catch (SecurityException ex) {
                    Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
                }
                //System.out.println("" + cont);
                cont++;
            } while (rs.next()); //mientras existan registros
            return "OK";//newRes.toString();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return "ERROR SQLEX";
        }
    }

    /**
     * Agrega los datos obtenidos al vector
     *
     * @param rs ResultSet
     * @param rsmd ResultSetMetaData
     * @throws SQLException
     * @return Vector el vector con la información dela BD
     */
    //ArrayList<String> tuplo = new ArrayList<String>();
    /**
     * Agrega los datos obtenidos al vector
     *
     * @param rs ResultSet
     * @param rsmd ResultSetMetaData
     * @throws SQLException
     * @return Vector el vector con la información dela BD
     */
    private ArrayList getNextTuplo(ResultSet rs, ResultSetMetaData rsmd) throws
            SQLException {
        //Vector currentRow = new Vector();
        //Vector filaActual = new Vector();
        ArrayList<String> tuplo = new ArrayList<String>();
        for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
            switch (rsmd.getColumnType(i)) {
                //Son metodos q pertenecen a java.sql.Types
                case Types.VARCHAR:
                    //filaActual.addElement(rs.getString(i));
                    tuplo.add(rs.getString(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }

                    // filaActual.addElement(rs.toString());
                    break;
                case Types.CHAR:

                    //filaActual.addElement(rs.getString(i));
                    tuplo.add(rs.getString(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                    break;
                case Types.LONGVARCHAR:
                    //filaActual.addElement(rs.getString(i));
                    tuplo.add(rs.getString(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                    break;
                case Types.INTEGER:

                    //filaActual.addElement("" + rs.getLong(i));
                    tuplo.add("" + rs.getLong(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                    break;
                case Types.DOUBLE:
                    //filaActual.addElement("" + rs.getDouble(i));
                    tuplo.add("" + rs.getDouble(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                    break;
                case Types.SMALLINT:
                    //filaActual.addElement("" + rs.getInt(i));
                    tuplo.add("" + rs.getInt(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                    break;
                case Types.TINYINT:
                    //filaActual.addElement("" + rs.getInt(i));
                    tuplo.add("" + rs.getInt(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                    break;
                case Types.OTHER:
                    //filaActual.addElement(rs.getString(i));
                    tuplo.add(rs.getString(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                    break;
                default:
                    //filaActual.addElement(rs.getString(i));
                    tuplo.add(rs.getString(i));
                    if (rs.wasNull()) {
                        tuplo.remove(tuplo.size() - 1);
                        tuplo.add("");
                    }
                //  System.out.println("El campo es de tipo:  " +
                //                   rsmd.getColumnTypeName(i));
            }
        }
        return tuplo;
    }
    StringBuilder sTuplo = new StringBuilder();
    //String sTuplo = "";

    private StringBuilder getNextTuploSB(ResultSet rs, ResultSetMetaData rsmd) throws
            SQLException {
        //Vector currentRow = new Vector();
        //Vector filaActual = new Vector();
        //ArrayList<String> tuplo = new ArrayList<String>();
        //  tuplo.clear();
        //  System.gc();
        //sTuplo = "";
        sTuplo = new StringBuilder();
        for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
            switch (rsmd.getColumnType(i)) {
                //Son metodos q pertenecen a java.sql.Types
                case Types.VARCHAR:
                    //filaActual.addElement(rs.getString(i));
                    //tuplo.add(rs.getString(i));
                    sTuplo.append(rs.getString(i)).append("\t");
                    // filaActual.addElement(rs.toString());
                    break;
                case Types.CHAR:

                    //filaActual.addElement(rs.getString(i));
                    //tuplo.add(rs.getString(i));
                    sTuplo.append(rs.getString(i)).append("\t");
                    break;
                case Types.LONGVARCHAR:
                    //filaActual.addElement(rs.getString(i));
                    //tuplo.add(rs.getString(i));
                    sTuplo.append(rs.getString(i)).append("\t");
                    break;
                case Types.INTEGER:

                    //filaActual.addElement("" + rs.getLong(i));
                    //tuplo.add("" + rs.getLong(i));
                    sTuplo.append(rs.getLong(i)).append("\t");
                    break;
                case Types.DOUBLE:
                    //filaActual.addElement("" + rs.getDouble(i));
                    //tuplo.add("" + rs.getDouble(i));
                    sTuplo.append(rs.getDouble(i)).append("\t");
                    break;
                case Types.SMALLINT:
                    //filaActual.addElement("" + rs.getInt(i));
                    //tuplo.add("" + rs.getInt(i));
                    sTuplo.append(rs.getInt(i)).append("\t");
                    break;
                case Types.TINYINT:
                    //filaActual.addElement("" + rs.getInt(i));
                    //tuplo.add("" + rs.getInt(i));
                    sTuplo.append(rs.getInt(i)).append("\t");
                    break;
                case Types.OTHER:
                    //filaActual.addElement(rs.getString(i));
                    //tuplo.add(rs.getString(i));
                    sTuplo.append(rs.getString(i)).append("\t");
                    break;
                default:
                    //filaActual.addElement(rs.getString(i));
                    //tuplo.add(rs.getString(i));
                    sTuplo.append(rs.getString(i)).append("\t");
                //  System.out.println("El campo es de tipo:  " +
                //                   rsmd.getColumnTypeName(i));
            }
        }
        //System.out.println(sTuplo);
        return sTuplo;
    }
      /* Este mÃ©todo esta diseÃ±ado para obtener una tabla de valores asociados
    a un conjunto de llaves, por ejemplo numeros enzimaticos.
    Todos los valores se almacenan como string, la clase que los usa deberÃ¡ procesar los datos
    en caso de requerirlo.
    @param callStatement: En este parÃ¡metro se escribe la llamada al 
    procedimiento que de vuelve el conjunto de datos.
    @param nombres, en este arreglo se pasan los valores de los parÃ¡metros
    del procedimento almacenado que ha de ejecutarse.
    @param par, arreglo en el que se pasan los parÃ¡metros que usa el procedimiento alamcenado.
    Todos ellos deben ser de tipo string.
    */
    public Map<String, List<String>> getHashTablaByMets(String[] nombres, String ruta, int m ){
        hTabla = new HashMap<>();
        CallableStatement callSt = null;
        
        try{
            callSt = connection.prepareCall("{CALL getMetGData(?, ?, ?)}");
            int nNombres = nombres.length;
        
            callSt.setString(2, ruta);
            callSt.setInt(3, m);
            for (int l=0;l<nNombres;l++){
            callSt.setString(1, nombres[l]);
            boolean hasData = callSt.execute();
            if (hasData==true){
		    ResultSet lrs = callSt.getResultSet();
                    while (lrs.next()){
                        String strKey = lrs.getString(1);
                     	String enz = lrs.getString(2);
			String freq = String.valueOf(lrs.getInt(4));
                        List<String> row = hTabla.get(strKey.trim());
			if (row==null){
			    List<String> rowData = new ArrayList<>();
                            rowData.add(enz);
			    for(int j=1;j<nNombres+1;j++){
				rowData.add(strNull);
			    }
			    rowData.set(l+1, freq);//new Integer(freq);
                            hTabla.put(strKey.trim(), rowData);
                            }
                            else{
                                hTabla.get(strKey.trim()).set(l+1, freq);
                                //System.out.println(hTabla.get(strKey.trim()).get(l+1));
                            }
                        }
                    }
                    
            
                }//fin de for names
            }//fin de try
            catch(SQLException eSQL ){
	    //System.out.println("Algo anda mal");
	    System.out.println("Error: " + eSQL.getMessage());
            }
            catch(Exception e){
		System.out.println(e.getMessage());
            }
            finally{
                if(callSt!=null){
                    try{
                        callSt.close();
                    }
                    catch(SQLException eSQL){
                       System.out.println("Error: " + eSQL.getMessage()); 
                    }
                    
                } 
            }//fin de finally
        
        
        return hTabla;
    }//Fin de getHashTablaByMets
    
        /*public Map<String, List<String>> getHashTablaByMetodo(String nombre, String ruta, int m ){
        hTabla = new HashMap<>();
        CallableStatement callSt = null;
        try{
            callSt = connection.prepareCall("{CALL getMetGData(?, ?, ?)}");
            int n;
            switch (m){
                case 2:
                    n=1;
                    break;
                case 3 :
                    n=1;
                    break;
                default:
                    n=2;
                    break;
            }
            //int n=0;
            callSt.setString(1, nombre);
            callSt.setString(2, ruta);
            callSt.setInt(3, m);
            //int l=0;
            boolean hasData = callSt.execute();
            if (hasData==true){
                //while(callSt.getMoreResults()){
		    ResultSet lrs = callSt.getResultSet();
                    while (lrs.next()){
                        String strKey = lrs.getString(1);
                     	String enz = lrs.getString(2);
			String freq = String.valueOf(lrs.getInt(4));
                        List<String> row = hTabla.get(strKey.trim());
			if (row==null){
			    List<String> rowData = new ArrayList<>();
                            rowData.add(enz);
			    for(int j=1;j<1+n;j++){
				rowData.add("0");
			    }
			    rowData.set(1, freq);//new Integer(freq);
                            hTabla.put(strKey.trim(), rowData);
                            }
                            else{
                                hTabla.get(strKey.trim()).set(1, freq);
                                //System.out.println(hTabla.get(strKey.trim()).get(1));
                            }
                        }
                        if(callSt.getMoreResults()){
                            lrs = callSt.getResultSet();
                            while (lrs.next()){
                            String strKey = lrs.getString(1);
                            String enz = lrs.getString(2);
                            String freq = String.valueOf(lrs.getInt(4));
                            List<String> row = hTabla.get(strKey.trim());
                            if (row==null){
                                List<String> rowData = new ArrayList<>();
                                rowData.add(enz);
                                for(int j=1;j<1+n;j++){
                                    rowData.add("0");
                                }
                                    rowData.set(2, freq);//new Integer(freq);
                                    hTabla.put(strKey.trim(), rowData);
                                }
                                else{
                                    hTabla.get(strKey.trim()).set(2, freq);
                                    //System.out.println(hTabla.get(strKey.trim()).get(1));
                                }
                            }
                            
                            
                        }
                    }
                //l++;
                //}//Fin de getMoreResults
            
                //}//fin de for names
            }//fin de try
            catch(SQLException eSQL ){
	    //System.out.println("Algo anda mal");
	    System.out.println("Error: " + eSQL.getMessage());
            }
            catch(Exception e){
		System.out.println(e.getMessage());
            }
            finally{
                if(callSt!=null){
                    try{
                        callSt.close();
                    }
                    catch(SQLException eSQL){
                       System.out.println("Error: " + eSQL.getMessage()); 
                    }
                    
                } 
            }//fin de finally
        
        
        return hTabla;
    }//Fin de getHashTablaByMetodo*/
    
    
    public Map<String, List<String>> getHashTablaByMetodo(String nombre, String ruta, int m ){
        hTabla = new HashMap<>();
        CallableStatement callSt = null;
        try{
            callSt = connection.prepareCall("{CALL getMetGData(?, ?, ?)}");
            int n;
            if (m==2 || m==3){
            //int n=0
                callSt.setString(1, nombre);
                callSt.setString(2, ruta);
                callSt.setInt(3, m);
                boolean hasData = callSt.execute();
                if (hasData==true){
                        ResultSet lrs = callSt.getResultSet();
                        while (lrs.next()){
                            String strKey = lrs.getString(1);
                            String enz = lrs.getString(2);
                            String freq = String.valueOf(lrs.getInt(4));
                            List<String> row = hTabla.get(strKey.trim());
                            if (row==null){
                                List<String> rowData = new ArrayList<>();
                                rowData.add(enz);
                                for(int j=1;j<3;j++){
                                    rowData.add("0");
                                }
                                rowData.set(1, freq);//new Integer(freq);
                                hTabla.put(strKey.trim(), rowData);
                            }
                            else{
                                hTabla.get(strKey.trim()).set(1, freq);
                            }
                        }
                }
            }
            else if (m==1){               
                callSt.setString(1, nombre);
                callSt.setString(2, ruta);                                
                for (int k=0;k<2;k++){
                        callSt.setInt(3,k+2);
                        boolean hasData = callSt.execute();
                        if (hasData==true){
                        ResultSet lrs = callSt.getResultSet();
                        while (lrs.next()){
                            String strKey = lrs.getString(1);
                            String enz = lrs.getString(2);
                            String freq = String.valueOf(lrs.getInt(4));
                            List<String> row = hTabla.get(strKey.trim());
                            if (row==null){
                                List<String> rowData = new ArrayList<>();
                                rowData.add(enz);
                                for(int j=0;j<2;j++){
                                    rowData.add("0");
                                }
                                rowData.set(k+1, freq);//new Integer(freq);
                                //System.out.println("El dato es: "+ rowData.get(k));
                                hTabla.put(strKey.trim(), rowData);
                            }
                            else{
                                hTabla.get(strKey.trim()).set(k+1, freq);
                            }
                        }  
                    }
                }
            }
            
        }//fin de try
        catch(SQLException eSQL ){
	    //System.out.println("Algo anda mal");
	    System.out.println("Error: " + eSQL.getMessage());
        }
        catch(Exception e){
		System.out.println(e.getMessage());
        }
        finally{
            if(callSt!=null){
                    try{
                        callSt.close();
                    }
                    catch(SQLException eSQL){
                       System.out.println("Error: " + eSQL.getMessage()); 
                    }
            } 
       }//fin de finally      
            return hTabla;
    }//Fin de getHashTablaByMetodo

/*public List<List<String>> getStateMap(String idRuta) {
       List<List<String>> m = new ArrayList<>();
       CallableStatement callSt = null;// = connection.prepareCall("{CALL getCoords(?, ?, ?)}");
       try{
            callSt = connection.prepareCall("{CALL getCoords(?, ?, ?)}");
            callSt.registerOutParameter(2, 4);
            callSt.registerOutParameter(3, 4);
        callSt.setString(1,idRuta);
        boolean hasData = callSt.execute();
        if (hasData==true){
          ResultSet rs = callSt.getResultSet();
                while (rs.next()){
                    int x_0 = rs.getInt(1);
                    int y_0 = rs.getInt(2);
                    String ECStr = rs.getString(3);
                    List<String> row = new ArrayList<>();
                    row.add(ECStr);
                    row.add(String.valueOf(x_0));
                    row.add(String.valueOf(y_0));
                    m.add(row);
                    //System.out.println(row.get(0) + ", " + row.get(1) + ": "+ ECStr);                        
                }
                
                List<String> lastRow = new ArrayList<>();
                int deltaX = callSt.getInt(2);
                int deltaY = callSt.getInt(3);
                lastRow.add("mapa");
                lastRow.add(String.valueOf(deltaX));
                lastRow.add(String.valueOf(deltaY));
                m.add(lastRow);
              }
   
            }//fin de try
            catch(SQLException eSQL ){
        //System.out.println("Algo anda mal");
        System.out.println("Error: " + eSQL.getMessage());
            }
            catch(Exception e){
        System.out.println(e.getMessage());
            }
            finally{
                if(callSt!=null){
                    try{
                        callSt.close();
                    }
                    catch(SQLException eSQL){
                       System.out.println("Error: " + eSQL.getMessage()); 
                    }
                    
                } 
            }//fin de finally
        
       
       return m;
    }//Fin de getStateMap
    */
    
    public List<List<String>> getStateMap(String idRuta) {
       List<List<String>> m = new ArrayList<>();
       CallableStatement callSt = null;
       try{
            callSt = connection.prepareCall("{CALL getCoords(?, ?, ?, ?, ?)}");
            callSt.registerOutParameter(2, 4);
            callSt.registerOutParameter(3, 4);
            callSt.registerOutParameter(4, 4);
            callSt.registerOutParameter(5, 4);
        callSt.setString(1,idRuta);
        boolean hasData = callSt.execute();
        if (hasData==true){
          ResultSet rs = callSt.getResultSet();
                while (rs.next()){
                    int x_0 = rs.getInt(1);
                    int y_0 = rs.getInt(2);
                    String ECStr = rs.getString(3);
                    List<String> row = new ArrayList<>();
                    row.add(ECStr);
                    row.add(String.valueOf(x_0));
                    row.add(String.valueOf(y_0));
                    m.add(row);
                    //System.out.println(row.get(0) + ", " + row.get(1) + ": "+ ECStr);                        
                }
                
                List<String> mapRow = new ArrayList<>(); //Guarda las dimensiones del mapa
                List<String> mapWebRow = new ArrayList<>();//Guarda las dimensiones del mapa que se despliega en la WEB 
                int deltaX = callSt.getInt(2);
                int deltaY = callSt.getInt(3);
                int deltaXW=callSt.getInt(4);
                int deltaYW=callSt.getInt(5);
                mapRow.add("mapaPNG");
                mapRow.add(String.valueOf(deltaX));
                mapRow.add(String.valueOf(deltaY));
                m.add(mapRow);
                mapWebRow.add("MapaWeb");
                mapWebRow.add(String.valueOf(deltaXW));
                mapWebRow.add(String.valueOf(deltaYW));
                m.add(mapWebRow);
                
                
              }
   
            }//fin de try
            catch(SQLException eSQL ){
        //System.out.println("Algo anda mal");
        System.out.println("Error: " + eSQL.getMessage());
            }
            catch(Exception e){
        System.out.println(e.getMessage());
            }
            finally{
                if(callSt!=null){
                    try{
                        callSt.close();
                    }
                    catch(SQLException eSQL){
                       System.out.println("Error: " + eSQL.getMessage()); 
                    }
                    
                } 
            }//fin de finally
        
       
       return m;
    }//Fin de getStateMap
    /**
     * Cierra la copnexión con la BD
     */
    public void shutDown() {
        try {
            connection.close();
        } catch (SQLException sqlex) {
            System.err.println("No se puede desconectar");
            sqlex.printStackTrace();
        }
    }
}
