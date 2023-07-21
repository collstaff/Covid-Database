import static org.junit.Assert.*;
import org.junit.*;
/*******************************************
 * Class to test the CovidEntry class
 *
 * @author - 
 * @version - 
 ******************************************/
public class CovidEntryJUnit{
    /** object of the CovidEntry class*/
    private CovidEntry covid;

    /******************************************************
     * Test constructor
     *****************************************************/
    @Test
    public void testConstructor()  {
        //Testing a Covid Entry
        covid = new CovidEntry("MI",8,1,758,7,91332,6457);

        assertEquals("Constructor: State should be equal to value of input parameter",
                "MI", covid.getState());

        assertEquals("Constructor: month should be equal to value of input parameter",
                8, covid.getMonth());

        assertEquals("Constructor: day should be equal to value of input parameter",
                1, covid.getDay());

        assertEquals("Constructor: daily infections should be equal to value of input parameter",
                758, covid.getDailyInfections());

        assertEquals("Constructor: daily deaths should be equal to value of input parameter",
                7, covid.getDailyDeaths());

        assertEquals("Constructor: total infections should be equal to value of input parameter",
                91332, covid.getTotalInfections());

        assertEquals("Constructor: total deaths should be equal to value of input parameter",
                6457, covid.getTotalDeaths());

    }
}