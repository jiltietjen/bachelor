package org;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.microsoft.z3.Z3Exception;

public class TestBench {

  private static boolean[][] picture;

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
        DamenProblem.solve(k, encodings.get(i));
      }
      for (int k = 1; k <= 8; k++) {
        picture = new boolean[k][k];
        DigitalTomography.hTree(k / 2, 4, k, k, k / 2, k / 2, picture);
        DigitalTomography.solve(picture, encodings.get(i));
      }
      for (double k = 0.1; k <= 1.5; k++) {
        DigitalTomographyCat.solve(DigitalTomographyCat.getCat(k), encodings.get(i));
      }
      for (int k = 1; k <= 8; k++) {
        SynthetischeTestfaelle.solve(k, encodings.get(i));
      }
    }
  }

  public static void file(String text) {
    FileWriter fw;
    try {
      fw = new FileWriter("ausgabe.txt");

      BufferedWriter bw = new BufferedWriter(fw);

      bw.write(text);

      bw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
