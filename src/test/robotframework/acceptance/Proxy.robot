*** Settings ***
Resource    common.robot
Force Tags    Proxy
*** Test Cases ***
Get Requests Through Non-existent Proxy
    &{proxy}    Create Dictionary    host=127.0.0.1    port=1234
    Create Session  google  http://www.google.com    proxy=${proxy}
    Run Keyword And Expect Error    *Connection refused*    Get Request  google  /
    
Get Request Through Open Proxy
    [Tags]    Non-Critical
    &{proxy}    Create Dictionary    host=192.95.18.162    port=8080
    Create Session  google  http://www.google.com    proxy=${proxy}
    ${resp}=  Get Request  google  /
    Should Be Equal As Strings  ${resp.status_code}  200