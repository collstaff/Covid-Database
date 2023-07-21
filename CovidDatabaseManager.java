import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;


import static org.junit.Assert.assertEquals;

public class CovidDatabaseManager {

    public static void main(String[] args) {
        CovidDatabase db = new CovidDatabase();
        //Actual data
//        db.readCovidData("C:\\Users\\Collin\\IdeaProjects\\Covid-19 Database\\covid_data.csv");
//        Test Data
        db.readCovidData("C:\\Users\\Collin\\IdeaProjects\\Covid-19 Database\\testCovid.csv");
        System.out.println ("Testing starts");

//        db.readCovidData("covid_data.csv");
        System.out.println(db.countRecords());

        System.out.println(db.getTotalDeaths());
        ArrayList<CovidEntry> temp = db.getCovidEntryList();
        for (CovidEntry c : temp){
            System.out.println(c);
        }
       System.out.println(db.getTotalInfections());
        System.out.println(db.peakDailyDeaths(5, 5));
        System.out.println(db.peakDailyDeaths("MI"));
        System.out.println(db.topTenDeaths(5, 5));
        assert db.countRecords() == 26 : "database should have 26";
        assert db.getTotalDeaths() == 487 : "Total deaths should be: 487";
        assert db.getTotalInfections() == 4550 : "infections should be: 4450";
        CovidEntry entry = db.peakDailyDeaths(5, 5);
    assert entry.getState().equals("MI") : "state should be MI";
    assert entry.getMonth() == 5 : "month should be 5";
    assert entry.getDay() ==5 : "day should be 5";
    assert entry.getDailyInfections()==999 : "daily infections should be 999";
    assert entry.getDailyDeaths()==50 : "daily deaths should be 50";
    assert entry.getTotalInfections()==600 : "total infections should be 600";
    assert entry.getTotalDeaths()==77 : "total deaths should be 77";
        CovidEntry entry2 = db.peakDailyDeaths("MI");
        assert entry.getState().equals("MI") : "state should be MI";
        assert entry2.getMonth() == 5 : "month should be 5";
        assert entry2.getDay() ==5 : "day should be 5";
        assert entry2.getDailyInfections()==999 : "daily infections should be 999";
        assert entry2.getDailyDeaths()==50 : "daily deaths should be 50";
        assert entry2.getTotalInfections()==600 : "total infections should be 600";
        assert entry2.getTotalDeaths()==77 : "total deaths should be 77";

        ArrayList<CovidEntry> topTen = db.topTenDeaths(5, 5);
        ArrayList<CovidEntry> testData = new ArrayList<>();
        testData.add(new CovidEntry("MI", 5, 5, 999, 50, 600, 77));
        testData.add(new CovidEntry("MI", 3, 1, 100, 10, 200, 20));
        testData.add(new CovidEntry("PA", 3, 1, 100, 10, 200, 20));
        testData.add(new CovidEntry("WA", 3, 1, 100, 10, 200, 20));
        testData.add(new CovidEntry("VA", 3, 1, 100,10, 200, 20));
        testData.add(new CovidEntry("NY", 3, 1, 100,10, 200, 20));
        testData.add(new CovidEntry("MI", 3, 2, 99, 9, 180, 19));
        testData.add(new CovidEntry("PA", 3, 2, 99, 9, 180, 19));
        testData.add(new CovidEntry("WA", 3, 2, 99, 9, 180, 19));
        testData.add(new CovidEntry("VA", 3, 2, 99, 9, 180, 19));
        assertEquals(topTen, testData);


        ArrayList<CovidEntry> testData2 = new ArrayList<>();
        ArrayList<CovidEntry> testSafe = db.safeToOpen("MI");
        for (CovidEntry c : testSafe){
            System.out.println(c);
        }
        testData2.add(new CovidEntry("MI", 3, 1, 100, 10, 200, 20));
        testData2.add(new CovidEntry("MI", 3, 2, 99, 9, 180, 19));
        testData2.add(new CovidEntry("MI", 3, 3, 88, 9, 160, 18));
        testData2.add(new CovidEntry("MI", 3, 4, 77, 8, 150, 15));
//        testData2.add(new CovidEntry("MI", 3, 5, 66, 7, 100, 10));
        assertEquals(testSafe, testData2);

        ArrayList<CovidEntry> testData3 = new ArrayList<>();
        ArrayList<CovidEntry> testMin = db.listMinimumDailyInfections(3,1,100);
        System.out.println(testMin);
        testData3.add(new CovidEntry("MI", 3, 1, 100, 10, 200, 20));
        testData3.add(new CovidEntry("PA", 3, 1, 100, 10, 200, 20));
        testData3.add(new CovidEntry("WA", 3, 1, 100, 10, 200, 20));
        testData3.add(new CovidEntry("VA", 3, 1, 100,10, 200, 20));
        testData3.add(new CovidEntry("NY", 3, 1, 100,10, 200, 20));
        assertEquals(testMin, testData3);



        System.out.println ("Testing ends");
        db.transferCovidData("C:\\Users\\Collin\\IdeaProjects\\Covid-19 Database\\covid_data.csv");
        db.readCovidData();

    }
}

