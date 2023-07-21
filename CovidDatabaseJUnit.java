import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.*;
/*******************************************
 * Class to test the CovidDatabase project
 *
 * @author -
 * @version -
 ******************************************/
public class CovidDatabaseJUnit{
    /** object of the CovidDatabase class */
    private CovidDatabase database;

    /******************************************************
     * Test constructor
     *****************************************************/
    @Test
    public void testConstructor()
    {
        database= new CovidDatabase();
        assertEquals("ArrayList should not contain any records at this time",
                0, database.countRecords ());
    }

    /******************************************************
     * Test read file and counts
     *****************************************************/
    @Test
    public void testReadFileAndCounts()
    {
        database= new CovidDatabase();
        database.readCovidData("covid_data.csv");
        assertEquals("ArrayList should not contain 10346 records",
                10346, database.countRecords ());
    }

    /******************************************************
     * Test statics for all the covid entries
     *****************************************************/
    @Test
    public void testStatistics()
    {
        database= new CovidDatabase();
        database.readCovidData("covid_data.csv");

        // testing total deaths
        assertEquals("Total deaths up to 9/27 should be 196,696",
                196696, database.getTotalDeaths());

        // testing total infections
        assertEquals("Total infections up to 9/27 should be 7,032,090",
                7032090, database.getTotalInfections());

        // testing most total infections
        CovidEntry c = database.mostTotalDeaths();
        assertEquals("State with most total deaths up to 9/27 is NY",
                "NY", c.getState());
        assertEquals("Number of deaths in NY up to 9/27: 25,456" ,
                25456, c.getTotalDeaths());
    }

    /******************************************************
     * Test peak daily deaths
     *****************************************************/
    @Test
    public void testPeakDailyDeaths()
    {
        database= new CovidDatabase();
        database.readCovidData("covid_data.csv");
        CovidEntry c;

        // testing peak daily deaths for a date 8/12
        c = database.peakDailyDeaths(8, 12);
        assertEquals("State with peak deaths on 8/12 is TX",
                "TX", c.getState());
        assertEquals("Number deaths in TX on 8/12: 324" ,
                324, c.getDailyDeaths());

        // testing peak daily deaths for a state
        c = database.peakDailyDeaths("MI");
        assertEquals("Peak deaths up to 9/27 in MI",
                "MI", c.getState());
        assertEquals("Peak number of deaths for MI: 169" ,
                169, c.getDailyDeaths());

        // testing peak daily deaths for a date 4/30
        c = database.peakDailyDeaths(4, 30);
        assertEquals("State with peak deaths on 4/30 is NJ",
                "NJ", c.getState());
        assertEquals("Number deaths in NJ on 4/30: 481" ,
                481, c.getDailyDeaths());

        // testing peak daily deaths for a state
        c = database.peakDailyDeaths("NY");
        assertEquals("Peak deaths up to 9/27 in NY",
                "NY", c.getState());
        assertEquals("Peak number of deaths for NY: 951" ,
                951, c.getDailyDeaths());

    }

    /******************************************************
     * Test peak daily deaths - invalid state & invalid date
     *****************************************************/
    @Test
    public void testPeakDailyDeathsInvalidParameters()
    {
        database= new CovidDatabase();
        database.readCovidData("covid_data.csv");
        CovidEntry c;

        // testing peak daily deaths for an invalid date
        c = database.peakDailyDeaths(13, 12);
        assertEquals("Wrong date 13/12 should return  null",
                null, c);

        // testing peak daily deaths for an invalid state
        c = database.peakDailyDeaths("XX");
        assertEquals("Wrong state XX should return null" ,
                null, c);

        // testing peak daily deaths for date not found in the file
        c = database.peakDailyDeaths(1, 1);
        assertEquals("No records for 1/1 should return  null",
                null, c);
    }

