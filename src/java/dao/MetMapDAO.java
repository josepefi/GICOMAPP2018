/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.Transacciones;
import java.util.*;
//import utils.BStats;
import static utils.BStats.NaturalBreaks;
import utils.Colores;
//import utils.Colores;

/**
 *
 * @author R Loza
 */
public class MetMapDAO {

    private final Transacciones tran;
    private final Map<String, List<String>> dataSeries;
    private List<List<Integer>> qMatrix = new ArrayList<>();
    private String[] titulos;
    private final int nRows;
    private int nCols;
    private List<List<String>> colorSeries;
    public List<String> llaves;

    /*
    Este metodo obtiene una tabla hash de datos, con el EC como clave.
    @param m=3 para obtener los datos procesados con GhostKoala, m=2 
    para obtener los datos procesados con Trinitate, para m=1 tomará ambos
    métodos solo si hay un metagenoma, esto es mets.length=1;
    @param metNombres, arreglo de Strings que almacena los nombres de los metagenomas.
    @ruta identificador de la ruta metabolica
     */
    public MetMapDAO(String[] metNombres, String ruta, int m) {
        //public Transacciones(String database, String user, String ip, String password) {
        this.tran = new Transacciones("cigomdb", "cigom_u2", "132.248.32.18", "CigomDB17");
        nCols = metNombres.length;
        colorSeries = new ArrayList<>();
        if (nCols > 1) {
            titulos = metNombres;
            dataSeries = tran.getHashTable(metNombres, ruta, m);
        } else {
            String metNombre = metNombres[0];
            int k;
            switch (m) {
                case 2:
                    this.titulos = new String[1];
                    titulos[0] = "Trinotate";
                    k = 2;
                    break;
                case 3:
                    this.titulos = new String[1];
                    titulos[0] = "GhostKoala";
                    k = 3;
                    break;
                default:
                    this.nCols = 2;
                    this.titulos = new String[2];
                    titulos[0] = "Trinotate";
                    titulos[1] = "GhostKOALA";
                    k = 1;
                    break;
            }
            dataSeries = tran.getHashTableSingleMet(metNombre, ruta, k);
        }//FIN DE ELSE (if nCols >1)
        if (dataSeries != null) {
            nRows = dataSeries.size();
            llaves = new ArrayList<>();
            //t k=0;
            for (String s : dataSeries.keySet()) {
                llaves.add(s);
                //+;
            }
            Collections.sort(llaves);
        } else {

            nRows = 0;
        }
    }//Fin del constructor principal-

    /**
     * ******************************************************
     * Este m\'etodo devuelve el conjunto de claves ordenado que estan presentes
     * en los datos.
     *
     * @return Devuelve un ArrayList con las claves de los datos
    *******************************************************
     */
    public List<String> getKeys() {
        return llaves;
    }//fin de getKeys

    /**
     * *********************************************************
     * Este m\'etodo devuelve el valor del dato almacenado en la posici\'on pos,
     * para la clave EC. El valor es -1 si no existe dato asociado a la clave.
     *
     * @param EC, clave de la entrada.
     * @param pos, posición en el array de datos. La base de esta posicion es 0.
     * @return valor almacenado en la posición "pos" de la columna de datos.
    **********************************************************
     */
    public String getColValue(String EC, int pos) {
        String val = "NA";
        int numCols = titulos.length;
        boolean enRango = (pos > -1 && pos < numCols);
        if (dataSeries.containsKey(EC) && enRango) {
            val = dataSeries.get(EC).get(pos + 1);
        }
        return val;
    }

    /*
    @param ECstr la clave para acceder al valor de la serie.
    @param pos, entero que denota el orden del dato que se desea obtener.
     */
    public String getEnzimeName(String ECstr) {
        String enz = dataSeries.get(ECstr).get(0);
        return enz;
    }

    public int getDataColCount() {
        //int k = titulos.length;
        return nCols;
    }

    public int getRowCount() {

        //int k = dataSeries.size();
        return nRows;
    }

