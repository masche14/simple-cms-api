# 맑은기술 신입 백엔드 개발자 과제 - CMS API 구현

Spring Boot 기반으로 간단한 CMS(Content Management System) REST API를 구현한 프로젝트입니다.  
사용자 등록, 로그인/로그아웃, 콘텐츠 CRUD, 권한 제어, 예외 처리, Swagger 문서화를 포함하여 과제 요구사항을 구현했습니다.

---

## 1. 개발 환경

- **Language**: Java 25
- **Framework**: Spring Boot 4.x
- **Database**: H2 Database (In-Memory)
- **ORM**: Spring Data JPA
- **Security**: Spring Security
- **API Documentation**: Swagger (Springdoc OpenAPI)
- **Build Tool**: Gradle

---

## 🚀 2. 프로젝트 실행 방법

### 1) 실행 환경 세팅 및 구동
1. 본 저장소를 Clone 하거나 Zip 파일을 다운로드하여 압축을 해제합니다.
2. IDE(IntelliJ 등)에서 프로젝트를 열고 **Java 25** 환경으로 SDK를 설정합니다.
3. 아래 메인 클래스를 실행합니다.
    - `src/main/java/com/malgn/Application.java`
4. 애플리케이션 실행 시 다음 SQL 파일이 자동으로 로드되어 테이블 생성 및 초기 테스트 데이터가 세팅됩니다.
    - `src/main/resources/db/sql/h2-schema.sql`
    - `src/main/resources/db/sql/h2-data.sql`
### 2) 테스트 계정 정보
초기 데이터(`h2-data.sql`)로 생성된 계정입니다.
| Role | ID | Password |
| :--- | :--- | :--- |
| **ADMIN** | `admin` | `1234` |
> ※ 비밀번호는 Spring Security의 BCrypt 방식으로 암호화되어 저장되어 있습니다.

### 3) API 테스트 및 DB 확인 방법
서버 실행 후 아래 세 가지 방법으로 프로젝트를 테스트하고 확인할 수 있습니다.

