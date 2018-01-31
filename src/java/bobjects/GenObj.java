/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobjects;

import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class GenObj {

    String genID; //ID único del gen asignado por nosotros
    String object_id; //ID del objeto en la base de datos mongo (archivo de contigs/scaffolds)
    String gene_map_id; //ID para mapear otros resultados por ejemplo FGS cnvierte los IDs a gen_id_1, gen_id_n
    String genType; //cds rna, trna, etc
    String gen_strand; //+ -
    String gen_function;//funcion o definicion del gen
    String contig_id; //ID del contig al cual pertenece el gen
    String contig_gen_id; //ID del gen dentro del archivo de contigs
    String gen_src; //genoma o metagenoma
    String idGeMe;
    int contig_from; //posicion de del gen dentro del contig
    int contig_to;//pos del gen dentro del ccontig
    int gen_lenght;//
    ArrayList<GenSeqObj> sequences;//secuencias asociadas a dicho gen
    ArrayList<DBProperty> props;//campos no modelados que pueden guardarse en MOngo
    private int idmuestra;
    private String etiquetaMuestra = "";
    private double latitud;
    private double longitud;
    private String profundidad = "";
    private String tipoMuestra = "";
    private String name = "";//nombre metagenoma/genoma
    ArrayList<Eggnog> eggno;
    ArrayList<PFAMObj> pfam;
    ArrayList<GOObj> go;
    SecuenciaObj sec5P;//= new SecuenciaObj(genID,"5p");
    SecuenciaObj sec3P;// = new SecuenciaObj(genID,"3p"); 
    SecuenciaObj secAA;// = new SecuenciaObj(genID,"aa");
    SecuenciaObj secNC;//= new SecuenciaObj(genID,"nc");
    SwissProtObj blastP;
    SwissProtObj blastX;
    public ArrayList<GOObj> getGo() {
        return go;
    }

    public String getIdGeMe() {
        return idGeMe;
    }

    public void setIdGeMe(String idGeMe) {
        this.idGeMe = idGeMe;
    }
    
    public ArrayList<DBProperty> getProps() {
        return props;
    }

    public void setProps(ArrayList<DBProperty> props) {
        this.props = props;
    }

    public SwissProtObj getBlastP() {
        return blastP;
    }

    public void setBlastP(SwissProtObj blastP) {
        this.blastP = blastP;
    }

    public SwissProtObj getBlastX() {
        return blastX;
    }

    public void setBlastX(SwissProtObj blastX) {
        this.blastX = blastX;
    }

    public void setGo(ArrayList<GOObj> go) {
        this.go = go;
    }

    
    
    public ArrayList<PFAMObj> getPfam() {
        return pfam;
    }

    public void setPfam(ArrayList<PFAMObj> pfam) {
        this.pfam = pfam;
    }

    
    public void addEggnog(Eggnog e) {
        this.eggno.add(e);
    }
    public void addPfam(PFAMObj p) {
        this.pfam.add(p);
    }
    
    public void addGenGo(GOObj g) {
        this.go.add(g);
    }

    public ArrayList<Eggnog> getEggno() {
        return eggno;
    }

    public void setEggno(ArrayList<Eggnog> eggno) {
        this.eggno = eggno;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGen_lenght() {
        return gen_lenght;
    }

    public void setGen_lenght(int gen_lenght) {
        this.gen_lenght = gen_lenght;
    }

    public int getIdmuestra() {
        return idmuestra;
    }

    public void setIdmuestra(int idmuestra) {
        this.idmuestra = idmuestra;
    }

    public String getEtiquetaMuestra() {
        return etiquetaMuestra;
    }

    public void setEtiquetaMuestra(String etiquetaMuestra) {
        this.etiquetaMuestra = etiquetaMuestra;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(String tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public String getGenID() {
        return genID;
    }

    public String getGen_src() {
        return gen_src;
    }

    public void setGen_src(String gen_src) {
        this.gen_src = gen_src;
    }

    public void addSequence(GenSeqObj seq) {
        sequences.add(seq);
    }

    public void addProperty(String key, String value) {
        props.add(new DBProperty(key, value));
    }

    public void addProperty(String key, String value, boolean isNumeric) {
        props.add(new DBProperty(key, value));
    }

    public void insertProperty(DBProperty dbProp) {
        props.add(dbProp);
    }

    public String getGene_map_id() {
        return gene_map_id;
    }

    public void setGene_map_id(String gene_map_id) {
        this.gene_map_id = gene_map_id;
    }

    public ArrayList<GenSeqObj> getSequences() {
        return sequences;
    }

    public void setSequences(ArrayList<GenSeqObj> sequences) {
        this.sequences = sequences;
    }

    public void setGenID(String genID) {
        this.genID = genID;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getGenType() {
        return genType;
    }

    public void setGenType(String genType) {
        this.genType = genType;
    }

    public String getGen_strand() {
        return gen_strand;
    }

    public void setGen_strand(String gen_strand) {
        this.gen_strand = gen_strand;
    }

    public String getGen_function() {
        return gen_function;
    }

    public void setGen_function(String gen_function) {
        this.gen_function = gen_function;
    }

    public String getContig_id() {
        return contig_id;
    }

    public void setContig_id(String contig_id) {
        this.contig_id = contig_id;
    }

    public String getContig_gen_id() {
        return contig_gen_id;
    }

    public void setContig_gen_id(String contig_gen_id) {
        this.contig_gen_id = contig_gen_id;
    }

    public int getContig_from() {
        return contig_from;
    }

    public void setContig_from(int contig_from) {
        this.contig_from = contig_from;
    }

    public int getContig_to() {
        return contig_to;
    }

    public void setContig_to(int contig_to) {
        this.contig_to = contig_to;
    }

    public GenObj(String genID) {
        this.genID = genID;
        sequences = new ArrayList<GenSeqObj>();
        props = new ArrayList<DBProperty>();

        sec5P = new SecuenciaObj(genID, "5p");
        sec3P = new SecuenciaObj(genID, "3p");
        secAA = new SecuenciaObj(genID, "aa");
        secNC = new SecuenciaObj(genID, "nc");
        
        eggno = new ArrayList<>();
        pfam = new ArrayList<>();
        go = new ArrayList<>();
    }

    public SecuenciaObj getSec5P() {
        return sec5P;
    }

    public void setSec5P(SecuenciaObj sec5P) {
        this.sec5P = sec5P;
    }

    public SecuenciaObj getSec3P() {
        return sec3P;
    }

    public void setSec3P(SecuenciaObj sec3P) {
        this.sec3P = sec3P;
    }

    public SecuenciaObj getSecAA() {
        return secAA;
    }

    public void setSecAA(SecuenciaObj secAA) {
        this.secAA = secAA;
    }

    public SecuenciaObj getSecNC() {
        return secNC;
    }

    public void setSecNC(SecuenciaObj secNC) {
        this.secNC = secNC;
    }

}
