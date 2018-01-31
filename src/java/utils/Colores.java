/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author rloza
 */
public class Colores {
public static final int AZULES = 0;
public static final int NARANJAS = 1;
public static final int  VERDES = 2;
public static final int PURPURAS = 3;
public static final int GRISES=4;

private static final int[][] GREENS =  {{255,255,204}, {194,230,153}, {120,198,121}, {49,163,84}, {0,104,55}};//YlGn
//{{237,248,251}, {178,226,226}, {102,194,164},{35,139,69}};
private static final int[][] PURPLES = {{241,238,246}, {215,181,216}, {223,101,176}, {221,28,119}, {152,0,67}};//PuRd
//{237,248,251}, {179,205,227}, {140,150,198}, {136,65,157}};
private static final int[][] BLUES =  {{240,249,232}, {186,228,188}, {123,204,196}, {67,162,202}, {8,104,172}}; //GnBlu
//{{240,249,232}, {186,228,188}, {123,204,196}, {43,140,190}};
private static final int[][] ORANGES = {{255,255,178}, {254,204,92}, {253,141,60}, {240,59,32}, {189,0,38}};//YlOrRed
//{{255,255,212}, {254,217,142}, {254,153,41}, {204,76,2}};
private static final int[][] PURPLERED = {{242,240,247}, {203,201,226}, {158,154,200}, {117,107,177}, {84,39,143}};//violet
//{{241,238,246}, {215,181,216}, {223,101,176}, {206,18,86}};
private static final int[][] GRAYS = {{247,247,247},{204,204,204},{150,150,150},{99,99,99},{37,37,37}};



public static String setColor(int categoria, int pallete){
    String s="nada";
    int[][] pal = new int[5][3];
    switch (pallete){
       case 0:
           pal = BLUES;
           break;
       case 1:
           pal = ORANGES;
           break;
       case 2:
           pal = GREENS;
           break;
       case 3:
           pal = PURPLES;
            break;
       case 4:
           pal=GRAYS;
           break;
       default :
           pal = PURPLERED;
           break;          
    }
    if (categoria < 0 || categoria > 4){
        s="1 0 0";
    }
    else if(categoria==0){
        s = "1 1 1";
    }
    else{
        double r = pal[categoria][0]/255.0;
        double g = pal[categoria][1]/255.0;
        double b = pal[categoria][2]/255.0;
        s = String.format( "%.3f", r ) + " " + String.format( "%.3f", g) + " " + String.format( "%.3f", b );
    }
    return s;
}

public static String setHexColor(int pallete, int categoria){
    String s="nada";
    int[][] pal = new int[5][3];
      switch (pallete){
       case 1:
           pal = GREENS;
           break;
       case 2:
           pal = ORANGES;
           break;
       case 3:
           pal = PURPLERED;
           break;
       case 4:
           pal = BLUES;
            break;
       case 5:
           pal = GRAYS;
           break;
       default :
           pal = PURPLES;
           break;          
    }
    if (categoria < 0 || categoria > 5){
        s="#FF0000";
    }
    else if(categoria==0){
        s = "#FFFFFF";
    }
    else{
        int red = pal[categoria-1][0];
        int green = pal[categoria-1][1];
        int blue = pal[categoria-1][2];
        s = "#" + diez2hex(red)+diez2hex(green)+diez2hex(blue);    }
    return s;
}

private static String diez2hex (int k){
String s="FF";
int q = k/16;
int r = k%16;
//System.out.println("este" + k+ " " + q+ " "+ r);
String su;
String sd;
switch (q){
    case 10:
        su = "A";
        break;
    case 11:
        su = "B";
        break; 
    case 12:
        su = "C";
        break;
    case 13:
        su = "D";
        break;
    case 14:
        su = "E";
        break;
    case 15:
        su = "F";
        break;
    default:
        su = String.valueOf(q);
    }        
    

 switch(r){
    case 10:
        sd = "A";
        break;
    case 11:
        sd = "B";
        break; 
    case 12:
        sd = "C";
        break;
    case 13:
        sd = "D";
        break;
    case 14:
        sd = "E";
        break;
    case 15:
        sd = "F";
        break;
    default:
        sd = String.valueOf(r);
    }
 s = su+sd;
 return s;
}

public String getHexColor(int categoria, int[][] paleta){
    String s="#FFFFFF";
        //int red = BLUES.[categoria-1][0];
        int red = paleta[categoria-1][0];
        int green = paleta[categoria-1][1];
        int blue = paleta[categoria-1][2];
        s = "#" + diez2hex(red)+diez2hex(green)+diez2hex(blue);   
    return s;
    }
}
