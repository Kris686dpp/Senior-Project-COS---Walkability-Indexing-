package walkability;

import java.util.*;

public class Graph {

    private Map<Long, Node> nodes = new HashMap<>();
    private Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node from, Node to, double weight) {
        Edge edge = new Edge(from, to, weight);
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.putIfAbsent(to, new ArrayList<>());
        adjacencyList.get(from).add(edge);

        Edge reverseEdge = new Edge(to, from, weight);
        adjacencyList.get(to).add(reverseEdge);
    }

    public Node getNode(long id) {
        return nodes.get(id);
    }

    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    public List<Node> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }

    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for(List<Edge> edges : adjacencyList.values()){
            allEdges.addAll(edges);
        }
        return allEdges;
    }

}

