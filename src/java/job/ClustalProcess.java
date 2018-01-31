/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro
 */
public class ClustalProcess {

    private String inFile = "";  //-INFILE
    private String option = "";
    final static String ALIGN_OPT = "-ALIGN";//:do full multiple alignment.
    final static String TREE_OPT = "-TREE";//calculate NJ tree.
    final static String PIM_OPT = "-PIM";// output percent identity matrix (while calculating the tree)
    final static String BOOTSTRAP_OPT = "-BOOTSTRAP";//BOOTSTRAP(=n) :bootstrap a NJ tree (n= number of bootstraps; def. = 1000).
    private String outFile = "";//-OUTFILE
    private String type = "";//:PROTEIN or DNA sequences
    private String outFormat="";//-OUTPUT=     :CLUSTAL(default), GCG, GDE, PHYLIP, PIR, NEXUS and FASTA
    private String command = "clustalw2";
    public String getInFile() {
        return inFile;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    
    public String generateCommandLine(){
        String cmd = command + " -INFILE="+this.inFile + " -OUTFILE="+this.outFile;
        return cmd;
    }
     public boolean correSimpleClustal() {
        boolean blast_ok = true;
        String[] command = {"sh", "-c", generateCommandLine()};
        try {            
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (process.exitValue() == 0) {                                
                blast_ok = true;               

            } else {              
                System.err.println("Error en alineamiento: " + process.exitValue() + " - " + generateCommandLine() + "\n" + process.getOutputStream().toString());
                blast_ok = false;
            }
        } catch (Exception e) {
            Logger.getLogger(BlastProcess.class.getName()).log(Level.SEVERE, null, e);           
            blast_ok = false;
        }
        return blast_ok;
    }
    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOutFile() {
        return outFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOutFormat() {
        return outFormat;
    }

    public void setOutFormat(String outFormat) {
        this.outFormat = outFormat;
    }
    
}
