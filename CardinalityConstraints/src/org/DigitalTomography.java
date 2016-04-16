package org;

import java.util.ArrayList;

import org.Constraint.Type;

import com.microsoft.z3.Z3Exception;

public class DigitalTomography {



  // Bildgrösse maxX und maxY
  public static void hTree(int lineSize, int depthRecursion, int maxX, int maxY, int centerX,
      int centerY, boolean[][] picture) {
    if (depthRecursion <= 0) {
      return;
    }
    for (int x = Math.max(0, centerX - lineSize / 2); x <= Math.min(maxX - 1, centerX + lineSize
        / 2)
        && centerY >= 0 && centerY < maxY; x++) {
      // schwarzer Pixel
      picture[x][centerY] = true;
    }
    for (int y = Math.max(0, centerY - lineSize / 2); y <= Math.min(maxY - 1, centerY + lineSize
        / 2)
        && centerX - lineSize / 2 >= 0 && centerX - lineSize / 2 < maxX; y++) {
      // schwarzer Pixel
      picture[centerX - lineSize / 2][y] = true;
    }
    for (int y = Math.max(0, centerY - lineSize / 2); y <= Math.min(maxY - 1, centerY + lineSize
        / 2)
        && centerX + lineSize / 2 >= 0 && centerX + lineSize / 2 < maxX; y++) {
      // schwarzer Pixel
      picture[centerX + lineSize / 2][y] = true;
    }
    hTree(lineSize / 2, depthRecursion - 1, maxX, maxY, centerX + lineSize / 2, centerY + lineSize
        / 2, picture);
    hTree(lineSize / 2, depthRecursion - 1, maxX, maxY, centerX - lineSize / 2, centerY - lineSize
        / 2, picture);
    hTree(lineSize / 2, depthRecursion - 1, maxX, maxY, centerX + lineSize / 2, centerY - lineSize
        / 2, picture);
    hTree(lineSize / 2, depthRecursion - 1, maxX, maxY, centerX - lineSize / 2, centerY + lineSize
        / 2, picture);
  }


  public static ArrayList<Literal> solve(boolean[][] picture) throws Z3Exception {
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
    return builder.solve(constraints, width * height);
  }

}
