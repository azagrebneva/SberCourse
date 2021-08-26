package utils;

import java.util.Comparator;
import java.util.Objects;

public class Pet implements Comparable<Pet> {
    private String name;
    private Integer age;

    public Pet(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void sound(){
        System.out.println("Pet " + name + " age " + age + " says something...");
    }

    @Override
    public String toString() {
        return "Pet(" + name + " " + age + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Pet pet = (Pet) o;
        return Objects.equals(name, pet.name) && Objects.equals(age, pet.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public int compareTo(Pet p) {
        int result;
        result = this.name.compareTo(p.getName());
        if(result != 0) return result;
        return this.age.compareTo(p.getAge());
    }
}
