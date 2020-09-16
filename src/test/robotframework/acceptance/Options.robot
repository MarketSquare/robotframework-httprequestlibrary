*** Settings ***
Resource    common.robot
Force Tags    OPTIONS

*** Test Cases ***
Options Request
    Create Session  httpbin  ${testServer} 
    ${resp}=  Options Request  httpbin  /headers
    Should Be Equal As Strings  ${resp.status_code}  200
    Dictionary Should Contain Key  ${resp.headers}  Allow

Options Request With Redirection
    Create Session  httpbin  ${testServer} 
    ${resp}=  Options Request  httpbin  /redirect/1
    Should Be Equal As Strings  ${resp.status_code}  200
    ${resp}=  Options Request  httpbin  /redirect/1  allow_redirects=${true}
    Should Be Equal As Strings  ${resp.status_code}  200