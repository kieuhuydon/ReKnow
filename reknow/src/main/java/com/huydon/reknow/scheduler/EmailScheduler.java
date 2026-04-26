package com.huydon.reknow.scheduler;

import com.huydon.reknow.entity.Note;
import com.huydon.reknow.entity.Reminder;
import com.huydon.reknow.entity.enums.Type;
import com.huydon.reknow.repository.NoteRepository;
import com.huydon.reknow.repository.ReminderRepository;
import com.huydon.reknow.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor

public class EmailScheduler {

    private final ReminderRepository reminderRepository;
    private final NoteRepository noteRepository;
    private final EmailService emailService;

    @Scheduled(cron= "0 * * * * ?")
    @Transactional
    public void sendDailyReminder() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        List<Reminder> reminders = reminderRepository.findByActiveTrue();

        for(Reminder reminder: reminders){

            //if true -> nhảy qua reminder mới, false tiếp tục chạy code dưới
            if(reminder.getSendTime().isAfter(now)) continue;

            //check hôm nay gửi chưa -> true khi gửi rồi
            if(reminder.getLastSentAt() != null &&
            reminder.getLastSentAt().toLocalDate().equals(today)) continue;
            /*lần đâu gửi lastSent = null, đk 1 false thì không chạy điều kiện 2 luôn, chạy code ở dưới để gửi,
            nếu không có đk 1 gây crash gọi toLocalDate vơis null */

            Note note = selectNote(reminder);
            if(note == null) continue;

            emailService.sendReminderEmail(
                    reminder.getUser().getEmail(),
                    reminder.getUser().getName(),
                    note.getBook().getTitle(),
                    note.getContent()
            );

            updateReminderState(reminder, note);

        }


    }

    private Note selectNote(Reminder reminder){
        //Random

        if(reminder.getType().equals(Type.RANDOM)){
            List<Note> unsentNotes = noteRepository.
                    findByBook_UserIdAndIsSentFalseAndDeletedAtIsNull(reminder.getUser().getId());
            //note không có userId phải đi qua book

            if(unsentNotes.isEmpty()){
                //nếu hết note -> reset tất cả về chưa gửi
                noteRepository.resetIsSentByUserId(reminder.getUser().getId());

                unsentNotes = noteRepository.
                        findByBook_UserIdAndIsSentFalseAndDeletedAtIsNull(reminder.getUser().getId());
                if(unsentNotes.isEmpty()) return null; // user không có note nào
            }

            Random random = new Random();
            int randomIndex = random.nextInt(unsentNotes.size());

            return unsentNotes.get(randomIndex);

        }else{
            List<Note> allNotes = noteRepository.findAllByBookAndDeletedAtIsNullOrderByCreatedAtAsc(reminder.getBook());
            if(allNotes.isEmpty()) return null;

            Long lastId = reminder.getLastSentNoteId();

            if(lastId == null){
                return allNotes.get(0);
            }

            //tìm note gửi lần trước
            for (int i = 0; i < allNotes.size(); i++){
                if(allNotes.get(i).getId().equals(lastId)){
                    //a < b, a modulo b thì ra chính a
                    int nextIndex = i + 1;
                    if(nextIndex >= allNotes.size()){
                        //hết note reset reminder về random
                        reminder.setType(Type.RANDOM);
                        reminder.setLastSentNoteId(null);
                        reminderRepository.save(reminder);
                        return null;
                    }
                    return allNotes.get(nextIndex);

                }
            }



        }
        return null;

    }

    private void updateReminderState(Reminder reminder, Note note){
        //thay đổi lastSentAt, lastSentNoteId của Reminder
        reminder.setLastSentAt(LocalDateTime.now());

        if(reminder.getType().equals(Type.BY_BOOK)){
            reminder.setLastSentNoteId(note.getId());
        }else{
            note.setSent(true);
            noteRepository.save(note);
        }


        reminderRepository.save(reminder);

    }
}
