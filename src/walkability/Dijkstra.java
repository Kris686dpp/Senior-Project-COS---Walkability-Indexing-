package walkability;

import java.util.*;

public class Dijkstra {

    public static Map<Node, Double> computeShortestPaths(Graph graph, Node source) {
        Map<Node, Double> distances = new HashMap<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>();
        Set<Node> visited = new HashSet<>();

        for (Node node : graph.getAllNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }

        distances.put(source, 0.0);
        queue.add(new NodeDistance(source, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            Node currentNode = current.node;

            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);

            for (Edge edge : graph.getEdges(currentNode)) {
                Node neighbor = edge.getTo();
                double newDist = distances.get(currentNode) + edge.getDistance();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        return distances;
    }

    private static class NodeDistance implements Comparable<NodeDistance> {
        Node node;
        double distance;

        NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }
}