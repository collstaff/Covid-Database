import java.text.DecimalFormat;

public class CovidEntry implements Comparable {
    private String state;
    private int month;
    private int day;
    private int dailyInfections;
    private int dailyDeaths;
    private int totalInfections;
    private int totalDeaths;

    public int compareTo(Object other) {
        CovidEntry c = (CovidEntry) other;
        return c.dailyDeaths - dailyDeaths;
    }


    public String getState() {
        return state;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getDailyInfections() {
        return dailyInfections;
    }

    public int getDailyDeaths() {
        return dailyDeaths;
    }

    public int getTotalInfections() {
        return totalInfections;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public CovidEntry(String st, int m, int d, int di, int dd,
                      int ti, int td) {

        state = st;
        month = m;
        day = d;
        dailyInfections = di;
        dailyDeaths = dd;
        totalInfections = ti;
        totalDeaths = td;


    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###");
        return state + " " + month + "/" + day + " " + df.format(dailyInfections) +" daily infections, " +  df.format(dailyDeaths) + " daily deaths, " + df.format(totalInfections) +
                " total infections, " + df.format(totalDeaths)
                + " total deaths";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o instanceof CovidEntry) {
            CovidEntry e = (CovidEntry) o;

            if (!this.state.equals(e.getState())) {
                return false;
            }
            if (this.month != e.getMonth()) {
                return false;
            }
            if (this.day != e.getDay()) {
               return false;
            }
            if (this.dailyInfections != e.getDailyInfections()) {
                return false;
            }
            if (this.dailyDeaths != e.getDailyDeaths()){
                return false;
            }
            if (this.totalInfections != e.getTotalInfections()){
                return false;
            }
            if (this.totalDeaths != e.getTotalDeaths()){
            return false;
            }
        }
    return true;
    }
}

