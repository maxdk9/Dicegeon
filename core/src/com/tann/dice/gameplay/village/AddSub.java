package com.tann.dice.gameplay.village;

public class AddSub {
    public int add, sub;

    public void reset(){
        add=0; sub=0;
    }
    public void add(int amount){
        if(amount>0) {
            this.add += amount;
        }
        else{
            this.sub += amount;
        }
    }


    public int getTotal(){
        return add + sub;
    }
}
