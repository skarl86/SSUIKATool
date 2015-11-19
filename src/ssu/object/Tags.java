package ssu.object;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import java.awt.*;
import java.util.ArrayList;

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
    public static final String ATOM_VALUE_SPLITER = "_";

    public static final String RULE_SPLITER = ",";
    public static final String RULE_THEN_SPLITER = "-";
    public static final String RULE_ATOM_SPLITER = "!";

    public static final String PATIENT_SPLITER = "_";
    public static final String PATIENT_TEST_RESULT_SPLITER = "!";
    public static final String PATIENT_TEST_VALUE_SPLITER = ",";
    public static final String PATIENT_OPINION_SPLITER = "!";
    public static final String PATIENT_OPINION_RULELIST_SPLITER = ":";
    public static final String PATIENT_OPINION_RULE_SPLITER = ",";

    public static final String TEST_ITEM_SPLITER = ",";
    public static final String TEST_ITEM_TYPE_SPLITER = "_";

    public static final String TEST_VALUE_SPLITER = ":";

    /**
     * Patient Value
     */
    public static final String PATIENT_GENDER_MALE = "Male";
    public static final String PATIENT_GENDER_FEMALE = "Female";
    public static final String PATIENT_GENDER_UNKNOWN = "Unknown";

    /**
     * Test Value Type
     */
    public static final String TEST_VALUE_TYPE_NORMAL = "NO";
    public static final String TEST_VALUE_TYPE_NORMAL_VALUE = "Norm";
    public static final String TEST_VALUE_TYPE_NUMERIC = "N";
    public static final String TEST_VALUE_TYPE_HIGHLOW = "HL";
    public static final String TEST_VALUE_TYPE_HIGH = "H";
    public static final String TEST_VALUE_TYPE_LOW = "L";
    public static final String TEST_VALUE_TYPE_POSNEG = "PN";
    public static final String TEST_VALUE_TYPE_POS = "Positive";
    public static final String TEST_VALUE_TYPE_NEG = "Negative";
    public static final String TEST_VALUE_TYPE_REACTIVE_NONREACTIVE = "RN";
    public static final String TEST_VALUE_TYPE_REACTIVE = "Reactive";
    public static final String TEST_VALUE_TYPE_NONREATIVE = "Non-Reactive";
    public static final String TEST_VALUE_TYPE_MTHFR_C677T = "MTHFRC677T";
    public static final String TEST_VALUE_TYPE_MTHFR_C677T_CT_HETEROZYGOTE = "CT heterozygote";
    public static final String TEST_VALUE_TYPE_FOUNDNOTFOUND = "FN";
    public static final String TEST_VALUE_TYPE_FOUND = "Found";
    public static final String TEST_VALUE_TYPE_NOT_FOUND = "Not found";
    public static final String TEST_VALUE_TYPE_AMOUNT = "AM";
    public static final String TEST_VALUE_TYPE_A_FEW = "A few";
    public static final String TEST_VALUE_TYPE_BLOOD = "ABO";
    public static final String TEST_VALUE_TYPE_BLOOD_A = "A";
    public static final String TEST_VALUE_TYPE_BLOOD_O = "O";
    public static final String TEST_VALUE_TYPE_BLOOD_AB = "AB";
    public static final String TEST_VALUE_TYPE_BLOOD_B = "B";
    public static final String TEST_VALUE_TYPE_RH = "RH";
    public static final String TEST_VALUE_TYPE_RANGE = "RA";
    public static final String TEST_VALUE_TYPE_ETC = "ETC";

    /**
     * 입력받은 type에 따라 관련 List를 리턴.
     * @param type
     * @return
     */
    public static final ArrayList<String> getTypeList(String type) {
        ArrayList<String> list = new ArrayList<String>();

        if (type.equals("HL")) {                    // High, Low, Normal
            list.add(Tags.TEST_VALUE_TYPE_HIGH);
            list.add(Tags.TEST_VALUE_TYPE_LOW);
            list.add(Tags.TEST_VALUE_TYPE_NORMAL);
        } else if (type.equals("PN")) {             // Positive, Negative
            list.add(Tags.TEST_VALUE_TYPE_POS);
            list.add(Tags.TEST_VALUE_TYPE_NEG);
        } else if (type.equals("RN")) {             // Active, Non Reactive
            list.add(Tags.TEST_VALUE_TYPE_REACTIVE);
            list.add(Tags.TEST_VALUE_TYPE_NONREATIVE);
        } else if (type.equals("FN")) {             // Found, Not found
            list.add(Tags.TEST_VALUE_TYPE_FOUND);
            list.add(Tags.TEST_VALUE_TYPE_NOT_FOUND);
        } else if (type.equals("ABO")) {            // Blood Type
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_A);
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_B);
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_O);
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_AB);
        } else if (type.equals("RH")) {             // Rh Positive, Negative
            list.add(Tags.TEST_VALUE_TYPE_POS);
            list.add(Tags.TEST_VALUE_TYPE_NEG);
        } else {                                    // 없으면
            list.add(Tags.TEST_VALUE_TYPE_HIGH);
            list.add(Tags.TEST_VALUE_TYPE_LOW);
            list.add(Tags.TEST_VALUE_TYPE_NORMAL);
            list.add(Tags.TEST_VALUE_TYPE_POS);
            list.add(Tags.TEST_VALUE_TYPE_NEG);
        }

        return list;
    }

    /**
     * jGraphX
     */
    public static final int GRAPH_NODE_WIDTH = 100;
    public static final int GRAPH_NODE_ATOM_WIDTH = 60;
    public static final int GRAPH_NODE_VALUE_WIDTH = 40;
    public static final int GRAPH_NODE_HEIGHT = 30;
    public static final int GRAPH_JUSTIFICATION_WIDTH = 20;
    public static final int GRAPH_JUSTIFICATION_HEIGHT = 20;

    public static final String GRAPH_NODE_ANTECEDENT_STYLE = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE + ";" + mxConstants.STYLE_FILLCOLOR + "=" + mxUtils.getHexColorString(new Color(207,252,252));
    public static final String GRAPH_NODE_CONSEQUENT_STYLE = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE + ";" + mxConstants.STYLE_FILLCOLOR + "=" + mxUtils.getHexColorString(new Color(242,177,188));
    public static final String GRAPH_JUSTIFICATION_STYLE = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_TRIANGLE + ";" + mxConstants.STYLE_FILLCOLOR + "=" + mxUtils.getHexColorString(new Color(242,250,17));


    private Tags() {}
}
