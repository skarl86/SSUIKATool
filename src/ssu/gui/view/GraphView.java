package ssu.gui.view;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxEdgeStyle;
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
        this.graph = new mxGraph() {
            // Ports are not used as terminals for edges, they are
            // only used to compute the graphical connection point
            public boolean isPort(Object cell)
            {
                mxGeometry geo = getCellGeometry(cell);

                return (geo != null) && geo.isRelative();
            }

            // Implements a tooltip that shows the actual
            // source and target of an edge
            public String getToolTipForCell(Object cell)
            {
                if (model.isEdge(cell))
                {
                    return convertValueToString(model.getTerminal(cell, true)) + " -> " +
                            convertValueToString(model.getTerminal(cell, false));
                }

                return super.getToolTipForCell(cell);
            }

            // Removes the folding icon and disables any folding
            public boolean isCellFoldable(Object cell, boolean collapse)
            {
                return false;
            }
        };

        // Sets the default edge style
//        Map<String, Object> style = this.graph.getStylesheet().getDefaultEdgeStyle();
//        style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);

        this.graphComponent = new mxGraphComponent(this.graph);
        this.graphComponent.getConnectionHandler().setEnabled(false);
    }

    /**
     *
     * @param rules
     */
    public void drawRules(ArrayList<Rule> rules) {
        clearGraph();

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
            Object justfication = this.graph.insertVertex(parent, null, "J" + rule.getId(), startAntecedentsWidth, startAntecedentsHeight,
                                                            Tags.GRAPH_JUSTIFICATION_WIDTH,
                                                            Tags.GRAPH_JUSTIFICATION_HEIGHT,
                                                            Tags.GRAPH_JUSTIFICATION_STYLE);

            // antecedents 그려줌.
            for (Atom ant : antecedents) {
                Object antCell = null;

                for (int i=0; i<allVertices.length; i++) {
                    if (this.graph.convertValueToString(allVertices[i]).equals(ant.getName())) {
                        antCell = allVertices[i];
                        break;
                    }
                }

                if (antCell != null) { // 이미 존재하는 노드.
                    this.graph.insertEdge(parent, null, "", antCell, justfication);
                } else {
                    antCell = this.graph.insertVertex(parent, null, ant.getName(), startAntecedentsWidth, startAntecedentsHeight,
                                                        Tags.GRAPH_NODE_WIDTH,
                                                        Tags.GRAPH_NODE_HEIGHT,
                                                        Tags.GRAPH_NODE_ANTECEDENT_STYLE);
                    this.graph.insertEdge(parent, null, "", antCell, justfication);
                    startAntecedentsHeight += 1;
                }
            }

            // consequent 그려줌.
            for (Atom con : concequents) {

                Object conCell = null;

                for (int i=0; i<allVertices.length; i++) {
                    if (this.graph.convertValueToString(allVertices[i]).equals(con.getName())) {
                        conCell = allVertices[i];
                        break;
                    }
                }

                if (conCell != null) { // 이미 존재하는 노드.
                    this.graph.insertEdge(parent, null, "", justfication, conCell);
                } else {
                    conCell = this.graph.insertVertex(parent, null, con.getName(), startConsequentsWidth, startConsequentsHeight,
                            Tags.GRAPH_NODE_WIDTH,
                            Tags.GRAPH_NODE_HEIGHT,
                            Tags.GRAPH_NODE_CONSEQUENT_STYLE);

                    this.graph.insertEdge(parent, null, "", justfication, conCell);
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
