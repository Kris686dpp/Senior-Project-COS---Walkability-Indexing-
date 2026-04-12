package walkability;

import java.io.FileWriter;
import java.util.List;

public class GeoResult {

    private Graph graph;
    private String output;

    public GeoResult(Graph graph, String output){
        this.graph = graph;
        this.output = output;
    }

    // Method for building .geojson files, result will be a string in geojson format
    public String makeGeoJSON(){
        List<Node> nodes = graph.getAllNodes();

        StringBuilder geoJOSN = new StringBuilder();
        geoJOSN.append("{\"type\":\"FeatureCollection\",\"features\":[");

        // Going through all the nodes
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            /* The "point" should look something like this:
                    { "type": "Feature",
                      "geometry": {"type": "Point", "coordinates": [-75.343, 39.984]},
                      "properties": {
                        "name": "Location A",
                        "category": "Store"
                      }
                    },
             */

            geoJOSN.append("{ \"type\": \"Feature\",");
            geoJOSN.append("\"geometry\": {\"type\": \"Point\", \"coordinates\": [").append(node.getLongitude()).append(",").append(node.getLatitude()).append("]},");
            geoJOSN.append("\"properties\": {");
            geoJOSN.append("\"id\": ").append(node.getId()).append(",");
            geoJOSN.append("\"type\": \"").append(node.getType()).append("\"");
            geoJOSN.append("}}");

            // If it is not the last node add a comma
            if (i < nodes.size() - 1) geoJOSN.append(",");
        }

        geoJOSN.append("]}");
        return geoJOSN.toString();
}

    // Exporting it as a map
    public void exportMap() throws Exception {
        String geojson = makeGeoJSON();

        java.nio.file.Files.writeString(
                java.nio.file.Path.of("data/map.geojson"), geojson
        );

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
              <link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>
              <script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>
              <style> body { margin: 0; } #map { width: 100%; height: 100vh; } </style>
            </head>
            <body>
            <div id='map'></div>
                <script>
                  var map = L.map('map');
                  fetch('map.geojson')
                          .then(response => response.json())
                          .then(data => {
                            var layer = L.geoJSON(data, {
                                pointToLayer: function(feature, latlng) {
                                    return L.circleMarker(latlng, {
                                        radius: 2,
                                        fillColor: "red",
                                        color: "#000",
                                        fillOpacity: 0.8,
                                        weight: 0.2
                                    });
                                }
                            }).addTo(map);   
                            map.fitBounds(layer.getBounds());
                          })
                  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
                </script>
            </html>
            """;

        java.nio.file.Files.writeString(
                java.nio.file.Path.of("data/map.html"), html
        );

        System.out.println("Map is exported");
    }
}
