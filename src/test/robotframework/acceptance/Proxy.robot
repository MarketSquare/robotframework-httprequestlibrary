*** Settings ***
Resource    common.robot
Force Tags    Proxy
*** Test Cases ***
Get Requests Through Non-existent Proxy
    &{proxy}    Create Dictionary    host=127.0.0.1    port=1234
    Create Session  google  https://www.google.com    proxy=${proxy}
    Run Keyword And Expect Error    *Connection refused*    Get Request  google  /
    
Get Request Through Local Squid Proxy
    &{proxy}    Create Dictionary    host=127.0.0.1    port=${proxyPort}
    Create Session  google  https://www.google.com    proxy=${proxy}
    ${resp}=  Get Request  google  /
    Should Contain  ${resp.text}  The Squid Software Foundation