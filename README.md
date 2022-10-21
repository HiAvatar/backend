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

<br>

## 🌈 &nbsp;ERD

<br>

## 👥 &nbsp;Contributor
|이용승|조혜진|박민호|
|:----:|:----:|:----:|
|[@yonson](https://github.com/yongseungLeeDec)|[@Johyejin-119](https://github.com/Johyejin-119)|[@dev-minoflower](https://github.com/minoflower31)|
|<img width="100" height="100" alt="image" src="https://user-images.githubusercontent.com/56334513/197153683-b2ee9de2-2b4c-4f09-ba7f-67c1d2db50fb.png">|<img width="100" height="100" alt="image" src="https://user-images.githubusercontent.com/56334513/197153793-463ca57b-0efc-457c-8b72-b8fc01102d16.png">|<img width="80" height="80" alt="image" src="https://user-images.githubusercontent.com/56334513/197157916-9d22c1db-44c9-429b-837a-119e095f2bdd.png">|

<br>

## ☕️&nbsp;회고
[Wiki 바로가기](https://github.com/HiAvatar/backend/wiki)
