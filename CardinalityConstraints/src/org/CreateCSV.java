package org;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Diese Klasse erstellt aus der txt CSV-Dateien
 * 
 * @author Tietjen
 * 
 */
public class CreateCSV {

  private static String avgtime;
  private static String k;
  private static String endtime;

  /**
   * @param args
   */
  public static void main(String[] args) {

    String thisLine = null;
    BufferedReader br = null;
    Locale.setDefault(new Locale("en", "US"));
    try {
      // open input stream test.txt for reading purpose.
      br = new BufferedReader(new FileReader("ausgabe_mit_snth.txt"));
      writeToCSV("Problem", "Kodierung", "k", "middletime", "endtime", "logendtime", "numClauses",
          "lognumclauses", "status");
      String problem = "";
      String encoding = "";
      String k = "";
      long startTime = 0, midTime = 0, endTime = 0;
      String time1 = "", time2 = "";
      String numClauses = "";
      String status = "";
      boolean done = false;
      while (!done) {
        int phase = 0;
        while ((thisLine = br.readLine()) != null) {
          if (thisLine.contains(";")) {
            String[] line = thisLine.split(";");
            problem = line[0].replace(", ", "_");
            if (line[1].contains("size")) {
              k = line[1].substring(7);
            } else {
              k = line[1].substring(5);
            }
            encoding = line[2].substring(12);
          } else if (thisLine.contains("starting preprocessing")) {
            phase = 1;
          } else if (thisLine.contains("preprocessing done.")) {
            phase = 2;
          } else if (thisLine.contains("SAT") || thisLine.contains("UNSAT")) {
            phase = 3;
            status = thisLine;
          } else if (thisLine.contains("UNKNOWN")) {
            phase = 3;
            status = "TIMEOUT";
            time2 = "TIMEOUT";
          } else if (thisLine.contains("out of memory")) {
            status = "OUT OF MEMORY";
            time1 = "OUT OF MEMORY";
            time2 = "OUT OF MEMORY";
            numClauses = "OUT OF MEMORY";
          } else if (thisLine.contains("Number of clauses")) {
            // numClauses = thisLine.substring(19);
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);
            numClauses = df.format(Double.parseDouble(thisLine.substring(19)));
          } else if (thisLine.contains("-----")) {
            break;
          } else if (thisLine.contains("Timestamp")) {
            switch (phase) {
              case 1:
                startTime = Long.parseLong(thisLine.substring(11));
                break;
              case 2:
                String pattern = "######.###";
                DecimalFormat df = new DecimalFormat(pattern);
                df.setMaximumFractionDigits(8);
                midTime = Long.parseLong(thisLine.substring(11));
                time1 = df.format((midTime - (double) startTime));
                break;
              case 3:
                if (!status.equals("TIMEOUT")) {
                  String patternm = "######.###";
                  DecimalFormat dfm = new DecimalFormat(patternm);
                  dfm.setMaximumFractionDigits(8);
                  endTime = Long.parseLong(thisLine.substring(11));
                  time2 = dfm.format((endTime - (double) midTime));
                }
                break;
              default:
                throw new IndexOutOfBoundsException();
            }
          }

        }
        writeToCSV(
            problem,
            encoding,
            k,
            time1,
            time2,
            time2.equals("TIMEOUT") || time2.equals("OUT OF MEMORY") ? "na" : Double.toString(Math
                .log10(Double.parseDouble(time2))),
            numClauses,
            numClauses.equals("OUT OF MEMORY") ? "na" : Double.toString(Math.log10(Double
                .parseDouble(numClauses))), status);
        done = (thisLine == null);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        // whatever
        e.printStackTrace();
      }
    }
    // csvPerEncoding("Queensproblem", "KnuthBailleux");
    // csvPerEncoding("DigiTomo_H-Tree", "KnuthBailleux");
    // csvPerEncoding("DigiTomo_Cats", "KnuthBailleux");
    makeSynth("KnuthBailleux");

    // csvPerEncoding("Queensproblem", "KnuthSinz");
    // csvPerEncoding("DigiTomo_H-Tree", "KnuthSinz");
    // csvPerEncoding("DigiTomo_Cats", "KnuthSinz");
    makeSynth("KnuthSinz");

    // csvPerEncoding("Queensproblem", "NetworksNaiv");
    // csvPerEncoding("DigiTomo_H-Tree", "NetworksNaiv");
    // csvPerEncoding("DigiTomo_Cats", "NetworksNaiv");
    makeSynth("NetworksNaiv");

