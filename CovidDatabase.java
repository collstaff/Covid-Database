import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.sql.*;
import java.io.*;

public class CovidDatabase {
    private ArrayList<CovidEntry> CovidEntryList;
    private static final int SAFE = 5;

    public ArrayList<CovidEntry> getCovidEntryList() {
        return CovidEntryList;
    }


    public CovidDatabase() {
        CovidEntryList = new ArrayList<CovidEntry>();
    }

    public void readCovidData(String filename) {
        FileInputStream fileByteStream = null;
        Scanner scnr = null;
        try {
            //open file and set delimiter
            fileByteStream = new FileInputStream(filename);
            scnr = new Scanner(fileByteStream);
            scnr.useDelimiter("[,\r\n]+");

            scnr.nextLine();
            while (scnr.hasNext()) {
                String st = scnr.next();
                int m = scnr.nextInt();
                int d = scnr.nextInt();
                int di = scnr.nextInt();
                int dd = scnr.nextInt();
                int ti = scnr.nextInt();
                int td = scnr.nextInt();

                CovidEntry temp = new CovidEntry(st, m, d, di, dd, ti, td);
                CovidEntryList.add(temp);
            }
//            System.out.println(CovidEntryList);
            fileByteStream.close();


        } catch (IOException error) {
            System.out.println("Error with file " + filename);

        }

    }

    public int countRecords() {
        return CovidEntryList.size();
    }

    public int getTotalDeaths() {
        int sum = 0;
        for (CovidEntry entry : CovidEntryList) {
            sum += entry.getDailyDeaths();
        }
        return sum;
    }

    public int getTotalInfections() {
        int sum = 0;
        for (CovidEntry entry : CovidEntryList) {
            sum += entry.getDailyInfections();
        }
        return sum;
    }

    public int countTotalDeaths(int m, int d) {
        int sum = 0;
        for (CovidEntry entry : CovidEntryList) {
            if (entry.getMonth() == m && entry.getDay() == d) {
                sum += entry.getDailyDeaths();
            }
        }
        return sum;
    }

    public int countTotalInfections(int m, int d) {
        int sum = 0;
        for (CovidEntry entry : CovidEntryList) {
            if (entry.getMonth() == m && entry.getDay() == d) {
                sum += entry.getDailyInfections();
            }
        }
        return sum;
    }

    public ArrayList<CovidEntry> safeToOpen(String st) {
        ArrayList<CovidEntry> result = new ArrayList<CovidEntry>();

        int counter = 1;
        int current = 100000;
        boolean safe = false;
        for (CovidEntry entry : CovidEntryList) {
            if (entry.getState().equalsIgnoreCase(st)) {
                if (entry.getDailyInfections() < current) {
                    counter++;
                    current = entry.getDailyInfections();
                    result.add(entry);
                } else {
                    counter = 1;
                    current = entry.getDailyInfections();
                    result.clear();
                    result.add(entry);
                }

            }
            if (counter == SAFE) {
                safe = true;
                break;
            }
        }
        if (safe == true) {
            System.out.println(st + " is safe to open");
            return result;
        } else {
            return null;
        }
    }

    public CovidEntry peakDailyDeaths(String st) {
        CovidEntry result = new CovidEntry("", 0, 0, 0, 0, 0, 0);
        int max = -1;
        boolean valid = false;
        for (CovidEntry entry : CovidEntryList) {
            if (entry.getState().equalsIgnoreCase(st)) {
                valid = true;
                if (entry.getDailyDeaths() > max) {
                    max = entry.getDailyDeaths();
                    result = entry;
                }
            }
        }
        if (valid) {
            return result;

        } else {
            return null;
        }
    }

    public CovidEntry peakDailyDeaths(int m, int d) {
        CovidEntry result = new CovidEntry("", 0, 0, 0, 0, 0, 0);
        int max = -1;
        boolean valid = false;
        for (CovidEntry entry : CovidEntryList) {
            if (entry.getMonth() == m && entry.getDay() == d) {
                valid = true;
                if (entry.getDailyDeaths() > max) {
                    max = entry.getDailyDeaths();
                    result = entry;
                }
            }
        }
        if (valid) {
            return result;

        } else {
            return null;
        }
    }

