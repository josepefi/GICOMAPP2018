/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.util.*;
import database.Transacciones;

/**
 *
 * @author rloza
 */
public class StateMap {
    Transacciones t;
    private int deltaX = 500;
    private int deltaY = 700;
    private int h = 17;
    private int w = 46;
    private int numSeries = 1;
    private int lFontSize;
    private int nFontSize;
    private int textLegendWidth;
    private int hBox; //altura de las cajas de la leyenda
    private int lcb; // longitud de las cajas de la leyenda
    private int dcb; //interlineado de la laas series en la leyenda
    private int shiftText; //Desplazamiento del texto en la leyenda
    //private final int nCajas;

 
    private final List<List<String>> cajas;// = new ArrayList<>();

    /***************************************************************************
    Este constructor recibe el identificador de ruta metabólica y el número de
    * series. Este último parámetro controla el número de sectores en los que ha
    * de dividirse al ancho de las cajas.     
    ***************************************************************************/
    public StateMap(String rutaId, int numCols){
	this.lFontSize = 8;
        this.nFontSize = 9;
        this.hBox = 18;
        this.lcb= 29;
        this.shiftText = this.lFontSize+hBox+3;
        this.textLegendWidth = 120;
        this.deltaX = 500;
	this.deltaY = 700;
	this.h = 17;
	this.w = 46;
	this.numSeries = numCols; 
        this.t = new Transacciones("cigomdb", "cigom_u2", "132.248.32.18", "CigomDB17");
        this.cajas = t.getMapTable(rutaId);
        if (cajas!=null){
            int k = cajas.size();
            this.deltaX = Integer.parseInt(cajas.get(k-1).get(1));
            this.deltaY = Integer.parseInt(cajas.get(k-1).get(2));
        }
    }

    public String getEC(int k){
	return cajas.get(k).get(0);
    }

    private int[] getLT(int k){
	int[] punto = {Integer.parseInt(cajas.get(k).get(1)), Integer.parseInt(cajas.get(k).get(2))};
	return punto;
    }

    public void setBoxDimention(int x, int y){
	this.h = y;
	this.w = x;
    }

    public void setMapDimention(int x, int y){
	this.deltaY = y;
	this.deltaX = x;
    }

    public void setNumSeries(int n){
	int m = (n > 0)? n : 1;
	this.numSeries = m;
    }

    public int getNumBoxes(){
	int n = cajas.size();
	return n;
	
    }

    
    public String getEpsCoords(int k, int ord){
        String s;//"0 0";
	int x = this.getLT(k)[0];
        int y = this.getLT(k)[1];
        double dbox;
        dbox = w/numSeries;
        //int r = w%numSeries; 
        double xeps;
        if (ord==0){
            xeps = x;
        }
        else{
           xeps = x+dbox*ord;
        }
	int yeps = deltaY-y-h;
        String pref1 = String.format("%.2f", -dbox) + " " + String.format("%.2f", dbox)+ " " + String.format("%.2f", xeps);
	s = pref1 + " " + String.valueOf(yeps);
       	return s;
    }
    
    public String getEpsCoords(int k){
        String s;
        int x = this.getLT(k)[0];
        int y = this.getLT(k)[1];
        int yeps = deltaY-y-h;
        s= String.valueOf(x)+" "+String.format("%d",yeps);
        return s;
    }
    /**
    * Este m\'etodo devuelve en una cadena de car\'acteres separada por comas
    *las coordenadas de la esquina superior izquierda \textit{ltx, lty} de la 
    *de la k-\'esima caja en el ArrayList de cajas, as\'i como las dimensiones 
    * (ancho \textit{w}, alto \textit{h}) de la misma.
    * @param k posici\'on en el  ArrayList  la colecci\'on de cajas. 
     *@return String \textit{ltx, lty, w, h}
     */ 
    public String getFrameBoxHTML (int k){
        String s;
        double x = this.getLT(k)[0]*1.0;
        double y = this.getLT(k)[1]*1.0;
	double x_html = w*1.0;
	double y_html = h*1.0;
        s=String.format(Locale.US, "%.3f, %.3f, %.3f, %.3f",x,y,x_html,y_html);
        return s;
    }

    /**
    * Este m\'etodo devuelve en una cadena de car\'acteres separada por comas
    *las coordenadas de la esquina superior izquierda \textit{ltx, lty} de la 
    *de la k-\'esima caja en el ArrayList de cajas. 
    * @param k posici\'on en el  ArrayList  la colecci\'on de cajas. 
     *@return String \textit{ltx, lty}
     */   
    public String getHTMLCoords(int k){
        int x = this.getLT(k)[0];
        int y = this.getLT(k)[1];
	double x_html = x*1.0;
	double y_html = y*1.0;
	String html_coords = String.format("%.3f", x_html) + ", " + String.format("%.3f", y_html);
	return html_coords;
    }
      
