package ssu.object.rule;

import ssu.object.Savable;
import ssu.object.Tags;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public abstract class Atom implements Savable {

    protected String name;
    protected int type;

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String printSavingFormat() {
        String line = getName() + Tags.ATOM_SPLITER + getType();

        return line;
    }

}
