package com.yusuf.taskmanagement.notificationservice.consumer;

import com.yusuf.taskmanagement.notificationservice.event.NotificationEvent;
import com.yusuf.taskmanagement.notificationservice.event.NotificationEventPublisher;
import com.yusuf.taskmanagement.notificationservice.event.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventListener {

    private final NotificationEventPublisher notificationEventPublisher;

    @KafkaListener(
            topics = "task-events",
            groupId = "notification-service-group"
    )
    public void consume(TaskEvent taskEvent) {
        String message = createMessage(taskEvent);

        if (message == null) {
            log.warn("Bilinmeyen task event türü: {}", taskEvent.eventType());
            return;
        }

        NotificationEvent notificationEvent = new NotificationEvent(
                UUID.randomUUID().toString(),
                "NOTIFICATION_CREATED",
                Instant.now(),
                UUID.randomUUID().toString(),
                taskEvent.taskId(),
                taskEvent.userId(),
                message
        );

        notificationEventPublisher.publish(notificationEvent);

        log.info(
                "Bildirim üretildi. taskId={}, eventType={}",
                taskEvent.taskId(),
                taskEvent.eventType()
        );
    }

    private String createMessage(TaskEvent taskEvent) {
        return switch (taskEvent.eventType()) {
            case "TASK_CREATED" ->
                    "Task oluşturuldu: " + taskEvent.title();
            case "TASK_UPDATED" ->
                    "Task güncellendi: " + taskEvent.title();
            case "TASK_DELETED" ->
                    "Task silindi: " + taskEvent.title();
            default -> null;
        };
    }
}