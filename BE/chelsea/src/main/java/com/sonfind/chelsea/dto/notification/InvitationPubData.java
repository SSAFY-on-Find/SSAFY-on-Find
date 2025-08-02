package com.sonfind.chelsea.dto.notification;

import lombok.Builder;

@Builder
public record InvitationPubData(
        NotificationMsgDto publisher,
        ApplicantDto subscriber
) {
}