    // csvPerEncoding("Queensproblem", "NetworksOddEvenMergesort");
    // csvPerEncoding("DigiTomo_H-Tree", "NetworksOddEvenMergesort");
    // csvPerEncoding("DigiTomo_Cats", "NetworksOddEvenMergesort");
    makeSynth("NetworksOddEvenMergesort");

    // csvPerEncoding("Queensproblem", "NetworksOwnSorting");
    // csvPerEncoding("DigiTomo_H-Tree", "NetworksOwnSorting");
    // csvPerEncoding("DigiTomo_Cats", "NetworksOwnSorting");
    makeSynth("NetworksOwnSorting");

    // csvPerEncoding("Queensproblem", "NiklasseBDDs");
    // csvPerEncoding("DigiTomo_H-Tree", "NiklasseBDDs");
    // csvPerEncoding("DigiTomo_Cats", "NiklasseBDDs");
    makeSynth("NiklasseBDDs");

    // csvPerEncoding("Queensproblem", "SinzParallel");
    // csvPerEncoding("DigiTomo_H-Tree", "SinzParallel");
    // csvPerEncoding("DigiTomo_Cats", "SinzParallel");
    makeSynth("SinzParallel");

    // csvPerEncoding("Queensproblem", "Naiv");
    // csvPerEncoding("DigiTomo_H-Tree", "Naiv");
    // csvPerEncoding("DigiTomo_Cats", "Naiv");
    makeSynth("Naiv");

  }

  private static void writeToCSV(String problem, String encoding, String k, String l, String m,
      String logm, String numClauses, String logClauses, String status) {
    FileWriter fw;
    try {
      fw = new FileWriter("ergebnisse.csv", true);

      BufferedWriter bw = new BufferedWriter(fw);

      bw.write(problem + ";" + encoding + ";" + k + ";" + l + ";" + m + ";" + logm + ";"
          + numClauses + ";" + logClauses + ";" + status + ";" + "\r\n");

      bw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private static void csvPerEncoding(String problem, String encoding) {

    BufferedReader br = null;
    String thisLine = null;

    try {
      FileWriter fw = new FileWriter("ergebnisse_" + problem + "_" + encoding + ".csv", true);
      FileWriter fw2 =
          new FileWriter("ergebnisse_" + problem + "_" + encoding + "-Kopie" + ".csv", true);
      BufferedWriter bw = new BufferedWriter(fw);
      BufferedWriter bw2 = new BufferedWriter(fw2);
      br = new BufferedReader(new FileReader("ergebnisse.csv"));
      bw.write("Problem;Kodierung;k;middletime;endtime;logendtime;numClauses;lognumClauses;status"
          + "\r\n");
      bw2.write("Problem;Kodierung;k;middletime;endtime;logendtime;numClauses;lognumClauses;status"
          + "\r\n");

      while ((thisLine = br.readLine()) != null) {
        if (thisLine.startsWith(problem + ";" + encoding)) {
          if (!thisLine.contains("OUT OF MEMORY")) {
            bw2.write(thisLine + "\r\n");
            if (!thisLine.contains("TIMEOUT")) {
              bw.write(thisLine + "\r\n");
            }
          }
        }
      }
      bw.close();
      bw2.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void makeSynth(String encoding) {
    /*
     * avgtime = ""; endtime = ""; k = "";
     */
    double[] timeSum = new double[20];
    double[] clauseSum = new double[20];
    BufferedReader br = null;
    String thisLine = null;

    try {
      FileWriter fw = new FileWriter("ergebnisse_" + "_" + encoding + "avgs_synth" + ".csv", true);
      BufferedWriter bw = new BufferedWriter(fw);
      br = new BufferedReader(new FileReader("ergebnisse.csv"));

      bw.write("Problem;Kodierung;k;avgtime;logavgtime;avgclauses;logavgClauses" + "\r\n");

      br.readLine();
      while ((thisLine = br.readLine()) != null) {
        String[] line = thisLine.split(";");
        if (line[1].equals(encoding)) {
          int k = Integer.parseInt(line[2]);
          double time = Double.parseDouble(line[4]);
          double clauses = Double.parseDouble(line[6]);
          timeSum[(k - 250) / 250] += time;
          clauseSum[(k - 250) / 250] += clauses;
        }
      }
      for (int i = 0; i < 20; i++) {
        bw.write("SynthTests;" + encoding + ";" + (i + 1) * 250 + ";" + (timeSum[i] / 5) + ";"
            + Math.log10(timeSum[i] / 5) + ";" + (clauseSum[i] / 5) + ";"
            + Math.log10(clauseSum[i] / 5) + "\r\n");
      }
      bw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
