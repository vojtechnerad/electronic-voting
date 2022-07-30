package cz.vse.java.elektronicke_volby.logic;

/**
 * Metoda pro vytvoření instance kandidáta, obsahující všechny ukládané informace.
 */
public class Candidate {
    public int id;
    public String name;

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
