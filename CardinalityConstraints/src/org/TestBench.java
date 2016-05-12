package org;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.microsoft.z3.Z3Exception;

public class TestBench {

  private static boolean[][] picture;

  public static final int TIMEOUT = 3000; // in ms

  public static void main(String[] args) throws Z3Exception {
    ArrayList<Encoding> encodings = new ArrayList<>();
    encodings.add(new KnuthBailleux());
    encodings.add(new KnuthSinz());
    encodings.add(new NetworksNaiv());
    encodings.add(new NetworksOddEvenMergesort());
    encodings.add(new NetworksOwnSorting());
    encodings.add(new NiklasseBDDs());
    encodings.add(new SinzParallel());
    for (int i = 0; i < encodings.size(); i++) {
      for (int k = 1; k <= 8; k++) {
        file("Queensproblem; k = " + k + "; Encoding = "
            + encodings.get(i).getClass().getSimpleName());
        DamenProblem.solve(k, encodings.get(i));
        file("-----------------------------------------------------");
      }
      for (int k = 1; k <= 8; k++) {
        file("DigiTomo, H-Tree; k = " + k + "; Encoding = "
            + encodings.get(i).getClass().getSimpleName());
        picture = new boolean[k][k];
        DigitalTomography.hTree(k / 2, 4, k, k, k / 2, k / 2, picture);
        DigitalTomography.solve(picture, encodings.get(i));
        file("-----------------------------------------------------");
      }
      for (int k = 10; k <= 50; k += 5) {
        double size = (double) k / 100;
        file("DigiTomo, Cats; size = " + size + "; Encoding = "
            + encodings.get(i).getClass().getSimpleName());
        DigitalTomographyCat.solve(DigitalTomographyCat.getCat(size), encodings.get(i));
        file("-----------------------------------------------------");
      }
      for (int k = 1; k <= 8; k++) {
        file("SynthTests; k = " + k + "; Encoding = " + encodings.get(i).getClass().getSimpleName());
        SynthetischeTestfaelle.solve(k, encodings.get(i));
        file("-----------------------------------------------------");
      }
    }
  }

  public static void file(String text) {
    FileWriter fw;
    try {
      fw = new FileWriter("ausgabe.txt", true);

      BufferedWriter bw = new BufferedWriter(fw);

      bw.write(text + "\r\n");

      bw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void measureTime() {
    file("Timestamp: " + Long.toString(System.currentTimeMillis()));
  }
}
