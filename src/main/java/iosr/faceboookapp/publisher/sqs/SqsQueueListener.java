package iosr.faceboookapp.publisher.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import iosr.faceboookapp.publisher.email.EmailService;
import iosr.faceboookapp.publisher.redis.PostIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Leszek Placzkiewicz on 28.11.17.
 */
@Service
public class SqsQueueListener {

    private static final Logger LOG = LoggerFactory.getLogger(SqsQueueListener.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PostIdRepository postIdRepository;

    @Autowired
    private EmailService emailService;

    @Value("${post.filter.keyword}")
    private String keywordToFind;

    @Value("${destination.mail}")
    private String destinationEmail;


    @SqsListener("${publisher.queue.name}")
    private void receiveMessage(String message) throws IOException {
        SqsMessage sqsMessage = objectMapper.readValue(message, SqsMessage.class);
        LOG.info("Received SQS message {}", sqsMessage);
        handleSqsMessage(sqsMessage);
    }

    private void handleSqsMessage(SqsMessage sqsMessage) {
        String postText = sqsMessage.getMessage();
        String postId = sqsMessage.getId();
        String link = sqsMessage.getLink();

        boolean shouldBePublished = shouldBePublished(postText, keywordToFind, postId);
        if (shouldBePublished) {
            postIdRepository.savePostId(postId);
            emailService.sendEmail(destinationEmail, String.format("New post containing \'%s\'", keywordToFind), link);
        }


    }

    private boolean shouldBePublished(String message, String keyword, String postId) {
        return !postIdRepository.isMember(postId) && message.contains(keyword);
    }

}

