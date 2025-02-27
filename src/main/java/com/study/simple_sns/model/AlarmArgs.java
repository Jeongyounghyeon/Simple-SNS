package com.study.simple_sns.model;

import lombok.Value;

@Value
public class AlarmArgs {

    Integer fromUserId;
    Integer targetId;
}
