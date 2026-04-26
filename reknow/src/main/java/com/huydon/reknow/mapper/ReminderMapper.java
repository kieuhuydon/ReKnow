package com.huydon.reknow.mapper;

import com.huydon.reknow.dto.reminder.ReminderRequest;
import com.huydon.reknow.dto.reminder.ReminderResponse;
import com.huydon.reknow.entity.Reminder;
import org.mapstruct.*;

@Mapper(componentModel= "spring")
public interface ReminderMapper {

    Reminder toEntity(ReminderRequest req);

    @Mapping(source = "book.id", target = "bookId")
    ReminderResponse toResponse (Reminder reminder);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReminder (ReminderRequest req, @MappingTarget Reminder reminder );
}
