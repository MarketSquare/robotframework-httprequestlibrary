*** Settings ***
Resource    common.robot
Force Tags    HEAD

*** Test Cases ***
Head Request
    Create Session  httpbin  ${testServer} 
    ${resp}=  Head Request  httpbin  /headers
    Should Be Equal As Strings  ${resp.status_code}  200
    
Head Request With Redirection
    Create Session  httpbin  ${testServer} 
    ${resp}=  Head Request  httpbin  /redirect/1  allow_redirects=${True}
    Should Be Equal As Strings  ${resp.status_code}  200

Head Request Without Redirection
    Create Session  httpbin  ${testServer} 
    ${resp}=  Head Request  httpbin  /redirect/1
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30
    ${resp}=  Head Request  httpbin  /redirect/1  allow_redirects=${False}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30