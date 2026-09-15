package com.yusuf.taskmanagement.notificationservice.event;

import java.time.Instant;

public record NotificationEvent(
        String eventId,
        String eventType,
        Instant occurredAt,
        String notificationId,
        String taskId,
        String userId,
        String message
) {
}