    public ArrayList<CovidEntry> getDailyDeaths(int m, int d) {
        ArrayList<CovidEntry> result = new ArrayList<CovidEntry>();


        for (CovidEntry entry : CovidEntryList) {
            if (entry.getMonth() == m && entry.getDay() == d) {

                result.add(entry);
            }
        }
        return result;
    }


    public CovidEntry mostTotalDeaths() {
        int max = -1;
        CovidEntry result = new CovidEntry("", 0, 0, 0, 0, 0, 0);
        for (CovidEntry entry : CovidEntryList) {

            if (entry.getTotalDeaths() > max) {
                max = entry.getTotalDeaths();
                result = entry;
            }
        }
        return result;

    }


    public ArrayList<CovidEntry>
    listMinimumDailyInfections(int m, int d, int min) {
        boolean valid = false;
        ArrayList<CovidEntry> result = new ArrayList<CovidEntry>();
        for (CovidEntry entry : CovidEntryList) {
            if (entry.getMonth() == m && entry.getDay() == d && entry.getDailyInfections() >= min) {
                valid = true;
                result.add(entry);
            }
        }
       return result;
    }

    public ArrayList<CovidEntry> topTenDeaths(int m, int d) {
        ArrayList<CovidEntry> allDeaths = new ArrayList<CovidEntry>();
        allDeaths = getDailyDeaths(m, d);
        Collections.sort(allDeaths);
        ArrayList<CovidEntry> result = new ArrayList<CovidEntry>();
        for (int i = 0; i < 10 && i < allDeaths.size(); i++) {
            result.add(allDeaths.get(i));
        }
        System.out.println("top 10 daily deaths for " + m + "/" + d);
        for (CovidEntry c : result){
            System.out.println(c);
        }
        return result;


    }

    public void transferCovidData(String filename) {
        int batchSize = 20;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:covid.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("drop table if exists COVIDENTRY");
            statement.executeUpdate("create table COVIDENTRY (state string, month integer, day integer, daily_infections integer, daily_deaths integer, total_infections integer, total_deaths integer)");
            String sql = "INSERT INTO COVIDENTRY (state, month, day, daily_infections, daily_deaths, total_infections, total_deaths) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement PS = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(filename));
            String lineText;
            int count = 0;
            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String state = data[0];
                String month = data[1];
                String day = data[2];
                String dailyInfections = data[3];
                String dailyDeaths = data[4];
                String totalInfections = data[5];
                String totalDeaths = data[6];

                PS.setString(1, state);
                PS.setInt(2, Integer.parseInt(month));
                PS.setInt(3, Integer.parseInt(day));
                PS.setInt(4, Integer.parseInt(dailyInfections));
                PS.setInt(5, Integer.parseInt(dailyDeaths));
                PS.setInt(6, Integer.parseInt(totalInfections));
                PS.setInt(7, Integer.parseInt(totalDeaths));
                PS.addBatch();

                if (count % batchSize == 0) {
                    PS.executeBatch();
                }
            }
            lineReader.close();
            PS.executeBatch();
            connection.commit();
            connection.close();
        }
            catch (IOException e){
            System.err.println(e);
            }
            catch (SQLException e){
            e.printStackTrace();
                try {
                    connection.rollback();
                }
                catch (SQLException ex){
                    ex.printStackTrace();
                }
        }

    }
    public void readCovidData() {
        Connection connection = null;


        try {
            connection = DriverManager.getConnection("jdbc:sqlite:covid.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select * from COVIDENTRY");

            String state;
            int month, day, dailyInfections, dailyDeaths, totalInfections, totalDeaths;

            while (rs.next()) {
                state = rs.getString("state");
                month = rs.getInt("month");
                day = rs.getInt("day");
                dailyInfections = rs.getInt("daily_infections");
                dailyDeaths = rs.getInt("daily_deaths");
                totalInfections = rs.getInt("total_infections");
                totalDeaths = rs.getInt("total_deaths");
                this.CovidEntryList.add(new CovidEntry(state, month, day, dailyInfections, dailyDeaths, totalInfections, totalDeaths));


            }
                connection.close();




        } catch (SQLException error) {
            System.out.println(error + "Error with covid.db");

        }

        }



    }









