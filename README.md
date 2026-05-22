# 📚 ReKnow

> **Lưu ghi chú sách — Nhận nhắc nhở hằng ngày qua email**

ReKnow giúp bạn lưu lại những ghi chú, trích dẫn hay từ sách đã đọc, sau đó tự động gửi email nhắc nhở mỗi ngày để bạn ôn lại kiến thức.

---

## ✨ Tính năng chính

- **Quản lý sách & ghi chú** — Tạo, chỉnh sửa, xóa sách và ghi chú theo từng cuốn
- **Thùng rác thông minh** — Soft delete, khôi phục dễ dàng, tự động xóa cứng sau 10 ngày
- **Email Reminder** — Tự động gửi ghi chú ngẫu nhiên hoặc theo thứ tự mỗi ngày
- **Xác thực bảo mật** — JWT Access Token + Refresh Token
- **Quên mật khẩu** — Reset qua email với token UUID có thời hạn 15 phút
- **Phân quyền** — Role USER / ADMIN với Spring Security
- **Admin Panel** — Quản lý người dùng, khóa/mở tài khoản

---

## 🛠 Tech Stack

## 🛠 Tech Stack

| Layer | Công nghệ |
| --- | --- |
| Framework | Spring Boot 3.x |
| Database | PostgreSQL |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA (Hibernate) |
| Mapper | MapStruct |
| Template | Thymeleaf (email) |
| API Docs | Swagger |
| Build | Maven |

---

## 🚀 Hướng dẫn chạy local

### Yêu cầu
- Java 17+
- PostgreSQL 14+
- Maven 3.8+
- Tài khoản Gmail (để gửi email)

### Các bước

**1. Clone repo**
```bash
git clone https://github.com/kieuhuydon/ReKnow.git
cd reknow
```

**2. Tạo database**
```sql
CREATE DATABASE reknow;
```

**3. Cấu hình môi trường**
```bash
cp src/main/resources/application-example.yml src/main/resources/application.yml
```
Mở `application.yml` và điền thông tin thực tế (xem hướng dẫn trong file).

**4. Chạy ứng dụng**
```bash
mvn spring-boot:run
```

**5. Truy cập Swagger UI**
```
http://localhost:8080/swagger-ui/index.html
```

## 🧪 Tài khoản test

| Role  | Email            | Password  |
|-------|------------------|-----------|
| ADMIN | admin@reknow.com | Admin@123 |
| USER  | user1@reknow.com | User@123  |
| USER  | user2@reknow.com | User@123  |

> Dữ liệu mẫu được tạo tự động khi app khởi động lần đầu.  
> **Lưu ý:** Chức năng quên mật khẩu và email reminder yêu cầu email thật.  
> Để test 2 tính năng này, bạn cần thay email trong `config/DataInitializer.java`  
> bằng email thật của bạn trước khi chạy app — data sách/ghi chú vẫn được tạo kèm theo.

## 📖 API Documentation

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Tóm tắt endpoints

| Module | Endpoint | Mô tả |
|---|---|---|
| **Auth** | `POST /api/auth/register` | Đăng ký |
| | `POST /api/auth/login` | Đăng nhập → trả Access + Refresh Token |
| | `POST /api/auth/refresh-token` | Lấy Access Token mới |
| | `POST /api/auth/logout` | Đăng xuất, thu hồi Refresh Token |
| **User** | `GET /api/users/me` | Xem thông tin cá nhân |
| | `PUT /api/users/me/password` | Đổi mật khẩu |
| | `POST /api/users/forgot-password` | Gửi email reset mật khẩu |
| **Book** | `POST /api/books` | Tạo sách mới |
| | `GET /api/books` | Danh sách + tìm kiếm + phân trang |
| | `PUT /api/books/{id}` | Cập nhật |
| | `DELETE /api/books/{id}` | Xóa mềm |
| **Note** | `POST /api/notes/books/{bookId}` | Tạo ghi chú |
| | `GET /api/notes` | Danh sách + filter theo sách |
| **Trash** | `GET /api/trash/books` | Xem thùng rác |
| | `PATCH /api/trash/books/{id}/restore` | Khôi phục |
| | `DELETE /api/trash/books/{id}` | Xóa vĩnh viễn |
| **Reminder** | `POST /api/reminders` | Tạo nhắc nhở |
| | `PATCH /api/reminders/{id}/toggle` | Bật/tắt nhắc nhở |
| **Admin** | `GET /api/admin/users` | Danh sách người dùng |
| | `PATCH /api/admin/users/{id}/toggle-status` | Khóa/mở tài khoản |

---

## 🗄 Database Schema

```
users (1) ──── (*) books
books (1) ──── (*) notes
users (1) ──── (1) reminders
users (1) ──── (1) refresh_tokens

```

**Các bảng chính:**

`users` — id, name, email, password, role, status, provider, created_at

`books` — id, user_id, title, author, published_year, description, deleted_at

`notes` — id, book_id, content, is_sent, deleted_at

`reminders` — id, user_id, book_id, type (RANDOM/BY_BOOK), send_time, last_sent_at

---

## ⚙️ Business Logic nổi bật

### 🔔 Email Reminder
```
EmailScheduler chạy mỗi phút để kiểm tra:
- RANDOM: chọn ngẫu nhiên 1 note có is_sent=false → gửi → đánh dấu is_sent=true
          Khi hết note → reset tất cả is_sent=false, bắt đầu lại
- BY_BOOK: gửi note tiếp theo sau last_sent_note_id theo thứ tự
           Khi hết note của sách đó → tự động chuyển sang RANDOM
- Chống gửi 2 lần/ngày: kiểm tra last_sent_at trước khi gửi
```

### 🗑 Soft Delete
```
- Xóa Book → tự động soft delete toàn bộ Note thuộc book đó
- TrashScheduler: quét mỗi đêm, xóa cứng các bản ghi đã soft delete > 10 ngày
```

### 🔐 JWT + Refresh Token
```
Access Token:  hết hạn sau 15 phút
Refresh Token: hết hạn sau 7 ngày, lưu trong DB
Logout:        đánh dấu refresh token revoked=true
```

---

## 🏗 Cấu trúc project

```
src/main/java/com/huydon/reknow/
├── controller/     # REST API layer
├── service/        # Business logic (interface + impl)
├── repository/     # Spring Data JPA
├── entity/         # JPA Entities + Enums
├── dto/            # Request / Response DTOs
├── mapper/         # MapStruct mappers
├── scheduler/      # EmailScheduler, TrashScheduler
├── exception/      # GlobalExceptionHandler
├── common/         # ApiResponse<T>, PageResponse<T>
└── config/         # Security, JWT, Swagger
```

---

## 🔒 Phân quyền

| Role | Endpoint được phép |
|---|---|
| Public | `/api/auth/**`, `/api/users/forgot-password`, `/api/users/reset-password` |
| USER | `/api/books/**`, `/api/notes/**`, `/api/reminders/**`, `/api/users/me/**` |
| ADMIN | `/api/admin/**` |

---

## 📝 Ghi chú

- `application.yml` đã được gitignore — dùng `application-example.yml` để cấu hình
- Tài khoản Admin mặc định tạo qua `DataInitializer.java` khi app khởi động lần đầu
