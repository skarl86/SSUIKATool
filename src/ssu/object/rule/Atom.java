package ssu.object.rule;

import ssu.object.Savable;
import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public abstract class Atom implements Savable {

    protected String name;
    protected int type;
    protected ArrayList<String> stringValues = new ArrayList<>();

    /**
     * 입력받은 StringValue 타입을 추가.
     * @param value
     * @return
     */
    public boolean addStringValue(String value) {
        return this.stringValues.add(value);
    }

    public boolean addAllStringValues(ArrayList<String> values) {
        return this.stringValues.addAll(values);
    }

    /**
     * 입력받은 StringValue 타입을 제거.
     * @param value
     * @return
     */
    public boolean removeStringValue(String value) {
        return this.stringValues.remove(value);
    }

    @Override
    public String printSavingFormat() {
        String line = getName() + Tags.ATOM_SPLITER + getType() + Tags.ATOM_SPLITER;

        for (int i=0; i<this.stringValues.size(); i++) {
            String value = this.stringValues.get(i);
            line += value;

            if (i < this.stringValues.size() - 1) {
                line += Tags.ATOM_STRING_VALUE_SPLITER;
            }

        }

        return line;
    }

    /*
    Getter & Setter
     */

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public ArrayList<String> getStringValues() {
        return stringValues;
    }
}
