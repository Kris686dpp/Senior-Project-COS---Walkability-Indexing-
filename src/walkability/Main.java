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
            double avgDistance = WalkabilityAnalyzer.computeAverageDistance(graph, algorithm);

            System.out.println("Average distance between all reachable nodes: "
                    + avgDistance + " meters");

            double radius = WalkabilityAnalyzer.computeRadius(graph.getAllNodes());

            System.out.println("The radius of the graph is: " + radius + " meters");




            List<Node> roadNodes = new ArrayList<>();
            List<Node> amenityNodes = new ArrayList<>();

            for (Node node : graph.getAllNodes()) {
                if (node.getType() == NodeType.ROAD_NODE) roadNodes.add(node);
                else amenityNodes.add(node);
            }

            Map<Node, Double> nodeScores = new HashMap<>();
            double amenityScoreAverage = 0.0;
            for (Node node : roadNodes) {
                Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, node);
                double score =  WalkabilityAnalyzer.computeAmenityAccess(node, amenityNodes, distances);
                amenityScoreAverage += score;
                nodeScores.put(node, score);
            }

            // Making the GeoJSON file
            GeoResult geoResult = new GeoResult(graph, "data", nodeScores);
            String geoJSON = geoResult.makeGeoJSON();

            //Exporting the map
            geoResult.exportMap();

            amenityScoreAverage /= roadNodes.size();
            System.out.println("Accessabiltiy score for the graph is: "+amenityScoreAverage);


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