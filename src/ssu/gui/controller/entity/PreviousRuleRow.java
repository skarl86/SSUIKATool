package ssu.gui.controller.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by NCri on 2015. 11. 30..
 * Complete(:=Previous) Rule Table의 Entity 클래스.
 */
public class PreviousRuleRow {
    private final StringProperty previousRule;

    public StringProperty previousRuleProperty(){ return previousRule; }

    public PreviousRuleRow(String atom){
        this.previousRule = new SimpleStringProperty(atom);
    }
    public void setPreviousRule(String previousRule){ this.previousRule.set(previousRule);}
    public String getPreviousRule() { return previousRule.get(); }
}
