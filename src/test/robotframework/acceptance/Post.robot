*** Settings ***
Resource    common.robot

*** Test Cases ***
Post Request With URL Params
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    &{params}=   Create Dictionary   key=value     key2=value2
    ${resp}=  Post Request  httpbin  /post		params=${params}
    Should Be Equal As Strings  ${resp.status_code}  200

Post Request With No Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    ${resp}=  Post Request  httpbin  /post  
    Should Be Equal As Strings  ${resp.status_code}  200
    
Post Request With No Dictionary
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=True
    Set Test Variable  ${data}  some content
    ${resp}=  Post Request  httpbin  /post  data=${data}
    Should Be Equal As Strings  ${resp.status_code}  200
    Should Contain  ${resp.text}  ${data}
    
Post Requests
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    &{data}=  Create Dictionary  name=bulkan  surname=evcimen
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Dictionary Should Contain Value  ${resp.json['form']}  bulkan
    Dictionary Should Contain Value  ${resp.json['form']}  evcimen

Post With Unicode Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=True
    &{data}=  Create Dictionary  name=度假村
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log    ${resp.json['form'].toString()}
    Dictionary Should Contain Value  ${resp.json['form']}  度假村

Post Request With Unicode Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=True
    &{data}=  Create Dictionary  name=度假村
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Dictionary Should Contain Value  ${resp.json['form']}  度假村

Post Request With Binary Data in Dictionary
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=True
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{data}=  Create Dictionary  name=${file_data.strip()}
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log  ${resp.json['form']}
    Log  ${resp.json['form']['name']}
    Should Contain  ${resp.json['form']['name']}  \u5ea6\u5047\u6751

Post Request With Binary Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=True
    ${data}=  Get Binary File  ${CURDIR}${/}data.json
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log  ${resp.json.toString()}
    Log  ${resp.json['form'].toString()}
    Should Contain  ${resp.json['form'].toString()}  度假村

Post Request With Arbitrary Binary Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=True
    ${data}=  Get Binary File  ${CURDIR}${/}randombytes.bin
    &{headers}=  Create Dictionary  Content-Type=application/octet-stream   Accept=application/octet-stream
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=&{headers}
    # TODO Compare binaries. Content is json with base64 encoded data
    Log    "Success"

Post With File
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{files}=  Create Dictionary  file=${file_data}
    ${resp}=  Post Request  httpbin  /post  files=${files}
    ${file}=  To Json  ${resp.json['files']['file']}
    Dictionary Should Contain Key  ${file}  one
    Dictionary Should Contain Key  ${file}  two


Post Request With File
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{files}=  Create Dictionary  file=${file_data}
    ${resp}=  Post Request  httpbin  /post  files=${files}
    ${file}=  To Json  ${resp.json['files']['file']}
    Dictionary Should Contain Key  ${file}  one
    Dictionary Should Contain Key  ${file}  two
    
Post Request With Redirection
    [Tags]  post
    Create Session  jigsaw  http://jigsaw.w3.org
    ${resp}=  Post Request  jigsaw  /HTTP/300/302.html
    Should Be Equal As Strings  ${resp.status_code}  200
    ${resp}=  Post Request  jigsaw  /HTTP/300/302.html  allow_redirects=${true}
    Should Be Equal As Strings  ${resp.status_code}  200

Post Request Without Redirection
    [Tags]  post
    Create Session  jigsaw  http://jigsaw.w3.org    debug=True
    ${resp}=  Post Request  jigsaw  /HTTP/300/302.html  allow_redirects=${false}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30