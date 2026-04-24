package com.huydon.reknow.scheduler;

import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Note;
import com.huydon.reknow.repository.BookRepository;
import com.huydon.reknow.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrashScheduler {
    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;

    @Scheduled(cron = "0 0 0 * * ?") // giây phút giờ ngày tháng thứ
    @Transactional
    public void permanentDeleteOldTrash(){
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(10);

        List<Note> notes = noteRepository.findNotesToDelete(cutoffDate);

        noteRepository.deleteAll(notes);

        List<Book> books = bookRepository.findBooksToDelete(cutoffDate);

        bookRepository.deleteAll(books);

    }
}
