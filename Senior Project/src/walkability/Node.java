package walkability;

public class Node {
    // The values of a node
    private long id;
    private double latitude;
    private double longitude;
    private NodeType type;

    // Node creation
    public Node(long id, double latitude, double longitude, NodeType type) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    //Node Methods
    public long getId() { return id; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public NodeType getType() { return type; }
}
