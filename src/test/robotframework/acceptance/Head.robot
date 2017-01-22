*** Settings ***
Resource    common.robot

*** Test Cases ***
Head Request
    [Tags]  head
    Create Session  httpbin  http://httpbin.org
    ${resp}=  Head Request  httpbin  /headers
    Should Be Equal As Strings  ${resp.status_code}  200
    
Head Request With Redirection
    [Tags]  head
    Create Session  httpbin  http://httpbin.org
    ${resp}=  Head Request  httpbin  /redirect/1  allow_redirects=${true}
    Should Be Equal As Strings  ${resp.status_code}  200

Head Request Without Redirection
    [Tags]  head
    Create Session  httpbin  http://httpbin.org
    ${resp}=  Head Request  httpbin  /redirect/1
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30
    ${resp}=  Head Request  httpbin  /redirect/1  allow_redirects=${false}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30