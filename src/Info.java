/*************************************************************************
 *  Part of the Polinalysis Project
 *  Authors: Kush Patel, Josh Shin, Kevin Sun, Aravind Yeduvaka, Jon Zhang
 *
 *************************************************************************/
public class Info {
    public long[] time;
    public double[] mood;
    public double[] liberal;
    public double[] green;
    public double[] conservative;
    public double[] libertarian;
    public String[] tweets;

    public Info(long[] time, double[] mood, double[] liberal, double[] green,
            double[] conservative, double[] libertarian, String[] tweets) {
        this.time = time;
        this.mood = mood;
        this.liberal = liberal;
        this.green = green;
        this.conservative = conservative;
        this.libertarian = libertarian;
        this.tweets = tweets;
    }
    public Info() {
        this.time = new long[2];
        time[0] = 1;
        time[1] = 2;
        this.mood = new double[2];
        this.liberal = new double[2];
        this.green = new double[2];
        this.conservative = new double[2];
        this.libertarian = new double[2];
        this.tweets = new String[2];
        tweets[0] = "hi";
        tweets[1] = "yo";
    }
}