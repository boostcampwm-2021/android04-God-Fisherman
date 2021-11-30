# 🐟 프로젝트 소개

<p align="center"><img src="https://user-images.githubusercontent.com/38002959/143963637-6963d42d-ed64-4349-9e5b-b4795271b920.gif"></p>

### 그물잠은 낚시를 보다 재밌게 즐길 수 있는 앱입니다.

🐠 물고기를 잡고 얼마나 큰 녀석인지 확인해보세요! 그리고 자랑도 해보세요! 

🔍 누가 어떤 물고기를 잡았는지 확인해보세요! 

⏰ 낚시를 하는 동안 잡힌 물고기를 타임라인으로 남겨보세요!

[WIKI](https://github.com/boostcampwm-2021/android04-God-Fisherman/wiki)

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fboostcampwm-2021%2Fandroid04-God-Fisherman&count_bg=%238357FF&title_bg=%23525252&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

# 🎬 주요 기능

## 홈 화면🏠

|홈 화면 보기|날씨 보기|
|----|----|
|<img src = "https://user-images.githubusercontent.com/38002959/143964207-4984638a-e94f-482a-9a1c-9714137e22d5.gif" height="600px">|<img src = "https://user-images.githubusercontent.com/38002959/143964234-b22ba2f1-2b56-4dac-8eb8-cc2ec76a3bdd.gif" height="600px">|
<br/>

> 낚시 정보 제공소

- **랭킹** Top 5
    - [더보기] 버튼을 클릭해 상세 랭킹도 확인할 수 있어요!
- 낚시 관련 **추천 영상**제공
- **현재 날씨 정보** 제공

`#메모리캐싱` `#Retrofit`

## 낚시 기록 및 업로드📷

|낚시 기록 및 업로드|
|----|
|<img src = "https://user-images.githubusercontent.com/38002959/143964908-2176ee4a-2926-4af4-9d66-7e8b28de4d39.gif" width="400px">|
<br/>

> 아직도 줄자를 들고 다녀?👤👥(웅성웅성)👥👤
> 

- **수평계**를 보고 지면과 수평이 되도록 수평을 맞추고 물고기를 촬영해보세요
- 물고기 하단에 1000원권 지폐를 놓고 찍으면 **물고기 길이(체장)을 자동으로 계산**해줘요
- 물고기는 노란색 / 1000원권 지폐는 분홍색으로 인식됩니다
- 촬영한 물고기 이미지는 Bounding Box를 기준으로 **자동 크롭**됩니다✂

`#CameraX` `#ML Kit` `#Firebase` 

## 낚시 피드 화면🎣

|피드 화면|**사진 타입**📷|**타입라인 타입**⏳|
|----|----|----|
|<img src = "https://user-images.githubusercontent.com/38002959/143965139-9be063f9-44b0-480c-973a-7d9806999b41.gif" height="400px">|<img src = "https://user-images.githubusercontent.com/38002959/143965149-0b12c4c6-c947-4eaf-b320-0fd3d8513d26.png" height="400px">|<img src = "https://user-images.githubusercontent.com/38002959/143965154-ec47d2a0-6e0d-4eb3-b186-4f1560ed8c94.gif" height="400px">|
<br/>

> 천하제일 강태공 자랑대회
> 

- **사용자들이 남긴** 낚시 기록을 확인
- **두 가지 타입**으로 제공
    - 사진 타입📷 - 낚시 기록 1개만!
    - 타임라인 타입⏳ - 1개이상의 기록들을 타임라인 형태로 제공
- **타입별 필터링** 가능
- **아래로 당겨서** 새로고침 가능

`#Paging` `#오프라인 캐싱` `#SwipeRefreshLayout`

## 낚시타임(스톱워치)⏰

|**스톱워치 실행**|**미니 화면**|
|----|----|
|<img src = "https://user-images.githubusercontent.com/38002959/143965784-26620705-76c1-4280-8c1c-5734ec921990.gif" height="600px">|<img src = "https://user-images.githubusercontent.com/38002959/143965792-f39a25a1-f300-4245-b350-7545c24dc52e.gif" height="600px">|
<br/>

> 낚시는 기다림의 미학
> 

- 낚시하는 동안 스톱워치를 실행해서 **낚시 시간을 체크**해보세요
- **앱을 종료해도** 계속 스톱워치가 실행되고 있어요
    - 앱을 종료하면 **알림 창**에서 확인할 수 있어요!
- 아래로 축소해두고 다른 탭으로 이동할 수 있어요
    - **미니 화면**이 제공됩니다
- 스톱워치를 킨 상태로 남긴 낚시 기록들은❓❔
    
    ➡스톱워치 종료시 **한꺼번에 타임라인 타입으로** 피드에 올라갑니다!
    
    `#ForegroundService` `#Notification` `#MotionLayout` `#Animation`

# 🧑‍💻 기술 스택

<p align="center"><img src="https://user-images.githubusercontent.com/38002959/143965947-86f45b98-8a16-4df9-ac1a-7bb5f3f1b120.png"></p>

- **외부 API**
    - Youtube API
    - Naver Reverse-GeoCoding API
    - OpenWeatherMap API

# 🧑‍💻 기술 특장점

[기술 특장점](https://www.notion.so/96dfeeff79324038a000ba56858fe06a)

# 팀원 소개

|K001|K006|K024|K053|
|:----:|:----:|:----:|:----:|
|[구윤경](https://github.com/potatoyum)|[김세진](https://github.com/sejins)|[박치윤](https://github.com/chiyoon)|[전종현](https://github.com/JeonJongHyeon97)|
|<img src = "https://user-images.githubusercontent.com/38002959/143966220-83278ffa-5541-46cf-b568-62699e4d2284.png" height="200px">|<img src = "https://user-images.githubusercontent.com/38002959/143966223-7c10b010-32a9-4fd5-b021-3a9764134318.png" height="200px">|<img src = "https://user-images.githubusercontent.com/38002959/143966226-04126b93-33ba-45b8-9543-0dbbeafe8bba.jpg" height="200px">|<img src = "https://user-images.githubusercontent.com/38002959/143966231-f2ed1b7a-c357-49e4-a434-f5759c37935a.jpg" height="200px">|
