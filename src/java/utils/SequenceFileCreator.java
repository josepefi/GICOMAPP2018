/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import database.Transacciones;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import job.ClustalProcess;

/**
 *
 * @author Alejandro
 */
public class SequenceFileCreator {

    private Transacciones transacciones;

    public SequenceFileCreator(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Este método se encarga de generar el string necesario para crear el
     * archivo fasta
     *
     * @param rank el rango del taxon a buscar, ejemplo genus
     * @param valueRank el valor de dicho taxon, ejemplo pseudomona
     * @param ids lista de ids de marcadores para la operación
     * @param idanalisis el identificador del análisis sobre el cual se piden las secuencias
     * @return
     */
    public String generaTaxoFileSequence(String rank, String valueRank, String ids, String idanalisis) {
        StringBuilder file = new StringBuilder("");
        String idsM[] = ids.split("-");
        HashMap<String, String> hashTaxones = new HashMap<>();
        String lineSep = System.getProperty("line.separator");
        for (String idMarcador : idsM) {
            //primero hay que encontrar los posibles tax_ids para un tax_id y un marcador dados
            ArrayList<ArrayList> taxaList = transacciones.getTaxonesByTaxonMarcador(idMarcador, rank, valueRank, idanalisis);
            StringBuilder allTaxIDS = new StringBuilder("");
            if (taxaList != null && taxaList.size() > 0) {
                //ponemos esos ids en un strib comma separated y llenamos un hash
                for (ArrayList<String> taxID : taxaList) {
                    hashTaxones.put(taxID.get(0), taxID.get(2) + " (" + taxID.get(1) + ")");
                    if (allTaxIDS.length() == 0) {
                        allTaxIDS.append(taxID.get(0));
                    } else {
                        allTaxIDS.append(",").append(taxID.get(0));
                    }
                }
                if (allTaxIDS.length() > 0) {
                    //llenamos las secuencias
                    //el sgt qquery trae tax_id, idseq, identity, score, seq
                    ArrayList<ArrayList> secuencias;
                    if (idanalisis.equals("2")) {//parallel-meta
                        secuencias = transacciones.getSecuenciasByTaxIdsAndMarcador(allTaxIDS.toString(), idMarcador, "_parallel");
                    } else {
                        secuencias = transacciones.getSecuenciasByTaxIdsAndMarcador(allTaxIDS.toString(), idMarcador, "");
                    }

                    for (ArrayList<String> secuencia : secuencias) {
                        String taxon = hashTaxones.get(secuencia.get(0));
                        if (taxon == null) {
                            taxon = "no_rank";
                        }
                        if (idanalisis.equals("2")) {//parallel-meta
                            file.append(">").append(secuencia.get(1)).append("|IDENT_").append(secuencia.get(2)).append("|EVAL_").append(secuencia.get(5)).append("|").append(taxon).append(lineSep);
                        } else {
                            file.append(">").append(secuencia.get(1)).append("|IDENT_").append(secuencia.get(2)).append("|SCORE_").append(secuencia.get(3)).append("|").append(taxon).append(lineSep);
                        }
                        file.append(secuencia.get(4)).append(lineSep);
                    }
                }

            }
        }
        return file.toString();
    }

    /**
     * Este método se encarga de crear una rchivo fasta, el cual será mandado al
     * usuario
     *
     * @param fileName nombre del archivo
     * @param ids lista de ids de genes separado por coma
     * @param seqType tipo de secuencia que pidio el usuario NC
     * @param seqHeader string con información para crear el header del fasta
     * @return
     */
    public boolean generateFileFromIDs(String dirPath, String fileName, String ids, String seqType, String seqHeader) {
        String idList[] = ids.split(",");
        StringBuilder sqlIds = new StringBuilder();
        for (String id : idList) {
            if (sqlIds.length() == 0) {
                sqlIds.append("'").append(id.trim()).append("'");
            } else {
                sqlIds.append(",'").append(id.trim()).append("'");
            }
        }
        //el usuario quiere detalles de la proteina predicha
        ArrayList<ArrayList<String>> seqs;
        //  if (seqHeader.contains("prot")) {
        //     seqs = transacciones.getSequenceWithProtInfo(sqlIds.toString(), seqType);
        //} else {
        seqs = transacciones.getSequenceWithoutProtInfo(sqlIds.toString(), seqType);
        //  }
        if (seqs == null || seqs.isEmpty()) {
            return false;
        }
        try {
            File f = new File(dirPath);
            if (!f.exists()) {
                f.mkdir();
                f.setWritable(true, false);
            }
            FileWriter fastaFile = new FileWriter(fileName);
            for (ArrayList<String> seq : seqs) {
                /*StringBuilder secuencia = new StringBuilder(">gnl|");
                 if (seq.get(1).equals("GEN")) {//gen_src
                 secuencia.append("GENODB|");
                 } else {
                 secuencia.append("METADB|");
                 }*/

                StringBuilder secuencia = new StringBuilder(">");
                if (seq.get(1).equals("GEN")) {//gen_src
                    secuencia.append("GDB|");
                } else {
                    secuencia.append("MDB|");
                }
                secuencia.append(seq.get(0));//gen_id
                secuencia.append(":").append(seqType).append("_").append(seq.get(3));//seq_type_starnd(+/-)
                if (seqHeader.contains("prot")) {
                    ArrayList<ArrayList<String>> protDetails = transacciones.getGenSwissProtDetailsNoTaxaInfo(seq.get(0));
                    if (protDetails != null && protDetails.size() > 0) {
                        ArrayList<String> details = protDetails.get(0);
                        if (!details.get(0).equals("NULL")) { //uniprot ID
                            secuencia.append(" ").append(details.get(0));
                        }
                        if (!details.get(1).equals("NULL")) { //prot_name
                            secuencia.append(" ").append(details.get(1));
                        }
                        if (!details.get(2).equals("NULL")) { //gene_name
                            secuencia.append(" ").append(details.get(2));
                        }
                    }
                }
                secuencia.append("\n").append(seq.get(2)).append("\n");//secuencia
                fastaFile.write(secuencia.toString());
            }
            fastaFile.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(SequenceFileCreator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Este método se encarga de crear una archivo fasta, el cual posteriormente
     * será alineado
     *
     * @param fileName nombre del archivo
     * @param ids lista de ids de genes separado por coma
     * @param seqType tipo de secuencia que pidio el usuario NC
     * @param seqHeader string con información para crear el header del fasta
     * @return
     */
    public boolean generateAlignFromIDs(String dirPath, String fileName, String ids, String seqType, String seqHeader, String alignCommand) {
        String idList[] = ids.split(",");
        StringBuilder sqlIds = new StringBuilder();
        for (String id : idList) {
            if (sqlIds.length() == 0) {
                sqlIds.append("'").append(id.trim()).append("'");
            } else {
                sqlIds.append(",'").append(id.trim()).append("'");
            }
        }
        //el usuario quiere detalles de la proteina predicha
        ArrayList<ArrayList<String>> seqs;
        // if (seqHeader.contains("prot")) {
        //    seqs = transacciones.getSequenceWithProtInfo(sqlIds.toString(), seqType);
        //} else {
        seqs = transacciones.getSequenceWithoutProtInfo(sqlIds.toString(), seqType);
        //}
        if (seqs == null || seqs.isEmpty()) {
            return false;
        }
        try {
            File f = new File(dirPath);
            if (!f.exists()) {
                f.mkdir();
                f.setWritable(true, false);
            }
            FileWriter fastaFile = new FileWriter(fileName);
            for (ArrayList<String> seq : seqs) {
                //StringBuilder secuencia = new StringBuilder(">gnl|");
                StringBuilder secuencia = new StringBuilder(">");
                if (seq.get(1).equals("GEN")) {//gen_src
                    secuencia.append("GDB|");
                } else {
                    secuencia.append("MDB|");
                }
                secuencia.append(seq.get(0));//gen_id
                secuencia.append(":").append(seqType).append("_").append(seq.get(3));//seq_type_starnd(+/-)
                if (seqHeader.contains("prot")) {
                    ArrayList<ArrayList<String>> protDetails = transacciones.getGenSwissProtDetailsNoTaxaInfo(seq.get(0));
                    if (protDetails != null && protDetails.size() > 0) {
                        ArrayList<String> details = protDetails.get(0);
                        if (!details.get(0).equals("NULL")) { //uniprot ID
                            secuencia.append(" ").append(details.get(0));
                        }
                        if (!details.get(1).equals("NULL")) { //prot_name
                            secuencia.append(" ").append(details.get(1));
                        }
                        if (!details.get(2).equals("NULL")) { //gene_name
                            secuencia.append(" ").append(details.get(2));
                        }
                    }
                }
                secuencia.append("\n").append(seq.get(2)).append("\n");//secuencia
                fastaFile.write(secuencia.toString());
            }
            fastaFile.close();
            ClustalProcess cp = new ClustalProcess();
            cp.setInFile(fileName);
            cp.setOutFile(fileName + ".align");
            cp.setCommand(alignCommand);

            return cp.correSimpleClustal();

        } catch (IOException ex) {
            Logger.getLogger(SequenceFileCreator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
