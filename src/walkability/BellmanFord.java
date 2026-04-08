package walkability;
import java.util.*;

public class BellmanFord {

    public static class Result{

    }

    public static Map<Node, Double> computeShortestPath(Graph graph, Node source)
    {
        // Getting a list of nodes + getting n = the amount of nodes
        List<Node> nodeList = new ArrayList<>(graph.getAllNodes());
        int n = nodeList.size();
        // Matrix of distances
        Map<Node, Double>dist = new HashMap<>();

        // Step 1: Initilize graph
        // Initilize the distance to all vertices to infinity
        for (Node node : graph.getAllNodes()) {
            dist.put(node, Double.POSITIVE_INFINITY);
        }

        // Have the distance from the source to itself be zero
        dist.put(source, 0.0);

        // Step 2: relax edges repeatedly
        for (int i = 1; i < n; i++){

        }


        return dist;
    }
}
