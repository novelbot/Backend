-- 테스트용 데이터 초기화
-- H2 데이터베이스에서 USER는 예약어이므로 백틱 사용

-- 테스트 사용자
INSERT INTO `user` (user_id, user_name, user_email, user_password, user_role, user_nickname, created_at) VALUES 
(1, 'testuser', 'test@example.com', '$2a$10$dummy.hash.for.test', 'USER', 'TestUser', CURRENT_TIMESTAMP);

-- 테스트 소설
INSERT INTO novel (novel_id, title, author, summary, genre, created_at) VALUES 
(1, '테스트 소설', '테스트 작가', 'WebSocket 테스트용 소설입니다.', 'FANTASY', CURRENT_TIMESTAMP);

-- 테스트 에피소드
INSERT INTO episode (episode_id, novel_id, episode_number, title, content, created_at) VALUES 
(1, 1, 1, '1화 - 시작', '주인공이 모험을 시작합니다.', CURRENT_TIMESTAMP),
(2, 1, 2, '2화 - 만남', '주인공이 동료를 만납니다.', CURRENT_TIMESTAMP),
(3, 1, 3, '3화 - 시련', '주인공이 시련에 직면합니다.', CURRENT_TIMESTAMP);

-- 테스트 구매 (에피소드 구매)
INSERT INTO purchase (purchase_id, user_id, novel_id, episode_id, purchased_at) VALUES 
(1, 1, 1, 1, CURRENT_TIMESTAMP),
(2, 1, 1, 2, CURRENT_TIMESTAMP);

-- 테스트 채팅방
INSERT INTO chatroom (chat_id, user_id, novel_id, chat_title, created_at) VALUES 
(1, 1, 1, '테스트 채팅방', CURRENT_TIMESTAMP);