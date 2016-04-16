package org;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import com.microsoft.z3.Z3Exception;

public class QueensGUI extends Application {

  private static final int QUEENS = 3;
  private static final int WIDTH = 40;
  private static final int HEIGHT = 40;
  private static final int BOARD_SIZE = 40 * QUEENS;

  private static ArrayList<Literal> result = new ArrayList<>();

  @Override
  public void start(Stage primaryStage) {

    Pane root = new Pane();
    for (int x = 0, c = 0; x < BOARD_SIZE; x = x + WIDTH, c++) {
      for (int y = 0; y < BOARD_SIZE; y = y + HEIGHT) {
        if (c % 2 == 0) {

          // Filled rectangle
          Rectangle rect1 = new Rectangle(40, 40, 40, 40);
          rect1.getArcHeight();
          rect1.getArcWidth();
          rect1.setX(x);
          rect1.setY(y);
          rect1.fillProperty();



          root.getChildren().add(rect1);

        }
        // circle, falls SAT
        if (result != null) {
          int xCoord = x / WIDTH;
          int yCoord = y / HEIGHT;
          Literal lit = null;
          for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getIndex() == xCoord + QUEENS * yCoord) {
              lit = result.get(i);
              break;
            }
          }
          if (lit.isPositive()) {
            Circle circle = new Circle();
            circle.setRadius(10.0f);
            circle.setCenterX(20.0f + x);
            circle.setCenterY(20.0f + y);
            circle.setFill(Color.RED);

            root.getChildren().add(circle);
          }
        }

        c++;
      }
    }



    Scene scene = new Scene(root, BOARD_SIZE, BOARD_SIZE);

    primaryStage.setTitle("Queens-Problem");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) throws Z3Exception {
    // Encoding enc = new NiklasseBDDs();
    // ArrayList<Literal> lits = new ArrayList<>();
    // lits.add(new Literal(0, true));
    // lits.add(new Literal(1, true));
    // lits.add(new Literal(2, true));
    // ArrayList<Literal> lits2 = new ArrayList<>();
    // lits2.add(new Literal(0, true));
    // lits2.add(new Literal(1, true));
    // Context ctx = new Context();
    // Solver solver = ctx.mkSolver("QF_LIA");
    // enc.encode(lits, 1, 0, solver, ctx);
    // enc.encode(lits2, 1, 1, solver, ctx);
    // System.out.println(solver);
    // return;

    try {
      result = DamenProblem.solve(QUEENS);
    } catch (Z3Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // zeichnet das Board
    launch(args);
  }
}
