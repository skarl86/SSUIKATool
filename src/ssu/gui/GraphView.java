package ssu.gui;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import ssu.object.Tags;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import javax.swing.*;
import java.util.*;

/**
 * Created by JasonHong on 2015. 11. 14..
 */
public class GraphView extends JPanel {

    private mxGraphComponent graphComponent;
    private mxGraph graph;

    public GraphView() {
        this.graph = new mxGraph();
        Object parent = this.graph.getDefaultParent();
        try
        {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
                    80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        this.graphComponent = new mxGraphComponent(this.graph);
        this.graphComponent.getConnectionHandler().setEnabled(false);
    }

    /**
     *
     * @param rules
     */
    public void drawRules(ArrayList<Rule> rules) {
        for (Rule rule : rules) {
            drawRule(rule);
        }
        // Hierarchical layout 적용.
        mxHierarchicalLayout hierarchicalLayout = new mxHierarchicalLayout(this.graph, JLabel.WEST);
        hierarchicalLayout.execute(this.graph.getDefaultParent());
    }

    /**
     *
     * @param rule
     */
    private void drawRule(Rule rule) {
        Object parent = this.graph.getDefaultParent();

        int startAntecedentsWidth = 10;
        int startAntecedentsHeight = 10;
        int startConsequentsWidth = 10;
        int startConsequentsHeight = 10;

        Object[] allVertices = this.graph.getChildVertices(parent);

        this.graph.getModel().beginUpdate();
        try {
            ArrayList<Atom> antecedents = rule.getAntecedents();
            ArrayList<Atom> concequents = rule.getConsequents();

            // justification 그려줌.
            Object justfication = this.graph.insertVertex(parent, null, "J" + rule.getId(), startAntecedentsWidth, startAntecedentsHeight, 20, 20, Tags.GRAPH_JUSTIFICATION_STYLE);

            // antecedents 그려줌.
            for (Atom ant : antecedents) {
                Object obj = null;

                for (int i=0; i<allVertices.length; i++) {
                    if (this.graph.convertValueToString(allVertices[i]).equals(ant.getName())) {
                        obj = allVertices[i];
                        break;
                    }
                }

                if (obj != null) { // 이미 존재하는 노드.
                    this.graph.insertEdge(parent, null, "", obj, justfication);
                } else {
                    obj = this.graph.insertVertex(parent, null, ant.getName(), startAntecedentsWidth, startAntecedentsHeight, 80, 30, Tags.GRAPH_NODE_STYLE);
                    this.graph.insertEdge(parent, null, "", obj, justfication);
                    startAntecedentsHeight += 1;
                }

            }

            // consequent 그려줌.
            for (Atom con : concequents) {

                Object obj = null;

                for (int i=0; i<allVertices.length; i++) {
                    if (this.graph.convertValueToString(allVertices[i]).equals(con.getName())) {
                        obj = allVertices[i];
                        break;
                    }
                }

                if (obj != null) { // 이미 존재하는 노드.
                    this.graph.insertEdge(parent, null, "", justfication, obj);
                } else {
                    obj = this.graph.insertVertex(parent, null, con.getName(), startConsequentsWidth, startConsequentsHeight, 80, 30, Tags.GRAPH_NODE_STYLE);
                    this.graph.insertEdge(parent, null, "", justfication, obj);
                    startConsequentsHeight += 1;
                }
            }

        } finally{
            this.graph.getModel().endUpdate();
        }

    }


    public void clearGraph() {
        this.graph.removeCells(this.graph.getChildVertices(this.graph.getDefaultParent()));
    }

    /*
    Getter & Setter
     */

    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    public mxGraph getGraph() {
        return graph;
    }
}
