package org;

import java.util.ArrayList;

import org.Constraint.Type;

import com.microsoft.z3.Z3Exception;

public class DigitalTomographyCat {


  // Geht die Spalten von unten nach oben durch und von links nach rechts
  private static boolean[][] getCat() {
    boolean t = true;
    boolean f = false;
    boolean[][] pic =
        { {f, f, f, f, f, f, f, f, t, f, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f},
            {f, f, f, f, f, f, f, f, t, f, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f},
            {f, f, f, f, f, f, f, f, t, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f, f},
            {f, f, f, f, f, f, t, t, t, f, t, f, f, f, f, f, t, t, t, t, t, t, f, f, f},
            {f, f, f, f, f, t, f, t, f, t, t, t, f, f, f, t, f, f, f, f, f, f, t, t, f},
            {f, f, f, f, t, f, f, t, f, t, f, f, t, t, t, f, f, f, t, t, t, f, f, t, f},
            {f, f, f, t, f, f, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, t, f},
            {f, f, f, t, f, f, f, f, f, f, t, t, t, f, f, f, f, f, f, f, f, t, t, f, f},
            {f, f, t, f, f, t, f, f, f, f, t, t, f, t, f, f, f, f, t, t, t, t, f, f, f},
            {f, f, t, f, t, t, f, f, f, f, t, t, f, t, f, f, f, f, f, f, t, f, f, f, f},
            {f, t, f, f, t, f, f, f, f, f, t, f, f, t, f, f, f, f, f, f, t, f, f, f, f},
            {t, f, f, t, t, f, f, f, f, f, t, t, t, f, f, f, f, f, f, f, t, f, f, f, f},
            {t, f, t, t, t, f, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f},
            {t, f, t, t, t, f, t, f, t, t, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f},
            {t, f, t, t, f, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f},
            {t, f, t, t, f, f, f, t, t, t, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f},
            {t, f, t, t, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, t, f, f, f},
            {t, f, t, t, f, f, f, f, f, f, f, t, t, f, f, f, f, f, f, f, f, t, f, f, f},
            {t, f, t, t, f, f, f, f, f, f, t, t, f, t, f, f, f, f, f, f, f, t, f, f, f},
            {t, f, f, t, f, f, f, f, f, f, t, t, f, t, f, f, f, f, f, f, t, f, f, f, f},
            {t, t, f, t, f, f, f, f, f, f, t, f, f, t, f, f, f, f, f, f, t, f, f, f, f},
            {f, t, f, t, f, t, f, f, f, f, f, t, t, f, f, f, f, t, t, t, t, t, t, f, f},
            {f, t, f, f, f, t, f, t, f, f, f, f, f, f, f, f, t, f, f, f, f, f, f, t, f},
            {f, f, t, f, f, t, f, t, f, f, f, f, f, f, f, f, f, f, t, t, f, f, f, t, t},
            {f, f, t, f, f, t, f, t, f, f, f, f, f, f, f, t, f, f, f, f, f, t, t, f, f},
            {f, f, f, t, f, t, f, t, f, f, f, f, f, f, f, f, t, t, t, t, t, f, f, f, f},
            {f, f, f, f, t, t, f, f, t, f, f, f, t, t, t, t, f, f, f, f, f, f, f, f, f},
            {f, f, f, f, f, t, t, t, t, t, t, t, f, f, f, f, f, f, f, f, f, f, f, f, f},
            {f, f, f, f, f, t, f, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f},
            {f, f, f, f, f, t, f, f, t, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f}};
    for (int i = 0; i < pic.length; i++) {
      for (int j = 0; j < pic[i].length / 2; j++) {
        boolean tmp = pic[i][j];
        pic[i][j] = pic[i][pic[i].length - 1 - j];
        pic[i][pic[i].length - 1 - j] = tmp;
      }
    }
    return pic;

  }

  public static boolean[][] getCat(double n) {
    boolean[][] picOriginal = getCat();
    boolean[][] result =
        new boolean[(int) (picOriginal.length * n)][(int) (picOriginal[0].length * n)];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = picOriginal[i % picOriginal.length][j % picOriginal[0].length];
      }
    }
    return result;
  }

  public static ArrayList<Literal> solve(boolean[][] picture, Encoding encoding) throws Z3Exception {
    int width = picture.length;
    int height = picture[0].length;
    System.out.println(width + " " + height);
    ArrayList<Constraint> constraints = new ArrayList<>();
    // Spalten
    for (int x = 0; x < width; x++) {
      ArrayList<Literal> lits = new ArrayList<>();
      int sum = 0;
      for (int y = 0; y < height; y++) {
        lits.add(new Literal(x + width * y, true));
        if (picture[x][y]) {
          sum++;
        }
      }
      constraints.add(new Constraint(Type.EQUALS, lits, sum));
    }
    // Reihen
    for (int y = 0; y < height; y++) {
      ArrayList<Literal> lits = new ArrayList<>();
      int sum = 0;
      for (int x = 0; x < width; x++) {
        lits.add(new Literal(x + width * y, true));
        if (picture[x][y]) {
          sum++;
        }
      }
      constraints.add(new Constraint(Type.EQUALS, lits, sum));
    }
    // Diagonale /
    for (int d = -height + 1; d < width; d++) {
      ArrayList<Literal> lits = new ArrayList<>();
      int sum = 0;
      for (int x = d, y = 0; y < height; x++, y++) {
        if (x >= 0 && x < width) {
          lits.add(new Literal(x + width * y, true));
          if (picture[x][y]) {
            sum++;
          }
        }
      }
      constraints.add(new Constraint(Type.EQUALS, lits, sum));
    }

    // Diagonale \
    for (int d = 0; d < width + height - 1; d++) {
      ArrayList<Literal> lits = new ArrayList<>();
      int sum = 0;
      for (int x = d, y = 0; y < height; x--, y++) {
        if (x >= 0 && x < width) {
          lits.add(new Literal(x + width * y, true));
          if (picture[x][y]) {
            sum++;
          }
        }
      }
      constraints.add(new Constraint(Type.EQUALS, lits, sum));
    }

    System.out.println(constraints);
    Builder builder = new Builder();
    return builder.solve(constraints, width * height, encoding);
  }

}
