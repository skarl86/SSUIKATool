package ssu.gui.controller.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by NCri on 2015. 11. 30..
 * 환자 Table의 Entity 클래스.
 */
public class PatientRow {
    private final StringProperty name;
    private final StringProperty gender;
    private final StringProperty age;
    private final StringProperty id;

    public StringProperty nameProperty() { return name; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty ageProperty() { return age; }
    public StringProperty idProperty() { return id; }

    public PatientRow(String name, String gender, String age, String id){
        this.name = new SimpleStringProperty(name);
        this.gender = new SimpleStringProperty(gender);
        this.age = new SimpleStringProperty(age);
        this.id = new SimpleStringProperty(id);
    }
    public void setId(String id){ this.id.set(id); }
    public String getId(){ return this.id.get(); }

    public void setName(String name){ this.name.set(name); }
    public String getName() { return name.get(); }

    public void setGender(String gender){ this.gender.set(gender); }
    public String getGender() { return gender.get(); }

    public void setAge(String age){ this.age.set(age); }
    public String getAge() { return age.get(); }
}