    /******************************************************
     * Test safe to open
     *****************************************************/
    @Test
    public void testSafeToOpen() {
        ArrayList<CovidEntry> list;
        database= new CovidDatabase();
        database.readCovidData("covid_data.csv");

        // testing safe to open WA
        list = database.safeToOpen("WA");
        assertEquals("Safe to open should have 5 records" ,
                5, list.size());

        //testing first record on the list
        assertEquals("The states should be WA",
                "WA", list.get(0).getState());
        assertEquals("Month in the first record in the sequence should be 5",
                5, list.get(0).getMonth());
        assertEquals("Day in the first record in the sequence should be 6",
                6, list.get(0).getDay());
        assertEquals("Infections in the first record in the sequence whould be 289",
                289, list.get(0).getDailyInfections());

        //testing last record on the list
        assertEquals("The states should be WA",
                "WA", list.get(4).getState());
        assertEquals("Month in the lasst record in the sequence should be 5",
                5, list.get(4).getMonth());
        assertEquals("Day in the last record in the sequence should be 10",
                10, list.get(4).getDay());
        assertEquals("Infections in the last record in the sequence whould be 62",
                62, list.get(4).getDailyInfections());

        // test safe to open - California
        list = database.safeToOpen("CA");
        assertEquals("Safe to open should have 5 records" ,
                5, list.size());

        //testing first record on the list
        assertEquals("The states should be CA",
                "CA", list.get(0).getState());
        assertEquals("Month in the first record in the sequence should be 5\4",
                4, list.get(0).getMonth());
        assertEquals("Day in the first record in the sequence should be 21",
                21, list.get(0).getDay());
        assertEquals("Infections in the first record in the sequence whould be 2283",
                2283, list.get(0).getDailyInfections());

        //testing last record on the list
        assertEquals("The states should be CA",
                "CA", list.get(4).getState());
        assertEquals("Month in the lasst record in the sequence should be 4",
                4, list.get(4).getMonth());
        assertEquals("Day in the last record in the sequence should be 25",
                25, list.get(4).getDay());
        assertEquals("Infections in the last record in the sequence whould be 1883",
                1883, list.get(4).getDailyInfections());

        // testing a state not found in the database
        list = database.safeToOpen("ZZ");
        assertEquals("State not valid - safeToOpen should return null" ,
                null, list);
    }

    /******************************************************
     * Test minimum infections on a date
     *****************************************************/
    @Test
    public void testMinInfections() {
        ArrayList<CovidEntry> list;
        database= new CovidDatabase();
        database.readCovidData("covid_data.csv");

        // testing a Minimum of 2,500 infections on 9/11
        list = database.listMinimumDailyInfections(9, 11, 2500);
        assertEquals("three records with 2,500 infections or more on 9/11" ,
                3, list.size());

        //testing highest - index 0
        assertEquals("TX should have the highest number of deaths on 8/1",
                "TX", list.get(0).getState());
        assertEquals("Infections in TX on 9/11 should be 3547",
                3547, list.get(0).getDailyInfections());

        //testing last entry
        assertEquals("CA should be the last entry on the list",
                "CA", list.get(2).getState());
        assertEquals("Infections in CA on 9/11 should be 3326",
                3326, list.get(2).getDailyInfections());

        // testing parameter values not found in database
        list = database.listMinimumDailyInfections(9, 11, 6000);
        assertEquals("Infections of 6,000 or more on 9/11 hould be zero" ,
                0, list.size());

        // testing wrong parameter values
        list = database.listMinimumDailyInfections(13, 11, 10);
        assertEquals("Invalid date 13/11 Infections of 10 or more on 9/11 hould be zero" ,
                0, list.size());
    }

    /******************************************************
     * Test top ten
     *****************************************************/
    @Test
    public void testTopTen() {
        ArrayList<CovidEntry> list;
        database= new CovidDatabase();
        database.readCovidData("covid_data.csv");

        // testing top ten for 8/1
        list = database.topTenDeaths(8, 1);
        assertEquals("top ten number of records should be 10" ,
                10, list.size());

        //testing highest - index 0
        assertEquals("TX should have the highest number of deaths on 8/1",
                "TX", list.get(0).getState());
        assertEquals("Deaths in TX on 8/1 should be 268",
                268, list.get(0).getDailyDeaths());

        //testing CovidEntry at index 6
        assertEquals("NC should be at index 6 of the list of deaths on 8/1",
                "NC", list.get(6).getState());
        assertEquals("Deaths in NC on 8/1 should be 40",
                40, list.get(6).getDailyDeaths());

        // testing a date not found in the database
        list = database.topTenDeaths(10, 31);
        assertEquals("top ten number of records should be zero" ,
                0, list.size());

        // testing an invalid date
        list = database.topTenDeaths(-10, 31);
        assertEquals("top ten number of records should be zero" ,
                0, list.size());
    }
}

