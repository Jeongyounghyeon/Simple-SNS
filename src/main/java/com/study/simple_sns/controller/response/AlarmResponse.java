package com.study.simple_sns.controller.response;

import com.study.simple_sns.model.Alarm;
import com.study.simple_sns.model.AlarmArgs;
import com.study.simple_sns.model.AlarmType;
import com.study.simple_sns.model.User;
import com.study.simple_sns.model.entity.AlarmEntity;
import lombok.Value;

import java.sql.Timestamp;

@Value
public class AlarmResponse {
    Integer id;
    AlarmType alarmType;
    AlarmArgs alarmArgs;
    String text;
    Timestamp registeredAt;
    Timestamp updatedAt;
    Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
