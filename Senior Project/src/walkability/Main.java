package walkability;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // Create graph
        Graph graph = new Graph();

        // Create nodes
        Node a = new Node(1, 42.0, 23.0, NodeType.INTERSECTION);
        Node b = new Node(2, 42.001, 23.001, NodeType.INTERSECTION);
        Node c = new Node(3, 42.002, 23.002, NodeType.PARK);
        Node d = new Node(4, 42.003, 23.003, NodeType.SHOP);

        // Add nodes to graph
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);

        // Add edges (distances in meters)
        graph.addEdge(a, b, 100);
        graph.addEdge(b, c, 150);
        graph.addEdge(a, d, 300);
        graph.addEdge(c, d, 100);

        // Run Dijkstra from node A
        System.out.println("Shortest walking distances from Node A:");
        Map<Node, Double> distances = Dijkstra.computeShortestPaths(graph, a);

        for (Node node : distances.keySet()) {
            System.out.println("To node " + node.getId() + " (" + node.getType() + ") = "
                    + distances.get(node) + " meters");
        }
    }
}
