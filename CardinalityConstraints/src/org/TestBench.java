package org;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.microsoft.z3.Z3Exception;

public class TestBench {

  private static boolean[][] picture;

  // Pro Kodierung pro Problem pro k
  public static final int TIMEOUT = 1800000; // in ms

  public static void main(String[] args) {
    int startingPoint = Integer.parseInt(args[0]);
    ArrayList<Encoding> encodings = new ArrayList<>();
//    encodings.add(new KnuthBailleux());
//    encodings.add(new KnuthSinz());
    // NetworksNaiv out of memory Dame k =50
//    encodings.add(new NetworksNaiv());
//    encodings.add(new NetworksOddEvenMergesort());
//    encodings.add(new NetworksOwnSorting());
//    encodings.add(new NiklasseBDDs());
//    encodings.add(new SinzParallel());
    // k=10 kracht bei Dame
    encodings.add(new Naiv());
    int counter = 0;
    try {
      for (int i = 0; i < encodings.size(); i++) {
//        // k-damen
//        counter++;
//        if (counter >= startingPoint) {
//          for (int k = 15; k <= 70; k += 5) {
//            file("Queensproblem; k = " + k + "; Encoding = "
//                + encodings.get(i).getClass().getSimpleName());
//            DamenProblem.solve(k, encodings.get(i));
//            file("-----------------------------------------------------");
//          }
//        }
//        counter++;
//        if (counter >= startingPoint) {
//          for (int k = 5; k <= 70; k += 5) {
//            file("DigiTomo, H-Tree; k = " + k + "; Encoding = "
//                + encodings.get(i).getClass().getSimpleName());
//            picture = new boolean[k][k];
//            // DigitalTomography.hTree(lineSize, depthRecursion, maxX, maxY, centerX, centerY,
//            // picture);
//            DigitalTomography.hTree(k / 2, 4, k, k, k / 2, k / 2, picture);
//            DigitalTomography.solve(picture, encodings.get(i));
//            file("-----------------------------------------------------");
//          }
//        }
//        counter++;
//        if (counter >= startingPoint) {
//          for (int k = 20; k <= 300; k += 20) {
//            double size = (double) k / 100;
//            file("DigiTomo, Cats; size = " + size + "; Encoding = "
//                + encodings.get(i).getClass().getSimpleName());
//            DigitalTomographyCat.solve(DigitalTomographyCat.getCat(size), encodings.get(i));
//            file("-----------------------------------------------------");
//          }
//        }
        counter++;
        if (counter >= startingPoint) {
          for (int k = 5; k <= 6; k += 1) {
            file("SynthTests; k = " + k + "; Encoding = "
                + encodings.get(i).getClass().getSimpleName());
            SynthetischeTestfaelle.solve(k, encodings.get(i));
            file("-----------------------------------------------------");
          }
        }
      }
    } catch (Z3Exception e) {
      file(e.getMessage());
      e.printStackTrace();
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
