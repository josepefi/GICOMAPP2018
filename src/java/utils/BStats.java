package utils;

import java.util.*;
import java.lang.Math;

/**
 *
 * @author R Loza
 * 
 */

public class BStats{


  public static double sigma(double[] data){
	double s = 0.0;
	double mu = 0.0;
	int n = data.length;
	for (int k=0; k<n;k++){
	    s = s + data[k];
	}
	
	if (n!=0){
	    mu = s/n;
	    for (int k =0; k<n;k++){
		s = s+Math.pow(data[k]-s,2);
	    }
	    s = Math.sqrt(s/n);
	    
	}
	else{
	    s = -1.0;
	}
	return s;
  }

    public static double mu(double[] data){
	double s = 0.0;
	double mu;
	int n = data.length;
	for (int k=0; k<n;k++){
	    s = s+data[k];
	}
	
	if (n!=0){
	    mu = s/n;
	}
	else{
	    mu=Double.NaN;
	} 
	return mu;
    }
    /*********************************************************
Este método implementa el algortimo Natural Breaks de Jenks,
que agrupa en un número determinado de clases,  los valores de
de una serie de datos. El criterio consiste en minimizar la desviación 
cuadrática entre los miembros del grupo y maximiza la desviación cuadrática
entre los centroides de los grupos. 
NOTA: Requiere los datos en un arreglo de números de doble precisión ORDENADO.
*************************************************************/

    /**
     * Este método implementa el algortimo Natural Breaks de Jenks,
que agrupa en un número determinado de clases,  los valores de
de una serie de datos.El criterio consiste en minimizar la desviación 
cuadrática entre los miembros del grupo y maximiza la desviación cuadrática
entre los centroides de los grupos. 
NOTA: Requiere los datos en un arreglo de números de doble precisión ORDENADO.
     * @param data
     */
    public static double[] NaturalBreaks(double[] data, int numberClasses) throws  java.lang.ArrayIndexOutOfBoundsException { 
	double[] limits = new double[numberClasses - 1]; 
   
	if (limits.length == 0) return limits; 
 
  //double[] orderedItems = DoubleArray.sort(data); 
	double[] orderedItems = data;
	int numData = data.length; 
	if (numData == 0) return limits; 
 
	double[][] mat1 = new double[numData + 1][numberClasses + 1]; 
	double[][] mat2 = new double[numData + 1][numberClasses + 1]; 
 
	for (int i = 1; i <= numberClasses; i++) { 
	    mat1[1][i] = 1; 
	    mat2[1][i] = 0; 
	    for (int j = 2; j <= numData; j++) 
		mat2[j][i] = Double.MAX_VALUE; 
	} 
	double v = 0; 
   
	for (int l = 2; l <= numData; l++) { 
            double s1 = 0; 
            double s2 = 0; 
            double w = 0; 
            for (int m = 1; m <= l; m++) { 
                int i3 = l - m + 1; 
                double val = orderedItems[i3-1];                 
                 
                s2 += val * val; 
                s1 += val;                
          
                w++; 
                v = s2 - (s1 * s1) / w; 
                int i4 = i3 - 1; 
                if (i4 != 0) { 
                    for (int j = 2; j <= numberClasses; j++) { 
                        if (mat2[l][j] >= (v + mat2[i4][j- 1])) { 
                            mat1[l][j] = i3; 
                            mat2[l][j] = v + mat2[i4][j -1]; 
                        }; 
                    }; 
                }; 
            }; 
            mat1[l][1] = 1; 
            mat2[l][1] = v; 
        }; 
        int k = numData; 
 
        for (int j = numberClasses; j >= 2; j--) { 
            int id =  (int) (mat1[k][j]) - 2; 
            //-- [sstein] modified version from Hisaji, 
            //      otherwise breaks will be "on" one item             
            // limits[j - 2] = orderedItems[id]; 
            //-- new 
            double limit = 0.5*(orderedItems[id]+orderedItems[id+1]); 
            limits[j - 2] = limit; 
             
            k = (int) mat1[k][j] - 1;    
        }; 
         
	return limits; 
    }//**********FIN DE NATURAL BREAKS DOUBLE**********

    //public static int[] NaturalBreaks(int[] data, int numberClasses) { 
    public static int[] NaturalBreaks(List<Integer> dataSerie, int numberClasses){
	
	int[] intLim = new int[numberClasses - 1];
	double[] limits = new double[numberClasses - 1];
	int[] data = dataSerie.stream().filter(i->i != null).mapToInt(i -> i).toArray();

	int j = data.length;

	double[] rDat = new double[j];

	for (int k=0;k<j;k++){
	    rDat[k] = (double) data[k];
	}

	limits = NaturalBreaks(rDat, numberClasses);

	for (int k=0; k < (numberClasses-1); k++){
	    intLim[k] = (int) limits[k];
	}
	return intLim;
	 
    }//**********FIN DE NATURAL BREAKS ARRAY LIST**********


    public static int[] NaturalBreaks(int[] data, int numberClasses) { 
   
	
	int[] intLim = new int[numberClasses - 1];
	double[] limits = new double[numberClasses - 1];
	//int[] data = dataSerie.stream().filter(i->i != null).mapToInt(i -> i).toArray();

	int j = data.length;

	double[] rDat = new double[j];

	for (int k=0;k<j;k++){
	    rDat[k] = (double) data[k];
	}

	limits = NaturalBreaks(rDat, numberClasses);

	for (int k=0; k < (numberClasses-1); k++){
	    intLim[k] = (int) limits[k];
	}
	return intLim;
	 
    }//**********FIN DE NATURAL BREAKS INT**********
    

    
}
