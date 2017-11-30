package iosr.faceboookapp.publisher.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

/**
 * Created by Leszek Placzkiewicz on 28.11.17.
 */
@Service
public class SqsQueueListener {

    private static final Logger LOG = LoggerFactory.getLogger(SqsQueueListener.class);


    @SqsListener("${publisher.queue.name}")
    private void receiveMessage(String message) {
        LOG.info("Received SQS message {}", message);
    }

}

