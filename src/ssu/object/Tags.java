package ssu.object;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import ssu.object.test.value.string.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public final class Tags {

    /**
     * File Path
     */
    public static final String ATOM_FILE_PATH = "resources/atoms.txt";
    public static final String PATIENT_FILE_PATH = "resources/patients.txt";
    public static final String RULE_FILE_PATH = "resources/rules.txt";
    public static final String RULE_CONFIGURE_FILE_PATH = "resources/rules_configure.txt";
    public static final String TESTITEM_FILE_PATH = "resources/test_items.txt";

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
    public static final String ATOM_STRING_VALUE_SPLITER = "!";

    public static final String RULE_SPLITER = ",";
    public static final String RULE_THEN_SPLITER = ":";
    public static final String RULE_ATOM_SPLITER = "!";
    public static final String RULE_PATIENT_SPLITER = "_";
    public static final String RULE_PATIENT_OPINION_SPLITER = "-";

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
    public static final String TEST_VALUE_TYPE_CELL = "CEL";
    public static final String TEST_VALUE_TYPE_CELL_DEAD = "Dead";
    public static final String TEST_VALUE_TYPE_CELL_OBSTRUCTION = "Obstruction";
    public static final String TEST_VALUE_TYPE_CELL_DISORDER = "Disorder";

    /**
     * 입력받은 type에 따라 관련 List를 리턴.
     * @param type
     * @return
     */
    public static final ArrayList<String> getTypeList(String type) {
        ArrayList<String> list = new ArrayList<String>();

        if (type.equals(Tags.TEST_VALUE_TYPE_HIGHLOW)) {                    // High, Low, Normal
            list.add(Tags.TEST_VALUE_TYPE_HIGH);
            list.add(Tags.TEST_VALUE_TYPE_LOW);
        } else if (type.equals(Tags.TEST_VALUE_TYPE_NORMAL)) {              // Normal
            list.add(Tags.TEST_VALUE_TYPE_NORMAL_VALUE);
        } else if (type.equals(Tags.TEST_VALUE_TYPE_POSNEG)) {             // Positive, Negative
            list.add(Tags.TEST_VALUE_TYPE_POS);
            list.add(Tags.TEST_VALUE_TYPE_NEG);
        } else if (type.equals(Tags.TEST_VALUE_TYPE_REACTIVE_NONREACTIVE)) {             // Active, Non Reactive
            list.add(Tags.TEST_VALUE_TYPE_REACTIVE);
            list.add(Tags.TEST_VALUE_TYPE_NONREATIVE);
        } else if (type.equals(Tags.TEST_VALUE_TYPE_FOUNDNOTFOUND)) {             // Found, Not found
            list.add(Tags.TEST_VALUE_TYPE_FOUND);
            list.add(Tags.TEST_VALUE_TYPE_NOT_FOUND);
        } else if (type.equals(Tags.TEST_VALUE_TYPE_BLOOD)) {            // Blood Type
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_A);
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_B);
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_O);
            list.add(Tags.TEST_VALUE_TYPE_BLOOD_AB);
        } else if (type.equals(Tags.TEST_VALUE_TYPE_RH)) {             // Rh Positive, Negative
            list.add(Tags.TEST_VALUE_TYPE_POS);
            list.add(Tags.TEST_VALUE_TYPE_NEG);
        } else if (type.equals(Tags.TEST_VALUE_TYPE_CELL)) {    // Cell type -> Dead, Disorder, Obstruction
            list.add(Tags.TEST_VALUE_TYPE_CELL_DEAD);
            list.add(Tags.TEST_VALUE_TYPE_CELL_DISORDER);
            list.add(Tags.TEST_VALUE_TYPE_CELL_OBSTRUCTION);
        } else {                                    // 없으면
            list.add(Tags.TEST_VALUE_TYPE_HIGH);
            list.add(Tags.TEST_VALUE_TYPE_LOW);
            list.add(Tags.TEST_VALUE_TYPE_NORMAL_VALUE);
            list.add(Tags.TEST_VALUE_TYPE_POS);
            list.add(Tags.TEST_VALUE_TYPE_NEG);
        }

        return list;
    }

    public static final String getParsedType(String exceptHighLowToken, String highLowToken) {
        String types = null;

        if (exceptHighLowToken.isEmpty() && highLowToken.isEmpty()) { // 문자값이 모두 존재하지 않는 경우.
            // 예비처리를 위한 HL, POSNEG를 추가해줌.
            types = Tags.TEST_VALUE_TYPE_POSNEG + "_" + Tags.TEST_VALUE_TYPE_HIGHLOW;
        } else if (!exceptHighLowToken.isEmpty()) {     // High, Low를 제외한 나머지 문자값이 존재하는 경우.
            if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_NEG) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_POS)) { // Positive, Negative
                types = Tags.TEST_VALUE_TYPE_POSNEG;
            } else if (exceptHighLowToken.contains("Norm")) {    // Norm
                types = Tags.TEST_VALUE_TYPE_NORMAL;
            } else if (exceptHighLowToken.contains("-")) {       // Range
                types = Tags.TEST_VALUE_TYPE_RANGE;
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_FOUND)
                    || exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_NOT_FOUND)) { // Found, Non found
                types = Tags.TEST_VALUE_TYPE_FOUNDNOTFOUND;
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_A) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_B) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_AB) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_O)) {     // Blood type
                types = Tags.TEST_VALUE_TYPE_BLOOD;
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_REACTIVE) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_NONREATIVE)) {
                types = Tags.TEST_VALUE_TYPE_REACTIVE_NONREACTIVE;
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_CELL_DEAD) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_CELL_DISORDER) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_CELL_OBSTRUCTION)) {   // Cell type
                types = Tags.TEST_VALUE_TYPE_CELL;
            } else {        // etc : 추후 타입이 추가될 수 있음.
                types = Tags.TEST_VALUE_TYPE_ETC;
            }
        } else if (!highLowToken.isEmpty()) {  //  // High, Low 문자값이 존재하는 경우.
            types = Tags.TEST_VALUE_TYPE_HIGHLOW;
        }

        return types;
    }

    public static StringValue getParsedStringValue(String exceptHighLowToken, String highLowToken) {
        // String Value 추가.
        if (!exceptHighLowToken.isEmpty()) {
            if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_NEG) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_POS)) { // Positive, Negative
                return new PosNeg(exceptHighLowToken);
            } else if (exceptHighLowToken.contains("Norm")) {    // Norm
                return new Normal(exceptHighLowToken);
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_FOUND)
                    || exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_NOT_FOUND)) { // Found, Non found
                return new Found_NotFound(exceptHighLowToken);
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_A) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_B) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_AB) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_BLOOD_O)) {     // Blood type
                return new BloodType(exceptHighLowToken);
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_REACTIVE) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_NONREATIVE)) { // Reactive, Non-reactive
                return new Reactive_NonReative(exceptHighLowToken);
            } else if (exceptHighLowToken.contains("-")) {       // Range
                return new Range(exceptHighLowToken);
            } else if (exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_CELL_DEAD) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_CELL_DISORDER) ||
                    exceptHighLowToken.contains(Tags.TEST_VALUE_TYPE_CELL_OBSTRUCTION)) {   // Cell type
                return new CellType(exceptHighLowToken);
            } else {        // etc : 추후 타입이 추가될 수 있음.
                return new Etc(exceptHighLowToken);
            }
        } else if (!highLowToken.isEmpty()) {
            return new HighLow(highLowToken);
        }

        return null;
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
