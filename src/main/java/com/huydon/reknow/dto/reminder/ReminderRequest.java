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

    @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "08:00:00", description = "Format: HH:mm:ss")
    @NotNull (message = " SendTime is required ")
    private LocalTime sendTime; // giờ gửi mỗi ngày

}
