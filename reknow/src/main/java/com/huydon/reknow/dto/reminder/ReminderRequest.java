package com.huydon.reknow.dto.reminder;

import com.huydon.reknow.entity.enums.Type;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequest {
    @NotNull( message = "Type is required")
    private Type type;

    private Long bookId;

    @NotNull (message = " SendTime is required ")
    private LocalTime sendTime; // giờ gửi mỗi ngày

}
