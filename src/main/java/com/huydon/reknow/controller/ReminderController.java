package com.huydon.reknow.controller;

import com.huydon.reknow.common.response.ApiResponse;
import com.huydon.reknow.dto.reminder.ReminderRequest;
import com.huydon.reknow.dto.reminder.ReminderResponse;
import com.huydon.reknow.service.ReminderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reminder")
@RequestMapping("/api/reminders")
public class ReminderController {
    private final ReminderService reminderService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReminderResponse>> createReminder(
            @RequestBody @Valid ReminderRequest req
            ){
        ReminderResponse reminder = reminderService.createReminder(req);
        ApiResponse<ReminderResponse> res = ApiResponse.success("Create Reminder successfully ", reminder);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

    //get api/reminders
    @GetMapping
    public ResponseEntity<ApiResponse<ReminderResponse>> getReminder(){
        ReminderResponse reminder = reminderService.getReminder();
        ApiResponse<ReminderResponse> res = ApiResponse.success("Get Reminder successfully ", reminder);

        return ResponseEntity.ok(res);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReminderResponse>> updateReminder(
            @PathVariable Long id, @RequestBody  @Valid ReminderRequest req
    ){
        ReminderResponse reminder = reminderService.updateReminder(id, req);
        ApiResponse<ReminderResponse> res = ApiResponse.success("Update Reminder successfully ", reminder);

        return ResponseEntity.ok(res);

    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<ReminderResponse>> toggleReminder(
            @PathVariable Long id
    ){
        ReminderResponse reminder = reminderService.toggleReminder(id);
        ApiResponse<ReminderResponse> res = ApiResponse.success("Update Status reminder successfully ", reminder);

        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReminder(@PathVariable Long id){
        reminderService.deleteReminder(id);
        ApiResponse<Void> res = ApiResponse.success("Delete reminder successfully ");
        return ResponseEntity.ok(res);

    }
}
