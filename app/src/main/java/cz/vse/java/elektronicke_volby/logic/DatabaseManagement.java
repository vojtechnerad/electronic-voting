package cz.vse.java.elektronicke_volby.logic;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Třída slouží
 */
public class DatabaseManagement {

    public static Connection connect() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/data.db");
            System.out.println("Connected");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }

        return connection;
    }

    public static HashMap<Integer, String> getRegions() {
        String sqlCommand = "SELECT * FROM region";
        HashMap<Integer, String> regions = new HashMap<Integer, String>();

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            while (resultSet.next()) {
                regions.put(resultSet.getInt("id"), resultSet.getString("name"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return regions;
    }

    public static void signUpNewVoter(Voter voter) {
        String sqlCommand = "INSERT INTO voters (birth_number, first_name, last_name, birth_date, region_number, password_hash, email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, voter.getBirthNumber());
            statement.setString(2, voter.getFirstName());
            statement.setString(3, voter.getLastname());
            statement.setString(4, voter.getBirthDate());
            statement.setInt(5, voter.getRegionNumber());
            statement.setString(6, voter.getPasswordHash());
            statement.setString(7, voter.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metoda pro získání instance voliče.
     * @param birthNumber
     * @return
     */
    public static Voter getVotersInstance(String birthNumber) {
        String sqlCommand = "SELECT * FROM voters WHERE birth_number = '" + birthNumber + "'";
        Voter voter = null;


        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            while (resultSet.next()) {
                voter = new Voter(resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        birthNumber,
                        resultSet.getString("email"),
                        resultSet.getInt("region_number"),
                        resultSet.getString("birth_date"),
                        resultSet.getString("password_hash"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return voter;
    }

    /**
     * Metoda pro ověření správcovských údajů.
     * @param loginCode
     * @param passwordHash
     * @return
     */
    public static boolean verifyAdminsCredentials(String loginCode, String passwordHash) {
        String sqlCommand = "SELECT * FROM admins WHERE login_code = '" + loginCode + "'";

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            int count = 0;
            String returnedHash = null;
            while (resultSet.next()) {
                count++;
                returnedHash = resultSet.getString("password_hash");
            }

            if (count == 1) {
                if (passwordHash.equals(returnedHash)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Metoda pro ověření uživatelských údajů.
     * @param birthNumber
     * @param passwordHash
     * @return
     */
    public static Boolean verifyUsersCredentials(String birthNumber, String passwordHash) {
        String sqlCommand = "SELECT * FROM voters WHERE birth_number = '" + birthNumber + "'";

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            int count = 0;
            String returnedHash = null;
            while (resultSet.next()) {
                count++;
                returnedHash = resultSet.getString("password_hash");
            }

            if (count == 1) {
                if (passwordHash.equals(returnedHash)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Metoda pro získání
     * @return
     */
    public static ArrayList<Elections> listOfElections () {
        String sqlCommand = "SELECT * FROM elections";

        ArrayList<Elections> elections = new ArrayList<>();

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            Elections election;

            while (resultSet.next()) {
                election = new Elections(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        resultSet.getInt("status")
                );
                elections.add(election);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return elections;
    }

    /**
     * Metoda vracející číslo, které bude přiřazeno jako ID dalších voleb.
     *
     * @return ID přidávaných voleb
     */
    public static int getElectionId() {
        int number = 0;
        try {
            String sqlCommand = "SELECT MAX(id) FROM elections";
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = preparedStatement.executeQuery();
            number = resultSet.getInt(1) + 1;
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return number;
    }

    public static ArrayList<Candidate> getCandidates(int idElections) {
        ArrayList<Candidate> candidates = new ArrayList<>();
        try {
            String sqlCommand = "SELECT * FROM candidates_" + idElections;
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Candidate candidate = new Candidate(resultSet.getInt("id"), resultSet.getString("candidate_name"));
                candidates.add(candidate);
                System.out.println(candidate.name);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return candidates;
    }

    public static void addElectionsIntoDb(String name, String date) {
        try {
            // Vytvoření záznamu voleb do tabulky
            String sqlCommand = "INSERT INTO elections (id, name, date, status) VALUES (?, ?, ?, ?)";
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
            preparedStatement.setInt(1, getElectionId());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static Boolean hasUserVoted(Elections elections, Voter voter) {
        boolean hasUserVoted = false;
        try {
            String sqlCommand = "SELECT * FROM votes_" + elections.getId() + " WHERE birth_number = " + voter.getBirthNumber();
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                hasUserVoted = true;
                System.out.println("Volil lmao");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return hasUserVoted;
    }

    public static void recordAVote(Elections elections, Voter voter, Candidate candidate) {
        try {
            String sqlCommand = "INSERT INTO votes_" + elections.getId() + " (birth_number, candidate_name) VALUES (?, ?)";
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand);
            preparedStatement.setString(1, voter.getBirthNumber());
            preparedStatement.setInt(2, candidate.id);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void createcCandidatesTable(Integer id) {
        String name = "candidates_" + id.toString();
        try {
            String sqlCommand = "CREATE TABLE " + name +  " (" +
                    "id INTEGER NOT NULL UNIQUE, " +
                    "candidate_name TEXT NOT NULL," +
                    "PRIMARY KEY(id AUTOINCREMENT))";
            Connection connection = connect();
            Statement statement = connection.createStatement();
            statement.execute(sqlCommand);
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void createVotesTable(Integer id) {
        String name = "votes_" + id.toString();
        try {
            String sqlCommand = "CREATE TABLE " + name +  " (" +
                    "birth_number TEXT NOT NULL UNIQUE, " +
                    "candidate_name INTEGER NOT NULL)";
            Connection connection = connect();
            Statement statement = connection.createStatement();
            statement.execute(sqlCommand);
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    /**
     * Metoda sloužící pro kompletní vytvoření podkladů pro volby.
     * Metoda vloží nový záznam do tabulky vobleb, a vytvoří příslušné tabulky
     * pro zaznamenávání kandidátů a hlasů voličů.
     *
     * @param name
     * @param date
     */
    public static void createElections(String name, String date) {
        Integer id = getElectionId();
        addElectionsIntoDb(name, date);
        createcCandidatesTable(id);
        createVotesTable(id);
    }

}
