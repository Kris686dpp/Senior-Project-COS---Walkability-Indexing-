package walkability;

import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

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
            System.out.println("3 - Jhonson's Algorithm");
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
}