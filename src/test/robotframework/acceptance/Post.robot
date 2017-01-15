*** Settings ***
Resource    common.robot

*** Test Cases ***
Post Request With URL Params
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    &{params}=   Create Dictionary   key=value     key2=value2
    Post Request  httpbin  /post		params=${params}
    Response Code Should Be  httpbin  200

Post Request With No Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    Post Request  httpbin  /post  
    Response Code Should Be  httpbin  200

Post Request With No Dictionary
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=3
    Set Test Variable  ${data}  some content
    Post Request  httpbin  /post  data=${data}
    Response Code Should Be  httpbin  200
    Response Should Contain  httpbin  ${data}
    
Post Requests
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    &{data}=  Create Dictionary  name=bulkan  surname=evcimen
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    Post Request  httpbin  /post  data=${data}  headers=${headers}
    ${jsondata}=  Get JSON Response  httpbin
    Dictionary Should Contain Value  ${jsondata['form']}  bulkan
    Dictionary Should Contain Value  ${jsondata['form']}  evcimen

Post With Unicode Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=3
    &{data}=  Create Dictionary  name=度假村
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    Post Request  httpbin  /post  data=${data}  headers=${headers}
    ${jsondata}=  Get JSON Response  httpbin
    Dictionary Should Contain Value  ${jsondata['form']}  度假村

Post Request With Binary Data in Dictionary
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=3
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{data}=  Create Dictionary  name=${file_data.strip()}
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log  ${resp.json()['form']}
    Should Contain  ${resp.json()['form']['name']}  \u5ea6\u5047\u6751

Post Request With Binary Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=3
    ${data}=  Get Binary File  ${CURDIR}${/}data.json
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log  ${resp.json()['form']}
    ${value}=  evaluate  list(${resp.json()}['form'].keys())[0]
    Should Contain  ${value}  度假村

Post Request With Arbitrary Binary Data
    [Tags]  post
    Create Session  httpbin  http://httpbin.org    debug=3
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
    ${file}=  To Json  ${resp.json()['files']['file']}
    Dictionary Should Contain Key  ${file}  one
    Dictionary Should Contain Key  ${file}  two


Post Request With File
    [Tags]  post
    Create Session  httpbin  http://httpbin.org
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{files}=  Create Dictionary  file=${file_data}
    ${resp}=  Post Request  httpbin  /post  files=${files}
    ${file}=  To Json  ${resp.json()['files']['file']}
    Dictionary Should Contain Key  ${file}  one
    Dictionary Should Contain Key  ${file}  two

Post Request With Data and File
    [Tags]    post
    Create Session    httpbin    http://httpbin.org
    &{data}=    Create Dictionary    name=mallikarjunarao    surname=kosuri
    Create File    foobar.txt    content=foobar
    ${file_data}=    Get File    foobar.txt
    &{files}=    Create Dictionary    file=${file_data}
    ${resp}=    Post Request    httpbin    /post    files=${files}    data=${data}
    Should Be Equal As Strings    ${resp.status_code}    200
    
Post Request With Redirection
    [Tags]  post
    Create Session  jigsaw  http://jigsaw.w3.org
    ${resp}=  Post Request  jigsaw  /HTTP/300/302.html
    Should Be Equal As Strings  ${resp.status_code}  200
    ${resp}=  Post Request  jigsaw  /HTTP/300/302.html  allow_redirects=${true}
    Should Be Equal As Strings  ${resp.status_code}  200

Post Request Without Redirection
    [Tags]  post
    Create Session  jigsaw  http://jigsaw.w3.org    debug=3
    ${resp}=  Post Request  jigsaw  /HTTP/300/302.html  allow_redirects=${false}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30