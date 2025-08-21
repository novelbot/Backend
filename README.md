# 🤖 NovelBot API

AI 기반 웹소설 상호작용 플랫폼의 백엔드 서비스입니다. 사용자가 웹소설과 대화하며 스토리에 참여할 수 있는 혁신적인 경험을 제공합니다.

## 🌟 주요 기능

### 📚 웹소설 관리
- 웹소설 등록, 조회, 수정, 삭제
- 표지 이미지 업로드 (Google Cloud Storage)
- 웹소설 검색 기능
- 에피소드 관리

### 💬 AI 채팅 시스템
- 웹소설 캐릭터와의 실시간 대화
- WebSocket 기반 스트리밍 응답
- 채팅방 생성 및 관리
- 질문/답변 히스토리 저장

### 👤 사용자 관리
- JWT 기반 인증/인가
- 회원가입, 로그인, 로그아웃
- 사용자 정보 관리
- 독서 진행도 추적

### 💰 구매 시스템
- 에피소드 구매 기능
- 구매 내역 관리
- 결제 상태 추적

## 🛠 기술 스택

### Backend Framework
- **Spring Boot 3.5.3** - 메인 프레임워크
- **Java 17** - 개발 언어
- **Gradle** - 빌드 도구

### Database & Storage
- **MySQL** - 메인 데이터베이스
- **Redis** - 세션 관리 및 캐싱
- **H2** - 테스트 환경
- **Google Cloud Storage** - 파일 저장소

### Security & Authentication
- **Spring Security** - 보안 프레임워크
- **JWT (JSON Web Token)** - 인증 토큰
- **BCrypt** - 비밀번호 암호화

### Communication
- **WebSocket** - 실시간 통신
- **WebFlux** - 비동기 HTTP 클라이언트
- **Apache POI** - Excel 파일 처리

### Documentation & Testing
- **OpenAPI/Swagger** - API 문서화
- **JUnit 5** - 단위 테스트
- **Testcontainers** - 통합 테스트
- **JaCoCo** - 테스트 커버리지

## 🚀 빠른 시작

### 필수 요구사항
- Java 17 이상
- MySQL 8.0 이상
- Redis 6.0 이상
- Google Cloud Storage 계정 (파일 업로드용)

### 1. 저장소 복제
```bash
git clone [repository-url]
cd novelBot/Backend
```

### 2. 환경 설정
`src/main/resources/application-local.properties` 파일을 생성하고 다음 내용을 추가:

```properties
# 데이터베이스 설정
spring.datasource.url=jdbc:mysql://localhost:3306/novelbot
spring.datasource.username=your_username
spring.datasource.password=your_password

# Redis 설정
spring.data.redis.host=localhost
spring.data.redis.port=6379

# JWT 설정
jwt.secret=your-secret-key
jwt.expiration=86400000

# Google Cloud Storage 설정
gcs.bucket-name=your-bucket-name
gcs.credentials-path=path/to/your/service-account.json

# AI 서버 설정
ai.server.url=http://your-ai-server-url
```

### 3. 데이터베이스 설정
MySQL에 `novelbot` 데이터베이스를 생성:
```sql
CREATE DATABASE novelbot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. 애플리케이션 실행

#### 개발 환경 실행
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### 테스트 실행
```bash
./gradlew test
```

#### 빌드
```bash
./gradlew build
```

### 5. API 문서 확인
애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## 📡 API 엔드포인트

### 🔐 인증 (Authentication)
- `POST /auth/login` - 로그인
- `POST /auth/logout` - 로그아웃
- `POST /auth/signup` - 회원가입

### 📚 웹소설 (Novels)
- `GET /novels` - 웹소설 목록 조회
- `GET /novels/{id}` - 웹소설 상세 조회
- `GET /novels/search?title={title}` - 웹소설 검색
- `POST /novels` - 웹소설 등록
- `PUT /novels/{id}` - 웹소설 수정
- `DELETE /novels/{id}` - 웹소설 삭제
- `POST /novels/{id}/cover` - 표지 이미지 업로드

### 📖 에피소드 (Episodes)
- `GET /episodes/novel/{novelId}` - 특정 웹소설의 에피소드 목록
- `GET /episodes/{id}` - 에피소드 상세 조회
- `POST /episodes` - 에피소드 등록

### 💬 채팅 (Chat)
- `GET /chatrooms` - 채팅방 목록 조회
- `POST /chatrooms` - 채팅방 생성
- `DELETE /chatrooms/{id}` - 채팅방 삭제
- `GET /chatrooms/novels` - 채팅방 소설 목록
- `GET /chatrooms/novel/{novelId}` - 특정 소설의 채팅방 목록

### 🤖 AI 질의응답
- `POST /api/query` - AI에게 질문하기
- WebSocket: `/ws` - 실시간 스트리밍 응답

### 💰 구매 (Purchase)
- `GET /purchases` - 구매 내역 조회
- `POST /purchases` - 에피소드 구매

### 👤 사용자 (User)
- `GET /users/profile` - 프로필 조회
- `PUT /users/profile` - 프로필 수정

## 🏗 프로젝트 구조

```
src/main/java/com/novelbot/api/
├── config/          # 설정 클래스
├── controller/      # REST API 컨트롤러
├── domain/          # JPA 엔티티
├── dto/             # 데이터 전송 객체
├── mapper/          # DTO 매퍼
├── repository/      # 데이터 접근 계층
├── service/         # 비즈니스 로직
└── utility/         # 유틸리티 클래스
```

## 🔧 개발 도구

### 테스트 실행
```bash
# 전체 테스트 실행
./gradlew test

# 테스트 커버리지 보고서 생성
./gradlew jacocoTestReport

# AI 서버 연동 테스트
./gradlew runAITest
```

### 코드 품질
- **JaCoCo**: 테스트 커버리지 측정
- **Spring Boot Actuator**: 애플리케이션 모니터링
- **Lombok**: 보일러플레이트 코드 제거

## 🌐 배포

### Docker 배포
```bash
# Docker 이미지 빌드
docker build -t novelbot-api .

# 컨테이너 실행
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod novelbot-api
```

### 프로덕션 빌드
```bash
./gradlew bootJar
java -jar -Dspring.profiles.active=prod build/libs/novelbot-api-0.0.1-SNAPSHOT.jar
```

## 🔒 보안 고려사항

- JWT 토큰 기반 인증
- CORS 설정 적용
- SQL Injection 방지 (JPA/MyBatis)
- XSS 공격 방지
- 파일 업로드 보안

## 📊 모니터링

Spring Boot Actuator를 통해 다음 메트릭을 모니터링할 수 있습니다:
- `/actuator/health` - 애플리케이션 상태
- `/actuator/metrics` - 성능 메트릭
- `/actuator/info` - 애플리케이션 정보

## 🤝 기여 가이드

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 지원

문의사항이 있으시면 다음으로 연락해주세요:
- 이메일: [earthenergy-1128@naver.com]
- GitHub Issues: [https://github.com/novelbot/Backend/issues]

---

💡 **NovelBot**은 AI와 웹소설의 만남으로 새로운 독서 경험을 제공합니다!