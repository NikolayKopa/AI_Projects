import java.io.*;
import java.util.*;

public class Main {
    static class Node {
        int id;
        List<Edge> edges;
        double distance; 
        Node previous;

        public Node(int id) {
            this.id = id;
            this.edges = new ArrayList<>();
            this.distance = Double.POSITIVE_INFINITY;
            this.previous = null;
        }
    }

    static class Edge {
        Node destination;
        double weight; 

        public Edge(Node destination, double weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <input_file>");
            return;
        }

        String inputFilePath = args[0];
        Map<Integer, Node> nodes = new HashMap<>(); // Create a map to store nodes

        try (Scanner scanner = new Scanner(new File(inputFilePath))) {
            int vertices = scanner.nextInt(); // Read number of vertices from input
            int edges = scanner.nextInt(); // Read number of edges from input
            scanner.nextLine(); // Consume the newline

            // Add nodes
            for (int i = 0; i < vertices; i++) {
                nodes.put(i, new Node(i)); // Create and add nodes to the map
            }

            // Add edges
            for (int i = 0; i < edges; i++) {
                int sourceId = scanner.nextInt(); // Read source node ID from input
                int destId = scanner.nextInt(); // Read destination node ID from input
                double weight = scanner.nextDouble(); // Read edge weight from input
                scanner.nextLine(); // Consume the newline

                Node source = nodes.get(sourceId); // Get source node from the map
                Node dest = nodes.get(destId); // Get destination node from the map

                source.edges.add(new Edge(dest, weight)); // Add edge to the source node
            }

            Scanner userInputScanner = new Scanner(System.in);
            System.out.print("Enter source vertex: ");
            int sourceId = userInputScanner.nextInt();
            System.out.print("Enter destination vertex: ");
            int destId = userInputScanner.nextInt();

            dijkstra(nodes, sourceId, destId); // Perform Dijkstra's algorithm
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + inputFilePath);
        }
    }

    public static void dijkstra(Map<Integer, Node> nodes, int sourceId, int destId) {
        Node source = nodes.get(sourceId); // Get the source node
        source.distance = 0.0;

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));
        queue.add(source); // Add the source node to the priority queue

        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get the node with the minimum distance from the queue

            for (Edge edge : current.edges) {
                Node neighbor = edge.destination; // Get the neighbor node
                double newDist = current.distance + edge.weight; // Calculate the new distance to the neighbor

                if (newDist < neighbor.distance) {
                    queue.remove(neighbor); // Remove neighbor from the queue
                    neighbor.distance = newDist;
                    neighbor.previous = current;
                    queue.add(neighbor); // Add neighbor back to the queue
                }
            }
        }

        Node destination = nodes.get(destId); // Get the destination node
        if (destination.distance == Double.POSITIVE_INFINITY) {
            System.out.println("No path exists.");
        } else {
            System.out.println("Shortest path cost: " + destination.distance); // Print the shortest path cost
            System.out.print("Shortest path: ");
            printPath(destination); // Print the shortest path
        }
    }
    
    public static void printPath(Node node) {
        if (node == null) return; // Base case for recursion
        printPath(node.previous); // Recur for the previous node
        System.out.print(node.id + " "); // Print the node ID
    }
}
