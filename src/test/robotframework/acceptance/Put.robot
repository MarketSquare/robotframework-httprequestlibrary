*** Settings ***
Resource    common.robot
Force Tags    PUT

*** Test Cases ***
Put Request With No Data
    Create Session  httpbin  http://httpbin.org
    ${resp}=  Put Request  httpbin  /put
    Should Be Equal As Strings  ${resp.status_code}  200
    
Put Request With URL Params
    Create Session  httpbin  http://httpbin.org
    &{params}=   Create Dictionary   key=value     key2=value2
    ${resp}=  Put Request  httpbin  /put  params=${params}
    Should Be Equal As Strings  ${resp.status_code}  200

Put Request With No Dictionary
    Create Session  httpbin  http://httpbin.org
    Set Test Variable  ${data}  some content
    ${resp}=  Put Request  httpbin  /put  data=${data}
    Should Be Equal As Strings  ${resp.status_code}  200
    Should Contain  ${resp.text}  ${data}
    
Put Requests
    Create Session  httpbin  http://httpbin.org
    &{data}=  Create Dictionary  name=bulkan  surname=evcimen
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Put Request  httpbin  /put  data=${data}  headers=${headers}
    Dictionary Should Contain Value  ${resp.json['form']}  bulkan
    Dictionary Should Contain Value  ${resp.json['form']}  evcimen
    
Put Request With Redirection
    Create Session  httpbin  http://httpbin.org    debug=True
    &{params}=  Create Dictionary  url=https://httpbin.org
    ${resp}=  Put Request  httpbin  /redirect-to    params=${params}
    Should Be Equal As Strings  ${resp.status_code}  200

Put Request Without Redirection
    Create Session  httpbin  http://httpbin.org
    &{params}=  Create Dictionary  url=https://httpbin.org
    ${resp}=  Put Request  httpbin  /redirect-to    params=${params}  allow_redirects=${false}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30