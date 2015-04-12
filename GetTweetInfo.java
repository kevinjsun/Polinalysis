import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Status;

public class GetTweetInfo {
    static List<Status> statuses;

    public static void main(String[] args) throws JSONException {

        Info info = getStats("@IndianasSweetie", 10);
        long[] time = info.time;
        double[] mood = info.mood;
        double[] liberal = info.liberal;
        double[] green = info.green;
        double[] conservative = info.conservative;
        double[] libertarian = info.libertarian;
        String[] tweets = info.tweets;
        StdOut.println("BarackObama:\ntime:");
        for (long l : time)
            StdOut.print(l + ",");
        StdOut.println("\nmood:\n");
        for (double d : mood)
            StdOut.print(d + ",");
        StdOut.println("\nliberal:\n");
        for (double d : liberal)
            StdOut.print(d + ",");
        StdOut.println("\ngreen:\n");
        for (double d : green)
            StdOut.print(d+ ",");
        StdOut.println("\nconservative:\n");
        for (double d : conservative)
            StdOut.print(d + ",");
        StdOut.println("\nlibertarian:\n");
        for (double d : libertarian)
            StdOut.print(d + ",");
        StdOut.println("\ntweets:\n");
        for (String s: tweets)
            StdOut.print("\""+s + "\"" + "\n");
        
    }

    public static Info getStats(String username, int numTweets) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpClient httpclientPolitics = new DefaultHttpClient();
        long[] time = null;
        double[] moodVal = null;
        double[] liberal = null;
        double[] green = null;
        double[] conservative = null;
        double[] libertarian = null;
        String[] tweet = null;
        try {
            // Add your data

            // Get twitter statuses
            statuses = TwitterMain.getTweets(username, numTweets);

            time = new long[statuses.size()];
            moodVal = new double[statuses.size()];
            liberal = new double[statuses.size()];
            green = new double[statuses.size()];
            conservative = new double[statuses.size()];
            libertarian = new double[statuses.size()];

            tweet = new String[statuses.size()];
            for (int i = 0; i < statuses.size(); i++) {
                String s = statuses.get(i).getText();
                // System.out.println(s);
                Date date = statuses.get(i).getCreatedAt();
                // System.out.println(date);

                // mood
                HttpPost httppost = new HttpPost(
                        "http://apiv1.indico.io/sentiment");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        1);
                nameValuePairs.add(new BasicNameValuePair("data", s));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // politics
                HttpPost httppostPolitics = new HttpPost(
                        "http://apiv1.indico.io/political");
                List<NameValuePair> nameValuePairsPolitics = new ArrayList<NameValuePair>(
                        1);
                nameValuePairsPolitics.add(new BasicNameValuePair("data", s));
                httppostPolitics.setEntity(new UrlEncodedFormEntity(
                        nameValuePairsPolitics));

                // Execute HTTP Post Request for mood
                HttpResponse response = httpclient.execute(httppost);
                String jsonString = EntityUtils.toString(response.getEntity());

                // Execute HTTP Post Request for politics
                HttpResponse responsePolitics = httpclientPolitics
                        .execute(httppostPolitics);
                String jsonStringPolitics = EntityUtils
                        .toString(responsePolitics.getEntity());
                try {
                    JSONObject obj = new JSONObject(jsonString);
                    JSONObject objPolitics = new JSONObject(jsonStringPolitics);
                    // System.out.println(jsonStringPolitics);

                    time[i] = statuses.get(i).getCreatedAt().getTime();
                    moodVal[i] = Double.parseDouble(obj.get("results")
                            .toString());
                    liberal[i] = Double.parseDouble(((JSONObject) objPolitics
                            .get("results")).get("Liberal").toString());
                    green[i] = Double.parseDouble(((JSONObject) objPolitics
                            .get("results")).get("Green").toString());
                    conservative[i] = Double
                            .parseDouble(((JSONObject) objPolitics
                                    .get("results")).get("Conservative")
                                    .toString());
                    libertarian[i] = Double
                            .parseDouble(((JSONObject) objPolitics
                                    .get("results")).get("Libertarian")
                                    .toString());
                    tweet[i] = statuses.get(i).getText();
                    // System.out.println(obj.get("results"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // System.out.println("this is the response " + jsonString);
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.out.println("CPE" + e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("IOE" + e);
        }
        return new Info(time, moodVal, liberal, green, conservative,
                libertarian, tweet);
    }
}
