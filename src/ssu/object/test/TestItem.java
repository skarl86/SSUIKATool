package ssu.object.test;

import ssu.object.Savable;
import ssu.object.Tags;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class TestItem implements Savable {

    private String name;
    private String description;

    public TestItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String printSavingFormat() {
        String line = getName() + Tags.TEST_ITEM_SPLITER + getDescription();

        return line;
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
}
