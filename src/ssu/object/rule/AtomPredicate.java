package ssu.object.rule;

import ssu.object.Tags;

/**
 * Created by JasonHong on 2015. 11. 11..
 */
public class AtomPredicate extends Atom {

    private String param1;
    private String param2;

    public AtomPredicate(String name) {
        // name | type
        this.name = name;
        this.type = Tags.ATOM_TYPE_PREDICATE;
    }

}
