package ssu.gui.controller.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by NCri on 2015. 12. 7..
 */
public class PatientOpinionRow {

    private final StringProperty opinionID;
    private final StringProperty opinion;

    public StringProperty opinionIDProperty() { return opinionID; }
    public StringProperty opinionProperty() { return opinion; }

    public PatientOpinionRow(String opinionID, String opinion){
        this.opinionID = new SimpleStringProperty(opinionID);
        this.opinion = new SimpleStringProperty(opinion);
    }

    public String getOpinionID() {
        return opinionID.get();
    }

    public String getOpinion() {
        return opinion.get();
    }

    public void setOpinionID(String opinionID) {
        this.opinionID.set(opinionID);
    }

    public void setOpinion(String opinion) {
        this.opinion.set(opinion);
    }

}
