/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job;

import dao.JobDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author Alejandro
 */
public class BlastProcess implements Runnable {

    private String jobId;
    BlastProperties properties;
    JobDAO jDao;

    public BlastProcess(String jobId) {
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public BlastProperties getProperties() {
        return properties;
    }

    public void setProperties(BlastProperties properties) {
        this.properties = properties;
    }

    public JobDAO getjDao() {
        return jDao;
    }

    public void setjDao(JobDAO jDao) {
        this.jDao = jDao;
    }

    /**
     * Se encarga de correr la inea de comandos con el blast
     *
     * @return
     */
    public boolean correblast() {
        boolean blast_ok = false;
        String line = properties.generateCommandLine();
        CommandLine cmdLine = CommandLine.parse(line);
        //CommandLine cmdLine = properties.generateCommandLineObjNew();
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(properties.getWorkingDir()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(streamHandler);
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        try {

            jDao.updateJobStatus(jobId, "Corriendo", "", false);
            //adao.insertAnalisisProceso(idAnalisis, "Creando bams individuales", "...", bamsIndividuales, "");
            //adao.updateAnalisisProcessEstatus(idAnalisis, "Corriendo", "");

            executor.execute(cmdLine, resultHandler);

            resultHandler.waitFor();
            if (resultHandler.getExitValue() == 0) {
                jDao.updateJobStatus(jobId, "End Blast", "", true);
                //     adao.insertAnalisisProceso(idAnalisis, "Bams creados", "OK", "", "");
                blast_ok = true;
                //blast_ok = blast_ok & creaSortedOutputBlast();
                blast_ok = blast_ok & creaSortedOutputBlastSH();
            } else {
                jDao.updateJobStatus(jobId, "Error", outputStream.toString().replace("'", "\\'").replace("\"", "\\\""), true);
                System.err.println("Todo mal: " + resultHandler.getExitValue() + " - " + line + "\n" + outputStream.toString());
                StringBuilder args = new StringBuilder();
                int i = 0;
                for (String arg : cmdLine.getArguments()) {
                    i++;
                    args.append(i).append("-").append(arg).append("\n");
                }
              //  System.out.println("ARGS:\n" + args.toString());
//   adao.insertAnalisisProceso(idAnalisis, "Error creando bams ", "Valor de salida incorrecto: " + resultHandler.getExitValue() + " Esperado: " + 0, "", outputStream.toString());
            }

        } catch (Exception ex) {
            Logger.getLogger(BlastProcess.class.getName()).log(Level.SEVERE, null, ex);
            jDao.updateJobStatus(jobId, "Error", ex.getMessage().replace("'", "\\'").replace("\"", "\\\""), true);
            // adao.updateAnalisisProcessEstatus(idAnalisis, "Excepci&oacute;n creando bams individuales", "");
            // adao.insertAnalisisProceso(idAnalisis, "Excepci&oacute;n creando bams individuales", "", "", "" + ex.getCause().getClass());
        }
        jDao.closeConexion();
        return blast_ok;
    }

    /**
     * Se encarga de correr la inea de comandos con el blast
     *
     * @return
     */
    public boolean correMultiBlast() {
        boolean blast_ok = true;
        ArrayList<String> dbs = properties.getDatabases();
        int dbs_to_proccess = dbs.size();
        int bds_process = 0;
        for (String db : dbs) {
            String line = properties.generateCommandLineCustomDB(db, "" + bds_process);
            CommandLine cmdLine = CommandLine.parse(line);
            //CommandLine cmdLine = properties.generateCommandLineObjNew();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(new File(properties.getWorkingDir()));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            try {
                bds_process++;
                jDao.updateJobStatus(jobId, "Corriendo bds: " + bds_process + "/" + dbs_to_proccess, "", false);
                executor.execute(cmdLine, resultHandler);
                resultHandler.waitFor();
                if (resultHandler.getExitValue() == 0) {
                    blast_ok &= true;
                    //blast_ok = blast_ok & creaSortedOutputBlast();

                } else {
                    jDao.updateJobStatus(jobId, "Error", outputStream.toString().replace("'", "\\'").replace("\"", "\\\""), true);
                    System.err.println("Todo mal: " + resultHandler.getExitValue() + " - " + line + "\n" + outputStream.toString());
                    StringBuilder args = new StringBuilder();
                    int i = 0;
                    for (String arg : cmdLine.getArguments()) {
                        i++;
                        args.append(i).append(" - ").append(arg).append("\n");
                    }
                  //  System.out.println("ARGS:\n" + args.toString());
//   adao.insertAnalisisProceso(idAnalisis, "Error creando bams ", "Valor de salida incorrecto: " + resultHandler.getExitValue() + " Esperado: " + 0, "", outputStream.toString());
                }

            } catch (Exception ex) {
                Logger.getLogger(BlastProcess.class.getName()).log(Level.SEVERE, null, ex);
                jDao.updateJobStatus(jobId, "Error", ex.getMessage().replace("'", "\\'").replace("\"", "\\\""), true);
                // adao.updateAnalisisProcessEstatus(idAnalisis, "Excepci&oacute;n creando bams individuales", "");
                // adao.insertAnalisisProceso(idAnalisis, "Excepci&oacute;n creando bams individuales", "", "", "" + ex.getCause().getClass());
            }
        }
        //blast_ok &= concatenaOutputsSH2();
        blast_ok &= concatenaOutputsSH();
        blast_ok &= creaSortedOutputBlastSH();
        jDao.closeConexion();
        if (blast_ok) {
            jDao.updateJobStatus(jobId, "Terminado", "", true);
        } else {
            //jDao.updateJobStatus(jobId, "Error", ex.getMessage().replace("'", "\\'").replace("\"", "\\\""), true);
        }
        return blast_ok;
    }

    /**
     * Este método se encarga de crear el archivo ordenado de la búsqueda de
     * blast.
     *
     * @return
     */
    public boolean correSimpleblast() {
        boolean blast_ok = true;
        String[] command = {"sh", "-c", properties.generateCommandLine()};
        try {
            jDao.updateJobStatus(jobId, "Corriendo", "", false);
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (process.exitValue() == 0) {
                jDao.updateJobStatus(jobId, "End Blast", "", true);
                //     adao.insertAnalisisProceso(idAnalisis, "Bams creados", "OK", "", "");
                blast_ok = true;
                //blast_ok = blast_ok & creaSortedOutputBlast();
                blast_ok = blast_ok & creaSortedOutputBlastSH();

            } else {
                // jDao.updateJobStatus(jobId, "Error", "Error formating output", false);
                /*
                 InputStream inputstream = proc.getInputStream();
                 InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                 BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                 while ((line = bufferedreader.readLine()) != null) {}
                 */
                jDao.updateJobStatus(jobId, "Error", process.getOutputStream().toString().replace("'", "\\'").replace("\"", "\\\""), true);
                System.err.println("Todo mal: " + process.exitValue() + " - " + properties.generateCommandLine() + "\n" + process.getOutputStream().toString());

            }
        } catch (Exception e) {
            Logger.getLogger(BlastProcess.class.getName()).log(Level.SEVERE, null, e);
            jDao.updateJobStatus(jobId, "Error", e.getMessage().replace("'", "\\'").replace("\"", "\\\""), true);
            blast_ok = false;
        }
        return blast_ok;
    }

    public boolean correSimpleblast2() {
        boolean blast_ok = false;
        try {
            String line = properties.generateCommandLine();
            Process proc = Runtime.getRuntime().exec(line);
            //System.out.println("perl " + pathTodistantes + " -s " + orgsParam + " -n " + limit);
            jDao.updateJobStatus(jobId, "Corriendo", "", false);
            proc.waitFor();
           // CommandLine cmdLine = CommandLine.parse(line);
            //CommandLine cmdLine = properties.generateCommandLineObjNew();
            // DefaultExecutor executor = new DefaultExecutor();
            // executor.setWorkingDirectory(new File(properties.getWorkingDir()));

            // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            //  executor.setStreamHandler(streamHandler);
            // DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            //adao.insertAnalisisProceso(idAnalisis, "Creando bams individuales", "...", bamsIndividuales, "");
            //adao.updateAnalisisProcessEstatus(idAnalisis, "Corriendo", "");
            //executor.execute(cmdLine, resultHandler);
            //resultHandler.waitFor();
            if (proc.exitValue() == 0) {
                jDao.updateJobStatus(jobId, "End Blast", "", true);
                //     adao.insertAnalisisProceso(idAnalisis, "Bams creados", "OK", "", "");
                blast_ok = true;
                //blast_ok = blast_ok & creaSortedOutputBlast();
                blast_ok = blast_ok & creaSortedOutputBlastSH();
            } else {
                InputStream inputstream = proc.getInputStream();
                InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                // read the output 
                String lineErr;
                StringBuilder sb = new StringBuilder();
                while ((lineErr = bufferedreader.readLine()) != null) {
                    //System.out.println(line);
                    sb.append(lineErr);
                }
                jDao.updateJobStatus(jobId, "Error", sb.toString().replace("'", "\\'").replace("\"", "\\\""), true);
                System.err.println("Todo mal: " + proc.exitValue() + " - " + line + "\n" + sb.toString());
            }

        } catch (Exception ex) {
            Logger.getLogger(BlastProcess.class.getName()).log(Level.SEVERE, null, ex);
            jDao.updateJobStatus(jobId, "Error", ex.getMessage().replace("'", "\\'").replace("\"", "\\\""), true);
            // adao.updateAnalisisProcessEstatus(idAnalisis, "Excepci&oacute;n creando bams individuales", "");
            // adao.insertAnalisisProceso(idAnalisis, "Excepci&oacute;n creando bams individuales", "", "", "" + ex.getCause().getClass());
        }
        jDao.closeConexion();
        return blast_ok;
    }

    /**
     * Este método se encarga de crear el archivo ordenado de la búsqueda de
     * blast.
     *
     * @return
     */
    public boolean creaSortedOutputBlastSH() {
        String[] command = {"sh", "-c", properties.generateSortedCutOutputCommandLine()};
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (process.exitValue() == 0) {
                jDao.updateJobStatus(jobId, "Terminado", "", true);
                return true;

            } else {
                // jDao.updateJobStatus(jobId, "Error", "Error formating output", false);
                /*
                 InputStream inputstream = proc.getInputStream();
                 InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                 BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                 while ((line = bufferedreader.readLine()) != null) {}
                 */
                jDao.updateJobStatus(jobId, "Error", process.getOutputStream().toString().replace("'", "\\'").replace("\"", "\\\""), true);
                return false;

            }
        } catch (Exception e) {
            jDao.updateJobStatus(jobId, "Error", e.getLocalizedMessage().replace("'", "\\'").replace("\"", "\\\""), true);
            return false;
        }

    }

