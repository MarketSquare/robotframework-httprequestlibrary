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
    ${resp}=     Get Request  httpbin  /get    params=${params}
    Response Code Should Be  httpbin  200
    ${jsondata}=  Get JSON Response  httpbin
    Should be Equal     ${jsondata['args']}     ${params}

Get HTTPS & Verify Cert
    [Tags]  get
    Create Session    httpbin    https://httpbin.org   verify=True
    ${resp}=  Get Request  httpbin  /get
    Should Be Equal As Strings  ${resp.status_code}  200

Get HTTPS & Verify Cert with a CA bundle
    [Tags]  get
    Create Session    httpbin    https://httpbin.org   verify=${CURDIR}${/}cacert.pem
    ${resp}=  Get Request  httpbin  /get
    Should Be Equal As Strings  ${resp.status_code}  200

Get With Auth
    [Tags]  get
    ${auth}=  Create List  user   passwd
    Create Session    httpbin    https://httpbin.org     auth=${auth}
    ${resp}=  Get Request  httpbin  /basic-auth/user/passwd
    Should Be Equal As Strings  ${resp.status_code}  200
    Should Be Equal As Strings  ${resp.json()['authenticated']}  True

Get With Digest Auth
    [Tags]    get
    ${auth}=    Create List    user    pass
    Create Digest Session    httpbin    https://httpbin.org    auth=${auth}    debug=3
    ${resp}=    Get Request    httpbin    /digest-auth/auth/user/pass
    Should Be Equal As Strings    ${resp.status_code}    200
    Should Be Equal As Strings    ${resp.json()['authenticated']}    True