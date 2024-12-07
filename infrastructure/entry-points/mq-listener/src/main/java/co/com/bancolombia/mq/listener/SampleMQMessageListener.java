package co.com.bancolombia.mq.listener;

import co.com.bancolombia.commons.jms.mq.MQListener;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class SampleMQMessageListener {
    private final Timer timer = Metrics.timer("mq_receive_message", "operation", "my-operation"); // TODO: Change operation name
    // private final SampleUseCase useCase;

    private final Logger loggerFactory = LoggerFactory.getLogger(SampleMQMessageListener.class);
    // For fixed queues
    @MQListener("${commons.jms.input-queue}")
    public Mono<Void> process(Message message) throws JMSException {
        timer.record(System.currentTimeMillis() - message.getJMSTimestamp(), TimeUnit.MILLISECONDS);
        String text = ((TextMessage) message).getText();
        loggerFactory.info("Received message: {}", text);
        return Mono.empty();
    }
}
