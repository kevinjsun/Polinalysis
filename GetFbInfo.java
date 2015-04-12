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
import facebook4j.FacebookException;
import facebook4j.Post;

public class GetFbInfo {
    static List<Post> statuses;

    public static void main(String[] args) throws JSONException,
            FacebookException {

        Info info = getStats(20);
        long[] time = info.time;
        for (int i = 0; i < time.length; i++) {
            System.out.println(info.liberal[i]);
            System.out.println(info.tweets[i]);

        }
        System.out.println();
        System.out.println();

    }

    public static Info getStats(int numStatuses) throws FacebookException {
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
            statuses = FacebookMain.getPosts(numStatuses);

            time = new long[statuses.size()];
            moodVal = new double[statuses.size()];
            liberal = new double[statuses.size()];
            green = new double[statuses.size()];
            conservative = new double[statuses.size()];
            libertarian = new double[statuses.size()];

            tweet = new String[statuses.size()];
            for (int i = 0; i < statuses.size(); i++) {
                String s = statuses.get(i).getMessage();
                // System.out.println(s);
                Date date = statuses.get(i).getUpdatedTime();
                // System.out.println(date);
                for (int j = 0; j < 10; j++) {
                    System.out.print("\"" + statuses.get(j).getMessage()
                            + "\", ");
                }
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

                    time[i] = statuses.get(i).getUpdatedTime().getTime();
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
                    tweet[i] = statuses.get(i).getMessage();
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
