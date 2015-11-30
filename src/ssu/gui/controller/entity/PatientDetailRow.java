package ssu.gui.controller.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by NCri on 2015. 11. 30..
 * 환자 상세 정보 Table의 Entity 클래스.
 */
public class PatientDetailRow {
    private final StringProperty testName;
    private final StringProperty testValue;
    private final StringProperty textValue;

    public PatientDetailRow(String testName, String testValue, String textValue){
        this.testName = new SimpleStringProperty(testName);
        this.testValue = new SimpleStringProperty(testValue);
        this.textValue = new SimpleStringProperty(textValue);
    }

    public void setTestName(String testName){ this.testName.set(testName); }
    public String getTestName(){return this.testName.get();}

    public void setTestValue(String testValue){ this.testValue.set(testValue); }
    public String getTestValue(){return this.testValue.get();}

    public void setTextValue(String textValue){ this.textValue.set(textValue); }
    public String getTextValue(){return this.textValue.get();}
}
