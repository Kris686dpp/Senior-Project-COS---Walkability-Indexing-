package walkability;

import java.util.*;

public class Graph {

    private Map<Long, Node> nodes = new HashMap<>();
    private Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node from, Node to, double distance) {
        Edge edge = new Edge(from, to, distance);
        adjacencyList.get(from).add(edge);

        Edge reverseEdge = new Edge(to, from, distance);
        adjacencyList.get(to).add(reverseEdge);
    }

    public Node getNode(long id) {
        return nodes.get(id);
    }

    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    public Collection<Node> getAllNodes() {
        return nodes.values();
    }
}

