package walkability;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class GeoResult {

    private Graph graph;
    private String output;
    private Map<Node, Double> nodeScores;

    public GeoResult(Graph graph, String output){
        this.graph = graph;
        this.output = output;
    }

    public GeoResult(Graph graph, String output, Map<Node, Double> nodeScores){
        this.graph = graph;
        this.output = output;
        this.nodeScores = nodeScores;
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
            geoJOSN.append("\"type\": \"").append(node.getType()).append("\",");
            geoJOSN.append("\"score\": ").append(nodeScores.getOrDefault(node, 0.0));
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
        //
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
                function getColor(score) {
                                if (score <= 0)   return '#d73027';
                                if (score < 1)  return '#d74427';
                                if (score < 2)  return '#d74d27';
                                if (score < 3)  return '#d75627';
                                if (score < 5)  return '#d76227';
                                if (score < 7)  return '#d76d27';
                                if (score < 11)   return '#d77c27';
                                if (score < 13)  return '#d79427';
                                if (score < 17)  return '#d7a227';
                                if (score < 25)  return '#d7b127';
                                if (score < 50)  return '#d7c227';
                                if (score < 75)  return '#d7d427';
                                if (score < 100)  return '#d7bd27';
                                if (score < 125)  return '#c8d727';
                                if (score < 150)  return '#b7d727';
                                if (score < 175)  return '#a2d727';
                                if (score < 200)  return '#94d727';
                                if (score < 200)  return '#88d727';
                                if (score < 250)  return '#68d727';
                                return '#1a9850';
                              }
                  var map = L.map('map');
                  fetch('map.geojson')
                          .then(response => response.json())
                          .then(data => {
                            var layer = L.geoJSON(data, {
                                pointToLayer: function(feature, latlng) {
                                    var isAmenity = feature.properties.type !== 'ROAD_NODE';
                                    return L.circleMarker(latlng, {
                                        radius: 2,
                                        fillColor: isAmenity ? "#2a27d7" : getColor(feature.properties.score),
                                        color: "#000",
                                        fillOpacity: 0.8,
                                        weight: 0.2
                                    });
                                },
                                onEachFeature: function(feature, layer) {
                                    layer.bindPopup(
                                        'ID: ' + feature.properties.id + '<br>' +
                                        'Type: ' + feature.properties.type + '<br>' +
                                        'Score: ' + feature.properties.score.toFixed(2)
                                    );
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
