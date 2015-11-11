package ssu.object.rule;

import ssu.object.Tags;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class AtomClass extends Atom {

    private String param;

    public AtomClass(String name) {
        // name | type
        this.name = name;
        this.type = Tags.ATOM_TYPE_CLASS;
    }

}
