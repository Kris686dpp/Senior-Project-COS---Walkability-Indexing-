package walkability;

public class Edge {
    private Node from;
    private Node to;
    private double distance;

    public Edge(Node from, Node to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Node getFrom() { return from; }
    public Node getTo() { return to; }
    public double getDistance() { return distance; }
}
