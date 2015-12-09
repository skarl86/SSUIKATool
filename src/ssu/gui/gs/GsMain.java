package ssu.gui.gs;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import static org.graphstream.algorithm.Toolkit.*;

/**
 * Created by JasonHong on 2015. 12. 9..
 */
public class GsMain {
    public static void main(String[] args) {
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        /*
        Graph graph = new SingleGraph("Tuto 2");

        String path = "input/graph/tuto2.dgs";
        graph.display(false);
        graph.addAttribute("ui.antialias");
        try {
            FileSource source = FileSourceFactory.sourceFor(path);
            source.addSink(graph);
            source.begin(path);
            while(source.nextEvents()) { Thread.sleep(1000); };
            source.end();
        } catch(Exception e) { e.printStackTrace(); }
        */

        Graph graph = new SingleGraph("Tutorial 1");

        Node a = graph.addNode("A");
        a.setAttribute("ui.label", "A");
        a.setAttribute("value", "High");
        Node b = graph.addNode("B");
        b.setAttribute("ui.label", "B");
        b.setAttribute("value", "Low");
        Node c = graph.addNode("C");
        c.setAttribute("ui.label", "C");
        c.setAttribute("value", "Pos");
        Node d = graph.addNode("D");
        d.setAttribute("ui.label", "D");
        d.setAttribute("value", "Neg");

        graph.addEdge("AB", "A", "B", true);
        graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A", true);
        graph.addEdge("AD", "A", "D", true);

        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
//        graph.setAttribute("ui.stylesheet", "edge { fill-color: grey; }");

        for (Node node : graph) {
            node.setAttribute("ui.label", node.getAttribute("value").toString());
            System.out.println(node.getId() + " : " + node.getAttribute("value").toString());
            System.out.println(node.getDegree() + ", " + node.getInDegree() + ", " + node.getOutDegree());
        }

        for (Edge edge : graph.getEachEdge()) {
            System.out.println(edge.getId());
        }

        // Traveling the graph
        Node node = randomNode(graph);

        for (Edge edge : node.getEachEdge()) {
            System.out.println(edge.getOpposite(node).getId() + ":" + edge.getId());
        }

        graph.setAttribute("ui.stylesheet", "url(input/graph/test.css)");

        graph.display();

    }
}
