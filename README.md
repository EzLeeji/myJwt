#JWT 서버 만들기

### 회원가입 권한 : ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
POST http://localhost:8081/user
Content-Type: application/json

{
"username": "user1234",
"password": "1234",
"roles": "ROLE_USER"
}

### 로그인 시도
POST http://localhost:8081/login
Content-Type: application/json

{
"username": "user1234",
"password": "1234"
}

### user 권한접근하기
GET http://localhost:8081/api/v1/user/

헤더에 토큰 추가.
<br>
Authorization:

### manager 권한접근하기
GET http://localhost:8081/api/v1/manager/

헤더에 토큰 추가.
<br>
Authorization:

### admin 권한접근하기
GET http://localhost:8081/api/v1/admin/

헤더에 토큰 추가.
<br>
Authorization: 

### 로그아웃
####GET http://localhost:8081/logout

