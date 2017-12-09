package iosr.faceboookapp.publisher.stats;

import iosr.faceboookapp.publisher.client.StatsClient;
import iosr.faceboookapp.publisher.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Leszek Placzkiewicz on 07.12.17.
 */
@Component
public class StatsPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(StatsClient.class);

    private static final String STATS_EMAIL_SUBJECT_PREFIX = "Stats";

    @Autowired
    private StatsClient statsClient;

    @Autowired
    private EmailService emailService;

    //TODO - add possibility of configuring several emails
    @Value("${destination.mail}")
    private String destinationEmail;

    @Scheduled(initialDelayString = "10000", fixedRateString = "#{60000 * ${publish.stats.interval.minutes}}")
    public void publishStats() {
        LOG.info("Sending stats to " + destinationEmail);

        List<Post> topPostsByLikes = statsClient.getTopPostsByLikes();
        List<Post> topPostsByShares = statsClient.getTopPostsByShares();
        List<Post> topPostsByComments = statsClient.getTopPostsByComments();

        String emailSubject = prepareEmailSubject();
        String emailBody = prepareEmailBody(topPostsByLikes, topPostsByShares, topPostsByComments);

        emailService.sendEmail(destinationEmail, emailSubject, emailBody);
    }

    private String prepareEmailSubject() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        return STATS_EMAIL_SUBJECT_PREFIX + " " + formattedDateTime;
    }

    private String prepareEmailBody(List<Post> topPostsByLikes, List<Post> topPostsByShares, List<Post> topPostsByComments) {
        String likesList = prepareEmailList("Top posts by likes:", topPostsByLikes, Post::getLikes);
        String sharesList = prepareEmailList("Top posts by shares:", topPostsByShares, Post::getShares);
        String commentsList = prepareEmailList("Top posts by comments:", topPostsByComments, Post::getComments);

        StringBuilder sb = new StringBuilder();
        sb.append(likesList);
        sb.append("\n\n");
        sb.append(sharesList);
        sb.append("\n\n");
        sb.append(commentsList);
        sb.append("\n\n");

        return sb.toString();
    }

    private String prepareEmailList(String header, List<Post> postList, Function<Post, Integer> postFeatureFunction) {
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        sb.append("\n");
        postList.forEach(p -> sb.append(createListEntry(p.getLink(), postFeatureFunction.apply(p))));
        return sb.toString();
    }

    private String createListEntry(String link, int value) {
        return String.format("%s (%s)\n", link, value);
    }

}