    /**
    * Este m\'etodo devuelve en una cadena de car\'acteres separada por comas
    *las coordenadas de la esquina superior izquierda \textit{ltx, lty} de la porci\'on
    *de la k-\'esima caja, en el ArrayList de cajas, para la serie de datos en ord-\'esima posici\'on; 
    *tambien devuelve la altura \textit{h} y el ancho de la caja \textit{w}.  
    *es decir, devuelve: \textit{ltx, lty, w, h}.
     * @param k posici\'on de la k-\'esima caja. 
     * @param ord orden de la serie a dibujar.
     * @return \textit{ltx, lty, w, h}
     */
    public String getSubBox(int k, int ord){
        String s;
        int x=this.getLT(k)[0];
        int y = this.getLT(k)[1];
        double dbox = w/numSeries;
        //double hbox = h;
        double xhtml;
        if (ord==0){
            xhtml = x;
        }
        else{
           xhtml = x+dbox*ord;
        }
        s=String.format(Locale.US, "%.3f, %d, %.3f, %d", xhtml, y, dbox, 16);
        return s;
    }
  /**
    * Este m\'etodo devuelve en una cadena de car\'acteres separada por comas
    *las posici\'on \textit{x, y} del punto medio de la k-\'esima caja en el ArrayList 
    *de cajas. 
    * @param k posici\'on de la k-\'esima caja. 
    * @return \textit{x, y}
     */  
    public String getBoxMeanPoint(int k){
        String s;
        int x = this.getLT(k)[0];
        int y = this.getLT(k)[1];
	double x_html = (x+w*0.5)*1.0;
	double y_html = (y+h*0.5)*1.0;
        s=String.format(Locale.US, "%.3f, %.3f", x_html, y_html);
        return s;
    }
    
 /*   public int[] setParamLegend(int[] numIntervalos){
        int[] setP = new int[7];
        int horSpace;
        lcb = (int) (1.6182*hBox);
        int dSeries = (int) (0.6*lcb);
        horSpace =0;        
        for (int j=0;j< numIntervalos.length; j++){
           horSpace = horSpace + (textLegendWidth + lcb*numIntervalos[j])+dSeries;
        }
        horSpace = horSpace- lcb;
        int x0;
        x0=this.deltaX - 40 - horSpace;
	int y0=10;
        setP[0]=x0;
        setP[1]=y0;
        setP[2]=lcb;
        setP[3]=hBox;
        setP[4]=dSeries;
        setP[5]=shiftText;
        setP[6]=textLegendWidth;
        return setP;
}*///Fin de setParamLegend
      public int[] setParamLegend(int[] numIntervalos){
        
        int horSpace;
        int numS = numIntervalos.length/2;
        int cols=numS;
        int[] setP = new int[cols+4];
        //lcb = (int) (1.6182*hBox);
        int dSeries = (int) (0.6*lcb);
        //int deltaS=0;
        horSpace =0;
        int[] L = new int[numS];
        for (int j=0;j<numS;j++){
           if (numIntervalos[2*j]>0){ 
            L[j] = (int)((numIntervalos[2*j]+0.3)*lcb+numIntervalos[2*j+1]*(this.textLegendWidth/20.5));
            //AquÃ­ estimamos que una cadena de 21 carÃ¡cteres tiene una longitud de 104 pixeles. 
           }
           else{
            L[j]=0;
            numS=numS-1;
           }
           horSpace=horSpace+L[j];        
        }
        //Esta condiciÃ³n prueba si cabe la leyenda a la derecha
        if ((int)(this.deltaX-30)-(horSpace+(numS-1)*dSeries)> (int)(0.25*this.deltaX)){//Dejo un margen de 30 por lado
            //int shiftX=0;
            int xPos=this.deltaX-30;
            for (int j=0;j<cols;j++){
                if (L[cols-j-1]!=0){
                    if (j!=0){//No separa las series en la primera caja.
                        xPos=xPos-dSeries;
                    }
                }
                xPos=xPos-L[cols-j-1];
                setP[cols-j-1]=xPos;
            }       
            
        }
        else{//Si no cabe a la derecha separa uniformemente
            int sobra=Math.abs((int)((this.deltaX-30))-(horSpace));
            //System.out.println("Factor de escala " + l1);
            //if(sobra>0){
                lcb = 19;
                dSeries = sobra/(numS-1);
                int xPos=6;
                int longPrevia=0;
                for (int j=0;j<cols;j++){
                    if (longPrevia!=0){
                        if (j!=0){//No separa las series en la primera caja.
                            xPos=xPos+dSeries;
                        }
                    }
                    setP[j]=xPos;
                    xPos=xPos+L[j];
                    longPrevia=L[j];
                }    
            //}
            //else{//Aqui no queda mas remedio que truncar los titulos.
                
            //}
        }
        
	int y0=(int)(10); //ESta es la altura (en pixeles) a partir de la cual, se dibuja la leyenda.
        setP[cols]=y0;
        setP[cols+1]=hBox;
        setP[cols+2]=lcb;
        setP[cols+3]=dSeries;
        return setP;
}//Fin de setParamLegend  
    
    public void showData(){
        int nRenglones = this.cajas.size();
        if (nRenglones!=0){
            int nColumnas = cajas.get(0).size();
            for (int l=0;l<nRenglones;l++){
                for (int k=0;k<nColumnas;k++){
                    System.out.println("Dato " + cajas.get(l).get(k));
                }                
            }
        }
        else{
            System.out.println("No tenemos datos");
        }
    }
    
    public int getHorDim(){
        return this.deltaX;
    }
    
    public int getVertDim(){
        return this.deltaY;
    }
    
     public String getLegendParameters(){
        String sP="{ wBox: 22, hBox: 12,  vertPos: 10, gap: 15, margin: 15, dTitle: 5, deltaX: " + this.deltaX + " };";
        return sP;
    }
    
}
