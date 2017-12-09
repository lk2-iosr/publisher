package iosr.faceboookapp.publisher.client;

import iosr.faceboookapp.publisher.stats.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Leszek Placzkiewicz on 07.12.17.
 */
@Component
public class StatsClient {

    private static final Logger LOG = LoggerFactory.getLogger(StatsClient.class);

    private static final String STATS_URL = "http://stats";
    private static final String TOP_BY_LIKES = "/topByLikes";
    private static final String TOP_BY_SHARES = "/topByShares";
    private static final String TOP_BY_COMMENTS = "/topByComments";


    private RestTemplate restTemplate = new RestTemplate();

    public List<Post> getTopPostsByLikes() {
        LOG.debug("Requesting stats for topByLikes");
        return getForListOfPosts(STATS_URL + TOP_BY_LIKES);
    }

    public List<Post> getTopPostsByShares() {
        LOG.debug("Requesting stats for topByShares");
        return getForListOfPosts(STATS_URL + TOP_BY_SHARES);
    }

    public List<Post> getTopPostsByComments() {
        LOG.debug("Requesting stats for topByComments");
        return getForListOfPosts(STATS_URL + TOP_BY_COMMENTS);
    }

    private List<Post> getForListOfPosts(String url) {
        ResponseEntity<List<Post>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Post>>() {});
        return response.getBody();
    }

}
