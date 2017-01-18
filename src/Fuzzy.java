
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Budi Pradnyana
 */
public class Fuzzy {
    Workbook w;
    double file_input[][];
    String file_output[];
    double row_input[] = new double[3];
    String target;
    int max_sample;

    //buat di fuzzy rule
    public String strVal;
    public double numVal;
    
    public String[] strVal_rule = new String[8];
    public double[] numVal_rule = new double[8];
    
    
    // buat di fungsi keanggotaan
    public String strVal_stg[] = new String[2];
    public String strVal_scg[] = new String[2];
    public String strVal_peg[] = new String[2];
    
    public double numVal_stg[] = new double[2];
    public double numVal_scg[] = new double[2];
    public double numVal_peg[] = new double[2];
       

    //read data dari excel
    public void ReadData() throws IOException, BiffException{
        w = Workbook.getWorkbook(new File("D:/data_uns.xls"));        
        Sheet sheet = w.getSheet(0); 
        file_input = new double[sheet.getRows()][sheet.getColumns()-1];
        file_output = new String[sheet.getRows()];
        for (int i = 0; i < sheet.getRows(); i++) {
            for(int j=0; j<sheet.getColumns()-1; j++){
                Cell data = sheet.getCell(j, i);
                file_input[i][j] = Double.parseDouble(data.getContents());          
            }
        }
        for (int i = 0; i < sheet.getRows(); i++) {
                Cell data = sheet.getCell(3, i);
                file_output[i] = data.getContents();          
        }
        max_sample = file_input.length;
    }    
    
    
    //buat cari keanggotaan fuzzy u/ stg
    public void stgLevel(double row_input){
        double stg_a = 0.22;
        double stg_b = 0.45;
        double stg_c = 0.76;
        
        if((row_input<stg_a)){
            strVal_stg[0]="Low";
            numVal_stg[0]=1;  
        }else if((row_input>stg_a)&&(row_input<stg_b)){
            strVal_stg[0]="Low";
            numVal_stg[0]=(stg_b-row_input)/(stg_b-stg_a);
            
            strVal_stg[1]="Middle";
            numVal_stg[1]=(row_input-stg_a)/(stg_b-stg_a);
        }else if((row_input>stg_b)&&(row_input<stg_c)){
            strVal_stg[0]="Middle";
            numVal_stg[0]=(stg_c-row_input)/(stg_c-stg_b);
            
            strVal_stg[1]="High";            
            numVal_stg[1]=(row_input-stg_b)/(stg_c-stg_b);
        }else if(row_input>stg_c){
            strVal_stg[0]="High";
            numVal_stg[0]=1;
        }
    }
    
    //buat cari keanggotaan fuzzy u/ scg
    public void scgLevel(double row_input){
        double scg_a = 0.22;
        double scg_b = 0.45;
        double scg_c = 0.76;
        
        if((row_input<0.25)){
            strVal_scg[0]="Low";
            numVal_scg[0]=1;
        }else if((row_input>scg_a)&&(row_input<scg_b)){
            strVal_scg[0]="Low";
            numVal_scg[0]=(scg_b-row_input)/(scg_b-scg_a);
            
            strVal_scg[1]="Middle";
            numVal_scg[1]=(row_input-scg_a)/(scg_b-scg_a);
        }else if((row_input>scg_b)&&(row_input<scg_c)){
            strVal_scg[0]="Middle";
            numVal_scg[0]=(scg_c-row_input)/(scg_c-scg_b);
            
            strVal_scg[1]="High";
            numVal_scg[1]=(row_input-scg_b)/(scg_c-scg_b);
        }else if(row_input>scg_c){
            strVal_scg[0]="High";
            numVal_scg[0]=1;
        }
    }
    
    //buat cari keanggotaan fuzzy u/ peg
    public void pegLevel(double input){
        double peg_a = 0.22;
        double peg_b = 0.45;
        double peg_c = 0.76;
        
        if((input<peg_a)){
            strVal_peg[0]="Low";
            numVal_peg[0]=1;
        }else if((input>peg_a)&&(input<peg_b)){
            strVal_peg[0]="Low";
            numVal_peg[0]=(peg_b-input)/(peg_b-peg_a);
            
            strVal_peg[1]="Middle";
            numVal_peg[1]=(input-peg_a)/(peg_b-peg_a);
        }else if((input>peg_b)&&(input<peg_c)){
            strVal_peg[0]="Middle";
            numVal_peg[0]=(peg_c-input)/(peg_c-peg_b);
            
            strVal_peg[1]="High";
            numVal_peg[1]=(input-peg_b)/(peg_c-peg_b);
        }else if(input>peg_c){
            strVal_peg[0]="High";
            numVal_peg[0]=1;
        }
    }
    
    //buat nyari nilai minimum
    public double FindMin(double a, double b, double c){
        ArrayList<Double> list = new ArrayList();
        double res;
        list.add(a);
        list.add(b);
        list.add(c);

        res = Collections.min(list);
        return res;
    }

