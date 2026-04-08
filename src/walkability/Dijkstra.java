package walkability;

import java.util.*;

public class Dijkstra {

    public static Map<Node, Double> computeShortestPaths(Graph graph, Node source) {
        Map<Node, Double> dist = new HashMap<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>();
        Set<Node> visited = new HashSet<>();

        for (Node node : graph.getAllNodes()) {
            dist.put(node, Double.POSITIVE_INFINITY);
        }

        dist.put(source, 0.0);
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
                double newDist = dist.get(currentNode) + edge.getDistance();

                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        return dist;
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