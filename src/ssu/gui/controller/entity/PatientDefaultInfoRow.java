package ssu.gui.controller.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by NCri on 2015. 12. 7..
 */
public class PatientDefaultInfoRow {
    private final StringProperty subject;
    private final StringProperty textValue;

    public PatientDefaultInfoRow(String subject, String textValue){
        this.subject = new SimpleStringProperty(subject);
        this.textValue = new SimpleStringProperty(textValue);
    }

    public void setSubject(String sub){ subject.set(sub); }

    public String getSubject(){ return subject.get(); }

    public void setTextValue(String textVal){ textValue.set(textVal); }

    public String getTextValue(){ return textValue.get(); }

    public StringProperty subjectProperty() {
        return this.subject;
    }
    public StringProperty textValueProperty() {
        return this.textValue;
    }
}
