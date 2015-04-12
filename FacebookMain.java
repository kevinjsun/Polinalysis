import java.util.List;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class FacebookMain {
    static final String appId = "14843378444";
    static final String appSecret = "c799998acd0153b0058806fbf030bc47";
    static final String accessToken = "CAACEdEose0cBANumn2pbOJmnfufVjjWuZBHB41LOz2gRRQ0Pf5hOZB9K4tn8J1pNtT9AuyPw3KGCi3dSx2wHS2wbAyJxhrsHvL78IPy97ghYFvhHCSBH5TDxJTInJS7LdpKtotX42Uxk6ZC7f151c7HZCPcXOEPwBcv6bbzdZCZAIPkpAya3HaoZCfeyKJGamhMKJmfkZBVEqMmrZBjEwnuJGIzCj9oKzyVs2yvVcMFZAUhAZDZD";

    public static List<Post> getPosts(int limit) throws FacebookException {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(appId, appSecret);
        // facebook.setOAuthPermissions("read_stream,");
        facebook.setOAuthAccessToken(new AccessToken(accessToken, null));

        ResponseList<Post> feeds = facebook.getStatuses(new Reading()
                .limit(limit));
        return feeds;
    }
}