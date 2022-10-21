<img width="1395" alt="image" src="https://user-images.githubusercontent.com/56334513/196926026-9e3fc7b4-8295-4c80-b90c-cc56d0071fb8.png">

## 📌 &nbsp;프로젝트 개요
### 프로젝트 기간
- 2022년 9월 5일 ~ 2022년 10월 14일 (6주)
### 프로젝트 배경
- `메가바이트스쿨: K-Digital Training FE/BE 개발자 양성 과정 2기` 수강생과 `패스트캠퍼스 UI/UX 교육과정 3기` 수강생이 함께했습니다.
- `AI Park`는 본 프로젝트 파트너 기업입니다. AI Park는 AI 기술을 활용한 아바타 영상 제작을 서비스하고 있습니다.
    - (홈페이지: https://www.aipark.co.kr)
- 본 프로젝트에서는 AI Park 서비스를 모델로 하는 `Hi Avatar` 서비스의  웹페이지와 서버가 구축되었습니다.

### 기획 방향성
- `Hi Avatar`는 사용자가 더 쉽고 빠르게 영상을 제작할 수 있도록 돕는 서비스입니다.
    - 코로나19와 디지털 전환으로, 비대면 영상 커뮤니케이션의 수요는 날마다 증가하고 있습니다.
    - 그러나 영상 제작에는 많은 비용이 필요하고, 자주 제작하는 경우라면 부담은 더 늘어납니다.
- `Hi Avatar`는 이러한 불편을 해소하기 위해 기획되었습니다.
    - 쉽고 빠른 스크립트 편집 → 영상 제작 과정 편의성 향상
    - 실시간 아바타 영상 생성 → 빠른 결과물 확인 가능
    - 음성 모델, 아바타 자유롭게 변경 → 영상 수정 편의성 향상


<br>

## ✨ &nbsp;기술 스택
<div>
<img width="968" alt="image" src="https://user-images.githubusercontent.com/56334513/196927381-145830ec-8fed-47da-8f9e-d356c010134c.png">
</div>

<br>

### Version
|    **Java**     |             **11**             |
|:---------------:|:------------------------------:|
| **Spring boot** |           **2.7.3**            |
|    **Flask**    |           **2.0.2**            |
|   **AWS EC2**   | **Amazon Linux 2 Kernel 5.10** |


<br>

## 🖥 &nbsp;결과 화면
### 랜딩페이지 (최상단, 최하단)
- 이미지와 캐치프레이즈를 이용해 서비스를 빠르게 소개
- 각 섹션에 `지금 시작해보세요` 버튼을 배치하여 서비스 사용을 유도

![image](https://user-images.githubusercontent.com/93692733/197129448-3c8e4e16-c851-432b-b244-ba3deea61f82.png)
![image](https://user-images.githubusercontent.com/93692733/197129537-63991548-fab8-434a-ac59-52fc97cee314.png)
<br><br>
---
### 프로젝트 및 영상 히스토리 페이지
- 최근 생성한 프로젝트와 영상을 확인
- `프로젝트 생성` 버튼을 눌러 새로운 프로젝트 시작
- 기존 프로젝트를 눌러 마지막 작업 상태로부터 작업 시작
- 영상 위 툴바를 이용해 작은/큰 화면으로 재생 가능
- `Download` 버튼을 눌러 생성된 영상 다운로드

![image](https://user-images.githubusercontent.com/93692733/197132138-bb6be0a0-aa28-4b0b-a31a-7296d5d4a399.png)
<br><br>
---
### 텍스트 편집 페이지 (입력 전)
- `음성 파일 업로드하기`와 `텍스트로 입력하기` 중 스크립트 입력 옵션을 선택
    - `텍스트로 입력하기` 선택 시 텍스트 입력 modal 창 팝업
    - `음성 파일 업로드하기` 선택 시 사용자 음성파일 업로드 창 팝업

![image](https://user-images.githubusercontent.com/93692733/197133335-2ccaa4b4-797f-465b-8230-59936cb50ad7.png)
<br><br>
---
### 텍스트 입력 페이지 (modal)
- 줄글 텍스트를 복사 및 붙여넣기하여 영상에 사용할 스크립트 텍스트를 입력
- 텍스트는 마침표를 기준으로 분리되어 텍스트 편집 페이지에 문장별 컴포넌트를 생성

![image](https://user-images.githubusercontent.com/93692733/197133076-e56238f5-b8fa-407d-9370-eba4ecf2ed47.png)
<br><br>
---
### 텍스트 편집 페이지 (입력 후)
- 문장별 텍스트 편집, 문장 간 호흡 간격 조정 가능
- 새로운 문장 컴포넌트 추가, 기존 컴포넌트 삭제 가능
- 문장별 음성 미리듣기 가능
- 현재 작업을 진행 중인 문장을 전체 텍스트에서 하이라이트 처리
- 전체 텍스트 음성 미리듣기 및 다운로드 가능

![image](https://user-images.githubusercontent.com/93692733/197132873-2f788933-7925-4003-baaf-2a408d199aef.png)
<br><br>
---
### 텍스트 편집 페이지 (사이드 탭바)
- 현재 편집하고 있는 스크립트를 전체적으로 확인하는 `전체 텍스트` 탭바
- TTS(Text-To-Speech) 음성(성별, 언어, 음성 모델)을 선택하는 `음성 모델` 탭바
- 발화 속도, 톤(피치), 호흡 간격을 전체적으로 조정하는 `음성 옵션` 탭바'
- 위 3개 탭바를 텍스트 편집 페이지에 통합

![image](https://user-images.githubusercontent.com/93692733/197134945-ad3d78f3-dfcf-4833-ba94-5ac209c41ece.png)
<br><br>
---
### 아바타 선택 페이지
- 아바타 종류(얼굴), 아바타 스타일(의상, 주요 제스처, 자세 등), 영상 배경 선택 가능
- 선택한 옵션에 따른 미리보기 이미지 생성
- 이전 페이지(텍스트 편집)에서 선택한 음성 모델, 음성 옵션을 재확인 가능
- `완료` 버튼 클릭 시, 백엔드 서버에 영상 합성을 요청

![image](https://user-images.githubusercontent.com/93692733/197133741-a0898bec-910c-43ab-849a-3575bf692322.png)
<br><br>
---
<br>

## 🔧 &nbsp;서비스 아키텍처

![image](https://user-images.githubusercontent.com/56334513/197141325-8de26bab-b078-4d7a-a406-de046ca43610.png)

### EC2 인스턴스에 존재하는 4가지 컨테이너
**Spring Boot 컨테이너**
+ 메인 서버
+ 클라이언트와 통신하며 데이터 갱신 및 처리

**Flask 컨테이너**
+ 음성 파일 또는 영상 파일 생성을 담당하는 서버
+ Spring boot 서버는 비동기 처리 기능을 추가한 WebClient로 통신

**Redis 컨테이너**
+ jwt 토큰을 저장하기 위한 용도
    + accessToken: 중복 로그인 방지
    + refreshToken: accessToken 만료 시 재발급

**Jenkins 컨테이너**
+ CI/CD를 위한 툴
+ Declarative 기반 pipeline 구현

<br>

### CI/CD 과정

1. 각 팀원들이 작업을 완료한 후 **Pull Request** 작성
2. 팀장은 Pull Request를 확인하고 이상 없으면 수락
3. GitHub의 **webhook**으로 **Jenkins**에게 **push event** 알림
4. 5단계로 구성된 **Jenkins Pipeline**를 거침
    1. **git scm**으로 원격 레포지토리의 코드를 가져옴
    2. ec2 환경에 있는 `배포용 application.yml` 적용 후, **Gradle 빌드 실행**
    3. 생성된 JAR 파일로 Docker **이미지 빌드**
    4. 생성된 이미지를 Dockerhub로 **Push**
    5. Dockerhub로부터 이미지를 **Pull**하여 해당 이미지를 기반으로 **컨테이너 실행**
5. Pipeline를 거치면서 **실패**하거나 **마지막 단계까지 수행**하면 **Slack** 채널에 알림

<br>

## 📋 &nbsp;API 명세서
### 모든 API 공통 사항
<details>
<summary>반환 타입, 상태코드</summary>
<div markdown="1">

#### 반환 타입
- `Content-Type: applciation/json; charset=utf-8`

<br>

#### HTTP 상태 코드 (형식: code status)

- `200 OK`: 클라이언트 요청에 대한 성공
- `400 Bad Request`: 클라이언트 에러
- `401 Unauthorized`:  권한이 없는 사용자가 접근 시
- `500 Internal Server Error`: 서버 에러

<br>

#### 응답 상태 코드 타입

- **code**     `Number`
- **status**     `String`
<br>
<br>
</div>
</details>

<details>
<summary>Speed, Pitch, SentenceSpacing 값</summary>
<div markdown="1">

- 기존 값 → 화면에 보여지는 값
- 내부적인 값 → FE와 BE 통신 시 사용되는 값, DB에 저장되는 값

  
#### Speed, Pitch 값

| 기존 값 | -0.5 | -0.4 | -0.3 | -0.2 | -0.1 | 0 | 0.1 | 0.2 | 0.3 | 0.4 | 0.5 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 내부적인 값 | -5 | -4 | -3 | -2 | -1 | 0 | 1 | 2 | 3 | 4 | 5 |
- default: 0
- move: 0.1

<br>

#### SentenceSpacing 값

| 기존 값 | 0.0 | 0.1 | 0.2 | … | 1.0 | 2.0 | 3.0 | 4.0 | 5.0 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 내부적인 값 | 0 | 1 | 2 | … | 10 | 20 | 30 | 40 | 50 |
- default: 0.5
- move: 0.1

<br>
<br>
</div>
</details>

<br>

### 회원 인증 관련

<details>
<summary>일반 회원 가입</summary>
<div markdown="1">

#### 👉 `/sign-up`
- `POST` 방식
- 일반 회원가입을 위한 API 


- 회원가입 성공 시: 서버스 DB에 반영된 회원의 ID를 반환 → 상태코드 `200 OK`
- 회원가입 실패 시: 에러 메시지로 실패 이유를 전달 → 상태코드 `400 BAD REQUEST`

#### request
```json
{
    "id":"my_awesome_email@email.com",
    "password":"my_plaintext_password"
}
```

#### response
```json
{
    "data": {
			 "id":"my_awesome_email@email.com"
		},
    "code": 200,
    "status": "OK"
}
```

#### exception
```json
{
    "code": 400,
    "status": "BAD REQUEST",
    "error":"APPLICATION_INFO_INVALID",
    "message":"입력된 회원가입 정보가 올바르지 않습니다."
}

{
	"code": 400,
	"status": "BAD REQUEST",
    "error":"EMAIL_ADDRESS_NOT_VERIFIED",
    "message":"인증되지 않은 이메일 주소입니다."
}
```
<br>
<br>
</div>
</details>



<details>
<summary>일반 회원 가입 시, 아이디 중복 검증</summary>
<div markdown="1">


#### 👉 `/sign-up/check/duplicate-id`
- `POST` 방식
- 일반 회원가입을 진행할 때, 사용중인 아이디가 있는 지 중복 검증을 수행하는 API

#### request
```json
{
    "id":"my_awesome_but_common_email@email.com"
}
```

#### response
- isIdAvailable: true → 사용 가능한 아이디
- isIdAvailable: false → 이미 가입된 아이디
```json
{
    "data": {
            "isIdAvailable": true
    },
    "code": 200,
    "status": "OK"
}
```
<br>
<br>
</div>
</details>



<details>
<summary>일반 로그인</summary>
<div markdown="1">


#### 👉 `/login`
- `POST` 방식
- 일반 로그인 기능을 수행하는 API

#### request
```json
{
    "id":"my_awesome_email@email.com",
    "password":"my_plaintext_password"
}
```

#### response
```json
{
    "data": {
            "type":"Bearer",
            "accessToken":"encodedHeader.encodedPayload.encodedSignature",
            "refreshToken":"encodedHeader.encodedPayload.encodedSignature"
    },
    "code": 200,
    "status": "OK"
}
```

#### exception
```json
{
    "code": 401,
    "status": "UNAUTHORIZED",
    "error":"AUTHENTICATION_FAILED",
    "message":"아이디가 존재하지 않거나 비밀번호가 올바르지 않습니다."
}
```
<br>
<br>
</div>
</details>



<details>
<summary>소셜 로그인(Kakao)</summary>
<div markdown="1">


#### 👉 `/oauth2/authorization/kakao`
- `POST` 방식
- Kakao 간편로그인 기능을 수행하는 API
<br>

#### Request From FE App to BE App (or Inside BE App)
1. FE App은 사용자가 Kakao에서 자신을 인증
2. 사용자가 정보 제공을 동의하고 인가 코드를 받음
3. 인가 코드를 받기 위해 사용한 서비스 redirectURI를 BE App에 전달
4. 서버에서 redirectURI를 매핑한 메서드로 직접 받을 수 있는지 확인 
```json
{
    "authorizationCode":"codeIssuedByKakao",
    "requestURI":"https://our.service.request.uri.registered.in.kakao"
}
```
<br>

#### Request From FE App to BE App (or Inside BE App)
1. FE App은 사용자가 Kakao에서 자신을 인증
2. 사용자가 정보 제공을 동의하고 인가 코드를 받음
3. 인가 코드를 받기 위해 사용한 서비스 redirectURI를 BE App에 전달
4. 서버에서 redirectURI를 매핑한 메서드로 직접 받을 수 있는지 확인 
```json
{
    "authorizationCode":"codeIssuedByKakao",
	"requestURI":"https://our.service.request.uri.registered.in.kakao"
}
```
<br>

#### Request from Server to Kakao (POST)
- 서버에서 카카오에 Access Token 발급을 위해 보내는 요청
- URL: https://kauth.kakao.com/oauth/token
- 헤더: -H "Content-Type: application/x-www-form-urlencoded"
```json
{
    "grant_type":"authorization_code",
    "client_id":${REST_API_KEY},
    "redirect_uri":${REDIRECT_URI},
    "code":${AUTHORIZE_CODE}
} 
```
<br>

#### Response from Kakao to Server
- HTTP/1.1 200 OK
```json
{
    "token_type":"bearer",
    "access_token":"${ACCESS_TOKEN}",
    "expires_in":43199,
    "refresh_token":"${REFRESH_TOKEN}",
    "refresh_token_expires_in":25184000,
    "scope":"account_email profile"
}
```
<br>

#### Response from Server to Client
- 카카오 API에서 사용자 정보를 조회한 결과가 신규 가입인 경우
  - 회원가입 결과 통지와 로그인이 함께 진행
  - 세션ID나 JWT를 함께 전달
```json
{
    "data": {
            "id":"my_awesome_email@email.com",
        "name":"성이름",
        "accountOrigin":"kakao"
    },
    "code": 200,
    "status": "OK"
}
```
<br>

- 신규 가입은 아니고 그냥 로그인만 되었을 경우(일반 회원 가입과 동일)
  - 세션ID나 JWT를 전달
```json
{
    "data": {
            "type":"Bearer",
            "accessToken":"encodedHeader, encodedPayload, encodedSignature",
            "refreshToken":"encodedHeader.encodedPayload.encodedSignature"
    }
    "code": 200,
    "status": "OK"
}
```


<br>
<br>
</div>
</details>



<details>
<summary>소셜 로그인(Google)</summary>
<div markdown="1">


#### 👉 `/oauth2/authorization/google`
- `POST` 방식
- Google 간편로그인 기능을 수행하는 API
- FE가 담당: 추후에 request, response 추가할 예정
  
<br>
<br>
</div>
</details>



<details>
<summary>계정 관리</summary>
<div markdown="1">

#### 👉 `/my-page`
- `POST` 방식
- 비밀번호 변경을 수행하는 API

#### request
```json
{
    "newPassword": "my_new_plaintext_password"
}
```

#### response
```json
{
    "code": 200,
    "status": "OK"
}
```

<br>
</div>
</details>



<details>
<summary>리프레시 토큰으로 새 토큰쌍 발급</summary>
<div markdown="1">

#### 👉 `/token/refresh`
- `POST` 방식
- 자동 로그인 기능 등을 위한 API

#### request
```json
{
    "accessToken": "accessToken.accessToken.accessToken",
    "refreshToken": "refreshToken.refreshToken.refreshToken"
}
```

#### response
```json
{
    "data": {
            "type":"Bearer",
            "accessToken":"encodedHeader.encodedPayload.encodedSignature",
            "refreshToken":"encodedHeader.encodedPayload.encodedSignature"
    },
    "code": 200,
    "status": "OK"
}
```

#### exception
```json
{
    "code": 400,
    "status": "Bad Request",
    "error": "ACCESS_TOKEN_INVALID",
    "message": "유효하지 않은 액세스 토큰입니다"
}

{
    "code": 400,
    "status": "Bad Request",
    "error": "ACCESS_TOKEN_STILL_VALID",
    "message": "액세스 토큰이 아직 유효합니다."

}

{
    "code": 400,
    "status": "Bad Request",
    "error": "REFRESH_TOKEN_INVALID",
    "message": "유효하지 않은 리프레시 토큰입니다"
}
```

<br>
</div>
</details>



<details>
<summary>회원 인증 확인</summary>
<div markdown="1">

#### 👉 `/auth`
- `GET` 방식
- 자동 로그인 기능 등을 위한 API

#### response
```json
{
		"code": 200,
		"status": "OK"
}
```

<br>
</div>
</details>

<br>

### 프로젝트 및 히스토리 관련

<details>
<summary>프로젝트 및 영상 히스토리 조회</summary>
<div markdown="1">

#### 👉 `/projects`
- `GET` 방식
- 최종 수정일이 가장 빠른 순서대로 프로젝트 및 영상을 최대 5개까지 화면에 띄워주는 API

#### response
```json
{
		"data": {
				"projects" : [
						{
								"projectId": 2,
								"projectName": "나만의 아나운서 만들기",
								"lastModifiedAt": "2022-09-08 21:35:35"
						},
						{
								"projectId": 3,
								"projectName": "래퍼 만들기",
								"lastModifiedAt": "2022-09-08 17:32:35"
						},
						{
								"projectId": 1,
								"projectName": "자기소개하기",
								"lastModifiedAt": "2022-09-07 17:30:35"
						}
				],
				"videos" : [
						{
								"videoId": 2,
								"videoName": "나만의 아나운서 만들기",
								"videoUrl": "url형식",
								"createdAt": "2022-09-08 21:35:35"
						},
						{
								"videoId": 3,
								"videoName": "래퍼 만들기",
								"videoUrl": "url형식",
								"createdAt": "2022-09-08 17:32:35"
						},
						{
								"videoId": 1,
								"videoName": "자기소개하기",
								"videoUrl": "url형식",
								"createdAt": "2022-09-07 17:30:35"
						}
				]
		},
		"code": 200,
		"status": "OK"
}
```
<br>
<br>
</div>
</details>



<details>
<summary>프로젝트 생성</summary>
<div markdown="1">

#### 👉 `/projects`
- `POST` 방식
- 프로젝트를 새로 생성하는 버튼을 클릭할 때 동작하는 API

#### response
- 프로젝트 이름의 기본값은 생성일
```json
{
    "data": {
            "projectId": 1,
            "projectName": "2022-09-08 21:35:35"
    }
}
```
<br>
<br>
</div>
</details>



<details>
<summary>프로젝트 이름 변경</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}`
- `PATCH` 방식
- 프로젝트 이름을 더블 클릭한 후, 이름 변경이 완료됐을 때 동작하는 API

#### request
```json
{
		"projectName": "프로젝트 이름 변경하기!"
}
```

#### response
```json
{
		"data": {
				"projectName": "프로젝트 이름 변경하기!"				
		},
		"code": 200,
		"status": "OK"
}
```

<br>
</div>
</details>

<br>

### 텍스트 페이지 관련

<details>
<summary>다음 버튼 또는 전체 듣기 버튼 클릭 시, 음성 합성 URL</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/save`
- `POST` 방식
- 다음 버튼 또는 전체 듣기 버튼을 클릭했을 때, 전체 text에 대한 음성 합성이 동작하는 API

#### request
```json
{
		"texts": "안녕하세요. 저는 아바타라고 합니다. 만나서 반갑습니다.",
		"language": "한국어",
		"sex": "남자",
		"characterName": "가온",
		"speed": 2,
		"pitch": -1,
		"sentenceSpacing": 12,
		"splitTextList": [
				{
						"sentenceId": 1,
						"text": "안녕하세요",
						"sentenceSpacing": 1
				},
				{
						"sentenceId": 2,
						"text": "저는 아바타라고 합니다",
						"sentenceSpacing": 12
				},
				{
						"sentenceId": 3,
						"text": "만나서 반갑습니다",
						"sentenceSpacing": 30
				}
		]
}
```  

#### response
```json
{
		"data": {
				"result": "Success"
				"totalAudioUrl": "https://facam-final-project.s3.ap-northeast-2.amazonaws.com/.../056e6eb8.wav"
		},
		"code": 200,
		"status": "OK"
}
```

#### exception
- 음성 합성 파일 생성 실패 시 `Failed`
- FE 측에서 요청 값은 정상적이므로 `200 OK`
```json
{
		"data": {
				"result": "Failed"
		},
		"code": 200,
		"status": "OK"
}
```
  
<br>
<br>
</div>
</details>




<details>
<summary>음성 파일 업로드</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/audio-file`
- `POST` 방식
- 사용자가 로컬에서 음성 파일을 업로드하면, 음성 파일을 저장하고 저장된 URL을 response에 내려주는 API

#### request
- Base64 형식으로 FE와 audioFile 데이터를 주고 받음
```json
{
		"audioFileName": "20221010.wav",
		"audioFile": "AAAAAAAAAAA="
}
```  

#### response
```json
{
		"code": 200,
		"status": "OK"
}
```


#### exception
- 업로드된 파일 생성 실패 시
```json
{
    "code": 500,
    "status": "Internal Server Error",
    "error": "java.io.FileNotFoundException: https://facam-final-project.s3.ap-northeast-2.amazonaws.com/.../056e6eb8.wav"(파일 이름, 디렉터리 이름 또는 볼륨 레이블 구문이 잘못되었습니다)",
    "message": "업로드된 파일을 생성하지 못했습니다."
}
```

<br>
<br>
</div>
</details>




<details>
<summary>문장별 미리 듣기 버튼 클릭 시, 음성 합성 파일</summary>
<div markdown="1">

#### 👉 `/projects/save/text`
- `POST` 방식
- 문장별 text에 대한 음성 파일을 생성하여 파일 데이터를 response로 내려주는 API

#### request
```json
{
		"text": "안녕하십니까"
}
```  

#### response
- Base64 형식으로 FE와 audioFile 데이터를 주고 받음
```json
{
		"data": {
				"result": "Success",
  				"audioFile": "AAAAAAAAAAA="
		},
		"code": 200,
		"status": "OK"
}
```
#### exception
- 음성 합성 파일 생성 실패 시 `Failed`
- FE 측에서 요청 값은 정상적이므로 `200 OK`
```json
{
		"data": {
				"result": "Failed"
		},
		"code": 200,
		"status": "OK"
}
```

<br>
<br>
</div>
</details>




<details>
<summary>텍스트 입력 팝업창에서 완료 버튼 클릭 시, 음성 합성 URL</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/save/script`
- `POST` 방식
- 텍스트 입력 팝업창에서 text 전체를 입력한 후, 완료 버튼을 누르면 전체 text에 대한 음성 합성 파일을 response로 내려주는 API

#### request
```json
{
		"text": "안녕하십니까"
}
```  

#### response
```json
{
		"data": {
				"result": "Success",
				"audioFile": "AAAAAAAAAAA="
		},
		"code": 200,
		"status": "OK"
}
```

#### exception
- 음성 합성 파일 생성 실패 시 `Failed`
- FE 측에서 요청 값은 정상적이므로 `200 OK`
```json
{
		"data": {
				"result": "Failed"
		},
		"code": 200,
		"status": "OK"
}
```  
  
<br>
<br>
</div>
</details>




<details>
<summary>텍스트 페이지 임시 저장</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/save`
- `PATCH` 방식
- 텍스트 페이지에서 임시 저장 버튼을 누르면, text 및 음성 옵션 값들이 저장되는 API

#### request
```json
{
		"texts": "안녕하세요. 저는 아바타라고 합니다. 만나서 반갑습니다.",
		"language": "한국어",
		"sex": "남자",
		"characterName": "가온",
		"speed": 2,
		"pitch": -1,
		"sentenceSpacing": 12,
		"splitTextList": [
				{
						"sentenceId": 1,
						"text": "안녕하세요",
						"sentenceSpacing": 1
				},
				{
						"sentenceId": 2,
						"text": "저는 아바타라고 합니다",
						"sentenceSpacing": 12
				},
				{
						"sentenceId": 3,
						"text": "만나서 반갑습니다",
						"sentenceSpacing": 30
				}
		]
}
```  

#### response
```json
{
		"code": 200,
		"status": "OK"
}
```
  
<br>
<br>
</div>
</details>




<details>
<summary>텍스트 페이지 조회</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/save`
- `GET` 방식
- 마지막까지 작업했던 옵션 값들을 유지하면서 텍스트 페이지를 조회하는 API

#### response
```json
{
		"data": {
				"texts": "안녕하세요. 저는 아바타라고 합니다. 만나서 반갑습니다.",
				"language": "한국어",
				"sex": "남자",
				"characterName": "가온",
				"speed": 0.5,
				"pitch": 0.3,
				"sentenceSpacing": 0.1,
				"splitTextList": [
						{
								"sentenceId": 1,
								"text": "안녕하세요",
								"sentenceSpacing": -0.5
						},
						{
								"sentenceId": 2,
								"text": "저는 아바타라고 합니다",
								"sentenceSpacing": -0.4
						},
						{
								"sentenceId": 3,
								"text": "만나서 반갑습니다",
								"sentenceSpacing": -0.3
						},
						...
				],
				"totalAudioUrl": "https://../",
				"dummyData": {
						"korean": {
								"femaleList": [
										{
												"characterName": "가영",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../kor_w_1.wav"
										},
										{
												"characterName": "나영",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../kor_w_2.wav"
										},
										...
								]
								"maleList": [
										{
												"characterName": "가온",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../kor_m_1.wav"
										},
										{
												"characterName": "나온",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../kor_m_2.wav"
										},
										...
								]
						},
						"english": {
								"femaleList": [
										{
												"characterName": "Abbi",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../eng_w_1.wav"
										},
										{
												"characterName": "Bella",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../eng_w_2.wav"
										},
										...
								],
								"maleList": [
										{
												"characterName": "Alfie",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../eng_m_1.wav"
										},
										{
												"characterName": "Elliot",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../eng_m_2.wav"
										},
										...
								]
						},
						"chinese": {
								"femaleList": [
										{
												"characterName": "HiuGaai(광둥어)",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../chi_w_1.wav"
										},
										{
												"characterName": "HiuMaan(광둥어)",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../chi_w_2.wav"
										},
										...
								],
								"maleList": [
										{
												"characterName": "WanLung(광둥어)",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../chi_m_1.wav"
										},
										{
												"characterName": "YunJhe(대만어)",
												"characterTags": ["명량", "10대", "쾌활"],
												"audioUrl": "https://../chi_m_2.wav"
										},
										...
								]
						}
				}		
		},
		"code": 200,
		"status": "OK"
}
```
  
<br>
<br>
</div>
</details>


<br>

### 아바타 페이지 관련

<details>
<summary>아바타 선택 페이지에서 완료 버튼 누를 시, 영상 합성 URL</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/avatar`
- `POST` 방식
- 아바타 옵션 선택 후 완료 버튼을 누르면, 음성 및 아바타를 토대로 영상 합성을 실행하는 API

#### request
```json
{
		"avatarName": "avatar1",
		"avatarType": "1-1-2",
		"bgName": "배경1"

}
```  

#### response
```json
{
		"data": {
				"result": "Success",
				"videoId": 1,
				"videoName": "자기소개 하기",
				"videoUrl": "url형식",
				"createdAt": "2022-09-07 17:30:35"
		},
		"code": 200,
		"status": "OK"
}
```

#### exception
- 영상 합성 파일 생성 실패 시 `Failed`
- FE 측에서 요청 값은 정상적이므로 `200 OK`
```json
{
		"data": {
				"result": "Failed"
		},
		"code": 200,
		"status": "OK"
}
```    
<br>
<br>
</div>
</details>



<details>
<summary>아바타 선택 페이지 임시 저장</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/avatar`
- `PATCH` 방식
- 아바타 선택 페이지에서 임시 저장 버튼을 누르면, 아바타 옵션 값들이 저장되는 API

#### request
```json
{
		"avatarName": "avatar1",
		"avatarType": "1-1-2",
		"bgName": "배경1"

}
```  

#### response
```json
{
		"code": 200,
		"status": "OK"
}
```
  
<br>
<br>
</div>
</details>



<details>
<summary>아바타 선택 페이지 조회</summary>
<div markdown="1">

#### 👉 `/projects/{projectId}/avatar`
- `GET` 방식
- 마지막까지 작업했던 옵션 값들을 유지하면서 아바타 선택 페이지를 조회하는 API

#### response
```json
{
		"data": {
				"avatarName": "avatar1",
				"avatarType": "1-1-1",
				"bgName": "배경0",
				"language": "한국어",
				"sex": "남자",
				"characterName": "가온",
				"speed": 0.5,
				"pitch": 0.3,
				"sentenceSpacing": 0.1,
				"dummyData": {
						"avatar1": {
								"thumbnail": "https://../AVATAR1.png",
								"detailList1": [
										{
												"position": "1-1-1", // 1-1-1 부터 1-1-8 까지
												"thumbnail": "https://../1-1-1.png"
										},
										{
												"position": "1-1-2",
												"thumbnail": "https://../1-1-2.png"
										},
										{
												"position": "1-1-3",
												"thumbnail": "https://../1-1-3.png"
										},
										...
								],
								"detailList2": [
										{
												"position": "1-2-1", // 1-2-1 부터 1-2-5 까지
												"thumbnail": "https://../1-2-1.png"
										},
										{
												"position": "1-2-2",
												"thumbnail": "https://../1-2-2.png"
										},
										{
												"position": "1-2-3",
												"thumbnail": "https://../1-2-3.png"
										},
										...
								],
								"detailList3": [
										{
												"position": "1-3-1", // 1-3-1 부터 1-3-5 까지
												"thumbnail": "https://../1-3-1.png"
										},
										{
												"position": "1-3-2",
												"thumbnail": "https://../1-3-2.png"
										},
										{
												"position": "1-3-3",
												"thumbnail": "https://../1-3-3.png"
										},
										...
								]
						},
						...,

						"avatar7": {
								"thumbnail": "https://../AVATAR7.png",
								"detailList1": [
										{
												"position": "7-1-1", // 7-1-1 부터 7-1-5 까지
												"thumbnail": "https://../7-1-1.png"
										},
										{
												"position": "7-1-2",
												"thumbnail": "https://../7-1-2.png"
										},
										{
												"position": "7-1-3",
												"thumbnail": "https://../7-1-3.png"
										},
										...
								],
								"detailList2": [
										{
												"position": "7-2-1", // 7-2-1 부터 7-2-5 까지
												"thumbnail": "https://../7-2-1.png"
										},
										{
												"position": "7-1-2",
												"thumbnail": "https://../7-2-2.png"
										},
										{
												"position": "7-1-3",
												"thumbnail": "https://../7-2-3.png"
										},
										...
								],
								"detailList3": [
										{
												"position": "7-3-1", // 7-3-1 부터 7-3-3 까지
												"thumbnail": "https://../7-3-1.png"
										},
										{
												"position": "7-3-2",
												"thumbnail": "https://../7-3-2.png"
										},
										{
												"position": "7-3-3",
												"thumbnail": "https://../7-3-3.png"
										},
										...
								]
						},
						"backgroundList": [
								{
										"position": "배경0",
										"thumbnail": "https://../BG_0.png"
								},
								{
										"position": "배경1",
										"thumbnail": "https://../BG_1.png"
								},
								{
										"position": "배경2",
										"thumbnail": "https://../BG_2.png"
								},
								{
										"position": "배경3",
										"thumbnail": "https://../BG_3.png"
								}
						]
				}		
		}
}
```
  
<br>
<br>
</div>
</details>



<details>
<summary>아바타 옵션별 미리보기</summary>
<div markdown="1">

#### 👉 `/projects/avatar-preview`
- `POST` 방식
- 아바타 옵션 선택 후 우측의 미리보기 버튼을 누르면, 옵션별로 합성된 썸네일을 내려주는 API

#### request
```json
{
		"avatarName": "아바타1",
		"avatarType": "1-1-2",
		"bgName": "배경1"
}
```  

#### response
```json
{
		"data": {
				"thumbnail": "1-1-1.png"
		},
		"code": 200,
		"status": "OK"
}
```
  
<br>
<br>
</div>
</details>

<br>

## 🌈 &nbsp;ERD
![erd](https://user-images.githubusercontent.com/81819509/197169365-aa2c6f57-647f-4fbb-ab5c-7e80e8bda5ab.png)

### 회원 인증 정보 테이블
- 회원 기본정보 테이블의 `login_type` 컬럼으로 일반 로그인 회원과 소셜 로그인 회원을 구분

- 회원은 `user_uid` 라는 고유한 정수값을 식별자로 가짐

- 일반 로그인 회원과 소셜 로그인 회원의 인증 정보는 서로 다른 테이블에서 관리되도록 설계

<br>


### 프로젝트 정보 테이블
- Audio와 Avatar 클래스를 Project 엔티티 클래스의 `@Embedded` 타입 필드로 구성

- 주요 컬럼
  - `total_audio_url`
  👉 전체 텍스트 기반으로 생성된 음성 파일의 URL을 저장한 후, AWS S3 이용

  - `audio_file_name`
  👉 Flask 서버로 영상 합성 요청 시 로컬 상에서 찾아질 음성 파일의 이름을 저장

  - `sentence_spacing_list`
  👉 문장 단위로 분리되어 있는 전체 텍스트의 문장별 간격(호흡 간격) 목록을 저장

<br>

### 영상 정보 테이블
- 프로젝트와 영상 히스토리를 효율적으로 조회하기 위해, 프로젝트와 영상 테이블을 분리


- 주요 컬럼
  - `video_url`
  👉 프로젝트로부터 생성된 영상 파일의 URL을 저장(AWS S3 이용)

<br>


### Test Data 테이블
- 기업측에서 제공해준 이미지 및 음성 옵션 파일을 저장

![더미erd](https://user-images.githubusercontent.com/81819509/197174278-1457a028-d181-48f0-aa4c-e5d0a1fac575.png)


<br>

## 👥 &nbsp;Contributor
|이용승|조혜진|박민호|
|:----:|:----:|:----:|
|[@yonson](https://github.com/yongseungLeeDec)|[@Johyejin-119](https://github.com/Johyejin-119)|[@dev-minoflower](https://github.com/minoflower31)|
|<img width="100" height="100" alt="image" src="https://user-images.githubusercontent.com/56334513/197153683-b2ee9de2-2b4c-4f09-ba7f-67c1d2db50fb.png">|<img width="100" height="100" alt="image" src="https://user-images.githubusercontent.com/56334513/197153793-463ca57b-0efc-457c-8b72-b8fc01102d16.png">|<img width="80" height="80" alt="image" src="https://user-images.githubusercontent.com/56334513/197157916-9d22c1db-44c9-429b-837a-119e095f2bdd.png">|

<br>

## ☕️&nbsp;회고
[Wiki 바로가기](https://github.com/HiAvatar/backend/wiki)
