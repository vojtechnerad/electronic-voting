package cz.vse.java.elektronicke_volby.logic;

public class Elections {

    int id;
    String name;
    String date;
    int status;

    public Elections (int id, String name, String date, int status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {return name;}
    public String getDate() {return date;}
    public int getStatus() {return status;}
}
