package ssu.object.rule;

import ssu.object.Savable;
import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 12. 12..
 */
public class ValuedAtom implements Savable {

    private Atom atom;
    private String value = "";

    public ValuedAtom(Atom atom, String value) {
        this.atom = atom;
        this.value = value;
    }

    public ValuedAtom(Atom atom) {
        this.atom = atom;
    }

    @Override
    public String printSavingFormat() {
        if (value.isEmpty())
            return this.atom.getName();
        else
            return this.atom.getName() + Tags.ATOM_VALUE_SPLITER + this.value;
    }

    /*
    Getter & Setter
     */

    public Atom getAtom() {
        return atom;
    }

    public String getValue() {
        return value;
    }
}
