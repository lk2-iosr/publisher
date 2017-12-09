package iosr.faceboookapp.publisher.stats;

import iosr.faceboookapp.publisher.client.StatsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Leszek Placzkiewicz on 07.12.17.
 */
@Component
public class StatsPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(StatsClient.class);

    @Scheduled(initialDelayString = "60000", fixedRateString = "#{60000 * ${publish.stats.interval.minutes}}")
    public void publishStats() {
        LOG.info("Publishing stats");
        //TODO - implement
    }
}
