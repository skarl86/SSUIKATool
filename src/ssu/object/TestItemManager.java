package ssu.object;

import ssu.object.test.TestItem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class TestItemManager {

    private HashMap<String, TestItem> allTestItems;

    // 혹시 추후 멀티스레딩을 위한.
    private volatile static TestItemManager uniqueInstance;

    private TestItemManager() {
        this.allTestItems = new HashMap<String, TestItem>();
    }

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static TestItemManager getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (TestItemManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TestItemManager();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }

        return uniqueInstance;
    }

    /**
     *
     */
    public void loadTestItemList() {
        try {
            InputStream is = this.getClass().getResourceAsStream(Tags.TESTITEM_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(Tags.TEST_ITEM_SPLITER);
                /*
                 * TestItem 형식
                 * name,description
                 */
                String name = tokens[0];
                String description = tokens[1];
                String types = tokens[2];

                TestItem newTestItem = new TestItem(name, description);
                if (types.contains(Tags.TEST_ITEM_TYPE_SPLITER)) { // Type이 여러개
                    String[] typeToken =  types.split(Tags.TEST_ITEM_TYPE_SPLITER);
                    for (int i=0; i<typeToken.length; i++) {
                        newTestItem.addType(typeToken[i]);
                    }
                } else {    // Type이 하나.
                    newTestItem.addType(types);
                }

                allTestItems.put(name, newTestItem);

                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void saveTestItemList() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getClass().getResource(Tags.TESTITEM_FILE_PATH).getPath()));
            for (Map.Entry<String, TestItem> entry : this.allTestItems.entrySet()) {
                TestItem testItem = entry.getValue();
                bw.write(testItem.printSavingFormat() + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTestItem(TestItem testItem) {
        this.allTestItems.put(testItem.getName(), testItem);
    }

    /*
     * Getter & Setter
     */
    public HashMap<String, TestItem> getAllTestItems() {
        return allTestItems;
    }
}
