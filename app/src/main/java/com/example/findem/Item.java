package com.example.findem;

public class Item {
    String name; //A string that contains the name of the item
    //String image; //A string that contains the path to the image of the item
    //constructor
    public Item(String name){//, String image){
        this.name = name;
        //this.image = image;
    }

    public static void main(String[] a) {
        new Item();
    }
}
