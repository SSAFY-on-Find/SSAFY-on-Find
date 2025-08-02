package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record MergePubData(
        NotificationMsgDto publisher,
        MergeTargetDto subscriber
) {
}
