package ssu.gui.controller.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by NCri on 2015. 11. 30..
 *
 * Atom Table의 Entity 클래스.
 */
public class AtomRow {
    private final StringProperty atom;
    private final StringProperty value;

    public StringProperty atomProperty(){ return atom; }
    public StringProperty valueProperty(){ return value; }

    public AtomRow(String atom, String value){
        this.atom = new SimpleStringProperty(atom);
        this.value = new SimpleStringProperty(value);
    }
    public void setAtom(String atom){ this.atom.set(atom);}
    public String getAtom() { return atom.get(); }
    public void setValue(String value){ this.value.set(value);}
    public String getValue() { return value.get(); }
}