    /**
     * @param pos Indica la serie de la cual se quiere obtener el número de
     * categorias.
     * @return Regresa el numero de categorías para la serie dada por el
     * parámetro pos.
     */
    public int getNumCategorias(int pos) {
        int res = 0;
        //boolean enRango;
        if (qMatrix != null) {
            if (pos < nCols + 1) {
                res = qMatrix.get(pos).size() - 1;
            } else {
                res = -1;
            }
        }
        return res;
    }

    /*
    * Devuelve el valor del límite inferior del intervalo de la categoría
    * dada por nivel para la serie especificada en pos. Esto 
    * implica que el límite superior de la categoría con el número
    * ordinal más alto se obtiene mediante la llamada: getCategoriaValue(k, n+1).
    *En donde, n es el número de categorías para la serie pos.
    @param pos: Entero que especifica la serie.
    @param nivel: Nivel del cual se desea obtener el límite inferior. 
     */
    public int getCategoryValue(int pos, int nivel) {
        int res = 0;
        boolean enRango;
        //System.out.println("Hey " + qMatrix.get(pos).size());
        if (qMatrix != null) {
            enRango = (nivel > -1 && nivel < qMatrix.get(pos).size() + 1);
            if (enRango) {
                res = qMatrix.get(pos).get(nivel);
            } else {
                res = -1;
            }
        }
        return res;
    }

    public String getColTitle(int pos) {
        String t = "Serie " + String.valueOf(pos);
        int numCols = titulos.length;
        boolean enRango = (pos > -1 && pos < numCols);
        if (enRango) {
            t = titulos[pos];
        }
        return t;
    }

