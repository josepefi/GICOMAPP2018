/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author Alejandro
 */
//import database.Conexion;
//import java.sql.SQLException;
import bobjects.EstacionObj;
import bobjects.Muestreo;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 *
 * @author Alejandro
 */
public class Transacciones {

    Conexion conexion;
    boolean estamosConectados = true;
    String tipoConexion = "";
    private String database;
    private String user;
    private String ip;
    private String password;
    private String query;
    private boolean debug = false;

    public Conexion getConexion() {
        return conexion;
    }

    public Transacciones() {
        conecta(true);
    }

    public Transacciones(boolean local) {
        conecta(local);
    }

    public Transacciones(String database, String user, String ip, String password) {
        this.database = database;
        this.user = user;
        this.ip = ip;
        this.password = password;
        conecta(true);
    }

    public boolean estamosConectados() {
        return estamosConectados;
    }

    public void setEstamosConectados(boolean estamosConectados) {
        this.estamosConectados = estamosConectados;
    }

    public void desconecta() {
        conexion.shutDown();
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getIp() {
        return ip;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList getGenSwissProtDetailsNoTaxaInfo(String gen_id) {
        String query = "SELECT s.uniprot_id, prot_name, gene_name "
                + "FROM gen_swiss_prot AS gsp "
                + "INNER JOIN swiss_prot AS s on s.uniprot_id = gsp.uniprot_id "
                + "WHERE gen_id ='" + gen_id + "'";
        conexion.executePreparedGene(gen_id);
        return conexion.getTabla();
    }

    /**
     * Trae los detalles de un swiss prot para una predicción dada en la
     * relacion gen_swiss_prot Es usado al crear el resultado de blast mientras
     * se lee el archivo out.txt
     *
     * @param gen_id
     * @return
     */
    public ArrayList getGenSwissProtDetails(String gen_id) {
        String query = "SELECT sp.uniprot_id, uniprot_acc, prot_name, eval, name, gene_name "
                + "FROM gen_swiss_prot AS gsp  "
                + "INNER JOIN swiss_prot AS sp on sp.uniprot_id = gsp.uniprot_id "
                + "INNER JOIN ncbi_node ON sp.ncbi_tax_id = ncbi_node.tax_id "
                + "WHERE gsp.gen_id ='" + gen_id + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Trae los detalles (tipo de muestra y profundidad) para una muestra dado
     * un gen predicho en un metagenoma
     *
     * @param gen_id
     * @param meta si es para metagenoma tiene que venir la palabra "meta" para
     * genomas, vacio.
     * @return
     */
    public ArrayList getDetallesMuestraByGen(String gen_id, String meta) {
        String query = "SELECT tipo_muestra, muestreo.profundidad, muestra.idmuestra  "
                + "FROM gen "
                + "INNER JOIN " + meta + "genoma ON " + meta + "genoma.id" + meta + "genoma = gen.id" + meta + "genoma "
                + "INNER JOIN muestra ON muestra.idmuestra = " + meta + "genoma.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN tipo_muestra ON tipo_muestra.idtipomuestra = muestreo.idtipomuestra "
                + "WHERE gen.gen_id = '" + gen_id + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public void conecta(boolean conex) {
        if (conex) {
            // ArchivoIP aip = new ArchivoIP();
            //String[]config =  aip.obtieneIP();
            // conexion = new Conexion(config[1], config[0]);
            conexion = new Conexion(database, ip, user, password);
            //System.out.println(config[1] + "  " + config[0]);
            //  JOptionPane.showMessageDialog(null, config[1],config[0],JOptionPane.INFORMATION_MESSAGE);
            estamosConectados = conexion.seConecto;
            tipoConexion = "remota";
        } else {
            //conexion = new Conexion("mantenimiento", "localhost");
            // conexion = new Conexion("bio", "localhost", "root", "AMORPHIS");
            estamosConectados = conexion.seConecto;
            tipoConexion = "local";
        }
    }

    /**
     * Trae la información básica de un usuario
     *
     * @param idUser
     * @return
     */
    public ArrayList getUser(int idUser) {
        String query = "SELECT nombres, apellidos, correo, terminos FROM usuario WHERE idUsuario = " + idUser;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este query se encarga de traer una lista con el id y el nombre de los
     * sitios que fueron muestreados en una campaña. Un derrotero puede tener
     * estaciones mas no siempre existen muestreos en dicha estación. Este
     * método es usado en CampanaDAO para obtener un resumen de la campana
     *
     * @param idCampana el id de la campaña de la cual necesitamos saber las
     * estaciones muestreadas
     * @return
     */
    public ArrayList getEstacionesMuestreadasFromCampana(int idCampana) {
        String query = " SELECT distinct estacion.idEstacion, estacion_nombre "
                + "FROM estacion INNER JOIN muestreo  ON  muestreo.idEstacion = estacion.idEstacion  "
                + "WHERE idCampana = " + idCampana;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getEstacionCoordsForMuestra(String idMuestra) {
        String query = "SELECT estacion.idestacion, estacion_nombre, muestreo.latitud_r, muestreo.longitud_r "
                + "FROM muestra INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN estacion ON estacion.idestacion = muestreo.idestacion "
                + "WHERE idmuestra = " + idMuestra;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este método se encarga de traer información complementaria al gen, es
     * usado para mostrar el resultado de blast
     *
     * @param gen_id
     * @return
     */
    public ArrayList getInfoComplementariaGenMetagenoma(String gen_id) {
        String query = " SELECT sp.uniprot_id, uniprot_acc, prot_name,ncbi_node.name,tipo_muestra,eval "
                + "FROM gen LEFT JOIN gen_swiss_prot as gsp on gsp.gen_id = gen.gen_id "
                + "LEFT JOIN swiss_prot as sp on sp.uniprot_id = gsp.uniprot_id "
                + "LEFT JOIN ncbi_node on sp.ncbi_tax_id = ncbi_node.tax_id "
                + "INNER JOIN metagenoma ON metagenoma.idmetagenoma = gen.idmetagenoma "
                + "INNER JOIN muestra on muestra.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN tipo_muestra on tipo_muestra.idtipomuestra = muestreo.idtipomuestra "
                + "WHERE gen.gen_id = '" + gen_id + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public void compilePreparedGene() {
        conexion.compilePreparedGene();
    }

    public ArrayList executePreparedGene(String gen_id) {
        conexion.executePreparedGene(gen_id);
        return conexion.getTabla();
    }

    public ArrayList executePreparedMetaGene(String gen_id) {
        conexion.executePreparedMetaGene(gen_id);
        return conexion.getTabla();
    }

    /**
     * Este query se encarga de traer una lista con el id y el nombre de los
     * sitios que fueron muestreados en una campaña. Un derrotero puede tener
     * estaciones mas no siempre existen muestreos en dicha estación. Este
     * método es usado en CampanaDAO para obtener un resumen de la campana
     *
     * @param idCampana el id de la campaña de la cual necesitamos saber las
     * estaciones muestreadas
     * @return
     */
    public ArrayList getEstacionesMuestreadasFromCampana2Map(int idCampana) {
        String query = " SELECT distinct estacion.idestacion, estacion_nombre, estacion.latitud, estacion.longitud "
                + "FROM estacion INNER JOIN muestreo ON estacion.idestacion = muestreo.idEstacion "
                + "WHERE muestreo.idcampana =" + idCampana;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * A diferencia de getEstacionesMuestreadasFromCampana2Map.Este query se
     * encarga de traer los puntos muestreados en una campaña, es decir el punto
     * de la muestra, no el de la estación. Genera una lista con el id y el
     * nombre de los sitios que fueron muestreados en una campaña. Un derrotero
     * puede tener estaciones mas no siempre existen muestreos en dicha
     * estación. Este método es usado en CampanaDAO para obtener un resumen de
     * la campana
     *
     * @param idCampana el id de la campaña de la cual necesitamos saber las
     * estaciones muestreadas
     * @return
     */
    public ArrayList getPuntosMuestreadosFromCampana2Map(int idCampana) {
        String query = " SELECT distinct estacion.idestacion, estacion_nombre, muestreo.latitud_r, muestreo.longitud_r "
                + "FROM estacion INNER JOIN muestreo ON estacion.idestacion = muestreo.idEstacion "
                + "WHERE muestreo.idcampana =" + idCampana;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Trae una llista de todas las campañas en la BD, las trae ordenadas por
     * fecha de término, de esta manera siempre la primera en desplegarse es la
     * última campaña realizada
     *
     * @return
     */
    public ArrayList getAllCampanas() {
        String query = "SELECT idcampana, siglas, nombre FROM campana order by fecha_termino desc";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este método se encarga de traer una lista de ids de marcador y su conteo
     * de acuerdo a un taxon dado
     *
     * @param rank el rango del taxón: phylum, class, ordeN <-- en la BD order
     * es orden-->, etc.
     * @param name el nombre del taxón Bacteria, proteobacteria, etc.
     * @return
     */
    public ArrayList getConteosMarcadorPorTaxon(String rank, String name) {
        String query = "SELECT idmarcador, count(idseq_marcador) "
                + "FROM seq_marcador AS sm "
                + "INNER JOIN taxon AS t on t.tax_id = sm.taxon_tax_id "
                + "WHERE  " + rank + "='" + name + "' group by idmarcador";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este método se encarga traer algunos de datos de la tabla taxon de
     * acuerdo a un tax_id dado.
     */
    public ArrayList getAgregarTaxon(String tax_id) {
        String query = "SELECT tax_id,taxon,rank "
                + "FROM taxon "
                + "WHERE tax_id=" + tax_id;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Identificador único para la libreria
     *
     * @param idLibreria
     * @return
     */
    public ArrayList getLibreria(String idLibreria) {
        String query = "SELECT fuente, selection, layout, vector, screen, metodo "
                + "FROM libreria "
                + "WHERE idlibreria=" + idLibreria;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este métoodo calcula lo mismo que el método: getConteosMarcadorPorTaxo
     * pero en lugar de hacer los conteos por medio de seq_marcador, lo hace
     * directamente en la tabla conteos lo que optimiza de manera muy importante
     * la búsqueda
     *
     * @param rank
     * @param name
     * @param idAnalisis el id del analisis 1 metaxa 2 = parallel meta
     * @return
     */
    public ArrayList getConteosMarcadorPorTaxonOptimized(String rank, String name, String idAnalisis) {
        String query = "SELECT idmarcador, sum(counts) "
                + "FROM conteos "
                + "INNER JOIN taxon ON taxon.tax_id = conteos.tax_id "
                + "WHERE  " + rank + "='" + name + "' AND idanalisis_clasificacion = " + idAnalisis + " group by idmarcador";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getConteosMetagenomaPorTaxonOptimized(String rank, String name) {
        String query = "SELECT idmetagenoma, sum(counts) "
                + "FROM conteos_shotgun "
                + "INNER JOIN taxon ON taxon.tax_id = conteos_shotgun.tax_id "
                + "WHERE  " + rank + "='" + name + "' group by idmetagenoma";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este método se encarga de crear una matriz de abundancia para algún corte
     * taxonómico en específico.
     *
     * @param niveles este parámetro se espera que venga con los niveles
     * taxonómicos sobre los cuales se hace la búsqueda
     * @param marcadores lista de ids de marcadores separados por coma. Se
     * espera que entre los dos parámetros se forme un query similar al
     * siguiente: " select distinct (concat(kingdom ,'\t', phylum ,'\t',
     * class,'\t', orden ,'\t', family,'\t',genus)) as phy,
     * count(idseq_marcador) from seq_marcador inner join taxon on tax_id =
     * taxon_tax_id where idmarcador = 119 group by phy order by phy"
     * @return matriz de abundancia.
     */
    public ArrayList getMatrizPorMarcadores(String niveles, String marcadores) {
        String query = "SELECT " + niveles + " AS phy, count(idseq_marcador) "
                + "FROM seq_marcador "
                + "INNER JOIN taxon AS t on t.tax_id = taxon_tax_id "
                + "WHERE  idMarcador IN(" + marcadores + ") "
                + "GROUP BY phy "
                + "ORDER BY phy";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMatrizPorMarcadoresNew(String niveles, String marcadores, String idanalisis, String where) {
        String query = "SELECT " + niveles + " AS phy, sum(counts), conteos.tax_id  "
                + "FROM conteos "
                + "INNER JOIN taxon AS t on t.tax_id = conteos.tax_id "
                + "WHERE  idMarcador IN(" + marcadores + ") "
                + "AND idanalisis_clasificacion =  " + idanalisis + " "
                + where
                + "GROUP BY phy "
                + "ORDER BY phy";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMatrizPorMetagenoma(String niveles, String metagenomas, String where) {
        String query = "SELECT " + niveles + " AS phy, sum(counts), conteos_shotgun.tax_id  "
                + "FROM conteos_shotgun "
                + "INNER JOIN taxon AS t on t.tax_id = conteos_shotgun.tax_id "
                + "WHERE  idmetagenoma IN(" + metagenomas + ") "
                + where
                + "GROUP BY phy "
                + "ORDER BY phy";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este método se encarga de traer todos los taxones distintos para todos
     * los marcadores anotados o en su defecto los marcadores dada una condicón
     * where
     *
     * @param niveles
     * @param where
     * @return
     */
    public ArrayList getDistinctTaxones(String niveles, String where) {
        String query = "SELECT " + niveles + " AS phy, taxon.tax_id "
                + "FROM taxon "
                + "INNER JOIN conteos ON conteos.tax_id = taxon.tax_id "
                + where
                + " GROUP BY phy "
                + " ORDER BY phy";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Se encarga de traer todas las muestras diferetnes, este método se usa en
     * la generaación de matrices
     *
     * @param where
     * @return
     */
    public ArrayList getDistinctMuestras(String where) {
        String query = "SELECT DISTINCT idmuestra "
                + "FROM muestra INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN estacion ON estacion.idestacion = muestreo.idestacion "
                + "WHERE " + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public int getTaxaCountByMarcador(String idMarcador, String tax_id) {
        String query = "SELECT counts FROM conteos "
                + "WHERE idmarcador = " + idMarcador
                + " AND tax_id = " + tax_id;
        conexion.executeStatement(query);
        //   System.out.println(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = 0;
        if (dbResult == null || dbResult.isEmpty()) {
            id = 0;
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = 0;
            }
        }
        return id;
    }

    /**
     * En las búsquedas taxonómicas, las columnas phylum, familia, etc ayudan a
     * crear las búsquedas en cascada pues de acuerdo a su classifcación
     * taxonómica se toma cualquiera de estos campos y a partir de ahí se
     * obtiene todos los taxones con dicho nivel taxonómico, sin embargo para
     * las cosas no raanqueadas, esto no sirve por lo cual se usa este query
     * para ver las abundancias de cosas no clasificadas
     *
     * @param rank
     * @param name
     * @return
     */
    public ArrayList getConteosMarcadorPorTaxonNoRank(String taxID) {
        String query = "SELECT idmarcador, count(idseq_marcador) "
                + "FROM seq_marcador AS sm WHERE taxon_tax_id = " + taxID;

        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMarcadoresByCampana(int idCampana) {
        String query = "SELECT estacion.idestacion, estacion_nombre, idmarcador, marc_name, "
                + "muestra.idmuestra, muestra.etiqueta, tipo_profundidad, muestreo.profundidad "
                + "FROM marcador INNER JOIN muestra ON muestra.idmuestra = marcador.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN estacion ON estacion.idestacion = muestreo.idestacion "
                + "WHERE idcampana = " + idCampana;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMetagenomasByCampana(int idCampana) {
        String query = "SELECT estacion.idestacion, estacion_nombre, idmetagenoma, meta_name, "
                + "muestra.idmuestra, muestra.etiqueta, tipo_profundidad, muestreo.profundidad "
                + "FROM metagenoma INNER JOIN muestra ON muestra.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN estacion ON estacion.idestacion = muestreo.idestacion "
                + "WHERE idcampana = " + idCampana;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getGenomasByCampana(int idCampana) {
        String query = "SELECT estacion.idestacion, estacion_nombre, idgenoma, genome_name, "
                + "muestra.idmuestra, muestra.etiqueta, tipo_profundidad, muestreo.profundidad "
                + "FROM genoma INNER JOIN muestra ON muestra.idmuestra = genoma.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN estacion ON estacion.idestacion = muestreo.idestacion "
                + "WHERE idcampana = " + idCampana;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getTermData(String idTerm) {
        String query = "SELECT t.idOntologia,o.name, t.name, definition, is_a, namespace, "
                + "relationship, is_obsolete, replaced_by, t.url, comment "
                + "FROM term as t left join ontologia as o ON o.idontologia = t.idontologia where idterm = '" + idTerm + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este método trae los distintos taxones para un marcador dado un taxon.
     * Este método es usado para traer los taxones cuando el usuario quiere
     * descargar secuencias del resultado de taxaSearch. Por ejemplo, sie el
     * usuario busca pseudomonas y luego quiere bajar esas secuencias, primero
     * se necesita saber también que especies de esas pseudomonas tiene. REste
     * query nos da esa información
     *
     * @param idMarcador El id del marcador en cuestion
     * @param rank el rango del marcador, ejemplo genus
     * @param valueRank el valor del rango, ejemplo pseudomonas
     * @return los diferentes tax_ids para esa búsqueda.
     */
    public ArrayList getTaxonesByTaxonMarcador(String idMarcador, String rank, String valueRank, String idanalisis) {
        String query = "SELECT DISTINCT taxon.tax_id, rank, taxon FROM taxon "
                + "INNER JOIN conteos ON conteos.tax_id = taxon.tax_id "
                + "WHERE idmarcador = " + idMarcador + " AND "
                + rank + "= '" + valueRank + "' "
                + "AND idanalisis_clasificacion = " + idanalisis;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este método se encarga de construir un query para traer las secuencias de
     * marcadores dada un lista de taxones
     *
     * @param taxIDS lista de tax_ids separada por coma
     * @param idMarcador los marcadores sobre los que se busca
     * @param parallel dependiendo del tipo de analisis busca en laa tabla
     * seq_marcador_classif o seq_marcador_classif_parallel. Si la intención es
     * servir las secuencias basadaas en la predicción de parallel, se espera
     * que ese string venga con el valor '_parallel'. Caso contrario tendrá que
     * venir en blanco
     * @return
     */
    public ArrayList getSecuenciasByTaxIdsAndMarcador(String taxIDS, String idMarcador, String parallel) {
        String query = "SELECT tax_id, seq_marcador.idseq_marcador, identity, score, seq, eval "
                + "FROM seq_marcador "
                + "INNER JOIN seq_marcador_classif" + parallel + " ON seq_marcador_classif" + parallel + ".idseq_marcador = seq_marcador.idseq_marcador "
                + "WHERE tax_id IN (" + taxIDS + ") AND idmarcador = " + idMarcador;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getSequenceWithProtInfo(String genIDList, String seqType) {
        String query = "SELECT DISTINCT gen.gen_id, gen_src, sequence, gen_strand, seq_from, "
                + "seq_to,gsp.uniprot_id, prot_name, gene_name "
                + "FROM gen "
                + "INNER JOIN gen_seq on gen_seq.gen_id = gen.gen_id "
                + "LEFT JOIN gen_swiss_prot as gsp on gsp.gen_id = gen_seq.gen_id "
                + "LEFT JOIN swiss_prot as s on s.uniprot_id = gsp.uniprot_id "
                + "WHERE gen.gen_id in(" + genIDList + ") "
                + "AND seq_type = '" + seqType + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getSequenceWithoutProtInfo(String genIDList, String seqType) {
        String query = "SELECT DISTINCT gen.gen_id, gen_src, sequence, gen_strand, seq_from, seq_to "
                + "FROM gen "
                + "INNER JOIN gen_seq on gen_seq.gen_id = gen.gen_id "
                + "WHERE gen.gen_id in(" + genIDList + ") "
                + "AND seq_type = '" + seqType + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     *
     * @param idUsuario
     * @return
     */
    public ArrayList getBlastJobsByUser(int idUsuario) {
        String query = "SELECT idblast_job, job_url FROM blast_job  "
                + "WHERE idusuario = " + idUsuario
                + " ORDER BY start_date desc";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getBlastMetaDbs(String blastJob) {
        String query = "SELECT bjm.idmetagenoma, meta_name "
                + "FROM blast_job_metagenoma AS bjm "
                + "INNER JOIN metagenoma ON metagenoma.idmetagenoma = bjm.idmetagenoma "
                + "WHERE bjm.idblast_job = " + blastJob;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getBlastGenoDbs(String blastJob) {
        String query = "SELECT bjg.idgenoma, genome_name "
                + "FROM blast_job_genoma AS bjg "
                + "INNER JOIN genoma ON genoma.idgenoma = bjg.idgenoma "
                + "WHERE bjg.idblast_job = " + blastJob;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public boolean deleteBlastJob(String idJob) {
        String query = "DELETE FROM blast_job WHERE idblast_job = " + idJob;
        return conexion.queryUpdate(query);
    }

    public boolean deleteBlastMetaBase(String idJob) {
        String query = "DELETE FROM blast_job_metagenoma WHERE idblast_job = " + idJob;
        return conexion.queryUpdate(query);
    }

    public boolean deleteBlastGenoBase(String idJob) {
        String query = "DELETE FROM blast_job_genoma WHERE idblast_job = " + idJob;
        return conexion.queryUpdate(query);
    }

    public String getIsGenoma(String idGen) {
        String query = "SELECT IF (gen_src='GEN','TRUE','FALSE') FROM gen WHERE gen_id ='" + idGen + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        if (dbResult != null && !dbResult.isEmpty()) {
            return (String) dbResult.get(0).get(0);
        } else {
            return "";
        }

    }

    public ArrayList getGenSecuencias(String idGen) {
        String query = "SELECT seq_type, sequence from gen_seq where gen_id ='" + idGen + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getGenEggnog(String idGen) {
        String query = "SELECT eggnog.ideggnog, eggnog.description, eggnog.proteins, eggnog.species, eval "
                + " FROM eggnog INNER JOIN gen_eggnog "
                + " ON gen_eggnog.ideggnog = eggnog.ideggnog "
                + " WHERE gen_id ='" + idGen + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getPfam(String idGen) {
        String query = "SELECT gp.pfam_acc, gp.pfam_from, gp.pfam_to, gp.eval, clan_acc, id_pfam, pfam_deff "
                + " FROM gen_pfam AS gp "
                + " INNER JOIN pfam on pfam.pfam_acc = gp.pfam_acc  "
                + " WHERE gp.gen_id ='" + idGen + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }
    public ArrayList getGenGo(String idGen) {
        String query = "SELECT go.id_GO, go.go_name, go.namespace, go.url ,go.definition "
                + " FROM gontology AS go "
                + " INNER JOIN gen_go on gen_go.id_GO = go.id_GO  "
                + " WHERE gen_go.gen_id ='" + idGen + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }
      public ArrayList getGenSwissProtByIDMethod(String idGen, String method) {
        String query = " SELECT gs.uniprot_id, uniprot_acc, prot_name, gene_name, eval, identity, "
                + "query, ncbi_tax_id, taxon FROM gen_swiss_prot as gs "
                + "inner join swiss_prot as s on gs.uniprot_id = s.uniprot_id "
                + "inner join taxon on taxon.tax_id = s.ncbi_tax_id "
                + "where gen_id = '"+idGen+"' AND prediction_method = '"+ method+ "'";
                
        conexion.executeStatement(query);
        return conexion.getTabla();
    }
    public ArrayList getGenMetagenoma(String idGen) {
        String query = "SELECT gen_id, gen_type, contig_id, contig_gen_id, gen_strand, gen_src, m.idmetagenoma, m.meta_name, gen_length, contig_from, contig_to, muestra.idmuestra, muestra.etiqueta, muestra.profundidad, tt.tipo_muestra "
                + "FROM gen INNER JOIN metagenoma AS m "
                + " ON m.idmetagenoma = gen.idmetagenoma "
                + " INNER JOIN muestra ON muestra.idmuestra = m.idmuestra "
                + " INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                + " INNER JOIN tipo_muestra as tt on tt.idtipomuestra = muestreo.idtipomuestra "
                + " where gen_id ='" + idGen + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getGenGenoma(String idGen) {
        String query = "SELECT gen_id, gen_type, contig_id, contig_gen_id, gen_strand, gen_src,g.idgenoma, g.genome_name, gen_length, contig_from, contig_to, "
                + " muestra.idmuestra, muestra.etiqueta, muestra.profundidad, tt.tipo_muestra "
                + " FROM gen INNER JOIN genoma AS g "
                + " ON g.idgenoma = gen.idgenoma "
                + " INNER JOIN muestra ON muestra.idmuestra = g.idmuestra "
                + " INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                + " INNER JOIN tipo_muestra as tt on tt.idtipomuestra = muestreo.idtipomuestra "
                + " where gen_id ='" + idGen + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Trae la información de todos los genomas dada una consición
     *
     * @param where
     * @return
     */
    public ArrayList getGenomas(String where) {
        String query = "SELECT * from genoma " + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getGenoma(String idGenoma) {
        String query = "SELECT muestra.idMuestra, muestra.etiqueta, genoma.genome_name, genoma.genome_desc, genoma.tax_id, genoma.strain, "
                + "genoma.crecimiento, genoma.version, latitud_r, longitud_r, cantidad_dna, clean_up_kit, clean_up_method, analisis, "
                + "referencia_anot, finishing_strategy, procesamiento, cite, gen_num_total, ts.nombre, ts.descripcion, "
                + "CONCAT(marca, ' ', modelo), genoma.comentarios, respaldo, idstats, genoma.idlibreria, idensamble, esTranscriptoma, "
                + "condicion_trans, estacion_nombre, muestreo.profundidad,  tipo_muestra, cs.nombre_centro "
                + "FROM genoma INNER JOIN tipo_secuenciacion AS ts ON ts.idtipo_secuenciacion = genoma.idtipo_secuenciacion "
                + "INNER JOIN secuenciador ON secuenciador.idSecuenciador = genoma.idSecuenciador "
                + "INNER JOIN centro_secuenciacion as cs ON cs.idcentro = secuenciador.idcentro "
                + "INNER JOIN muestra ON muestra.idmuestra = genoma.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN estacion ON muestreo.idestacion = estacion.idestacion "
                + "INNER JOIN tipo_muestra ON muestreo.idtipomuestra = tipo_muestra.idtipomuestra "
                + "WHERE idgenoma = " + idGenoma;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMetagenomas(String where) {
        String query = "SELECT * from metagenoma " + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMetagenoma(String idMetagenoma) {
        String query = " SELECT muestra.idmuestra, muestra.etiqueta, meta_name, meta_desc, "
                + "medio_cultivo, latitud_r, longitud_r ,ts.nombre, ts.descripcion, CONCAT(marca, ' ', modelo), "
                + "cantidad_dna, kit, metodo, library_selection, library_layout, metagenoma.comentarios, idstats, idensamble "
                + "FROM metagenoma INNER JOIN tipo_secuenciacion AS ts ON ts.idtipo_secuenciacion = metagenoma.idtipo_secuenciacion "
                + "INNER JOIN secuenciador ON secuenciador.idSecuenciador = metagenoma.idSecuenciador "
                + "INNER JOIN muestra ON muestra.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "WHERE idmetagenoma = " + idMetagenoma;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getNewMetagenoma(String idMetagenoma) {
        String query = " SELECT mu.idmuestra, mu.etiqueta, tt.tipo_muestra, muestreo.profundidad, meta_name, meta_desc, "
                + "medio_cultivo, procesamiento, analisis, latitud_r, longitud_r ,ts.nombre, ts.descripcion, CONCAT(marca, ' ', modelo), "
                + "cs.nombre_centro, cantidad_dna, clean_up_kit, clean_up_method, metagenoma.comentarios, idstats, idensamble,idlibreria, cite, "
                + "gen_num_total, esTranscriptoma, condicion_trans, estacion_nombre "
                + "FROM metagenoma INNER JOIN tipo_secuenciacion AS ts ON ts.idtipo_secuenciacion = metagenoma.idtipo_secuenciacion "
                + "INNER JOIN secuenciador ON secuenciador.idSecuenciador = metagenoma.idSecuenciador "
                + "INNER JOIN muestra AS mu ON mu.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = mu.idmuestreo "
                + "INNER JOIN centro_secuenciacion as cs ON cs.idcentro = secuenciador.idcentro "
                + "INNER JOIN estacion ON estacion.idestacion = muestreo.idestacion "
                + "INNER JOIN tipo_muestra as tt on tt.idtipomuestra = muestreo.idtipomuestra "
                + "WHERE idmetagenoma = " + idMetagenoma;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Trae la información de los metagenomas cuando son presentados en la tabla
     * para seleccionar el target en la página de blast
     *
     * @param where
     * @return
     */
    public ArrayList getMetagenomasBlast(String where) {
        String query = " SELECT idmetagenoma, meta_name, tipo_muestra, estacion_nombre "
                + "FROM metagenoma INNER JOIN muestra ON muestra.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN tipo_muestra ON tipo_muestra.idtipomuestra = muestreo.idtipomuestra  "
                + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "INNER JOIN estacion on estacion.idestacion = derrotero.idestacion "
                + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
//+ "WHERE seq_num_total > 0";
    }

    /**
     * Trae la información de los Genomas cuando son presentados en la tabla
     * para seleccionar el target en la página de blast
     *
     * @param where
     * @return
     */
    public ArrayList getGenomasBlast(String where) {
        String query = "SELECT idgenoma, genome_name, name, estacion_nombre "
                + "FROM genoma INNER JOIN muestra ON muestra.idmuestra = genoma.idmuestra "
                + "INNER JOIN ncbi_node ON ncbi_node.tax_id = genoma.tax_id  "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                // + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "INNER JOIN estacion on estacion.idestacion = muestreo.idestacion "
                //Quizas poner un inner join con genes para asegurar que no sean genomas vacios
                + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
//+ "WHERE seq_num_total > 0";
    }

    /**
     * Autentica un usuario en la BD
     *
     * @param user
     * @param pass
     * @return
     */
    public int authentUser(String user, String pass) {
        String query = "SELECT idusuario "
                + "FROM usuario WHERE correo='" + user + "' AND password=MD5('" + pass + "')";

        conexion.executeStatement(query);
        //   System.out.println(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = -1;
        if (dbResult == null || dbResult.isEmpty()) {
            id = -1;
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = -1;
            }
        }
        return id;
    }

    public ArrayList getLinaje(String tax_id) {
        String query = "SELECT tax_id, kingdom, phylum, class, orden, family , genus , species, subspecies from taxon where tax_id = " + tax_id;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getArchivosMarcador(String idMarcador) {
        String query = "SELECT idarchivo FROM marcador_archivo where idmarcador = " + idMarcador;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getArchivosMetagenoma(String idMetagenoma) {
        String query = "SELECT idarchivo FROM metagenoma_archivo where idmetagenoma = " + idMetagenoma;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getArchivosGenoma(String idGenoma) {
        String query = "SELECT idarchivo FROM genoma_archivo where idgenoma = " + idGenoma;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMarcadores(String where) {
        String query = "SELECT marcador.idmarcador, marc_name FROM marcador " + where + " ORDER BY marc_name";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getMarcador(String idMarcador) {
        String query = "SELECT idmarcador, m.idMuestra,mu.etiqueta, m.idtipo_marcador,genes,subfragment, "
                + "m.idtipo_secuenciacion,ts.nombre, ts.descripcion, m.idSecuenciador, s.marca, s.modelo, "
                + "m.idpcr, marc_name, marc_desc, seq_num_total, library_selection,  library_layout, library_vector, "
                + "raw_data_path, pro_data_path, data_pre_process, data_qc, idstats, cantidad_dna, m.comentarios  "
                + "FROM marcador AS m INNER JOIN tipo_marcador AS tp ON tp.idtpo_marcador = m.idtipo_marcador "
                + "INNER JOIN muestra AS mu ON mu.idmuestra = m.idmuestra "
                + "INNER JOIN secuenciador AS s ON s.idsecuenciador = m.idsecuenciador "
                + "INNER JOIN tipo_secuenciacion as ts ON ts.idtipo_secuenciacion = m.idtipo_secuenciacion "
                + "WHERE m.idmarcador = " + idMarcador;

        conexion.executeStatement(query);
        return conexion.getTabla();
//+ "WHERE seq_num_total > 0";
    }

    public ArrayList getNewMarcador(String idMarcador) {
        String query = "SELECT idmarcador, m.idMuestra,mu.etiqueta, m.idtipo_marcador,genes,subfragment, "
                + "m.idtipo_secuenciacion,ts.nombre, ts.descripcion, m.idSecuenciador, s.marca, s.modelo, cs.nombre_centro,  "
                + "m.idpcr, idlibreria, idstats, marc_name, marc_desc, raw_data_path, pro_data_path, analisis, clean_up_kit, "
                + "clean_up_method, cantidad_dna, m.comentarios, cite, seq_num_total, procesamiento, estacion_nombre, latitud_r, "
                + "longitud_r,muestreo.profundidad, tt.tipo_muestra "
                + "FROM marcador AS m INNER JOIN tipo_marcador AS tp ON tp.idtpo_marcador = m.idtipo_marcador "
                + "INNER JOIN muestra AS mu ON mu.idmuestra = m.idmuestra "
                + "INNER JOIN secuenciador AS s ON s.idsecuenciador = m.idsecuenciador "
                + "INNER JOIN tipo_secuenciacion as ts ON ts.idtipo_secuenciacion = m.idtipo_secuenciacion "
                + "INNER JOIN centro_secuenciacion as cs ON cs.idcentro = s.idcentro "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = mu.idmuestreo "
                + "INNER JOIN estacion ON estacion.idestacion = muestreo.idestacion "
                + "INNER JOIN tipo_muestra as tt on tt.idtipomuestra = muestreo.idtipomuestra "
                + "WHERE m.idmarcador = " + idMarcador;

        conexion.executeStatement(query);
        return conexion.getTabla();
//+ "WHERE seq_num_total > 0";
    }

    public ArrayList getPCR(String idPCR) {
        String query = "SELECT fw_primer, rv_primer, primers_ref, comentarios, pcr_cond "
                + "FROM pcr WHERE idpcr = " + idPCR;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getArchivo(String idArchivo) {
        String query = "SELECT t.idtipo_archivo, nombre_tipo, t.descripcion, nombre, extension, path, checksum, "
                + "archivo.descripcion, fecha, alcance, editor, derechos, origen, tags, tipo, size "
                + "FROM archivo INNER JOIN tipo_archivo AS t ON t.idtipo_archivo = archivo.idtipo_archivo "
                + "WHERE idarchivo = " + idArchivo;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getArchivosByUser(String idArchivo) {
        String query = "SELECT usuario.idusuario, nombres, apellidos, reltype, comentarios "
                + "FROM usuario "
                + "INNER JOIN usuario_archivo ON usuario_archivo.idusuario = usuario.idusuario "
                + "WHERE idarchivo = " + idArchivo;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Este query trae todos los datos para presnetar en la lista de metagenomas
     *
     * @param where
     * @return
     */
    public ArrayList getListaMetagenomas(String where) {
        String query = "SELECT CONCAT('showMetagenoma?idMetagenoma=',idmetagenoma), meta_name, c.nombre, e.idestacion, e.estacion_nombre,mu.etiqueta,"
                + "CONCAT('showMuestra?idMuestra=',m.idmuestra), m.etiqueta, mu.profundidad,meta_desc "
                + "FROM metagenoma "
                + "INNER JOIN muestra AS m ON m.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo AS mu ON mu.idmuestreo = m.idmuestreo "
                + "INNER JOIN estacion AS e ON e.idestacion = mu.idestacion "
                + "INNER JOIN campana AS c ON c.idcampana = mu.idcampana "
                + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getListaGenomas(String where) {
        String query = " SELECT CONCAT('showGenoma?idGenoma=',idgenoma), genome_name, c.nombre, e.idestacion, e.estacion_nombre,mu.etiqueta,"
                + "CONCAT('showMuestra?idMuestra=',m.idmuestra), m.etiqueta, mu.profundidad FROM genoma "
                + "INNER JOIN muestra AS m ON m.idmuestra = genoma.idmuestra "
                + "INNER JOIN muestreo AS mu ON mu.idmuestreo = m.idmuestreo "
                + "INNER JOIN estacion AS e ON e.idestacion = mu.idestacion "
                + "INNER JOIN campana AS c ON c.idcampana = mu.idcampana "
                + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getListaMarcadores(String where) {
        String query = "SELECT CONCAT('showMarcador?idMarcador=',idmarcador), marc_name, c.nombre, e.idestacion, e.estacion_nombre,"
                + "mu.etiqueta,CONCAT('showMuestra?idMuestra=',m.idmuestra), m.etiqueta, mu.profundidad "
                + "FROM marcador "
                + "INNER JOIN muestra AS m ON m.idmuestra = marcador.idmuestra "
                + "INNER JOIN muestreo AS mu ON mu.idmuestreo = m.idmuestreo "
                + "INNER JOIN estacion AS e ON e.idestacion = mu.idestacion "
                + "INNER JOIN campana AS c ON c.idcampana = mu.idcampana "
                + where;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }
    
        public ArrayList getListaMuestra(String where) {
        String query = "SELECT m.idmuestra, m.etiqueta, c.nombre, estacion_nombre as e, mu.etiqueta, fecha_i, mu.profundidad, count(idmarcador), count(idmetagenoma), COUNT(idgenoma) "
                + " FROM muestreo as mu INNER JOIN muestra AS m ON m.idmuestreo = mu.idmuestreo "
                + " INNER JOIN estacion ON estacion.idestacion = mu.idestacion "
                + " INNER JOIN campana AS c ON c.idcampana = mu.idcampana "
                + " LEFT JOIN marcador ON marcador.idmuestra = m.idmuestra  "
                + " LEFT JOIN metagenoma ON metagenoma.idmuestra = m.idmuestra "
                + " LEFT JOIN genoma ON genoma.idmuestra = m.idmuestra "
                + " GROUP BY m.idmuestra ";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }  

     public ArrayList getListaMetGe(String where) {
        String query = "SELECT CONCAT('showMetagenoma?idMetagenoma=',idmetagenoma), meta_name,meta_desc from metagenoma\n" +
                        "UNION\n" +
                        "SELECT CONCAT('showGenoma?idGenoma=',idgenoma), genome_name,genome_desc from genoma where idGenoma>2";
        conexion.executeStatement(query);
        return conexion.getTabla();
    }    
        
    public ArrayList getEnsambleByID(String idEnsamble) {
        String query = "SELECT ensamblador, comentarios, contigs, contig_lon, contig_avg, n5090, lecturas_mapeadas "
                + "FROM ensamble "
                + "WHERE idensamble = " + idEnsamble;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    public ArrayList getStats(String idStats) {
        String query = "SELECT lecturas, bases, long_avg, gc_prc, qc_avg, ns_prc, q20, q30, combined_prc "
                + "FROM stats WHERE idstats = " + idStats;
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    public ArrayList getDegradadorasByGenus() {
        String query = " SELECT kingdom, phylum, class, orden, family, genus, tax_id "
                + "FROM taxon "
                + "WHERE degradadora = 1 "
                + "ORDER BY kingdom, phylum, class, orden, family, genus";
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    public int getSecuenciasByMarcador(String idMarcador) {
        String query = "SELECT seq_num_total  "
                + "FROM marcador WHERE idmarcador='" + idMarcador + "'";

        conexion.executeStatement(query);
        //   System.out.println(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int counts = 1;
        if (dbResult == null || dbResult.isEmpty()) {
            counts = 1;
        } else {
            try {
                counts = Integer.parseInt((String) dbResult.get(0).get(0));
                if (counts == 0) {
                    counts = 1;
                }
            } catch (NumberFormatException nfe) {
                counts = 1;
            }
        }
        return counts;
    }

    public int getSecuenciasByMetagenoma(String idMetagenoma) {
        String query = "SELECT tax_num_total  "
                + "FROM metagenoma WHERE idmetagenoma='" + idMetagenoma + "'";

        conexion.executeStatement(query);
        //   System.out.println(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int counts = 1;
        if (dbResult == null || dbResult.isEmpty()) {
            counts = 1;
        } else {
            try {
                counts = Integer.parseInt((String) dbResult.get(0).get(0));
                if (counts == 0) {
                    counts = 1;
                }
            } catch (NumberFormatException nfe) {
                counts = 1;
            }
        }
        return counts;
    }

    /**
     * Trae el id de la campana mas reciente, es usado en el home para mostrar
     * la útima campaña por default
     *
     * @return
     */
    public int getLastIDCampana() {
        String query = "SELECT idcampana "
                + "FROM campana ORDER BY fecha_termino DESC";

        conexion.executeStatement(query);
        //   System.out.println(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = -1;
        if (dbResult == null || dbResult.isEmpty()) {
            id = -1;
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = -1;
            }
        }
        return id;
    }

    public int createJob(int idUsuario, String jobName, String jobUrl, String job_type, double eval, String path, String host, String status, String message) {
        String query = "INSERT INTO blast_job (idblast_job, idUsuario, job_name, job_url, job_type, eval, path, host, "
                + "start_date, end_date, status, message) "
                + "values(0," + idUsuario + ",'" + jobName + "','" + jobUrl + "','" + job_type + "'," + eval + ",'" + path + "','" + host
                + "',NOW(), NULL, '" + status + "','" + message + "')";
        // System.out.println(query);
        return conexion.queryUpdateWithKey(query);
    }

    /**
     * Obtiene cual es el max id de muestro para poder asignar nuevos ya que no
     * esta declarado como auto_increment
     *
     * @return
     */
    public int getMaxIDMuestreo() {
        String query = "SELECT MAX(idMuestreo) FROM muestreo";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = -1;
        if (dbResult == null || dbResult.isEmpty()) {
            id = -1;
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = -1;
            }
        }
        return ++id;

    }

    /**
     * Este query trae el conteo de muestreos (no muetsras) realizadas en una
     * estacion para cierta campaña y un tipo de muestra dado. Este query sirve
     * de apoyo para construir la tabla de resumen de camaña mostrada en home
     * usado en CampanaDao.getResumenCampana
     *
     * @param idCampana
     * @param idEstacion
     * @param idTipoMuestra
     * @return
     */
    public int getConteoMuestreosCampanaEstacionTipoMuestra(int idCampana, int idEstacion, int idTipoMuestra) {
        String query = "SELECT count(idMuestreo) FROM muestreo "
                + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE derrotero.idcampana = " + idCampana + " AND derrotero.idEstacion = " + idEstacion
                + " AND idTipomuestra = " + idTipoMuestra;
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;
    }

    /**
     * Este query trae el conteo de muestreos (no muetsras) realizadas en una
     * estacion para cierta campaña y un tipo de muestra dado. Este query sirve
     * de apoyo para construir la tabla de resumen de camaña mostrada en home
     * usado en CampanaDao.getResumenCampana
     *
     * @param idCampana
     * @param idEstacion
     * @param tipoProfundidad
     * @return
     */
    public int getConteoMuestreosCampanaEstacionTipoProfundidad(int idCampana, int idEstacion, String tipoProfundidad) {
        /*  String query = "SELECT count(idMuestreo) FROM muestreo "
         + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
         + "WHERE derrotero.idcampana = " + idCampana + " AND derrotero.idEstacion = " + idEstacion
         + " AND tipo_profundidad = '" + tipoProfundidad + "'";*/
        String query = "SELECT count(idMuestreo) FROM muestreo "
                // + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE idcampana = " + idCampana + " AND idEstacion = " + idEstacion
                + " AND tipo_profundidad = '" + tipoProfundidad + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;

    }

    /**
     * Este query trae el conteo de amplicones realizados de una muestra de una
     * estacion para cierta campaña y un tipo de profundidad. Este query sirve
     * de apoyo para construir la tabla de resumen de camaña mostrada en home
     * usado en CampanaDao.getResumenCampanaRegistro
     *
     * @param idCampana la campaña
     * @param idEstacion la estacion
     * @param tipoProfundidad el tipo de profundidad muestreada: FONDO, MAX_F,
     * MIL, MIN_O, SED
     * @return
     */
    public int getConteoAmpliconesCampanaEstacionTipoProfundidad(int idCampana, int idEstacion, String tipoProfundidad) {
        String query = "SELECT COUNT(idmarcador) FROM marcador "
                + "INNER JOIN muestra ON muestra.idmuestra = marcador.idmuestra "
                + "INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                //   + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE idcampana = " + idCampana + " AND idEstacion = " + idEstacion
                + " AND tipo_profundidad = '" + tipoProfundidad + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;

    }

    /**
     * Este query trae el conteo de metagenomas realizados de una muestra de una
     * estacion para cierta campaña y un tipo de profundidad. Este query sirve
     * de apoyo para construir la tabla de resumen de camaña mostrada en home
     * usado en CampanaDao.getResumenCampanaRegistro
     *
     * @param idCampana la campaña
     * @param idEstacion la estacion
     * @param tipoProfundidad el tipo de profundidad muestreada: FONDO, MAX_F,
     * MIL, MIN_O, SED
     * @return
     */
    public int getConteoMetagenomasCampanaEstacionTipoProfundidad(int idCampana, int idEstacion, String tipoProfundidad) {
        String query = "SELECT COUNT(idmetagenoma) FROM metagenoma "
                + "INNER JOIN muestra ON muestra.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                //      + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE idcampana = " + idCampana + " AND idEstacion = " + idEstacion
                + " AND tipo_profundidad = '" + tipoProfundidad + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;

    }

    /**
     * Este query trae el conteo de genomas realizados de una muestra de una
     * estacion para cierta campaña y un tipo de profundidad. Este query sirve
     * de apoyo para construir la tabla de resumen de camaña mostrada en home
     * usado en CampanaDao.getResumenCampanaRegistro
     *
     * @param idCampana la campaña
     * @param idEstacion la estacion
     * @param tipoProfundidad el tipo de profundidad muestreada: FONDO, MAX_F,
     * MIL, MIN_O, SED
     * @return
     */
    public int getConteoGenomasCampanaEstacionTipoProfundidad(int idCampana, int idEstacion, String tipoProfundidad) {
        String query = "SELECT COUNT(idgenoma) FROM genoma "
                + "INNER JOIN muestra ON muestra.idmuestra = genoma.idmuestra "
                + "INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                //   + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE idcampana = " + idCampana + " AND idEstacion = " + idEstacion
                + " AND tipo_profundidad = '" + tipoProfundidad + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;
    }

    /**
     * Este método trae el conteo de metagenomas en una estación para una
     * campaña en específico. Este query es usado para onstruir la tabla del
     * resumen en el home de la app
     *
     * @param idCampana
     * @param idEstacion
     * @return
     */
    public int getConteoMetagenomasPorEstacionCampana(int idCampana, int idEstacion) {
        String query = "SELECT COUNT(idMetagenoma) FROM metagenoma "
                + "INNER JOIN muestra ON muestra.idMuestra = metagenoma.idMuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE derrotero.idcampana = " + idCampana + " AND derrotero.idEstacion = " + idEstacion;
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;
    }

    /**
     * Este método trae el conteo de genomas en una estación para una campaña en
     * específico. Este query es usado para construir valores en la tabla del
     * resumen en el home de la app
     *
     * @param idCampana
     * @param idEstacion
     * @return
     */
    public int getConteoGenomaPorEstacionCampana(int idCampana, int idEstacion) {
        String query = "SELECT COUNT(idgenoma) FROM genoma "
                + "INNER JOIN muestra ON muestra.idMuestra = genoma.idMuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE derrotero.idcampana = " + idCampana + " AND derrotero.idEstacion = " + idEstacion;
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;
    }

    /**
     * Este método trae el conteo de amplicones en una estación para una campaña
     * en específico. Este query es usado para construir valores en la tabla del
     * resumen en el home de la app
     *
     * @param idCampana
     * @param idEstacion
     * @return
     */
    public int getConteoAmpliconPorEstacionCampana(int idCampana, int idEstacion) {
        String query = "SELECT COUNT(idMarcador) FROM marcador "
                + "INNER JOIN muestra ON muestra.idMuestra = marcador.idMuestra "
                + "INNER JOIN muestreo ON muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN derrotero ON derrotero.idderrotero = muestreo.idCE "
                + "WHERE derrotero.idcampana = " + idCampana + " AND derrotero.idEstacion = " + idEstacion;
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int count;
        if (dbResult == null || dbResult.isEmpty()) {
            count = 0;
        } else {
            try {
                count = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                count = 0;
            }
        }
        return count;
    }

    /**
     * Este m´todo se encarga de regresar el path absoluto + nombre del archivo
     * krona dado un marcador
     *
     * @param idMarcador
     * @param idTipoArchivo
     * @return
     */
    public String getPath2Krona(int idMarcador, int idTipoArchivo) {
        String query = "SELECT path, nombre, extension FROM archivo "
                + "INNER JOIN marcador_archivo AS ma ON ma.idarchivo = archivo.idarchivo "
                + "WHERE idmarcador = " + idMarcador + " AND idtipo_archivo =  " + idTipoArchivo;
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        if (dbResult != null && !dbResult.isEmpty()) {
            return (String) dbResult.get(0).get(0) + (String) dbResult.get(0).get(1);
        } else {
            return "";
        }

    }

    /**
     * Metodo que evoluciona del método getPath2Krona pero este nuevo es
     * universal tanto para marcadores como para metagenomas
     *
     * @param idKrona id del marcador o del metagenoma
     * @param idTipoArchivo id del tipo de archivo
     * @param src metagenoma o marcador
     * @return
     */
    public String getKronaPath(int idKrona, int idTipoArchivo, String src, String nombre) {
        String query = "SELECT path, nombre, extension FROM archivo "
                + "INNER JOIN " + src + "_archivo AS ma ON ma.idarchivo = archivo.idarchivo "
                + "WHERE id" + src + " = " + idKrona + " AND idtipo_archivo =  " + idTipoArchivo + " AND nombre = '" + nombre + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        if (dbResult != null && !dbResult.isEmpty()) {
            return (String) dbResult.get(0).get(0) + (String) dbResult.get(0).get(1);
        } else {
            query = "SELECT path, nombre, extension FROM archivo "
                    + "INNER JOIN " + src + "_archivo AS ma ON ma.idarchivo = archivo.idarchivo "
                    + "WHERE id" + src + " = " + idKrona + " AND idtipo_archivo =  " + idTipoArchivo;
            conexion.executeStatement(query);
            dbResult = conexion.getTabla();
            if (dbResult != null && !dbResult.isEmpty()) {
                return (String) dbResult.get(0).get(0) + (String) dbResult.get(0).get(1);
            } else {
                return "";
            }
        }

    }

    public String getRankByTaxID(String tax_id) {
        String query = "SELECT rank FROM taxon "
                + "WHERE tax_id = " + tax_id;
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        if (dbResult != null && !dbResult.isEmpty()) {
            return (String) dbResult.get(0).get(0);
        } else {
            return "_";
        }

    }

    /**
     * Obtiene cual es el max id de muestra para poder asignar nuevos.
     *
     * @return
     */
    public int getMaxIDMuestra() {
        String query = "SELECT MAX(idMuestra) FROM muestra";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = -1;
        if (dbResult == null || dbResult.isEmpty()) {
            id = -1;
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = -1;
            }
        }
        return ++id;

    }

    /**
     * Recibe un objeto de tipo estacion y en base a su nombe ve si este existe
     * si no existe, la estacion es creada en la BD
     *
     * @param est
     * @return
     */
    public int testEstacionByName(EstacionObj est) {
        String query = "SELECT idEstacion from estacion WHERE estacion_nombre = '" + est.getNombre() + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = -1;
        if (dbResult == null || dbResult.isEmpty()) {
            query = "INSERT INTO estacion(idEstacion, estacion_nombre, id_tipo_estacion, longitud, latitud, comentarios) "
                    + "VALUES(0,'" + est.getNombre() + "'," + est.getTipo_est() + "," + est.getLongitud().getCoordenadas()
                    + "," + est.getLatitud().getCoordenadas() + ",'" + est.getComentarios() + "')";
            id = conexion.queryUpdateWithKey(query);
            query = "INSERT INTO estacion_tipo_estacion VALUES(" + id + "," + est.getTipo_est() + ")";
            conexion.queryUpdate(query);
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = -1;
            }
        }
        return id;
    }

    /**
     * Recibe un objeto de tipo estacion y en base a su nombe ve si este existe
     * A diferencia del otro testEstacionByName pero con param Estacion, este
     * solo verifica y regresa el ID, no inserta nada a la BD
     *
     * @param est
     * @return
     */
    public int testEstacionByName(String est) {
        String query = "SELECT idEstacion from estacion WHERE estacion_nombre = '" + est + "'";
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = -1;
        if (dbResult == null || dbResult.isEmpty()) {
            id = -1;
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = -1;
            }
        }
        return id;

    }

    /**
     * Esste metodo se encarga de obtener el ID del derrotero es decir la
     * convinación estacion y campaña. Es usado durante la carga de los
     * muestreos, dado que este es el ID que se relaciona con el muestreo. El
     * único problema es que si se visita mas de una vez la misma estación en
     * una sola campaña este método tiende aa fallar por lo que hay que hacer
     * uso de la fecha o algún otro campo para realizar la correcta validación.
     *
     * @param idEst id de la estación
     * @param idCampana id de la campaña.
     * @return
     */
    public int getIDDerrotero(int idEst, int idCampana) {
        String query = "SELECT idDerrotero from derrotero "
                + "WHERE idEstacion = " + idEst + " AND idCampana = " + idCampana;
        conexion.executeStatement(query);
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        int id = -1;
        if (dbResult == null || dbResult.isEmpty()) {
            id = -1;
        } else {
            try {
                id = Integer.parseInt((String) dbResult.get(0).get(0));
            } catch (NumberFormatException nfe) {
                id = -1;
            }
        }
        return id;

    }

    public int insertaDerrotero(int idCampana, int idEstacion, String nombre, String fPlaneada, String fEjecutada, int numEstP, int numEstE, String comentarios) {
        String query = "INSERT INTO derrotero "
                + "VALUES(0," + idCampana + "," + idEstacion + ",'" + nombre + "'," + fPlaneada + "," + fEjecutada
                + "," + numEstP + "," + numEstE + ",'" + comentarios + "')";
        return conexion.queryUpdateWithKey(query);
    }

    public boolean testConnection() {
        String query = "select 1";
        conexion.executeStatement(query);
        //Vector paraRegresar = conexion.getFilas();
        ArrayList<ArrayList> dbResult = conexion.getTabla();

        //if (paraRegresar.size() > 0) {
        if (dbResult != null && dbResult.size() > 0) {
            try {
                // return ( (Vector) paraRegresar.elementAt(0)).elementAt(
                //    0).toString();
                return true;
            } catch (NullPointerException npe) {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean updateHierarchyNCBINode(String taxid, String hierarchy) {
        String query = "UPDATE NCBI_NODE SET hierarchy = '" + hierarchy + "' WHERE tax_id =" + taxid;
        if (conexion.queryUpdate(query)) {
            return true;
        } else {
            System.err.println(conexion.getLog());
            return false;
        }
    }

    public boolean insertJobIDMetagenoma(int idJob, String idmetagenoma) {
        String query = "INSERT INTO blast_job_metagenoma VALUES(" + idmetagenoma + ", " + idJob + ")";
        if (conexion.queryUpdate(query)) {
            return true;
        } else {
            System.err.println(conexion.getLog());
            return false;
        }
    }

    public boolean insertJobIDGenoma(int idJob, String idgenoma) {
        String query = "INSERT INTO blast_job_genoma VALUES(" + idgenoma + ", " + idJob + ")";
        if (conexion.queryUpdate(query)) {
            return true;
        } else {
            System.err.println(conexion.getLog());
            return false;
        }
    }

    /**
     * since: GCT3 NG Metodo para actualizar el status de un job
     *
     * @param jobID
     * @param status
     * @param withDate
     * @return
     */
    public boolean updateJobStatus(String jobID, String status, String msg, boolean withDate) {
        String query;
        if (withDate) {
            query = "UPDATE blast_job set status = '" + status + "', message = '" + msg + "', end_date = NOW() WHERE idblast_job = " + jobID;
        } else {
            query = "UPDATE blast_job set status = '" + status + "', message = '" + msg + "' WHERE idblast_job = " + jobID;
        }
        return conexion.queryUpdate(query);
    }

    /**
     * Este metodo se encarga de traer la info necesario para inicializar un job
     * a partir de un user id o url que es el "link" que se le genera al usuario
     * para visualizar el job.
     *
     * @param uId ID que tiene el link del usuario para accesar al job
     * @return
     */
    public ArrayList<ArrayList> getJobDetails(String uId) {
        String query = "SELECT idblast_job, job_name, job_type, eval,  start_date, end_date, status, message, path "
                + "FROM blast_job "
                + "WHERE job_url = '" + uId + "'";
        conexion.executeStatement(query);
        //Vector paraRegresar = conexion.getFilas();
        ArrayList<ArrayList> dbResult = conexion.getTabla();
        return dbResult;
    }

    public boolean insertJobIDMGenoma(String idJob, String idgenoma) {
        String query = "INSERT INTO blast_job VALUES(" + idgenoma + ", " + idJob + ")";
        if (conexion.queryUpdate(query)) {
            return true;
        } else {
            System.err.println(conexion.getLog());
            return false;
        }
    }

    public boolean insertaQuery(String query) {
        if (debug) {
            System.out.println(query);
        }
        if (conexion.queryUpdate(query)) {
            return true;
        } else {
            System.err.println(conexion.getLog());
            return false;
        }

    }

    public boolean insertSwissProt(String uniprotID, String uniprotACC, String taxID, String uniprotName, String sequence, int seqLength, String clusterId, String clusterName, String clusterTax) {
        String query = "INSERT INTO Swiss_prot (uniprot_id, uniprot_acc, ncbi_tax_id, "
                + "prot_name, prot_seq, prot_length, cluster_id, cluster_name, cluster_ncbi_tax) VALUES ('"
                + uniprotID + "', '"
                + uniprotACC + "', '"
                + taxID + "', '"
                + uniprotName + "', '"
                + sequence + "', "
                + seqLength + ", '"
                + clusterId + "', '"
                + clusterName + "', '"
                + clusterTax + "');\n";
        if (debug) {
            System.out.println(query);
        }
        return conexion.queryUpdate(query);
    }

    public boolean writeFastaFileByOrg(String orgID, String seqType, String extra, String fileName) {
        String query = " SELECT DISTINCT(CONCAT('>',seq_gen_id,char(10),seq_seq)) "
                + "FROM gen_seq "
                + "WHERE seq_org = '" + orgID + "' "
                + "AND seq_type = '" + seqType + "' "
                + " " + extra
                + " INTO OUTFILE '" + fileName + "'"
                + "FIELDS ESCAPED BY ''";

        //Vector paraRegresar = conexion.getFilas();
        //return conexion.queryUpdate(query);
        conexion.executeStatementToFile(query);
        return true;
    }

    /**
     * Este método es usado por TaxaDAO para generar la tabla de especies
     * encontradas
     *
     * @param idMarcador
     * @return
     */
    public ArrayList getDetallesMuestraMarcador(String idMarcador) {
        String query = "SELECT marc_name,muestra.idmuestra, muestra.etiqueta, tipo_muestra, tipo_profundidad, muestreo.profundidad "
                + "FROM marcador "
                + "INNER JOIN muestra on muestra.idmuestra = marcador.idmuestra "
                + "INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN tipo_muestra on tipo_muestra.idtipomuestra = muestreo.idtipomuestra "
                + "WHERE idmarcador = " + idMarcador;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    /**
     * Igual que método: getDetallesMuestraMarcador pero para metagenomas
     *
     * @param idMetagenoma
     * @return
     */
    public ArrayList getDetallesMuestraMetagenomaa(String idMetagenoma) {
        String query = "SELECT meta_name,muestra.idmuestra, muestra.etiqueta, tipo_muestra, tipo_profundidad, muestreo.profundidad "
                + "FROM metagenoma "
                + "INNER JOIN muestra on muestra.idmuestra = metagenoma.idmuestra "
                + "INNER JOIN muestreo on muestreo.idmuestreo = muestra.idmuestreo "
                + "INNER JOIN tipo_muestra on tipo_muestra.idtipomuestra = muestreo.idtipomuestra "
                + "WHERE idmetagenoma = " + idMetagenoma;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    ///METODO PARA MOSTRAR EL NOMBRE SELECCIONADO DE LA CAMPAÑA EN EL HOME
    public ArrayList getAllCampanasId(int idCampana) {
        String query = "SELECT idcampana, siglas, nombre FROM campana where idcampana=" + idCampana;
        conexion.executeStatement(query);
        return conexion.getTabla();
    }

    ///METODO PARA ACTUALIZAR TERMINO DE UN SUARIO AL ENTRAR AL SISTEMA
    public boolean updateTerminos(String user) {

        String query = "update usuario set terminos=1 where correo='" + user + "'";
        if (conexion.queryUpdate(query)) {
            return true;
        } else {
            // System.out.println(conexion.getLog());
            return false;
        }

    }

    public ArrayList buscaNodosByName(String name, boolean limit) {
        String query = "SELECT tax_id, name, rank FROM ncbi_node WHERE name like '" + name + "%' ORDER BY name";
        if (limit) {
            query += " limit 100";
        }
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    public ArrayList buscaNodosByTaxID(String taxId, boolean limit) {
        String query = "select tax_id, name, rank from ncbi_node where tax_id like '" + taxId + "%' ORDER BY tax_id";
        if (limit) {
            query += " limit 100";
        }
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    public ArrayList getBuscarncbinode(String taxId) {
        String query = "select * from ncbi_node where tax_id=" + taxId;
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    /**
     * Este metodo se encarga de traer la info necesario para inicializar una
     * muestra a partir de una etiqueta url que es el "link" que se le genera al
     * usuario para visualizar la muestra.
     *
     * @param url ID que tiene el link del usuario para accesar al job
     * @return
     */
    public ArrayList getMuestra(int idMuestra) {
        String query = "SELECT idMuestra,etiqueta,contenedor,tamano,protocolo,notas,rel_to_oxygen,contenedor_temp "
                + "FROM muestra "
                + "WHERE idMuestra = '" + idMuestra + "'";
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    public ArrayList getMuestreo(int idMuestra) {
        String query = "SELECT muestra.idMuestreo,muestreo.etiqueta,nombre,estacion_nombre,bioma,env_material,env_feature,"
                + "muestreo.protocolo,muestreo.comentarios,latitud_r,longitud_r,latitud as latitud_estacion,longitud as longitud_estacion, fecha_i, fecha_f,tipo_profundidad,muestreo.profundidad "
                + "FROM muestreo "
                + "INNER JOIN campana on campana.idCampana=muestreo.idCampana "
                + "INNER JOIN estacion on estacion.idEstacion=muestreo.idEstacion "
                + "INNER JOIN muestra on muestra.idMuestreo = muestreo.idMuestreo "
                + "WHERE idMuestra= " + idMuestra + "";
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    public ArrayList getVariables(int idMuestra) {
        String query = "SELECT muestreo.idMuestreo,mv.idvariable, nombre_web, medicion_t1, unidades "
                + "FROM muestreo_variable as mv  "
                + "inner join variable on variable.idvariable = mv.idvariable "
                + "inner join muestreo on muestreo.idMuestreo= mv.idMuestreo "
                + "inner join muestra on muestra.idMuestreo = muestreo.idMuestreo "
                + "WHERE muestra.idmuestra = " + idMuestra + "";
        conexion.executeStatement(query);
        return conexion.getTabla();

    }

    public ArrayList getMuestreo2(int idMuestreo) {
        String query = "SELECT muestreo.idMuestreo,muestreo.etiqueta as etiqueta_muestreo,nombre as nombre_campana,estacion_nombre,bioma,env_material,env_feature,muestreo.protocolo as muestreo_protocolo,"
                + "muestra.protocolo as muestra_protocolo,contenedor,muestra.tamano,notas,rel_to_oxygen,contenedor_temp "
                + "FROM muestreo "
                + "INNER JOIN campana on campana.idCampana=muestreo.idCampana "
                + "INNER JOIN estacion on estacion.idEstacion=muestreo.idEstacion "
                + "INNER JOIN muestra on muestra.idMuestreo = muestreo.idMuestreo "
                + "WHERE muestreo.idMuestreo= '" + idMuestreo + "';";
        conexion.executeStatement(query);
        return conexion.getTabla();

    }
    public ArrayList getCat1Metabolismo(int clave) {
        String query = "call getBriteCat("+clave+");";
        conexion.executeStatement(query);
        return conexion.getTabla();

    }
    
        public ArrayList getCat2Metabolismo(String met, String nivel2, String metodo) {
        String query = "call getMetGData('"+met+"','"+nivel2+"','"+metodo+"');";
        conexion.executeStatement(query);
        return conexion.getTabla();

    }
        
     public Map<String, List<String>> getHashTable(String[] nombresMet, String rutaId, int method){
        Map<String, List<String>> m = null;//
        if (method==2 || method == 3){
            m=conexion.getHashTablaByMets(nombresMet, rutaId, method);
            if(m.isEmpty()){
                m=null;
            }
        }
        return m;
    }
    
    public Map<String, List<String>> getHashTableSingleMet(String metName, String rutaId, int method){
        Map<String, List<String>> m = null;
        m=conexion.getHashTablaByMetodo(metName, rutaId, method);
        if (m.size()==0){
            m=null;
        }
        return m;
    }    
    
public List<List<String>> getMapTable(String rutaId){
        List<List<String>> stateM = conexion.getStateMap(rutaId);        
        return stateM;
    }

}
