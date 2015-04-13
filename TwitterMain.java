import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterMain {

    static String consumerKeyStr = "nQMRFrXcG6pA1bTSgWO9eIUgw";
    static String consumerSecretStr = "ZnuTTroYlp9K45ohUFkAEkdyxamrUI8mYOSepsMxRNFKoITXDI";
    static String accessTokenStr = "2815613689-yrbKVSGWd5BieiSlmDUpkcxI8gL6809qnLNmjQL";
    static String accessTokenSecretStr = "4bh1NIWQFRw91N4lax5MX9LOSR1XZ2KCobCaiIal3aBxF";

    public static List<Status> getTweets(String user, int tweets) {
        try {
            
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(consumerKeyStr);
            builder.setOAuthConsumerSecret(consumerSecretStr);
            Configuration configuration = builder.build();
            TwitterFactory factory = new TwitterFactory(configuration);
            Twitter twitter = factory.getInstance();
            
            //Twitter twitter = new TwitterFactory().getInstance();

            //twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
            AccessToken accessToken = new AccessToken(accessTokenStr,
                    accessTokenSecretStr);

            twitter.setOAuthAccessToken(accessToken);

            // twitter.updateStatus("Post using Twitter4J Again");
            final Paging paging = new Paging();
            paging.count(tweets); // max statuses you can request for this call

            // Hashtags
           // Twitter twitterHashtags = TwitterFactory.getSingleton();
           // twitterHashtags.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
            //AccessToken accessTokenHash = new AccessToken(accessTokenStr,
              //      accessTokenSecretStr);

            //twitterHashtags.setOAuthAccessToken(accessTokenHash);
            if (user.charAt(0) == '#') {
                Query query = new Query(user); // user is actually just the
                                               // hashtag
                QueryResult result = twitter.search(query);
                List<Status> statuses = new ArrayList<Status>();

                for (int i = 0; i < tweets; i++) { // make sure not more than
                                                   // 100
                    statuses.add(result.getTweets().get(i));
                    // System.out.println("@" + status.getUser().getScreenName()
                    // + ":" + status.getText());
                }
                return statuses;
            }

            List<Status> statuses = twitter.getUserTimeline(user.substring(1),
                    paging);
            return statuses;
            // System.out.println(statuses.size());
            // for (Status status : statuses) {
            // System.out.println(status);
            // }
        } catch (TwitterException te) {
            te.printStackTrace();
        }
        System.out.println("couldnt find user or tweets or something bad");
        return null;
    }

    public static void main(String[] args) {

        getTweets("@taylorswift13", 20);
    }
}