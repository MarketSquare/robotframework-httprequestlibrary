*** Settings ***
Resource    common.robot

*** Test Cases ***
Get Requests
    [Tags]  get
    Create Session  google  http://www.google.com
    Create Session  github  https://api.github.com
    Get Request  google  /
    Response Code Should Be  google  200
    Get Request  github  /users/bulkan
    Response Code Should Be  github  200
    Response Should Contain  github  Bulkan Evcimen

Get Requests with Url Parameters
    [Tags]  get
    Create Session  httpbin     http://httpbin.org
    &{params}=   Create Dictionary   key=value     key2=value2
    Get Request  httpbin  /get    params=${params}
    Response Code Should Be  httpbin  200
    ${jsondata}=  Get JSON Response  httpbin
    Should be Equal     ${jsondata['args']}     ${params}

Get HTTPS & Verify Cert
    [Tags]  get
    Create Session    httpbin    https://httpbin.org   verify=True
    Get Request  httpbin  /get
    Response Code Should Be  httpbin  200
    
Get HTTPS & Verify Cert with a CA bundle
    [Tags]  get
    Create Session    httpbin    https://httpbin.org   verify=${CURDIR}${/}cacert.pem
    Get Request  httpbin  /get
    Response Code Should Be  httpbin  200

Get HTTPS & Verify Cert with a CA bundle without correct certificate
    [Tags]  get
    Create Session    httpbin    https://httpbin.org   verify=${CURDIR}${/}single.pem
    ${msg}    Run Keyword And Expect Error    *    Get Request  httpbin  /get
    Should contain    ${msg}    unable to find valid certification    case_insensitive=True
    
Get With Auth
    [Tags]  get
    ${auth}=  Create List  user   passwd
    Create Session    httpbin    https://httpbin.org     auth=${auth}
    Get Request  httpbin  /basic-auth/user/passwd
    Response Code Should Be  httpbin  200
    ${jsondata}=  Get JSON Response  httpbin
    Should Be Equal As Strings  ${jsondata['authenticated']}  True