package walkability;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        /*try {
            String filePath = "data/city.osm";

            OSMParser parser = new OSMParser();
            Graph graph = parser.parse(filePath);

            if (graph.getAllNodes().isEmpty()) {
                System.out.println("Test failed: graph has no nodes.");
                return;
            }

            int edgeCount = 0;
            for (Node node : graph.getAllNodes()) {
                edgeCount += graph.getEdges(node).size();
            }

            if (edgeCount == 0) {
                System.out.println("Test failed: graph has no edges.");
                return;
            }

            Node start = null;
            for (Node node : graph.getAllNodes()) {
                if (!graph.getEdges(node).isEmpty()) {
                    start = node;
                    break;
                }
            }

            if (start == null) {
                System.out.println("Test failed: no connected start node found.");
                return;
            }

            Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, start);

            int reachableCount = 0;
            for (double distance : distances.values()) {
                if (distance != Double.POSITIVE_INFINITY) {
                    reachableCount++;
                }
            }

            if (reachableCount <= 1) {
                System.out.println("Test failed: Dijkstra did not reach other nodes.");
                return;
            }

            System.out.println("OSM Dijkstra test passed.");
            System.out.println("Total nodes: " + graph.getAllNodes().size());
            System.out.println("Total edges: " + edgeCount);
            System.out.println("Start node: " + start.getId());
            System.out.println("Reachable nodes: " + reachableCount);

        } catch (Exception e) {
            System.out.println("Test failed due to exception:");
            e.printStackTrace();
        }*/

        try {
            String filePath = "data/city.osm";

            System.out.println("Loading OSM file...");

            OSMParser parser = new OSMParser();
            Graph graph = parser.parse(filePath);

            System.out.println("Graph successfully created.");
            System.out.println("Total nodes: " + graph.getAllNodes().size());

            int edgeCount = 0;
            for (Node node : graph.getAllNodes()) {
                edgeCount += graph.getEdges(node).size();
            }
            System.out.println("Total edges: " + edgeCount);

            System.out.println("\nSample nodes:");
            int printed = 0;
            for (Node node : graph.getAllNodes()) {
                System.out.println(
                        "Node ID: " + node.getId() +
                                " Lat: " + node.getLatitude() +
                                " Lon: " + node.getLongitude() +
                                " Degree: " + graph.getEdges(node).size()
                );
                printed++;
                if (printed >= 5) break;
            }

            System.out.println("\nFinding a connected start node...");

            Node start = null;
            for (Node node : graph.getAllNodes()) {
                if (!graph.getEdges(node).isEmpty()) {
                    start = node;
                    break;
                }
            }

            if (start == null) {
                System.out.println("No connected node found in graph.");
                return;
            }

            System.out.println("Start node: " + start.getId());

            System.out.println("\nTesting Dijkstra...");
            Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, start);

            int count = 0;
            for (Map.Entry<Node, Double> entry : distances.entrySet()) {
                if (entry.getValue() != Double.POSITIVE_INFINITY) {
                    System.out.println(
                            "Distance from start to node " +
                                    entry.getKey().getId() +
                                    " = " + entry.getValue() + " meters"
                    );
                    count++;
                }

                if (count >= 10) break;
            }

            System.out.println("\nTest completed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}