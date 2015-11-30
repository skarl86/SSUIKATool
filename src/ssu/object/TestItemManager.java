package ssu.object;

import ssu.object.test.TestCategory;
import ssu.object.test.TestComponent;
import ssu.object.test.TestItem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class TestItemManager {

    private HashMap<String, TestComponent> allTestItems;
    private final int TEST_ITEM_TOKENS_LENGTH = 5;

    // 혹시 추후 멀티스레딩을 위한.
    private volatile static TestItemManager uniqueInstance;

    private TestItemManager() {
        this.allTestItems = new HashMap<String, TestComponent>();
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
                String[] tokens = line.split(Tags.TEST_ITEM_SPLITER, TEST_ITEM_TOKENS_LENGTH);
                /*
                 * TestItem 형식
                 * code, subcode, name, description, value-type
                 */
                String code = tokens[0];
                String subcode = tokens[1];
                String name = tokens[2];
                String description = tokens[3];
                String types = tokens[4];

                if (this.allTestItems.containsKey(code)) { // 이미 Category가 있는 경우.
                    TestCategory testCategory = (TestCategory) this.allTestItems.get(code);
                    TestItem newTestItem = createTestItem(subcode, name, description, types);
                    testCategory.add(newTestItem);
                } else {
                    TestCategory newTestCategory = new TestCategory(code);
                    TestItem newTestItem = createTestItem(subcode, name, description, types);
                    newTestCategory.add(newTestItem);

                    this.allTestItems.put(code, newTestCategory);
                }

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
     * @param code
     * @param name
     * @param description
     * @param types
     * @return
     */
    public TestItem createTestItem(String code, String name, String description, String types) {
        TestItem newTestItem = new TestItem(code, name, description);

        if (types.contains(Tags.TEST_ITEM_TYPE_SPLITER)) { // Type이 여러개
            String[] typeToken =  types.split(Tags.TEST_ITEM_TYPE_SPLITER);
            for (int i=0; i<typeToken.length; i++) {
                newTestItem.addType(typeToken[i]);
            }
        } else {    // Type이 하나.
            newTestItem.addType(types);
        }

        return newTestItem;
    }

    /**
     *
     */
    public void saveTestItemList() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getClass().getResource(Tags.TESTITEM_FILE_PATH).getPath()));
            for (Map.Entry<String, TestComponent> entry : this.allTestItems.entrySet()) {
                TestComponent testComponent = entry.getValue();
                bw.write(testComponent.printSavingFormat() + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printAllTestItems() {
        String line = "";

        for (Map.Entry<String, TestComponent> entry : this.allTestItems.entrySet()) {
            TestComponent testComponent = entry.getValue();
            line += testComponent.printSavingFormat() + "\n";
        }

        return line;
    }

    public int countAllTestItems() {
        int count = 0;

        for (Map.Entry<String, TestComponent> entry : this.allTestItems.entrySet()) {
            TestComponent testComponent = entry.getValue();
            count += testComponent.count();
        }

        return count;
    }

    public void addTestItem(TestItem testItem) {
        this.allTestItems.put(testItem.getName(), testItem);
    }

    /*
     * Getter & Setter
     */
    public HashMap<String, TestComponent> getAllTestItems() {
        return allTestItems;
    }
}
