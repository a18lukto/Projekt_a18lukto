package com.example.projekt;

public class Companies {
    //Skapa statements här
    private String name;
    private String location;
    private int Anstallda;
    private int Omsattning;
    private String Type;


    //standard konstruktor
    public Companies(){
        name="Saknar namn";
        location="Saknar Plats";
        Anstallda=-1;
        Omsattning=-1;
        Type="Saknar typ";
    }

    public Companies(String n, String l, int h, int o, String t){
        name=n;
        location=l;
        Anstallda=h;
        Omsattning=o;
        Type=t;
    }

    public String info(){
        String tmp=new String();
        tmp+="Namn: "+name+"\n"+"Område: "+Type+"\n"+"Antal Anställda: "+Anstallda+" personer \n"+"Omsättning: "+Omsattning+" Mkr \n"+"Huvudkontor: "+location;

        return tmp;
    }

    @Override
    public String toString(){
        return name;
    }



}
