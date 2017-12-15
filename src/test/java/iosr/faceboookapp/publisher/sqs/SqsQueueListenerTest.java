package iosr.faceboookapp.publisher.sqs;

import iosr.faceboookapp.publisher.email.EmailService;
import iosr.faceboookapp.publisher.redis.PostIdRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by Leszek Placzkiewicz on 15.12.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SqsQueueListenerTest {

    @MockBean
    private EmailService emailService;

    @MockBean
    private PostIdRepository postIdRepository;

    @Autowired
    private SqsQueueListener sqsQueueListener;

    @Value("${post.filter.keyword}")
    private String postFilterKeyword;

    @Value("${destination.mail}")
    private String destinationMail;

    @Test

    public void shouldSendEmailWhenPostContainsKeywordAndNotInRedis() throws IOException {
        //given
        given(postIdRepository.isMember(any())).willReturn(false);

        //when
        String postId = "postId";
        String message = String.format("{\n" +
                "  \"id\": \"%s\",\n" +
                "  \"message\": \"%s\",\n" +
                "  \"link\": \"published link\",\n" +
                "  \"shares\": 1,\n" +
                "  \"likes\": 2,\n" +
                "  \"comments\": 3,\n" +
                "  \"createdTime\": \"2017-12-01T15:04:24Z\"\n" +
                "}", postId, postFilterKeyword);
        sqsQueueListener.receiveMessage(message);

        //then
        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> postTextCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendEmail(toCaptor.capture(), subjectCaptor.capture(), postTextCaptor.capture());

        assertEquals(destinationMail, toCaptor.getValue());
        assertEquals(String.format("New post containing \'%s\'", postFilterKeyword), subjectCaptor.getValue());
        assertEquals("published link", postTextCaptor.getValue());

        verify(postIdRepository).isMember(postId);
        verify(postIdRepository).savePostId(postId);
    }

    @Test
    public void shouldNotSendEmailWhenPostContainsKeywordAndInRedis() throws IOException {
        //given
        given(postIdRepository.isMember(any())).willReturn(true);

        //when
        String message = String.format("{\n" +
                "  \"id\": \"postId\",\n" +
                "  \"message\": \"%s\",\n" +
                "  \"link\": \"published link\",\n" +
                "  \"shares\": 1,\n" +
                "  \"likes\": 2,\n" +
                "  \"comments\": 3,\n" +
                "  \"createdTime\": \"2017-12-01T15:04:24Z\"\n" +
                "}", postFilterKeyword);
        sqsQueueListener.receiveMessage(message);

        //then
        verifyZeroInteractions(emailService);
    }

    @Test
    public void shouldNotSendEmailWhenPostDoesNotContainKeywordAndInRedis() throws IOException {
        //given
        given(postIdRepository.isMember(any())).willReturn(true);

        //when
        String message = String.format("{\n" +
                "  \"id\": \"postId\",\n" +
                "  \"message\": \"%s\",\n" +
                "  \"link\": \"published link\",\n" +
                "  \"shares\": 1,\n" +
                "  \"likes\": 2,\n" +
                "  \"comments\": 3,\n" +
                "  \"createdTime\": \"2017-12-01T15:04:24Z\"\n" +
                "}", postFilterKeyword.substring(1));
        sqsQueueListener.receiveMessage(message);

        //then
        verifyZeroInteractions(emailService);
    }

    @Test
    public void shouldNotSendEmailWhenPostDoesNotContainKeywordAndNotInRedis() throws IOException {
        //given
        given(postIdRepository.isMember(any())).willReturn(false);

        //when
        String message = String.format("{\n" +
                "  \"id\": \"postId\",\n" +
                "  \"message\": \"%s\",\n" +
                "  \"link\": \"published link\",\n" +
                "  \"shares\": 1,\n" +
                "  \"likes\": 2,\n" +
                "  \"comments\": 3,\n" +
                "  \"createdTime\": \"2017-12-01T15:04:24Z\"\n" +
                "}", postFilterKeyword.substring(1));
        sqsQueueListener.receiveMessage(message);

        //then
        verifyZeroInteractions(emailService);
    }

}