    /**
     * Este método se encarga de crear el archivo ordenado de la búsqueda de
     * blast.
     *
     * @
     *
     * return
     */
    public boolean concatenaOutputsSH() {
        String[] command = {"sh", "-c", properties.generateConcatOutputCommandLine()};
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (process.exitValue() == 0) {
                jDao.updateJobStatus(jobId, "Concatenado", "", true);
                return true;

            } else {
                // jDao.updateJobStatus(jobId, "Error", "Error formating output", false);
                /*
                 InputStream inputstream = proc.getInputStream();
                 InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                 BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                 while ((line = bufferedreader.readLine()) != null) {}
                 */
                jDao.updateJobStatus(jobId, "Error concatenando archivos", process.getOutputStream().toString().replace("'", "\\'").replace("\"", "\\\""), true);
                return false;

            }
        } catch (Exception e) {
            jDao.updateJobStatus(jobId, "Error concatenando archivos", e.getLocalizedMessage().replace("'", "\\'").replace("\"", "\\\""), true);
            return false;
        }

    }

    public boolean concatenaOutputsSH2() {
        String[] command = {"sh", "-c", properties.generateConcatOutputCommandLine2()};
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (process.exitValue() == 0) {
                jDao.updateJobStatus(jobId, "Concatenado", "", true);
                return true;

            } else {
                // jDao.updateJobStatus(jobId, "Error", "Error formating output", false);
                /*
                 InputStream inputstream = proc.getInputStream();
                 InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                 BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                 while ((line = bufferedreader.readLine()) != null) {}
                 */
                jDao.updateJobStatus(jobId, "Error concatenando archivos", process.getOutputStream().toString().replace("'", "\\'").replace("\"", "\\\""), true);
                return false;

            }
        } catch (Exception e) {
            jDao.updateJobStatus(jobId, "Error concatenando archivos", e.getLocalizedMessage().replace("'", "\\'").replace("\"", "\\\""), true);
            return false;
        }

    }

    public boolean creaSortedOutputBlast() {
        boolean blast_ok = false;
        String line = properties.generateSortedCutOutputCommandLine();
        CommandLine cmdLine = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(properties.getWorkingDir()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(streamHandler);
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        try {
            jDao.updateJobStatus(jobId, "Sorting output", "", false);
            //adao.insertAnalisisProceso(idAnalisis, "Creando bams individuales", "...", bamsIndividuales, "");
            //adao.updateAnalisisProcessEstatus(idAnalisis, "Corriendo", "");
          //  System.out.println("Por empezar sort:" + line);
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            if (resultHandler.getExitValue() == 0) {
                jDao.updateJobStatus(jobId, "Finished", "", true);
                //     adao.insertAnalisisProceso(idAnalisis, "Bams creados", "OK", "", "");
                blast_ok = true;

            } else {
                jDao.updateJobStatus(jobId, "Error", outputStream.toString(), true);
                System.err.println("Error blast job: " + resultHandler.getExitValue() + " - " + line + "\n" + outputStream.toString());
                //   adao.insertAnalisisProceso(idAnalisis, "Error creando bams ", "Valor de salida incorrecto: " + resultHandler.getExitValue() + " Esperado: " + 0, "", outputStream.toString());
            }

        } catch (Exception ex) {
            Logger.getLogger(BlastProcess.class.getName()).log(Level.SEVERE, null, ex);
            jDao.updateJobStatus(jobId, "Error", ex.getMessage(), true);
            // adao.updateAnalisisProcessEstatus(idAnalisis, "Excepci&oacute;n creando bams individuales", "");
            // adao.insertAnalisisProceso(idAnalisis, "Excepci&oacute;n creando bams individuales", "", "", "" + ex.getCause().getClass());
        }
        return blast_ok;
    }

    @Override
    public void run() {
        this.correMultiBlast();
    }

}
