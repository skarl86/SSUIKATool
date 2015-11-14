package ssu.object;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import java.awt.*;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public final class Tags {

    /**
     * File Path
     */
    public static final String ATOM_FILE_PATH = "input/atoms.txt";
    public static final String PATIENT_FILE_PATH = "input/patients.txt";
    public static final String RULE_FILE_PATH = "input/rules.txt";
    public static final String RULE_CONFIGURE_FILE_PATH = "input/rules_configure.txt";
    public static final String TESTITEM_FILE_PATH = "input/test_items.txt";

    /**
     * Atom의 타입.
     */
    public static final int ATOM_TYPE_CLASS = 0;
    public static final int ATOM_TYPE_PREDICATE = 1;

    /**
     * Spliter 타입.
     */
    public static final String ATOM_SPLITER = ",";

    public static final String RULE_SPLITER = ",";
    public static final String RULE_THEN_SPLITER = "-";
    public static final String RULE_ATOM_SPLITER = "_";

    public static final String PATIENT_SPLITER = "_";
    public static final String PATIENT_TEST_RESULT_SPLITER = "!";
    public static final String PATIENT_TEST_VALUE_SPLITER = ",";
    public static final String PATIENT_OPINION_SPLITER = "-";
    public static final String PATIENT_OPINION_RULELIST_SPLITER = ":";
    public static final String PATIENT_OPINION_RULE_SPLITER = ",";

    public static final String TEST_ITEM_SPLITER = ",";

    public static final String TEST_VALUE_SPLITER = ":";

    /**
     * Patient Value
     */
    public static final String PATIENT_GENDER_MALE = "M";
    public static final String PATIENT_GENDER_FEMALE = "F";
    public static final String PATIENT_GENDER_UNKNOWN = "U";

    /**
     * Test Value
     */
    public static final String TEST_VALUE_TYPE_NORMAL = "NO";
    public static final String TEST_VALUE_TYPE_NORMAL_VALUE = "Normal";
    public static final String TEST_VALUE_TYPE_NUMERIC = "N";
    public static final String TEST_VALUE_TYPE_HIGHLOW = "HL";
    public static final String TEST_VALUE_TYPE_HIGH = "High";
    public static final String TEST_VALUE_TYPE_LOW = "LOW";
    public static final String TEST_VALUE_TYPE_POSNEG = "PN";
    public static final String TEST_VALUE_TYPE_POS = "Positive";
    public static final String TEST_VALUE_TYPE_NEG = "Negative";
    public static final String TEST_VALUE_TYPE_REACTIVE_NONREACTIVE = "RN";
    public static final String TEST_VALUE_TYPE_REACTEVE = "Reactive";
    public static final String TEST_VALUE_TYPE_NONREATIVE = "Non Reactive";
    public static final String TEST_VALUE_TYPE_MTHFR_C677T = "MTHFRC677T";
    public static final String TEST_VALUE_TYPE_MTHFR_C677T_CT_HETEROZYGOTE = "CT heterozygote";

    /**
     * Test Item 이름.
     */


    /**
     * jGraphX
     */
    public static final String GRAPH_NODE_STYLE = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE;
    public static final String GRAPH_NODE_COLOR = mxConstants.STYLE_FILLCOLOR + "=" + mxUtils.getHexColorString(new Color(242,177,188));
    public static final String GRAPH_JUSTIFICATION_STYLE = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_TRIANGLE;
    public static final String GRAPH_JUSTIFICATION_COLOR =  mxConstants.STYLE_FILLCOLOR + "=" + mxUtils.getHexColorString(new Color(242,250,17));


    private Tags() {}
}
