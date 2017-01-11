*** Settings ***
Resource    common.robot

*** Test Cases ***
Post Request With URL Params
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    &{params}=   Create Dictionary   key=value     key2=value2
    Post Request  httpbin  /post		params=${params}
    Response Code Should Be  httpbin  200

Post Request With No Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    Post Request  httpbin  /post  
    Response Code Should Be  httpbin  200

Post Request With No Dictionary
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=3
    Set Test Variable  ${data}  some content
    Post Request  httpbin  /post  data=${data}
    Response Code Should Be  httpbin  200
    Response Should Contain  httpbin  ${data}