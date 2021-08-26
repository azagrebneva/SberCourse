package utils;

public class Dog extends Pet{

    public Dog(String name, Integer age) {
        super(name, age);
    }

    @Override
    public void sound() {
        System.out.println("Dog " + getName() + " age " + getAge() + " says something...");
    }

    @Override
    public String toString() {
        return "Dog(" + getName() + " " + getAge() + ')';
    }
}