    /**
     * Este método devuelve la serie de datos para la columna especificada el
     * parámetro pos. El orden de la serie de datos se determina por la
     * ordenación alfabética de los EC's asociados. NOTA: Este método omite los
     * ceros.
     *
     * @param pos especifica la columna de la cual se devuelve la serie de
     * datos.
     * @return Devuelve la serie de datos, mediente una lista de enteros,
     * asociados a la columna de orden pos.
     */
    private List<Integer> serie(int pos) {
        List<Integer> r = new ArrayList<>();
        int numCols = titulos.length;
        boolean enRango = (pos > -1 && pos < numCols);
        try {
            if (nRows != 0 && enRango) {
                for (String s : dataSeries.keySet()) {
                    Integer n = Integer.valueOf(dataSeries.get(s).get(pos + 1));
                    //System.out.println(n);
                    if (n > 0) {
                        r.add(n);
                    }
                }
                Collections.sort(r);
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return r;
    }//Fin de serie

private void setIntervals(int pos, int numPart){
        List<Integer> serie = serie(pos);
        List<Integer> qArray = new ArrayList<>();
        int nDatos = serie.size();
        int numCat = numPart;
        boolean yerro;
        int[] q;// = new int[numPart-1];
        if (!serie.isEmpty()){
            //System.out.println("No esta vacÃ­o");
            int min= serie.get(0);
            int max= serie.get(nDatos-1);
            if (min==max){
                numCat=1;
                q=new int[2];
                //q[0]=0;
                //q[1]=0;
            }
            else{
                if (serie.size() < numCat){
                    numCat=serie.size();
                }
                do{               
                    try{
                        q=new int[numCat-1];
                        q = NaturalBreaks(serie, numCat);
                        yerro=false;
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                    //System.out.println("No se han podido establecer " + numCat + " categorias.");
                        numCat=numCat-1;
                        q=null;
                        q=new int[numCat-1];
                    //q=new double[numCat-1];
                        yerro=true;
                    }
                } while(numCat > 1 && yerro);
            }
            qArray.add(serie.get(0)); //Agrega el valor mÃ­nimo de la serie a qArray
        //A continuaciÃ³n agrega los valores lÃ­mites de los intervalos.
            if (numCat!=1){
                int a0=-1;
                for (int k=0;k<numCat-1;k++){
                    int a = q[k];
                    if (a0!=a){
                        qArray.add(a);
                        a0=a;
                    }
                }
            //qArray.add(serie.get(nDatos-1)); 
            }
            else{
                qArray.set(0, min-1);
                //qArray.add(serie.get(nDatos-1));
            }
            qArray.add(serie.get(nDatos-1)); //Agrega el valor mÃ¡ximo de la serie.
            qMatrix.add(qArray); //Agrega el vector de lÃ­mites a la matriz de lÃ­mites.
        //Imporime los valores de los lÃ­mites para confirmar el procedimiento.
            qArray.forEach((k) -> {
                System.out.println(k);
                });
           }//Fin de if(!isempty())
        else{
            System.out.println("Serie sin datos.");
            qArray.add(-1);
            qMatrix.add(qArray);
        }
        
    }//Fin de fixIntervals(pos, nCol)

    public void fixIntervals(int numPart) {
        for (int k = 0; k < nCols; k++) {
            setIntervals(k, numPart);
            int numIntervals = qMatrix.get(k).size() - 1;
            List<String> colorRow = new ArrayList<>();
            if (numIntervals != 1) {
                for (int j = 0; j < numIntervals; j++) {
                    colorRow.add(Colores.setHexColor(k, j + 2));
                }
            } else {
                colorRow.add(Colores.setHexColor(k, 3));
            }
            boolean add = colorSeries.add(colorRow);
        }

    }

    public String getHexColor(String EC, int pos) {
        String s = "#00000FF";
        //int m=-1;
        int val;
        val = Integer.parseInt(dataSeries.get(EC).get(pos + 1));
        if (qMatrix != null) {
            int maxI = qMatrix.get(pos).size();//Numero de intervalos en las serie dada en pos.
            if (maxI != 0) {                      //Recordando que en qMatrix se incluye los valores mínimo
                //y máximo de la serie.
                if (val == qMatrix.get(pos).get(0)) {//Al valor mínimo se le asigna la categoría 1.
                    //m=1;                   
                    s = colorSeries.get(pos).get(0);
                } else if (val == 0) {//Si no hay valor se le asigna la categoría 0 y se pinta blanco.
                    //m=0;
                    s = "#FFFFFF";
                } else {
                    int m = 0;
                    for (int k = 1; k < maxI; k++) {
                        int limitInterval = qMatrix.get(pos).get(k);
                        if (val <= limitInterval) {
                            m = k;
                            break;
                        }
                    }
                    s = colorSeries.get(pos).get(m - 1);
                }//fin if(val==0)

            }//q!=null => Hay intervalos.
            else {
                System.out.println("!!No se han establecido los intervalos!!");
            }
        }
        return s;

    }

    public boolean containsEC(String s) {
        boolean b = false;
        if (dataSeries != null) {
            b = dataSeries.containsKey(s.trim());
        }
        return b;
    }

    public String[] getColorSerie(int colData) {
        String[] s;
        if (colData > -1 & colData < nCols) {
            if (colorSeries != null) {
                int nCat = colorSeries.get(colData).size();
                s = new String[nCat];
                for (int k = 0; k < nCat; k++) {
                    s[k] = colorSeries.get(colData).get(k);
                }
            } else {
                s = new String[1];
                s[0] = "#FF0000";
            }
        } else {
            System.out.println("Error en el número de columnas.");
            s = new String[1];
            s[0] = "#00FF00";
        }
        return s;
    }

    /**
     * Este método devuelve en un arreglo de Strings, las columnas de las series
     * para dibujar el histograma, el orden en el arreglo es igual al de la
     * tabla de datos. La dimensión del arrglo es igual al de columnas de datos.
     * Y el número de renglones corresponden al número de EC's. NOTA: Se
     * incluyen los valores nulos (cero).
     *
     * @return Arreglo de las series de datos (columnas de la tabla).
     */
    public String getHistogramArrayData() {
        System.out.println("Cominenza el procesamiento del histograma");
        //String arrData ="{";
        String strCol = "";
        //String[] labels = new String[mMapa.getRowCount()];
        //List<String> claves = this.getKeys();
        for (int k = 0; k < nCols; k++) {
            strCol = strCol + "\"";
            for (String s : llaves) {
                strCol = strCol + this.getColValue(s, k) + "-";
                //System.out.println(strRow);
                //arrData=arrData+strCol;            
            }
            strCol = strCol.substring(0, strCol.length() - 1) + "\",";
        }
        strCol = "{" + strCol.substring(0, strCol.length() - 1) + "}";
        return strCol;
    }

    public String getTitles() {
        String s = "{";
        for (int k = 0; k < nCols; k++) {
            s = s + "\"" + titulos[k] + "\",";
        }
        s = s.substring(0, s.length() - 1) + "}";
        return s;
    }

    public String getHistogramColors(int nivel) {
        String s = "{";
        if (colorSeries.size() > 0) {
            for (int k = 0; k < nCols; k++) {
                String sTemp;
                int nCat = colorSeries.get(k).size();
                if (nCat != 0) {
                    sTemp = "\"" + colorSeries.get(k).get(nCat - 1) + "\"";
                } else {
                    sTemp = "\"#AAAAAA\"";
                }
                s = s + sTemp + ",";
            }

        } else {//Porque no se han establecido los intervalos. Por ejemplo, no hay mapa metabólico.
            for (int k = 0; k < nCols; k++) {
                String sTemp;
                sTemp = "\"" + Colores.setHexColor(k, 3) + "\"";
                s = s + sTemp + ",";
            }
        }
        s = s.substring(0, s.length() - 1) + "}";
        return s;
    }

    public String getHistogramData(int col) {
        String sData = "";
        String sColor;
        String metName;
        String sDataCol;
        metName = this.titulos[col];
        sColor = Colores.setHexColor(col, 2);
        sDataCol = "label : \"" + metName + "\", backgroundColor : \"" + sColor + "\", data : ";
        String arrDat = "[";
        for (String kS : llaves) {
            arrDat = arrDat + dataSeries.get(kS).get(col + 1) + ",";
        }
        arrDat = arrDat.substring(0, arrDat.length() - 1) + "]";
        sDataCol = sDataCol + arrDat;
        return sDataCol;
    }

    public String getHistogramLabels() {
        String s = " labels: [";
        for (String clave : llaves) {
            s = s + "\"" + clave + "\",";
        }
        s = s.substring(0, s.length() - 1) + "] ";
        return s;
    }
    
    private String buildLegendObject(int pos){
        String sObject = "NIL";
        int res=0;
        if (qMatrix!=null){
            if (pos < nCols+1){            
                res = qMatrix.get(pos).size()-1;
            }
            else{
              res=-1;  
            }
            if (res>0){ //La serie tiene datos.
                sObject= "{ boxGrad: "; //Construimos boxGrad
                int nCat = colorSeries.get(pos).size(); //Ya no verifico que haya datos pues he impuesto la condicion previamente.
                String s="[";
                for(int k=0;k<nCat;k++){
                    s = s + "\"" + colorSeries.get(pos).get(k)+"\",";
                }
                s=s.substring(0,s.length() -1) + "], labels: ";
                sObject = sObject+s;
                s="["; //Ahora obtenemos los labels de las categorias
                for (int k=0; k<res+1;k++){
                    s=s+"\""+qMatrix.get(pos).get(k).toString()+"\",";
                }
                s=s.substring(0,s.length() -1) + "], serieTitle: \"" + titulos[pos]+"\", longitud: 120 }";
                sObject=sObject+s;
            } 
            
        }//qMatrix es null si no se ha usado el metod FixIntervals
        
        return sObject;
    }//Fin de buildLegendObject
    
    public String getLegendBox(){
        String sB="[ ";
        for(int k=0;k<nCols;k++){
            String subBox=this.buildLegendObject(k);
            if (!subBox.equals("NIL")){
                sB=sB+subBox+ ",";
            }
        }
        sB=sB.substring(0,sB.length()-1);
        sB=sB+" ];";
        return sB;
    }


}//Fin de la clase MetTablaDAO