    public void FuzzyRule() {
        int index = 0;
        //klo low atau high berarti index 1 belum diisi, isiin dulu ama value sesuai value array 0
        if(strVal_stg[0].equals("Low") && strVal_stg[1] == null){
            strVal_stg[1]="Low";
            numVal_stg[1]=1;
        } else if(strVal_stg[0].equals("High") && strVal_stg[1] == null){
            strVal_stg[1]="High";
            numVal_stg[1]=1;
        }
        if(strVal_scg[0].equals("Low") && strVal_scg[1] == null){
            strVal_scg[1]="Low";
            numVal_scg[1]=1;
        } else if(strVal_scg[0].equals("High") && strVal_scg[1] == null){
            strVal_scg[1]="High";
            numVal_scg[1]=1;
        }
        if(strVal_peg[0].equals("Low") && strVal_peg[1] == null){
            strVal_peg[1]="Low";
            numVal_peg[1]=1;
        } else if(strVal_peg[0].equals("High") && strVal_peg[1] == null){
            strVal_peg[1]="High";
            numVal_peg[1]=1;
        }
        
        //loop sebanyak 8 kali, ada 3 level yaitu low Middle hig, satu tipe scg,stg & peg maks bsa nampung 2 level, jadi 2 pangkat 3 = 8
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                                
            /*1*/   if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Very Low";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]); 
            /*2*/   } else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Low";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*3*/   } else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*4*/   } else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Low";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*5*/   }  else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*6*/   } else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*7*/   } else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Low";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*8*/   }  else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*9*/   } else if ((strVal_stg[i].equals("Low")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*10*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Low";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*11*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*12*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*13*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Low";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*14*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*15*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*16*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*17*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*18*/  } else if ((strVal_stg[i].equals("Middle")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*19*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Low";
                         numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*20*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*21*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("Low")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*22*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Low";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*23*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*24*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("Middle")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*25*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("Low"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*26*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("Middle"))) {
                        strVal = "Middle";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
            /*27*/  } else if ((strVal_stg[i].equals("High")) && (strVal_scg[j].equals("High")) && (strVal_peg[k].equals("High"))) {
                        strVal = "High";
                        numVal = FindMin(numVal_stg[i], numVal_scg[j], numVal_peg[k]);
                    }
                    
                    strVal_rule[index] = strVal;
                    numVal_rule[index] = numVal;
                    index = index+1;  
                }
            }
        }
        //udh selesai ngeloop 8 kali lalu reset index 
        index = 0;
    }

    //nyari centerofgravity dr hasil fuzzy rule
    public double CenterOfGravity() {
        double centerOfGravity;
        double summing = 0.0;
        double summakh = 0.0;
                
        for(int i=0; i< numVal_rule.length; i++){
            summing = summing +(numVal_rule[i]*Model(strVal_rule[i]));
            summakh = summakh + numVal_rule[i];
        }
        centerOfGravity = (summing/summakh);
        return centerOfGravity;
    }
    
    //buat ngambil model sugeno nya
    public double Model(String m) {
        double nilai = 0;

        if ("Very Low".equals(m)) {
            nilai = 0.2;
        } else if ("Low".equals(m)) {
            nilai = 0.4;
        } else if ("Middle".equals(m)) {
            nilai = 0.67;
        } else if ("High".equals(m)) {
            nilai = 0.8;
        }

        return nilai;
    }
    
    //convert nilai akhir centerOfGravity menjadi level low/very low/Middle/high
    public String deffuzification(double val){
        double a = 0.07;
        double b = 0.46;
        double c = 0.7;
        double d = 0.8;
        double nilai1,nilai2 = 0;
        String defuz;
        
        if (val <= a) {
            defuz = "Very Low";
        } else if ((val > a) && (val <= b)) {
            nilai1 = (b - val) / (b - a);
            nilai2 = (val - a) / (b - a);
            if (nilai1 > nilai2) {
                defuz = "Very Low";
            } else {
                defuz = "Low";
            }
        } else if ((val > b) && (val <= c)) {
            nilai1 = (c - val) / (c - b);
            nilai2 = (val - b) / (c - b);
            if (nilai1 > nilai2) {
                defuz = "Low";
            } else {
                defuz = "Middle";
            }
        } else if ((val > c) && (val <= d)) {
            nilai1 = (d - val) / (d - c);
            nilai2 = (val - c) / (d - c);
            if (nilai1 > nilai2) {
                defuz = "Middle";
            } else {
               defuz = "High";
            }
        } else {
            defuz = "High";
        }
        return defuz;
    }

   
    //apply smua proses
    public void FuzzySystem() throws IOException, BiffException{
        double benar=0.0;
        double akurasi;

        ReadData();
        System.out.printf("%-10s %-10s %-10s %-10s %-14s %-14s\n", "No", "STG", "SCG", "PEG", "OUTPUT", "TARGET");
        for(int i=0; i<max_sample; i++){
            for(int j=0; j<3; j++){
                row_input[j] = file_input[i][j];
            }
            target = file_output[i];
            
            stgLevel(row_input[0]);
            scgLevel(row_input[1]);
            pegLevel(row_input[2]);
            
            FuzzyRule();
            System.out.printf("%-10s %-10s %-10s %-10s %-14s %-14s\n", (i+1), row_input[0], row_input[1], row_input[2], deffuzification(CenterOfGravity()), target);

            if(deffuzification(CenterOfGravity()).equals(target)){
                benar = benar+1;
            }
        }
        akurasi = (benar/max_sample)*100;
        System.out.println();
        System.out.println("Jumlah Benar : "+benar);
        System.out.println("Total Sample : "+max_sample);
        System.out.println("Akurasi      : "+akurasi+" %");
    }

}
