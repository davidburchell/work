public class Word {

    private String name;
    private int length;

    public Word(String name){
        this.name = name;
        this.length = name.length();
    }

    public int getLength(){
        return this.length;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String newName){
        this.name = newName;
        this.length = newName.length();
    }

    public String toString(){
        return this.name;
    }
}
