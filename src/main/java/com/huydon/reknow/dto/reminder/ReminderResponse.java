package com.huydon.reknow.dto.reminder;

import com.huydon.reknow.entity.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponse {
    private Long id;
    private Type type;
    private Long bookId; // dùng bookI gọn và không lộ thông tin nhạy cảm

    @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "08:00:00", description = "Format: HH:mm:ss")
    private LocalTime sendTime;
    private Boolean active;
}
