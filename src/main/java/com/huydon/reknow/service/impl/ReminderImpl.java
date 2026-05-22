package com.huydon.reknow.service.impl;

import com.huydon.reknow.dto.reminder.ReminderRequest;
import com.huydon.reknow.dto.reminder.ReminderResponse;
import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Reminder;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.entity.enums.Type;
import com.huydon.reknow.exception.AppException;
import com.huydon.reknow.exception.ResourceNotFoundException;
import com.huydon.reknow.mapper.ReminderMapper;
import com.huydon.reknow.repository.BookRepository;
import com.huydon.reknow.repository.ReminderRepository;
import com.huydon.reknow.repository.UserRepository;
import com.huydon.reknow.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements ReminderService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReminderMapper reminderMapper;


    @Override
    public ReminderResponse createReminder(ReminderRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        // nếu user có reminder
        boolean existed = reminderRepository.findByUserId(user.getId()).isPresent();

        if(existed){
            throw new AppException(400, "Reminder already exists");
        }


        Book book = null;
        if(request.getType().equals(Type.BY_BOOK)){
            Long bookId = request.getBookId();
            if(bookId == null){
                throw new AppException(400, "BookId required for BY_BOOK type");
            }
            book = bookRepository.findByIdAndDeletedAtIsNull(bookId)
                    .orElseThrow(()->new ResourceNotFoundException("Book not found"));
            if(!book.getUser().getEmail().equals(user.getEmail())) {
                throw new AppException(403, "Forbidden");
            }
        }

        Reminder reminder = Reminder.builder()
                .user(user)
                .book(book)
                .type(request.getType())
                .active(true)
                .sendTime(request.getSendTime())
                .build();
        //lastSentNoteId, lastSenAt mặc định null


        Reminder savedReminder = reminderRepository.save(reminder);

        return reminderMapper.toResponse(savedReminder);
    }

    @Override
    public ReminderResponse updateReminder(Long id, ReminderRequest request) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Reminder is not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // kiểm tra quyền
        if(!reminder.getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }
        // reset khi type thay đổi
        if(request.getType() != null && !request.getType().equals(reminder.getType())) {
            reminder.setLastSentAt(null);
            reminder.setLastSentNoteId(null);
        }

        Type finalType = request.getType() != null ? request.getType() : reminder.getType();

        if(finalType == Type.BY_BOOK){
            Long bookId =null;
            if(request.getBookId() != null){
                bookId =request.getBookId();
            }else{
                bookId = reminder.getBook().getId();
            }
            // BY_BOOK bắt buộc phải có bookId
            if(bookId == null) {
                throw new AppException(400, "BookId is required for BY_BOOK type");
            }
            // Tìm book trong DB
            Book book = bookRepository.findByIdAndDeletedAtIsNull(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

            // Kiểm tra book có thuộc user hiện tại không
            if(!book.getUser().getEmail().equals(email)) {
                throw new AppException(403, "Forbidden");
            }

            // Set book vào reminder
            reminder.setBook(book);


        }else{
            // nếu update sentAt thì mapper làm rồi
            reminder.setBook(null);
        }


         reminderMapper.updateReminder(request, reminder);
         Reminder saved = reminderRepository.save(reminder);

        return reminderMapper.toResponse(saved );
    }

    @Override
    public ReminderResponse getReminder() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));

        Reminder reminder = reminderRepository.findByUserId(user.getId())
                .orElseThrow(()->new ResourceNotFoundException("Reminder is not found"));

        return reminderMapper.toResponse(reminder);

    }

    @Override
    public void deleteReminder(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Reminder is not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));

        // kiểm tra quyền
        if(!reminder.getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }

        reminderRepository.delete(reminder);


    }

    @Override
    public ReminderResponse toggleReminder(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Reminder is not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // kiểm tra quyền

        if(!reminder.getUser().getEmail().equals(email)){
            throw new AppException(403, "Forbidden");
        }
        reminder.setActive(!reminder.getActive()); // !true

        Reminder saved = reminderRepository.save(reminder);

        return reminderMapper.toResponse(saved);
    }


}
