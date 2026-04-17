package walkability;

import java.util.*;

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

            List<Node> roadNodes = new ArrayList<>();
            List<Node> amenityNodes = new ArrayList<>();

            for (Node node : graph.getAllNodes()) {
                if (node.getType() == NodeType.ROAD_NODE) roadNodes.add(node);
                else amenityNodes.add(node);
            }

            WalkabilityAnalyzer.Result result = WalkabilityAnalyzer.computeAverageDistance(graph, algorithm, amenityNodes);
            double radius = WalkabilityAnalyzer.computeRadius(graph.getAllNodes());
            double totalAccessibility = 0.0;
            for (double score : result.accessabilityScores.values()) {
                totalAccessibility += score;
            }
            double averageAccessibility = totalAccessibility / result.accessabilityScores.size();


            System.out.println("Average distance between all reachable nodes: "
                    + result.averageDistance + " meters");
            System.out.println("The radius of the graph is: " + radius + " meters");
            System.out.println("The average accessibility score: " + String.format("%.2f", averageAccessibility));

            // Making the GeoJSON file
            GeoResult geoResult = new GeoResult(graph, "data", result.accessabilityScores);
            String geoJSON = geoResult.makeGeoJSON();

            //Exporting the map
            geoResult.exportMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}