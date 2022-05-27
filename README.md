# EzyChat



1) Các công nghệ sử dụng:

a) Client: 
- Ezyfox Server Android SDK
- Retrofit
- SqliteDB

b) Server:
- Springboot
- EzyFox Socket Server
- MongoDB

2) Các chức năng:
- Đăng nhập: bằng tài khoản mật khẩu, bằng tài khoản google
- Đăng ký
- Lấy danh sách liên hệ
- Lấy danh sách liên hệ đang online
- Tìm kiếm người liên hệ mới
- Chat
- Lưu log chat tin nhắn ở local, chỉ gửi request lấy tin nhắn mới để tăng khả năng truy xuất và giảm tải cho server
- Mini game "nối từ" bằng tiếng anh:( Ký tự đầu tiên của từ tiếp theo phải trùng với ký tự cuối cùng của từ trước đó. Ví dụ: home => eat => thin,...)
    - Command "/game": bắt đầu game
    - Mỗi từ sẽ được validate qua api từ điển: https://dictionaryapi.dev/
    - Command "/end": đầu hàng khi muốn kết thúc game sớm.
