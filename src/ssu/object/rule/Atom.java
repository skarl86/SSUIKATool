package ssu.object.rule;

import ssu.object.Tags;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public abstract class Atom {

    protected String name;
    protected int type;

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public String printSavingFormat() {
        String line = getName() + Tags.ATOM_SPLITER + getType();

        return line;
    }

}
