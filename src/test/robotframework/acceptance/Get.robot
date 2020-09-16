*** Settings ***
Resource    common.robot
Force Tags    GET

*** Test Cases ***
Get Requests
    Log    ${testServer}
    Create Session  google  https://www.google.com
    Create Session  httpbin     ${testServer} 
    ${resp}=  Get Request  google  /
    Should Be Equal As Strings  ${resp.status_code}  200
    ${resp}=  Get Request  httpbin  /
    Should Be Equal As Strings  ${resp.status_code}  200

Get Requests with Url Parameters
    Create Session  httpbin     ${testServer} 
    &{params}=   Create Dictionary   key=value     key2=value2
    ${resp}=     Get Request  httpbin  /get    params=${params}
    Should Be Equal As Strings  ${resp.status_code}  200
    ${jsondata}=  To Json  ${resp.content}
    Should be Equal     ${jsondata['args']}     ${params}

Get HTTPS & Verify Cert
    Create Session    httpbin    https://httpbin.org   verify=True
    ${resp}=  Get Request  httpbin  /get
    Should Be Equal As Strings  ${resp.status_code}  200

Get HTTPS & Verify Cert with a CA bundle
    Create Session    httpbin    https://httpbin.org   verify=${CURDIR}${/}cacert.pem
    ${resp}=  Get Request  httpbin  /get
    Should Be Equal As Strings  ${resp.status_code}  200

Get With Auth
    ${auth}=  Create List  user   passwd
    Create Session    httpbin    https://httpbin.org     auth=${auth}
    ${resp}=  Get Request  httpbin  /basic-auth/user/passwd
    Should Be Equal As Strings  ${resp.status_code}  200
    Should Be Equal As Strings  ${resp.json['authenticated']}  True

Get Request With Redirection
    Create Session  httpbin  ${testServer}     debug=True
    ${resp}=  Get Request  httpbin  /redirect/1
    Should Be Equal As Strings  ${resp.status_code}  200
    ${resp}=  Get Request  httpbin  /redirect/1  allow_redirects=${true}
    Should Be Equal As Strings  ${resp.status_code}  200

Get Request Without Redirection
    Create Session  httpbin  ${testServer} 
    ${resp}=  Get Request  httpbin  /redirect/1  allow_redirects=${false}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30
