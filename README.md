# 실시간 지하철

수도권 지하철의 실시간 열차 위치를 볼 수 있는 앱입니다.

## 스크린샷

[메인 액티비티](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/MainActivity.java)

<img src="https://github.com/CPstudy/SilsiganMetro-Android/blob/master/images/main.jpg" width="250px">

[각 노선 별 액티비티](https://github.com/CPstudy/SilsiganMetro-Android/tree/master/app/src/main/java/com/ganada/silsiganmetro/activity)

<img src="https://github.com/CPstudy/SilsiganMetro-Android/blob/master/images/line.jpg" width="250px">

[모아보기](https://github.com/CPstudy/SilsiganMetro-Android/tree/master/app/src/main/java/com/ganada/silsiganmetro/activity/FavoriteActivity.java)

<img src="https://github.com/CPstudy/SilsiganMetro-Android/blob/master/images/moa.jpg" width="250px">

[역 별 열차 위치](https://github.com/CPstudy/SilsiganMetro-Android/tree/master/app/src/main/java/com/ganada/silsiganmetro/activity/StationActivity.java)(개발 중)

<img src="https://github.com/CPstudy/SilsiganMetro-Android/blob/master/images/train.gif" width="250px">



## 주요 패키지

### [activity](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity)

| 파일                                                         | 내용                     |
| ------------------------------------------------------------ | ------------------------ |
| [BundangActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/BundangActivity.java) | 분당선 액티비티          |
| [FabsortActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/FabsortActivity.java) | 모아보기 정렬 액티비티   |
| [FavoriteActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/FavoriteActivity.java) | 모아보기 액티비티        |
| [GonghangActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/GonghangActivity.java) | 공항철도 액티비티        |
| [HelperActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/HelperActivity.java) | 도움말 액티비티          |
| [KChoonActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/KChoonActivity.java) | 경춘선 액티비티          |
| [KJoongActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/KJoongActivity.java) | 경의 · 중앙선 액티비티   |
| [Line1Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line1Activity.java) | 1호선 액티비티           |
| [Line2Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line2Activity.java) | 2호선 액티비티           |
| [Line3Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line3Activity.java) | 3호선 액티비티           |
| [Line4Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line4Activity.java) | 4호선 액티비티           |
| [Line5Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line5Activity.java) | 5호선 액티비티           |
| [Line6Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line6Activity.java) | 6호선 액티비티           |
| [Line7Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line7Activity.java) | 7호선 액티비티           |
| [Line8Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line8Activity.java) | 8호선 액티비티           |
| [Line9Activity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/Line9Activity.java) | 9호선 액티비티           |
| [MainActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/MainActivity.java) | 메인 액티비티            |
| [OpenSourceActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/OpenSourceActivity.java) | 오픈소스 고지 액티비티   |
| [ShinBundangActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/ShinBundangActivity.java) | 신분당선 액티비티        |
| [SortActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/SortActivity.java) | 메인화면 정렬 액티비티   |
| [SplashActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/SplashActivity.java) | 스플래시 화면 액티비티   |
| [StationActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/StationActivity.java) | 역 별 열차 위치 액티비티 |
| [SuinActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/SuinActivity.java) | 수인선 액티비티          |
| [ThemeActivityActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/TimetableActivity.java) | 테마 설정 액티비티       |
| [TimetableActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/TimetableActivity.java) | 역 별 시간표 액티비티    |

### [common](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/common)

| 파일                                                         | 내용                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [ItemMoveListener.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/common/ItemMoveListener.java), [OnStartDragListener.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/common/OnStartDragListener.java), [SimpleItemTouchHelperCallback.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/common/SimpleItemTouchHelperCallback.java) | 리사이클러뷰 각 아이템을 드래그로 정렬할 수 있도록 해주는 클래스와 인터페이스 |
| [StringRefactor.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/common/StringRefactor.java) | 문자열을 다시 고쳐주는 클래스                                |
| [SimpleItemTouchHelperCallback.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/common/SimpleItemTouchHelperCallback.java) | 리사이클러뷰의 드래그 정렬 콜백                              |

### [laboratory](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/laboratory) - 실험실

개발 중인 작업들을 모아놓은 패키지로 미완성인 부분이 있음

| 파일                                                         | 내용                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [BindViewHolder.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/laboratory/BindViewHolder.java) | [LineActivity](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/LineActivity.java)에서 사용하는 리사이클러뷰의 뷰 홀더 |
| [LabActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/laboratory/LabActivity.java) | 실험실 액티비티. 메인 액티비티를 재구성하기 위해 실험적으로 사용하고 있음 |
| [LineActivity.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/laboratory/LineActivity.java) | 열차 위치를 보여주는 액티비틀이 각 노선마다(15개) 존재하는 불편함이 있어 모두 통합하여 보여주는 액티비티 |
| [LineType.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/laboratory/LineType.java) | 노선 정보                                                    |
| [MetroConstant.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/laboratory/MetroConstant.java) | 상수 클래스                                                  |
| [StationAdapter.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/laboratory/StationAdapter.java) | [LineActivity](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/activity/LineActivity.java)에서 사용하는 리사이클러뷰의 어댑터 |

### [listitem](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/listitem)

ArrayList나 HashMap에 사용할 객체들 모음

### [real](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/real)

| 파일                                                         | 내용                                         |
| ------------------------------------------------------------ | -------------------------------------------- |
| [FavoriteInfo.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/real/FavoriteInfo.java) | 역 별 열차 위치를 불러오는 클래스            |
| [GetType.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/real/GetType.java) | 1호선, 3호선, 4호선의 차종을 불러오는 클래스 |
| [Realtable.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/real/Realtable.java) | 역 별 시간표를 불러오는 클래스               |
| [Realtime.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/real/Realtime.java) | 노선 별 열차 위치 목록을 불러오는 클래스     |

### [util](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/util)

| 파일                                                         | 내용                                                     |
| ------------------------------------------------------------ | -------------------------------------------------------- |
| [CalcDate.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/util/CalcDate.java) | 날짜 계산 클래스                                         |
| [DBManager.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/util/DBManager.java) | 정보 저장에 관한 클래스                                  |
| [LineManager.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/util/LineManager.java) | 노선 번호, 역 명 등에 관한 클래스                        |
| [ThemeManager.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/util/ThemeManager.java) | 앱 테마 매니저 클래스                                    |
| [Units.java](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/util/Units.java) | 단위 클래스. 현재는 픽셀을 dp값으로 바꾸는 메서드만 있음 |

### [view](https://github.com/CPstudy/SilsiganMetro-Android/blob/master/app/src/main/java/com/ganada/silsiganmetro/view)

커스텀뷰를 모아놓은 패키지