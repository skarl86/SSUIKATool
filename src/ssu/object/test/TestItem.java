package ssu.object.test;

import ssu.object.Savable;
import ssu.object.Tags;

import java.util.ArrayList;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class TestItem implements Savable {

    private String name;
    private String description;
    private ArrayList<String> types;


    public TestItem(String name, String description) {
        this.name = name;
        this.description = description;
        this.types = new ArrayList<String>();
    }

    @Override
    public String printSavingFormat() {
        String line = getName() + Tags.TEST_ITEM_SPLITER + getDescription() + Tags.TEST_ITEM_SPLITER;
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
}
