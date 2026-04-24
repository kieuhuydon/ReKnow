package com.huydon.reknow.mapper;

import com.huydon.reknow.dto.reminder.ReminderRequest;
import com.huydon.reknow.dto.reminder.ReminderResponse;
import com.huydon.reknow.entity.Reminder;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel= "spring")
public interface ReminderMapper {
    Reminder toEntity(ReminderRequest req);

    ReminderResponse toResponse (Reminder reminder);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReminder (ReminderRequest req, @MappingTarget Reminder reminder );
}
