package ssu.object;

import ssu.object.rule.Atom;
import ssu.object.rule.AtomClass;
import ssu.object.rule.AtomPredicate;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beggar3004 on 15. 11. 8..
 */
public class AtomManager {

    private HashMap<String, Atom> allAtoms;

    // 혹시 추후 멀티스레딩을 위한.
    private volatile static AtomManager uniqueInstance;

    private AtomManager() {
        this.allAtoms = new HashMap<String, Atom>();
    }

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static AtomManager getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (AtomManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AtomManager();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }

        return uniqueInstance;
    }

    /**
     * Exp: 지정한 파일을 읽어와 atom list를 만듬.
     */
    public void loadAtomList() {

        try {
            InputStream is = this.getClass().getResourceAsStream(Tags.ATOM_FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                String[] tokens = line.split(Tags.ATOM_SPLITER);
                /*
                 * Atom 형식
                 * name,type,StringValue1!StringValue2
                 */
                String name = tokens[0];
                // token의 마지막이 atom의 타입.
                int atomType = Integer.parseInt(tokens[1]);
                String stringtValues = tokens[2];

                // Atom에 대한 type을 정의(Class/Predicate)
                Atom newAtom = null;
                switch (atomType) {
                    case Tags.ATOM_TYPE_CLASS:
                        newAtom = new AtomClass(name);
                        break;
                    case Tags.ATOM_TYPE_PREDICATE:
                        // WARN : 현재 Predicate는 존재하지 않음.
                        newAtom = new AtomPredicate(name);
                        break;
                }

                //Atom이 가질 수 있는 StringValue
                if (stringtValues.contains(Tags.ATOM_STRING_VALUE_SPLITER)) { // 여러개의 StringValue를 가질 경우.
                    String[] values = stringtValues.split(Tags.ATOM_STRING_VALUE_SPLITER);

                    for (String value : values) {
                        newAtom.addStringValue(value);
                    }
                } else {                                                      // 하나의 StringValue를 가질 경우.
                    newAtom.addStringValue(stringtValues);
                }

                this.allAtoms.put(newAtom.getName(), newAtom);

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
     * Exp: 지정한 파일에 atom을 저장함.
     * @return
     */
    public void saveAtomList() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.getClass().getResource(Tags.ATOM_FILE_PATH).getPath()));
            for (Map.Entry<String, Atom> entry : this.allAtoms.entrySet()) {
                Atom atom = entry.getValue();
                bw.write(atom.printSavingFormat() + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exp : Atom을 추가.
     * @param atom
     */
    public void addAtom(Atom atom) {
        this.allAtoms.put(atom.getName(), atom);
    }

    /**
     * Exp : Atom을 제거.
     * @param atom
     */
    public void removeAtom(Atom atom) {
        // 제거 체크.
        this.allAtoms.remove(atom.getName(), atom);
    }

    /**
     *
     * @param value
     * @param type
     * @return
     */
    public Atom getAtomOrCreate(String value, int type) {
        Atom atom = null;

        if (this.allAtoms.containsKey(value)) {
            atom = this.allAtoms.get(value);
        } else {
            // 새로운 Atom을 생성.
            // 1. Value가 있는 경우.
            if (value.contains(Tags.ATOM_VALUE_SPLITER)) {
                atom = new AtomClass(value.substring(0, value.indexOf(Tags.ATOM_VALUE_SPLITER)));
                addAtom(atom);
            }

            switch (type) {
                case Tags.ATOM_TYPE_CLASS:
                    atom = new AtomClass(value);
                    break;
                case Tags.ATOM_TYPE_PREDICATE:
                    atom = new AtomPredicate(value);
                    break;
            }

            // 새로 생성한 Atom을 추가.
            addAtom(atom);
        }

        return atom;
    }

    /*
    Getter & Setter
     */

    public HashMap<String, Atom> getAllAtoms() {
        return allAtoms;
    }

}
