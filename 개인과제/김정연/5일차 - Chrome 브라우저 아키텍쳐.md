# Chrome 브라우저 아키텍처

![browser-architecture-9d143004c2a63_856.png](attachment:5dc32054-f8fb-4b8c-8271-62f848702acd:browser-architecture-9d143004c2a63_856.png)

## 브라우저 아키텍처

**브라우저는 표준이 없기 때문**에 chrome, safari, firefox등 브라우저들은 구현 방식이 다를 수 있다.

좌상단 그림처럼 어떤 브라우저는 브라우저 **프로세스가 모든 스레드를 담당**하고 있고, 어떤 브라우저는 우상단 그림처럼 **스레드 별로 프로세스를 분리**하여 **IPC**(inter process communication) 통신을 할 수 있다.

## chrome의 브라우저 아키텍처

크롬은 우상단 그림처럼 프로세스를 여러개 사용하는 **다중 프로세스 아키텍처**이다.

크롬 브라우저가 사용하는 프로세스 종류는 크게 **4가지** 이다.

![chrome-processes-79aaecca78d23_856.png](attachment:5193c113-f473-45d0-9502-a266197069da:chrome-processes-79aaecca78d23_856.png)

- **브라우저 프로세스**
    - 주소 표시줄, 북마크, 뒤로가기 버튼 등 크롬을 제어하고, 네트워크 요청, 파일 접근도 처리한다.
- **렌더러 프로세스**
    - 탭 안에서 웹 사이트가 표시되는 부분의 모든 것을 제어한다.
- **플러그인 프로세스**
    - 웹 사이트에서 사용하는 플러그인을 제어한다.

![image.png](attachment:55d93139-4c74-4354-a770-fb48235662f4:image.png)

- **GPU 프로세스**
    - 요청받은 내용을 화면에 그린다.
    - GPU 작업을 CPU와 격리하여 처리한다.

![image.png](attachment:84b3d75c-1581-4bbd-b603-60772e945f6b:image.png)

# 브라우저의 멀티 프로세스 아키텍쳐 이점

## 1. 장애 전파

- 크롬 브라우저는 **탭마다 렌더러 프로세스**를 만들어서 사용하는 특징이 있다.

브라우저에서 **탭을 3개 열었다면, 3개의 각 렌더러 프로세스**에서 브라우저 탭이 실행된다. 만약 프로세스 하나가 응답하지 않아도 **다른 프로세스들은 정상적으로 동작**한다.

렌더러 프로세스가 응답하지 않아 화면을 그리지 못하는 상황에도 **브라우저 프로세스와 분리**가 되어있기 때문에, 오류 페이지를 보여주는 방법으로 문제를 처리할 수 있다.

![multiple-renderer-tabs-c29a1fd34d4d_856.png](attachment:e4d35caa-7e57-4dc1-9a47-4b014d7d71d7:multiple-renderer-tabs-c29a1fd34d4d_856.png)

![image.png](attachment:bdf966ad-5255-4e10-b415-3f57b169b485:image.png)

## 2. 보안 : [샌드박싱](https://chromium.googlesource.com/chromium/src/+/main/docs/design/sandbox.md)

운영체제가 프로세스에게 제한된 권한을 제공하기 때문에, 브라우저는 특정 프로세스가 특정 기능을 사용할 수 없게 제한할 수 있다.

운영체제에서 파일 시스템 접근을 차단하여 웹페이지가 사용자의 로컬 파일을 읽거나 수정하지 못하도록 한다.

크롬 브라우저의 렌더러 프로세스에서 실행되는 동작은 기본적으로 신뢰할 수 없기 때문에 ‘샌드박스’라는 격리된 환경에서 코드를 실행될 수 있도록 한다.

# 멀티 프로세스 아케텍쳐에서 메모리 절약 방법

![chrome-servification-f06f547c54405.svg](attachment:15d4754a-962e-45b0-a45e-e8760a62907f:chrome-servification-f06f547c54405.svg)

각 프로세스는 자신만의 메모리를 가지기 때문에 멀티 프로세스 환경에서 자원 소모가 많다. 따라서 사용 환경에 따라 생성 가능한 프로세스 수에 제한을 둔다. 만약 제한에 도달 한다면, 분리되어 있던 프로세스를 한 프로세스에서 실행되도록 한다.

여러 서비스를 하나의 프로세스에서 실행하여 메모리를 절약하는 방식은 안드로이드와 같은 플랫폼에서 이전부터 사용되었다.

# 프레임 별 렌더러 프로세스 - 사이트 격리 site isolation

<aside>
💡

[**iframe**](https://developer.mozilla.org/ko/docs/Learn_web_development/Core/Structuring_content/General_embedding_technologies)을 **독립적인 렌더러 프로세스**로 실행하는 것을 의미

iframe : 다른 html페이지 현재 페이지에 포함시키는 중첩 브라우저

</aside>

![site-isolation-2521dc96bb823_856.png](attachment:9fa8d610-aab9-4f85-b1ac-b55271308304:site-isolation-2521dc96bb823_856.png)

**사이트 격리**는 크롬 브라우저에서 처음 도입된 기술이다. 탭마다 렌더러 프로세스를 하나만 사용하는 모델에서는 iframe의 사이트가 **같은 프로세스에서 동작**하기 때문에 서로 다른 사이트간 **메모리가 공유**될 수 있다는 문제가 있다. 

**SOP**에 의하여 사이트끼리 **동의 없이 데이터에 접근**할 수 없다. 같은 프로세스 내에서 자원이 쉽게 공유되기 때문에 [**악의적 공격에 취약**](https://developer.chrome.com/blog/meltdown-spectre?hl=ko)하다.

사이트 격리를 위해 **iframe이 서로 통신**하는 방법이 개발되었다. 단순히 `ctrl+F` 를 눌러 페이지에서 단어를 찾으려 해도 **서로 다른 렌더러 프로세스**를 동시에 확인하는 기술이 필요로 한다.

<aside>
💡

[**SOP**](https://developer.mozilla.org/ko/docs/Web/Security/Same-origin_policy)(same origin policy) :  **동일 출처 정책**이라고 부르며 **어떤 출처**([origin](https://developer.mozilla.org/ko/docs/Glossary/Origin))에서 불러온 **데이터**가 **다른 출처**에서 가져온 리소스와 상호 작용할 수 있는 **방법을 제한**하는 보안 정책

</aside>