* **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - 전체 API 명세 확인 및 UI 기반 테스트가 가능합니다.
* **H2 DB Console**: [http://localhost:8080/h2-console/](http://localhost:8080/h2-console/)
    - **JDBC URL**: `jdbc:h2:mem:test` (User Name: `sa` / Password: 없음)
    - 웹 환경에서 생성된 DB 스키마 및 적재된 데이터를 직접 조회할 수 있습니다.
* **IntelliJ HTTP Client**: 프로젝트 루트에 포함된 `api-test.http` 파일을 통해 주요 API 시나리오(로그인, 생성, 조회, 수정, 삭제, 권한 검증 등)를 순차적으로 테스트할 수 있습니다.

---
## 📌 3. 주요 API 명세

| Method | Endpoint                      | Description     |
| :--- |:------------------------------|:----------------|
| `POST` | `/api/reg/insertUser`         | 사용자 등록          |
| `POST` | `/api/auth/login`             | 로그인 (인증)        |
| `GET` | `/api/contents`               | 콘텐츠 목록 조회 (페이징) |
| `GET` | `/api/contents/{id}`          | 콘텐츠 상세 조회       |
| `POST` | `/api/contents/createContent` | 콘텐츠 생성          |
| `PUT` | `/api/contents/update/{id}`   | 콘텐츠 수정          |
| `DELETE` | `/api/contents/delete/{id}`   | 콘텐츠 삭제          |

## 💡 4. 핵심 구현 내용

### 콘텐츠 CRUD 및 권한 관리
* **페이징 처리**: Spring Data JPA의 `Pageable`을 활용하여 콘텐츠 목록 조회 시 페이징 기능을 구현했습니다.
* **데이터 검증**: DTO와 `@Valid`를 활용하여 Title 길이 제한, 필수 값 검증 등 클라이언트 요청 데이터의 구조와 무결성을 검증합니다.
* **접근 제어 (Authorization)**: 서비스 계층에서 작성자 검증 로직을 직접 구현하여 아래와 같이 권한을 제어했습니다.
  | 기능 | 요구 권한 |
  | :--- | :--- |
  | **조회** | 모든 사용자 |
  | **생성** | 로그인 사용자 |
  | **수정/삭제** | 콘텐츠 **작성자 본인** 또는 **ADMIN** 권한 보유자 |

---
## 🔐 5. 인증 및 보안 구현

* **Spring Security 기반 로그인**: `UserDetailsService`를 상속 구현한 `UserService`를 구현하여 DB에 저장된 사용자 정보를 기반으로 인증을 수행합니다.
* **비밀번호 암호화**: `BCryptPasswordEncoder`를 통한 단방향 해시 암호화를 적용하여 보안을 강화했습니다.
* **Security 설정 분리**: 보안 설정을 `SecurityConfiguration`(메인 API)과 `H2DbSecurityConfiguration`(H2 콘솔), `ActuatorSecurityConfiguration`(모니터링) 등으로 목적에 맞게 분리 구성하여 가독성과 유지보수성을 향상시켰습니다.

---

## ⚙️ 6. 추가 구현 기능

* **Global Exception Handling**: `@RestControllerAdvice`를 활용하여 전역 예외 처리를 구현했습니다. 예외 발생 시 `ErrorResponseDTO`라는 일관된 응답 구조를 반환하여 API 사용 시 예외 응답을 예측 가능하게 설계했습니다.
* **ContentNotFoundException**: 별도의 클래스를 만들어, `404`에러에 대한 예외를 처리를 구현했습니다.
* **사용자 등록 기능**: `UserRegController`와`UserService`를 통해 사용자 등록을 처리합니다. 이 과정에서 비밀번호는 `BCryptPasswordEncoder`를 사용하여 암호화됩니다.
* **DTO 계층 분리**: 엔티티 객체가 API 스펙에 직접 노출되지 않도록 DTO 계층을 별도로 구성하여 API 스펙 안정성을 확보하고 엔티티 변경 시 API 영향을 최소화했습니다.
* **JPA Auditing**: `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy` 등을 적용하여 콘텐츠 생성 및 수정 시 작성자와 시간 정보를 자동 기록합니다.

---
## 🧱 7. 프로젝트 구조

```text
com.malgn
 ├─ auth                 # 인증 사용자 정보 및 권한(Role) 관련 클래스
 ├─ configure            # 애플리케이션/Swagger/Security 설정 클래스
 │   └─ security         # SecurityFilterChain, H2 Console, Actuator 보안 설정
 ├─ controller           # 사용자 등록, 인증, 콘텐츠 관련 REST API Controller
 ├─ dto                  # 요청/응답 DTO
 │   ├─ content          # 콘텐츠 요청/응답 DTO
 │   └─ user             # 사용자 로그인/등록/조회 DTO
 ├─ entity               # JPA Entity (Content, User)
 ├─ exception            # 커스텀 예외 및 전역 예외 처리
 ├─ repository           # Spring Data JPA Repository
 ├─ service              # 서비스 인터페이스
 │   └─ impl             # 비즈니스 로직 구현체
 └─ util                 # BCrypt 해시 생성 등 유틸리티 클래스
```
---
## 🔧 8. 트러블슈팅 및 최적화

**QueryDSL 의존성 충돌 해결 및 경량화**
- 초기 템플릿에 QueryDSL 의존성이 포함되어 있었으나, 본 과제 요구사항은 기본 CRUD 및 페이징 수준이므로 Spring Data JPA만으로 충분하다고 판단했습니다.
- 최신 Spring Boot 환경에서 Swagger(Springdoc) 연동 시 QueryDSL 관련 Bean 생성 충돌 문제(`NoClassDefFoundError`)가 발생하여, 불필요한 QueryDSL 의존성을 과감히 제거하고 프로젝트를 경량화하는 방향으로 트러블슈팅을 완료했습니다.

**최신 Spring Boot YAML 설정 대응**
- H2 DB Console 활성화 과정에서 최신 버전에 따른 Boolean 값 파싱(`on` -> `true`) 이슈 및 정적 리소스 경로 매핑 이슈를 디버깅하여 해결했습니다.

---
## 🤖 9. AI 도구 활용

본 과제 수행 과정에서 코드 구조 설계 및 트러블슈팅 과정에서 AI 도구(JetBrains AI Assistant, ChatGPT)를 참고 자료로 활용했습니다.
단, 최종 코드 작성, 로직 병합, 트러블슈팅 원인 파악 및 기능 검증은 직접 주도하여 개발을 진행했습니다.

---
*소중한 시간 내어 검토해 주셔서 감사합니다. 부족한 부분이 있다면 적극적으로 배우고 성장하겠습니다.*