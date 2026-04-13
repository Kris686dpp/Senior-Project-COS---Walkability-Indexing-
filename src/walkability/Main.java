package walkability;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
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

public class Main {

    public static void main(String[] args) {
        //launch(args);
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
            System.out.println("3 - Bellman-Ford Algorithm");
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
                    algorithm = "bellman-ford";
                    break;
                default:
                    algorithm = "dijkstra";
                    break;
            }
            double avgDistance = WalkabilityAnalyzer.computeAverageDistance(graph, algorithm);

            System.out.println("Average distance between all reachable nodes: "
                    + avgDistance + " meters");

            double radius = WalkabilityAnalyzer.computeRadius(graph.getAllNodes());

            System.out.println("The radius of the graph is: " + radius + " meters");

            //For temporary testing of making GeoJSON files
            GeoResult geoResult = new GeoResult(graph, "data");
            String geoJSON = geoResult.makeGeoJSON();
            FileWriter writer = new FileWriter("data/output.geojson");
            writer.write(geoJSON);
            writer.close();
            System.out.println("The GeoJSON file has been created...");

            //Exporting the map
            geoResult.exportMap();

            List<Node> roadNodes = new ArrayList<>();
            List<Node> amenityNodes = new ArrayList<>();

            for (Node node : graph.getAllNodes()) {
                if (node.getType() == NodeType.ROAD_NODE) roadNodes.add(node);
                else amenityNodes.add(node);
            }

            for (int i=0; i<10; i++) {
                Node testNode = roadNodes.get(i);
                Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, testNode);
                double score = WalkabilityAnalyzer.computeAmenityAcess(testNode, amenityNodes, distances);
                System.out.println("Amenity score for node " + testNode.getId() + ": " + score);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, 600,600);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
    }*/
}