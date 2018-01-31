/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job;

/**
 *
 * @author Alejandro
 */
public class Job {

    private String id_job;
    private String idUsuario;
    private String job_url;
    private String job_name;
    private String job_type;
    private String evalue;
    private String host;
    private String start_date;
    private String end_date;
    private String status;
    private String message;
    private String metagenomas;
    private String genomas;
    private String path; //el path hasta antes de la carpeta con el id del job ejemplo /home/blast/querys/
    private String queryPath;

    public Job(String id_job) {
        this.id_job = id_job;
    }

    public String getQueryPath() {
        return queryPath;
    }

    public void setQueryPath(String queryPath) {
        this.queryPath = queryPath;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getJob_url() {
        return job_url;
    }

    public void setJob_url(String job_url) {
        this.job_url = job_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getEvalue() {
        return evalue;
    }

    public void setEvalue(String evalue) {
        this.evalue = evalue;
    }

    public String getMetagenomas() {
        return metagenomas;
    }

    public void setMetagenomas(String metagenomas) {
        this.metagenomas = metagenomas;
    }

    public String getGenomas() {
        return genomas;
    }

    public void setGenomas(String genomas) {
        this.genomas = genomas;
    }

    public String getId_job() {
        return id_job;
    }

    public void setId_job(String id_job) {
        this.id_job = id_job;
    }

    public String getURL() {
        return job_url;
    }

    public void setURL(String uid_job) {
        this.job_url = uid_job;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
