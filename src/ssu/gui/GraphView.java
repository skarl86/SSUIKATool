package ssu.gui;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import org.w3c.dom.Document;
import ssu.object.rule.Atom;
import ssu.object.rule.Rule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by JasonHong on 2015. 11. 14..
 */
public class GraphView extends  JPanel {

    private mxGraphComponent graphComponent;
    private mxGraph graph;

    public GraphView() {
        this.graph = new mxGraph();
        this.graphComponent = new mxGraphComponent(this.graph);
        this.graphComponent.getConnectionHandler().setEnabled(false);
    }

    public void drawGraph(ArrayList<Rule> rules) {
        Object parent = this.graph.getDefaultParent();

        int startAntecedentsWidth = 10;
        int startAntecedentsHeight = 10;
        int startConsequentsWidth = 10;
        int startConsequentsHeight = 10;
        int startJustificationWidth = 200;
        int startJustificationHeight = 50;

        Object[] allVertices = this.graph.getChildVertices(parent);
        this.graph.getModel().beginUpdate();

        try {
            ArrayList<Atom> antecedents = newRule.getAntecedents();
            ArrayList<Rule.Concequent> concequents = newRule.getConcequents();

            // justification 그려줌.
            Object justfication = graph.insertVertex(parent, null, "J" + newRule.getId(), startAntecedentsWidth, startAntecedentsHeight, 20, 20, styleJustficationShape +styleConsFont+ styleJustficationShape + styleJustficationColor);
            // Added by NK.
            justificationList.add(justfication);
            // antecedents 그려줌.
            for (Rule.Antecedent ant : antecedents) {
                Object obj = null;

                for (int i=0; i<allVertices.length; i++) {
                    if (graph.convertValueToString(allVertices[i]).equals(ant.getName())) {
                        obj = allVertices[i];
                        break;
                    }
                }

                if (obj != null) { // 이미 존재하는 노드.
                    graph.insertEdge(parent, null, "", obj, justfication);
                } else {
                    obj = graph.insertVertex(parent, null, ant.getName(), startAntecedentsWidth, startAntecedentsHeight, 80, 30, styleShape + styleConsFont+styleAppendColor);
                    graph.insertEdge(parent, null, "", obj, justfication);
                    startAntecedentsHeight += 1;
                }

                ant.setGraphObj(obj);
            }
            ArrayList<Object> a = null;
            //graph.foldCells(true, true,new Object[]{vAB, vCD});
            // consequent 그려줌.
            for (Rule.Concequent con : concequents) {

                Object obj = null;

                for (int i=0; i<allVertices.length; i++) {
                    if (graph.convertValueToString(allVertices[i]).equals(con.getName())) {
                        obj = allVertices[i];
                        // a.add(obj);
                        // Added by NK.
                        concequentList.add(obj);
                        break;
                    }
                }
                //graph.foldCells(true, true,a.toArray());
                if (obj != null) { // 이미 존재하는 노드.
                    graph.insertEdge(parent, null, "", justfication, obj);
                } else {
                    obj = graph.insertVertex(parent, null, con.getName(), startConsequentsWidth, startConsequentsHeight, 80, 30, styleConsShape + styleConsFont + styleConsColor);
                    graph.insertEdge(parent, null, "", justfication, obj);
                    startConsequentsHeight += 1;
                }

                con.setGraphObj(obj);
            }

            newRule.setjGraphObj(justfication);

        } finally{
            graph.getModel().endUpdate();
        }
    }

    protected void clearGraph() {
        this.graph.removeCells(this.graph.getChildVertices(this.graph.getDefaultParent()));
    }

    /*
    Getter & Setter
     */

    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graph View");
        frame.getContentPane().add(new GraphView().getGraphComponent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setResizable(false);

        frame.setVisible(true);
    }

}
