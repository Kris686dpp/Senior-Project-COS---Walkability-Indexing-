package walkability;

import java.util.*;

public class WalkabilityAnalyzer {

    public static Result computeAverageDistance(Graph graph, String algorithm, List<Node> amenityNodes) throws Exception {

        List<Node> nodes = new ArrayList<>(graph.getAllNodes());
        int n = nodes.size();

        if (n == 0) throw new IllegalArgumentException("Graph is empty.");

        // For timing the algorithms
        long startTime = System.currentTimeMillis();

        // For calculating the average: dividing the total distance by the number of all reachable nodes
        double totalDistance = 0.0;
        int reachablePairs = 0;
        // For all the accessability scores for the nodes.
        Map<Node, Double> nodeScores = new HashMap<>();

        // Computing FLOYD-WARSHALL
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
        }

        // Computing BELLMAN-FORD
        else if (algorithm.equals("bellman-ford")) {
            for (Node source : nodes) {
                Map<Node, Double> distances = BellmanFord.computeShortestPath(graph, source);

                // Computing average distance
                for (Map.Entry<Node, Double> entry : distances.entrySet()) {
                    if (entry.getKey() != source && entry.getValue() != Double.POSITIVE_INFINITY) {
                        totalDistance += entry.getValue();
                        reachablePairs++;
                    }
                }

                // Computing the amenity score
                double score = computeAmenityAccess(amenityNodes, distances);
                nodeScores.put(source, score);
            }
        }

        // Default to DIJKSTRA
        else {
            for (Node source : nodes) {

                Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, source);

                // Computing average distance
                for (Map.Entry<Node, Double> entry : distances.entrySet()) {
                    if (entry.getKey() != source && entry.getValue() != Double.POSITIVE_INFINITY) {
                        totalDistance += entry.getValue();
                        reachablePairs++;
                    }
                }

                // Computing the amenity score
                double score = computeAmenityAccess(amenityNodes, distances);
                nodeScores.put(source, score);
            }
        }


        if (reachablePairs == 0) {
            throw new IllegalStateException("No reachable node pairs found in graph.");
        }

        // Result of the timer
        {
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println("Time taken: " + (String.format("%.2f", timeTaken / 1000.0) + "s"));
        }

        // Result
        double averageDistance = totalDistance / reachablePairs;
        return new Result(averageDistance, nodeScores);
    }

    public static double computeRadius(List <Node> nodes){
        double totalLat = 0.0;
        double totalLon = 0.0;
        for (Node node : nodes){
            totalLat += node.getLatitude();
            totalLon += node.getLongitude();
        }
        double centerLat = totalLat / nodes.size();
        double centerLon = totalLon / nodes.size();

        List<Double> dist = new ArrayList<>();
        for (Node node : nodes){
            dist.add(Utilities.haversine(centerLat, centerLon, node.getLatitude(), node.getLongitude()));
            }

        Collections.sort(dist);
        int index = (int) Math.ceil(0.95 * dist.size()) - 1;
        return dist.get(index);
    }

    // Computing how accessible amenities are to a given node
    public static double computeAmenityAccess(List<Node> amenityNodes, Map<Node, Double> distances){
        double score = 0.0;
        final double MAX_WALKING_DISTANCE = 1200.0;

        for (Node amenity : amenityNodes){
            double distance = distances.getOrDefault(amenity, Double.POSITIVE_INFINITY);
            if (distance <= MAX_WALKING_DISTANCE) {
                double amenityWeight = getAmenityWeight(amenity.getType());
                double accessScore = ((1 - distance / MAX_WALKING_DISTANCE)*10);
                score += amenityWeight * accessScore;
            }
        }
        return score;
    }

    // How important the different amenity types are
    private static double getAmenityWeight(NodeType type){
        switch (type) {
        case TRANSIT: return 0.4;
        case SHOP: return 0.5;
        case PARK: return 0.1;
        default:   return 0.0;
        }
    }

    public static class Result{
        public final double averageDistance;
        public final Map<Node, Double> accessabilityScore;
        public Result(double averageDistance, Map<Node, Double> accessabilityScore){
            this.accessabilityScore = accessabilityScore;
            this.averageDistance = averageDistance;
        }
    }

}

