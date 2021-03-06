package org;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import com.microsoft.z3.Z3Exception;

/**
 * GUI für den H-Tree. Nicht benutzt für den Benchmark
 * 
 * @author Tietjen
 * 
 */
public class TomographyGUI extends Application {

  // Bildgrösse
  private static boolean[][] picture;
  // n
  private static final int TOMOGRAPHY = 4;

  private static ArrayList<Literal> result = new ArrayList<>();



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

  @Override
  public void start(Stage primaryStage) {


    Pane root = new Pane();

    // zeichnet die einzelnen H-Linien
    Line line1 = new Line(0, 0, 0, picture[0].length * 10);
    root.getChildren().add(line1);

    Line line2 = new Line(picture.length * 10, 0, picture.length * 10, picture[0].length * 10);
    root.getChildren().add(line2);

    Line line3 = new Line(picture.length * 20, 0, picture.length * 20, picture[0].length * 10);
    root.getChildren().add(line3);



    for (int x = 0; x < picture.length; x++) {
      for (int y = 0; y < picture[x].length; y++) {
        if (picture[x][y]) {
          Rectangle rec1 = new Rectangle(x * 10, y * 10, 10, 10);
          root.getChildren().add(rec1);
        }
      }
    }

    if (result != null) {
      for (int i = 0; i < result.size(); i++) {
        Literal lit = result.get(i);
        if (lit.isPositive()) {
          int x = lit.getIndex() % picture.length;
          int y = lit.getIndex() / picture.length;
          // färbt die Felder entsprechend ein
          Rectangle rec1 = new Rectangle(x * 10 + 10 * picture.length, y * 10, 10, 10);
          root.getChildren().add(rec1);
        }
      }
    }



    Scene scene = new Scene(root, picture.length * 20, picture[0].length * 10);

    primaryStage.setTitle("Tomography");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) throws Z3Exception {

    try {
      // Grösse
      picture = new boolean[10][8];
      // hTree(lineSize, depthRecursion, maxX, maxY, centerX, centerY, picture)
      DigitalTomography.hTree(5, TOMOGRAPHY, 10, 8, 5, 4, picture);
      result = DigitalTomography.solve(picture, new KnuthBailleux());
    } catch (Z3Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // zeichnet das Board
    launch(args);
  }
}
