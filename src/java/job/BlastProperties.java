/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job;

import java.util.ArrayList;
import org.apache.commons.exec.CommandLine;

/**
 *
 * @author Alejandro
 */
public class BlastProperties {

    private String workingDir;//el directorio de trabajo
    String instruction; //la instrucción a ejecutar /opt/ncbi-blast-2.2.30+/bin/blastn
    String database;//la base de datos contra la que se blastea
    String query;//query que se blastea vs la BD seleccionada
    String evalue = "0.0001";//valor evalue de corte
    String out_file;//archivo de salida
    String outfmt = "6";//'6 qseqid stitle evalue bitscore'";//id del formato de salida
    ArrayList<String> databases = new ArrayList<>();

    //default = 'qseqid sseqid pident length mismatch gapopen qstart qend sstart send evalue bitscore'
    public String generateCommandLine() {
        String command;
        command = instruction + " -db " + database + " -query " + query + " -evalue " + evalue + " -outfmt " + outfmt + " -out " + out_file;
        return command;
    }

    public String generateCommandLineCustomDB(String database, String out_ext) {
        String command;
        command = instruction + " -db " + database + " -query " + query + " -evalue " + evalue + " -outfmt " + outfmt + " -out " + out_file+"."+out_ext;
        return command;
    }

    public void insertDataBase(String db) {
        this.databases.add(db);
    }

    public ArrayList<String> getDatabases() {
        return databases;
    }

    public void setDatabases(ArrayList<String> databases) {
        this.databases = databases;
    }

    public CommandLine generateCommandLineObj() {
        CommandLine command = new CommandLine(instruction);
        //String command;
        //command = instruction + " -db " + database + " -query " + query + " -evalue " + evalue + " -outfmt " + outfmt + " -out " + out_file;
        command.addArgument("-db");
        command.addArgument(database);
        command.addArgument("-query");
        command.addArgument(query);
        command.addArgument("-evalue");
        command.addArgument(evalue);
        command.addArgument("-outfmt");
        command.addArgument(outfmt);
        command.addArgument("-out");
        command.addArgument(out_file);
        return command;
    }

    public CommandLine generateCommandLineObjNew() {
        CommandLine command = new CommandLine(instruction);
        //String command;
        //command = instruction + " -db " + database + " -query " + query + " -evalue " + evalue + " -outfmt " + outfmt + " -out " + out_file;
        command.addArgument("-db " + database, false);
        command.addArgument("-query " + query, false);
        command.addArgument("-evalue " + evalue, false);
        command.addArgument("-outfmt " + outfmt, false);
        command.addArgument("-out " + out_file, false);
        return command;
    }

    public String generateSortedOutputCommandLine() {
        String command;
        command = "sort -g -k 3 " + out_file + " > " + out_file + ".sorted";
        return command;
    }

    /**
     * Este método se encarga de generar una linea de comandos para tomar la
     * salida del blast y dejar unicamente 4 columnas y ordenadas por evalue y
     * bit-score.
     *
     * @return
     */
    public String generateSortedCutOutputCommandLine() {
        String command;
        command = "cut -f1,2,3,7,8,9,10,11,12 " + workingDir + out_file + " | sort -g -k 8 -k 9r > " + workingDir + out_file + ".sorted";
        return command;
    }
    /**
     * Este método se encarga de generar una linea de comandos para tomar la
     * salida del blast y dejar unicamente 4 columnas y ordenadas por evalue y
     * bit-score.
     *
     * @return
     */
    public String generateConcatOutputCommandLine() {
        String command;
        command =  "cat " + workingDir +"out.txt.* > " + workingDir + out_file;
        //System.out.println(command);
        return command;
    }
       public String generateConcatOutputCommandLine2() {
        String command;
        command =  "cat " + workingDir +"out.txt.1 out.txt.2 > " + workingDir + out_file+".concat";
      //  System.out.println(command);
        return command;
    }
    public BlastProperties(String workingDir, String instruction) {
        this.workingDir = workingDir;
        this.instruction = instruction;
    }

    public String getOutfmt() {
        return outfmt;
    }

    public void setOutfmt(String outfmt) {
        this.outfmt = outfmt;
    }

    public void setEvalueFromString(String e) {
        int etmp = 3;
        try {
            etmp = Integer.parseInt(e);
        } catch (NumberFormatException nfe) {
            etmp = 3;
        }
        evalue = "0.";
        for (int i = 0; i < etmp - 1; i++) {
            evalue += "0";
        }
        evalue += "1";
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getEvalue() {
        return evalue;
    }

    public void setEvalue(String evalue) {
        this.evalue = evalue;
    }

    public String getOut_file() {
        return out_file;
    }

    public void setOut_file(String out_file) {
        this.out_file = out_file;
    }

}
