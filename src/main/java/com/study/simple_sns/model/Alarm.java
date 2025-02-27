package com.study.simple_sns.model;

import com.study.simple_sns.model.entity.AlarmEntity;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class Alarm {
    Integer id;
    AlarmType alarmType;
    AlarmArgs args;
    Timestamp registeredAt;
    Timestamp updatedAt;
    Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
                entity.getId(),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
