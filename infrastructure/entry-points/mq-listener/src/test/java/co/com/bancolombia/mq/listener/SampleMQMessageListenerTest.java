package co.com.bancolombia.mq.listener;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

public class SampleMQMessageListenerTest {

    @Mock
    private TextMessage textMessage;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processTest() throws JMSException {
        when(textMessage.getText()).thenReturn("message");

        SampleMQMessageListener sampleMQMessageListener = new SampleMQMessageListener();

        StepVerifier.create(sampleMQMessageListener.process(textMessage)).verifyComplete();
    }
}
