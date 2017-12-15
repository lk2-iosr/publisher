package iosr.faceboookapp.publisher.stats;

import iosr.faceboookapp.publisher.client.StatsClient;
import iosr.faceboookapp.publisher.email.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by Leszek Placzkiewicz on 15.12.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StatsPublisherTest {

    @MockBean
    private EmailService emailService;

    @MockBean
    private StatsClient statsClient;

    @Autowired
    private StatsPublisher statsPublisher;

    @Value("${destination.mail}")
    private String destinationMail;


    @Test
    public void shouldSendEmail() {
        //given
        Post post = new Post("p1", "m", "l", 1,1,1, "time");
        List<Post> postList = new ArrayList<>();
        postList.add(post);
        given(statsClient.getTopPostsByLikes()).willReturn(postList);
        given(statsClient.getTopPostsByComments()).willReturn(postList);
        given(statsClient.getTopPostsByShares()).willReturn(postList);

        //when
        statsPublisher.publishStats();

        //then
        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> postTextCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendEmail(toCaptor.capture(), any(), any());
        assertEquals(destinationMail, toCaptor.getValue());
    }

}