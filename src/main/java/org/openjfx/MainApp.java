package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.HashMap;

class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Point))
            return false;
        Point p = (Point) o;
        return p.x == this.x && p.y == this.y;
    }
    @Override
    public int hashCode() {
     return x * 31 + y;
    }
}

public class MainApp extends Application {

    private final static int WINDOW_WIDTH = 800;
    private final static int WINDOW_HEIGHT = 600;

    private final static double r = 20; // the inner radius from hexagon center to outer corner
    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    private final static double TILE_HEIGHT = 2 * r;
    private final static double TILE_WIDTH = 2 * n;

    private HashMap<Point, Polygon> tileHashMap = new HashMap<Point, Polygon>();
    public static void main(String[] args) {
        launch(args);
    } 

    public void start(Stage primaryStage) {
        
        AnchorPane tileMap = new AnchorPane();
        Scene content = new Scene(tileMap, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(content);

        int minHexPerRow = 4;
        int maxHexPerRow = 7;
        int totalRows = (maxHexPerRow - minHexPerRow) * 2 + 1;
        int xStartOffset = 40; // offsets the entire field to the right
        int yStartOffset = 40; // offsets the entire fiels downwards
        
        int hexInRow = minHexPerRow;
        int increment = 1;
        for (int x = 0; x < totalRows; x++) {
            for (int y = 0; y < hexInRow; y++) {
                double xCoord = y * TILE_WIDTH  + xStartOffset + (maxHexPerRow - hexInRow)*n;
                double yCoord = x * TILE_HEIGHT * 0.75 + yStartOffset;
                Polygon tile = new Tile(xCoord, yCoord, x, y);
                tileMap.getChildren().add(tile);
                tileHashMap.put(new Point(x, y), tile);
            }
            
            if (hexInRow == maxHexPerRow) increment = -1;
            hexInRow += increment;
        }            
        primaryStage.show();
    }

    private class Tile extends Polygon {
        Tile(double x, double y, int row, int col) {
            // creates the polygon using the corner coordinates
            getPoints().addAll(
                    x, y,
                    x, y + r,
                    x + n, y + r * 1.5,
                    x + TILE_WIDTH, y + r,
                    x + TILE_WIDTH, y,
                    x + n, y - r * 0.5
            );

            // set up the visuals and a click listener for the tile
            setFill(Color.ANTIQUEWHITE);
            setStrokeWidth(1);
            setStroke(Color.BLACK);
            setOnMouseClicked(e -> {
                System.out.println("Clicked: (row, col) = (" + row + ", " + col + ")");
                (tileHashMap.get(new Point(row, col)))
                    .setFill(Color.RED);
             } );
        }
    }
}
