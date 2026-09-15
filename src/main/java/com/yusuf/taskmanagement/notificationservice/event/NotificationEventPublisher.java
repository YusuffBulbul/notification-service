package com.yusuf.taskmanagement.notificationservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private static final String NOTIFICATION_EVENTS_TOPIC =
            "notification-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(NotificationEvent event) {
        kafkaTemplate.send(
                NOTIFICATION_EVENTS_TOPIC,
                event.taskId(),
                event
        );
    }
}