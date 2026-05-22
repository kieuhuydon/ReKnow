package com.huydon.reknow.config;

import com.huydon.reknow.entity.Book;
import com.huydon.reknow.entity.Note;
import com.huydon.reknow.entity.Reminder;
import com.huydon.reknow.entity.User;
import com.huydon.reknow.entity.enums.Provider;
import com.huydon.reknow.entity.enums.Role;
import com.huydon.reknow.entity.enums.Status;
import com.huydon.reknow.entity.enums.Type;
import com.huydon.reknow.repository.BookRepository;
import com.huydon.reknow.repository.NoteRepository;
import com.huydon.reknow.repository.ReminderRepository;
import com.huydon.reknow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReminderRepository reminderRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.existsByEmail("admin@reknow.com")) return;

        // ── ADMIN ──────────────────────────────
        userRepository.save(User.builder()
                .name("Admin")
                .email("admin@reknow.com")
                .password(passwordEncoder.encode("Admin@123"))
                .role(Role.ADMIN).status(Status.ACTIVE)
                .provider(Provider.LOCAL).build());

        // ── USER 1 ─────────────────────────────
        User user1 = userRepository.save(User.builder()
                .name("Nguyễn Văn A")
                .email("user1@reknow.com")
                .password(passwordEncoder.encode("User@123"))
                .role(Role.USER).status(Status.ACTIVE)
                .provider(Provider.LOCAL).build());

        // ── USER 2 ─────────────────────────────
        User user2 = userRepository.save(User.builder()
                .name("Trần Thị B")
                .email("user2@reknow.com")
                .password(passwordEncoder.encode("User@123"))
                .role(Role.USER).status(Status.ACTIVE)
                .provider(Provider.LOCAL).build());

        // ── SÁCH + GHI CHÚ của User 1 ──────────
        Book book1 = bookRepository.save(Book.builder()
                .user(user1).title("Đắc Nhân Tâm")
                .author("Dale Carnegie").pubYear(1936)
                .description("Nghệ thuật thu phục lòng người").build());

        noteRepository.saveAll(List.of(
                Note.builder().book(book1).isSent(false)
                        .content("Hãy thành thật quan tâm đến người khác.").build(),
                Note.builder().book(book1).isSent(false)
                        .content("Nụ cười không tốn tiền nhưng tạo ra nhiều giá trị.").build(),
                Note.builder().book(book1).isSent(false)
                        .content("Tên của một người là âm thanh ngọt ngào nhất với họ.").build()
        ));

        Book book2 = bookRepository.save(Book.builder()
                .user(user1).title("Atomic Habits")
                .author("James Clear").pubYear(2018)
                .description("Thay đổi tí hon, kết quả phi thường").build());

        noteRepository.saveAll(List.of(
                Note.builder().book(book2).isSent(false)
                        .content("Bạn không tăng lên ngang tầm mục tiêu, bạn tụt xuống ngang tầm hệ thống.").build(),
                Note.builder().book(book2).isSent(false)
                        .content("Thói quen là lãi suất kép của sự tự cải thiện.").build(),
                Note.builder().book(book2).isSent(false)
                        .content("Mỗi hành động bạn thực hiện là một lá phiếu cho con người bạn muốn trở thành.").build()
        ));

        // ── REMINDER của User 1 ────────────────
        reminderRepository.save(Reminder.builder()
                .user(user1).type(Type.RANDOM)
                .active(true)
                .sendTime(LocalTime.of(8, 0)) // gửi lúc 8:00 sáng
                .build());

        // ── SÁCH + GHI CHÚ của User 2 ──────────
        Book book3 = bookRepository.save(Book.builder()
                .user(user2).title("Nhà Giả Kim")
                .author("Paulo Coelho").pubYear(1988)
                .description("Hành trình theo đuổi vận mệnh cá nhân").build());

        noteRepository.saveAll(List.of(
                Note.builder().book(book3).isSent(false)
                        .content("Khi bạn muốn điều gì đó, cả vũ trụ sẽ hợp lực giúp bạn đạt được nó.").build(),
                Note.builder().book(book3).isSent(false)
                        .content("Đừng bỏ cuộc khi đang trên đường đến vận mệnh của mình.").build()
        ));
    }
}
