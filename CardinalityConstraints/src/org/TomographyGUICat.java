package org;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import com.microsoft.z3.Z3Exception;

public class TomographyGUICat extends Application {

  // Bildgrösse
  private static boolean[][] picture;

  private static ArrayList<Literal> result = new ArrayList<>();

  @Override
  public void start(Stage primaryStage) {

    Pane root = new Pane();

    // zeichnet
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

    primaryStage.setTitle("TomographyCat");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) throws Z3Exception {

    try {
      picture = DigitalTomographyCat.getCat(0.2);
      result = DigitalTomographyCat.solve(picture, new KnuthBailleux());
    } catch (Z3Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // zeichnet das Board
    launch(args);
  }
}
