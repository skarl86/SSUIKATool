package ssu.object;

import ssu.object.rule.Atom;
import ssu.object.rule.AtomClass;
import ssu.object.rule.AtomPredicate;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by NCri on 2015. 12. 7..
 */
public class UserManager {

    private static final String USER_FILE_PATH = "resources/users.txt";
    private static final String USER_TOKEN = ";";

    // 혹시 추후 멀티스레딩을 위한.
    private volatile static UserManager uniqueInstance;

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static UserManager getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (AtomManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new UserManager();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }

        return uniqueInstance;
    }
    public void saveCurrentUser(String user){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getClass().getResource(USER_FILE_PATH).getPath()));
            bw.write(user);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isExistUser(String user){
        boolean result = false;

        try {
            InputStream is = this.getClass().getResourceAsStream(USER_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(USER_TOKEN);
                for(String us : tokens){
                    result = us.equals(user);
                }
                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    public String loadCurrentUser() {
//        ArrayList<String> users = new ArrayList<String>();

        String currentUser = null;

        try {
            InputStream is = this.getClass().getResourceAsStream(USER_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                currentUser = line;
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentUser;
    }
}
