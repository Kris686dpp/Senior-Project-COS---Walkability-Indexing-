package walkability;

import java.util.Map;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
        System.out.println("Average Distance Test");

        try {
            String filePath = "data/city.osm";

            OSMParser parser = new OSMParser();
            Graph graph = parser.parse(filePath);

            System.out.println("Total nodes: " + graph.getAllNodes().size());

            // Let the user choose the algorithm
            Scanner scanner = new Scanner(System.in);
            System.out.println("Which algorithm would you like to use?");
            System.out.println("1 - Dijkstra");
            System.out.println("2 - Floyd-Warshall");
            System.out.println("3 - Johnson's Algorithm");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            String algorithm;

            switch(choice) {
                case 1:
                    algorithm = "dijkstra";
                    break;
                case 2:
                    algorithm = "floyd-warshall";
                    break;
                case 3:
                    algorithm = "johnson";
                    break;
                default:
                    algorithm = "dijkstra";
                    break;
            }
            double avgDistance = WalkabilityAnalyzer.computeAverageDistance(graph, algorithm);

            System.out.println("Average distance between all reachable nodes: "
                    + avgDistance + " meters");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, 600,600);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
    }
}