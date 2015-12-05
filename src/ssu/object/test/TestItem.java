package ssu.object.test;

import ssu.object.HasStringType;
import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class TestItem extends TestComponent{

    private String code;
    private String name;
    private String description;
    private ArrayList<String> types = new ArrayList<String>();


    public TestItem(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @Override
    public String printSavingFormat() {
        String line = getCode() + Tags.TEST_ITEM_SPLITER + getName() + Tags.TEST_ITEM_SPLITER + getDescription() + Tags.TEST_ITEM_SPLITER;
        for (int i=0; i<this.types.size(); i++) {
            line += this.types.get(i);

            if (i < this.types.size() - 1) {
                line += Tags.TEST_ITEM_TYPE_SPLITER;
            }
        }

        return line;
    }

    public boolean addType(String type) {
        return this.types.add(type);
    }

    @Override
    public String print() {
        return printSavingFormat();
    }

    @Override
    public int count() {
        return 1;
    }

    @Override
    public boolean contains(String name) {
        return getName().equals(name);
    }

    /*
     * Getter & Setter
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
