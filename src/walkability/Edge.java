package walkability;

public class Edge {
    /*Values of edge, and edge goes from one Node to another,
    weight is a stand-in for now it is just distance but allows us to later define it based on
    other criteria as well such as slope, sidewalk availability, etc.*/
    private Node from;
    private Node to;
    private double weight;

    // Edge creation
    public Edge(Node from, Node to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    //Node Methods
    public Node getFrom() { return from; }
    public Node getTo() { return to; }
    public double getDistance() { return weight; }
}
