package cz.vse.java.elektronicke_volby.logic;

/**
 * Třída Voter slouží pro vytvoření instance voliče, která obsahuje všechny uchovávané atributy.
 */
public class Voter {
    public String firstname;
    public String lastname;
    public String birthNumber;
    public String email;
    public int regionNumber;
    public String birthDate;
    public String passwordHash;

    public Voter (String firstname, String lastname, String birthNumber, String email, int regionNumber, String birthDate, String passwordHash) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthNumber = birthNumber;
        this.email = email;
        this.regionNumber = regionNumber;
        this.birthDate = birthDate;
        this.passwordHash = passwordHash;
    }

    public void signUp() {
        DatabaseManagement.signUpNewVoter(this);
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBirthNumber() {
        return birthNumber;
    }

    public String getBirthDate() {
        return  birthDate;
    }

    public int getRegionNumber() {
        return regionNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail(){ return email; }
}
