package walkability;

import java.util.*;

public class FloydWarshall {

    public static class Result {
        public final double[][] distances;
        public final Map<Node, Integer> indexMap;

        public Result(double[][] dist, Map<Node, Integer> indexMap) {
            this.distances = dist;
            this.indexMap = indexMap;
        }
    }

    public static Result computeAllPairs(Graph graph) {

        List<Node> nodeList = new ArrayList<>(graph.getAllNodes()); // An array list is easier to work with
        int n = nodeList.size(); // Total number of nodes

        /*A HashMap that will store the indexes of the nodes. OSM Nodes already have IDs, but they are too long, unordered
        non-sequential and can not be used in a matrix */
        Map<Node, Integer> indexMap = new HashMap<>();
        // Matrix of distances
        double[][] dist = new double[n][n];

        // Going through every node and assining them an integer index
        for (int i = 0; i < n; i++) {
            indexMap.put(nodeList.get(i), i);
            Arrays.fill(dist[i], Double.POSITIVE_INFINITY);
            dist[i][i] = 0.0;
        }

        // Filling in direct edge distances:
        /*We loop through every node in the graph and its edges and adds the distances to the dist[][] matrix.*/
        for (Node node : graph.getAllNodes()){
            // Converting the current node to its index
            int i = indexMap.get(node);
            for (Edge edge : graph.getEdges(node)){
                // Getting the index of the node that that edge connects to
                int j = indexMap.get(edge.getTo());
                // Since there are two pairs of edges for any given node we only really need to take one of them
                if (edge.getDistance() < dist[i][j]){
                    dist[i][j] = edge.getDistance();
                }
            }
        }


        // Floyd-Warshall Algorithm
        for (int k=0; k < n; k++){
            for (int i = 0; i < n; i++){
                if (dist[i][k] == Double.POSITIVE_INFINITY) continue;
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return new Result(dist, indexMap);
    }
}
