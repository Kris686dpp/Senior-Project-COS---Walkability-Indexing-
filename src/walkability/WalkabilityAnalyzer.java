package walkability;

import java.util.*;

public class WalkabilityAnalyzer {

    public static double computeAverageDistance(Graph graph, String algorithm) throws Exception {

        List<Node> nodes = new ArrayList<>(graph.getAllNodes());
        int n = nodes.size();

        if (n == 0) throw new IllegalArgumentException("Graph is empty.");

        double totalDistance = 0.0;
        int reachablePairs = 0;

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

        } else if (algorithm.equals("Johnson")) {
            Johnson.Result result = Johnson.computeAllPairs(graph);
            double[][] dist = result.dist;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && dist[i][j] != Double.POSITIVE_INFINITY) {
                        totalDistance += dist[i][j];
                        reachablePairs++;
                    }
                }
            }

        } else {
            // Default to Dijkstra
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

        if (reachablePairs == 0) {
            throw new IllegalStateException("No reachable node pairs found in graph.");
        }

        return totalDistance / reachablePairs;
    } }