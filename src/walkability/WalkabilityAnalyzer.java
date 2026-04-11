package walkability;

import java.util.*;

public class WalkabilityAnalyzer {

    public static double computeAverageDistance(Graph graph, String algorithm) throws Exception {

        List<Node> nodes = new ArrayList<>(graph.getAllNodes());
        int n = nodes.size();

        if (n == 0) throw new IllegalArgumentException("Graph is empty.");

        // For timing the algorithms
        long startTime = System.currentTimeMillis();

        // For calculating the average: dividing the total distance by the number of all reachable nodes
        double totalDistance = 0.0;
        int reachablePairs = 0;

        // Computing average distance for FLOYD-WARSHALL

        if (algorithm.equals("floyd-warshall")) {
            FloydWarshall.Result result = FloydWarshall.computeAllPairs(graph);
            double[][] dist = result.dist;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && dist[i][j] != Double.POSITIVE_INFINITY) {
                        totalDistance += dist[i][j];
                        reachablePairs++;
                    }
                }
            }

        // Computing average distance for BELLMAN-FORD

        } else if (algorithm.equals("bellman-ford")) {
            for (Node source : nodes) {
                Map<Node, Double> distances = BellmanFord.computeShortestPath(graph, source);
                for (Map.Entry<Node, Double> entry : distances.entrySet()) {
                    if (entry.getKey() != source && entry.getValue() != Double.POSITIVE_INFINITY) {
                        totalDistance += entry.getValue();
                        reachablePairs++;
                    }
                }
            }
        } else {
            // Default to DIJKSTRA
            for (Node source : nodes) {
                Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, source);
                for (Map.Entry<Node, Double> entry : distances.entrySet()) {
                    if (entry.getKey() != source && entry.getValue() != Double.POSITIVE_INFINITY) {
                        totalDistance += entry.getValue();
                        reachablePairs++;
                    }
                }
            }
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.println("Time taken: " + (String.format("%.2f", timeTaken / 1000.0) + "s"));

        if (reachablePairs == 0) {
            throw new IllegalStateException("No reachable node pairs found in graph.");
        }

        return totalDistance / reachablePairs;
    } }