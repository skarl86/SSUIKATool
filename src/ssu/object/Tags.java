package ssu.object;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public final class Tags {

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
    public static final String PATIENT_TEST_RESULT_SPLITER = "-";
    public static final String PATIENT_TEST_VALUE_SPLITER = ",";
    public static final String PATIENT_OPINION_SPLITER = "-";
    public static final String PATIENT_OPINION_RULELIST_SPLITER = ":";
    public static final String PATIENT_OPINION_RULE_SPLITER = ",";

    public static final String PATIENT_GENDER_MALE = "M";
    public static final String PATIENT_GENDER_FEMALE = "F";

    public static final String TEST_ITEM_SPLITER = ",";

    public static final String TEST_VALUE_SPLITER = ":";

    public static final String TEST_VALUE_TYPE_NUMERIC = "N";
    public static final String TEST_VALUE_TYPE_HIGHLOW = "HL";
    public static final String TEST_VALUE_TYPE_HIGH = "High";
    public static final String TEST_VALUE_TYPE_LOW = "LOW";
    public static final String TEST_VALUE_TYPE_POSNEG = "PN";
    public static final String TEST_VALUE_TYPE_POS = "Positive";
    public static final String TEST_VALUE_TYPE_NEG = "Negative";

    private Tags() {}
}
