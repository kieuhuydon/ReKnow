package com.huydon.reknow.service;

import com.huydon.reknow.dto.reminder.ReminderRequest;
import com.huydon.reknow.dto.reminder.ReminderResponse;

public interface ReminderService {
    ReminderResponse createReminder (ReminderRequest request);

    ReminderResponse updateReminder(Long id, ReminderRequest request);

    ReminderResponse getReminder(); // lấy reminder user hiện tại

    void deleteReminder (Long id);

    ReminderResponse toggleReminder (Long id); // bật tắt reminder

}
