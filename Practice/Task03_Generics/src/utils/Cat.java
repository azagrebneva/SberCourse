package utils;

public class Cat extends Pet {

    public Cat(String name, Integer age) {
        super(name, age);
    }

    @Override
    public void sound() {
        System.out.println("Cat " + getName() + " age " + getAge() + " says something...");
    }

    @Override
    public String toString() {
        return "Cat(" + getName() + " " + getAge() + ')';
    }
}
