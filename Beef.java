package bsu.rfe.java.group5.Lab1_Barsukevich.var12B;

import java.util.Objects;

public  class Beef extends Food {
    private String type;
    public void consume() {
        System.out.println(this.name + " " +  this.type +  " consumed" );
    }       //скушато -> boring
    public Beef(String type) {
        super("Beef");
        setType(type);
    }
    @Override
    public void print_name(){
        System.out.println((this.name) + " " + this.type);
    }
    public String getType(){
        return type;
    }
    public void setType( String type) {
        this.type = type;
    }

    public boolean equals(Object arg0){
        if(super.equals(arg0)){
            if(!(arg0 instanceof Beef)) return false;
            return type.equals(((Beef)arg0).type);
        }
        else return false;
    }
    @Override
    public int calculateCalories(){
        if(Objects.equals(this.type, "bloody")){
            return  700;
        }
        else if(Objects.equals(this.type,"normal")) {
            return  600;
        }
        else if(Objects.equals(this.type,"fried")) {
            return  450;
        }
        return 0;
    }

    public String toString(){
        return super.toString()+ " type " + type.toUpperCase() + "-";
    }
}

