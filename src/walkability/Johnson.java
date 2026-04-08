package walkability;

import java.util.*;

public class Johnson {

    // The result class that the computeAllPairs is going to return, as in my implementation of Floyd-Warshall
    public static class Result {
        public final double[][] dist;
        public final Map<Node, Integer> indexMap;

        public Result(double[][] dist, Map<Node, Integer> indexMap) {
            this.dist = dist;
            this.indexMap = indexMap;
        }
    }

    public static Result computeAllPairs(Graph graph) {

        List<Node> nodeList = new ArrayList<>(graph.getAllNodes());
        int n = nodeList.size();

        //THIS IMPLEMENTATION OF JOHNSON'S ALGORITHM EXCLUDES THE BELLMAN-FORD STEP WHICH

        // Assinging an index to all nodes
        Map<Node, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indexMap.put(nodeList.get(i), i);
        }

        // Crating a distance matrix
        double[][] distMatrix = new double[n][n];
        for (double[] row : distMatrix) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }

        // Run Dijkstra for every node and fill the matrix
        for (int s = 0; s < n; s++) {
            Node source = nodeList.get(s);
            Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, source);

            for (int t = 0; t < n; t++) {
                Node target = nodeList.get(t);
                distMatrix[s][t] = distances.getOrDefault(target, Double.POSITIVE_INFINITY);
            }
        }

        return new Result(distMatrix, indexMap);
    }
}