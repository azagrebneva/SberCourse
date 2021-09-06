package reflection;

public class Cat extends Pet {

    String breed;

    public Cat(String name, Integer age, String breed) {
        super(name, age);
        this.breed = breed;
    }

    public Cat(String name, Integer age) {
        super(name, age);
        this.breed = null;
    }

    @Override
    public void sound() {
        System.out.println("Cat " + getName() + " age " + getAge() + " says something...");
    }

    @Override
    public String toString() {
        return "Cat(" + getName() + " " + getAge() +
                " " + getBreed()+')';
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    private void catPrivateMethod(){
        return;
    }

    protected void catProtectedMethod(){
        return;
    }

    void catDefaultMethod(){
        return;
    }
}
