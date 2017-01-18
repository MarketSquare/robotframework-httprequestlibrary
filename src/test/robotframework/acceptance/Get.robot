*** Settings ***
Resource    common.robot

*** Test Cases ***
Get Requests
    [Tags]  get
    Create Session  google  http://www.google.com
    Create Session  github  https://api.github.com
    ${resp}=  Get Request  google  /
    Should Be Equal As Strings  ${resp.status_code}  200
    ${resp}=  Get Request  github  /users/bulkan
    Should Be Equal As Strings  ${resp.status_code}  200
    Dictionary Should Contain Value  ${resp.json}  Bulkan Evcimen

Get Requests with Url Parameters
    [Tags]  get
    Create Session  httpbin     http://httpbin.org
    &{params}=   Create Dictionary   key=value     key2=value2
    ${resp}=     Get Request  httpbin  /get    params=${params}
    Should Be Equal As Strings  ${resp.status_code}  200
    ${jsondata}=  To Json  ${resp.content}
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
    Should Be Equal As Strings  ${resp.json['authenticated']}  True

Get Request With Redirection
    [Tags]  get
    Create Session  httpbin  http://httpbin.org    debug=True
    ${resp}=  Get Request  httpbin  /redirect/1
    Should Be Equal As Strings  ${resp.status_code}  200

    ${resp}=  Get Request  httpbin  /redirect/1  allow_redirects=${true}
    Should Be Equal As Strings  ${resp.status_code}  200

Get Request Without Redirection
    [Tags]  get
    Create Session  httpbin  http://httpbin.org
    ${resp}=  Get Request  httpbin  /redirect/1  allow_redirects=${false}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30
