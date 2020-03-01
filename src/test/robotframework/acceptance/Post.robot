*** Settings ***
Resource    common.robot
Force Tags    POST

*** Test Cases ***
Post Request With URL Params
    Create Session  httpbin  http://httpbin.org
    &{params}=   Create Dictionary   key=value     key2=value2
    ${resp}=  Post Request  httpbin  /post		params=${params}
    Should Be Equal As Strings  ${resp.status_code}  200

Post Request With No Data
    Create Session  httpbin  http://httpbin.org
    ${resp}=  Post Request  httpbin  /post  
    Should Be Equal As Strings  ${resp.status_code}  200
    
Post Request With No Dictionary
    Create Session  httpbin  http://httpbin.org    debug=True
    Set Test Variable  ${data}  some content
    ${resp}=  Post Request  httpbin  /post  data=${data}
    Should Be Equal As Strings  ${resp.status_code}  200
    Should Contain  ${resp.text}  ${data}
    
Post Request With No Dictionary (plain JSON)
    Create Session  httpbin  http://httpbin.org    debug=True
    Set Test Variable  ${data}  {"two": 2, "one": 1}
    ${resp}=  Post Request  httpbin  /post  data=${data}
    Log    ${resp}
    Should Be Equal As Strings  ${resp.status_code}  200
    Should Be Equal As Strings  ${resp.json['data'].replace('\"','"')}  ${data}
    
Post Request With No Dictionary (plain JSON) inside array (Keycloak)
    Create Session  httpbin  http://httpbin.org    debug=True
    Set Test Variable  ${data}  [{"id":"role-id","name":"rolename","scopeParamRequired":false,"composite":false,"clientRole":true,"containerId":"id"}]
    ${resp}=  Post Request  httpbin  /post  data=${data}
    Log    ${resp}
    Should Be Equal As Strings  ${resp.status_code}  200
    Should Be Equal As Strings  ${resp.json['data'].replace('\"','"')}  ${data}
    
Post Request With Dictionary inside array (Keycloak)
    Create Session  httpbin  http://httpbin.org    debug=True
    ${testDict}    Create Dictionary    id=role-id    name=rolename    scopeParamRequired=${False}    composite=${False}   clientRole=${True}    containerId=id
    ${data}    Create List    ${testDict}
    ${dataAsJson}    Evaluate    json.dumps(${data})    modules=json
    ${resp}=  Post Request  httpbin  /post  data=${data}
    Log    ${resp}
    Should Be Equal As Strings  ${resp.status_code}  200
    ${respAsJson}    Evaluate    json.dumps(${resp.json['data']})    modules=json
    Should Be Equal As Strings  ${respAsJson}  ${dataAsJson}
    
Post Requests
    Create Session  httpbin  http://httpbin.org
    &{data}=  Create Dictionary  name=bulkan  surname=evcimen
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log  ${resp.json}
    Dictionary Should Contain Value  ${resp.json['form']}  bulkan
    Dictionary Should Contain Value  ${resp.json['form']}  evcimen

Post With Unicode Data
    Create Session  httpbin  http://httpbin.org    debug=True
    &{data}=  Create Dictionary  name=度假村
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log    ${resp.json['form'].toString()}
    Dictionary Should Contain Value  ${resp.json['form']}  度假村

Post Request With Unicode Data
    Create Session  httpbin  http://httpbin.org    debug=True
    &{data}=  Create Dictionary  name=度假村
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Dictionary Should Contain Value  ${resp.json['form']}  度假村
    
Post With Unicode Data Without Dictionary
    Create Session  httpbin  http://httpbin.org    debug=True
    ${data}=  Set Variable    度假村
    ${resp}=  Post Request  httpbin  /post  data=${data}
    Log    ${resp}
    Should Contain  ${resp.text}  \\u5ea6\\u5047\\u6751

Post Request With Binary Data in Dictionary
    Create Session  httpbin  http://httpbin.org    debug=True
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{data}=  Create Dictionary  name=${file_data.strip()}
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Log  ${resp.json['form']}
    Log  ${resp.json['form']['name']}
    Should Contain  ${resp.json['form']['name']}  \u5ea6\u5047\u6751

Post Request With Data From File
    Create Session  httpbin  http://httpbin.org    debug=True
    ${data}=  Get File  ${CURDIR}${/}data.json
    &{headers}=  Create Dictionary  Content-Type=application/x-www-form-urlencoded
    ${resp}=  Post Request  httpbin  /post  data=${data.strip()}  headers=${headers}
    Log  ${resp.json['form'].toString()}
    Should Contain  ${resp.json['form'].toString()}  度假村

Post Request With Arbitrary Binary Data
    Create Session  httpbin  http://httpbin.org    debug=True
    ${data}=  Get Binary File  ${CURDIR}${/}randombytes.bin
    &{headers}=  Create Dictionary  Content-Type=application/octet-stream   Accept=application/octet-stream
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=&{headers}
    # TODO Compare binaries. Content is json with base64 encoded data
    Log    "Success"

Post With File
    Create Session  httpbin  http://httpbin.org    debug=True
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{files}=  Create Dictionary  file=${file_data}
    ${resp}=  Post Request  httpbin  /post  files=${files}
    ${file}=  To Json  ${resp.json['form']['file']}
    Dictionary Should Contain Key  ${file}  one
    Dictionary Should Contain Key  ${file}  two


Post Request With File
    Create Session  httpbin  http://httpbin.org    debug=True
    ${file_data}=  Get Binary File  ${CURDIR}${/}data.json
    &{files}=  Create Dictionary  file=${file_data}
    ${resp}=  Post Request  httpbin  /post  files=${files}
    ${file}=  To Json  ${resp.json['form']['file']}
    Dictionary Should Contain Key  ${file}  one
    Dictionary Should Contain Key  ${file}  two
    
Post Request With Redirection
    Create Session  httpbin  http://httpbin.org    debug=True
    &{params}=  Create Dictionary  url=https://httpbin.org
    ${resp}=  Post Request  httpbin  /redirect-to    params=${params}
    Should Be Equal As Strings  ${resp.status_code}  200
    ${resp}=  Post Request  httpbin  /redirect-to    params=${params}  allow_redirects=${True}
    Should Be Equal As Strings  ${resp.status_code}  200

Post Request Without Redirection
    Create Session  httpbin  http://httpbin.org    debug=True
    &{params}=  Create Dictionary  url=https://httpbin.org
    ${resp}=  Post Request  httpbin  /redirect-to    params=${params}  allow_redirects=${False}
    ${status}=  Convert To String  ${resp.status_code}
    Should Start With  ${status}  30
    
Post Request With XML File
    Create Session  httpbin  http://httpbin.org
    ${file_data}=  Get File  ${CURDIR}${/}test.xml
    &{files}=  Create Dictionary  xml=${file_data}
    &{headers}=  Create Dictionary  Authorization=testing-token
    Log  ${headers}
    ${resp}=  Post Request  httpbin  /post  files=${files}  headers=${headers}
    Log  ${resp.json}
    Set Test Variable  ${req_headers}  ${resp.json['headers']}
    Dictionary Should Contain Key  ${req_headers}  Authorization    
    
Encoding Error
    Create Session   httpbin   http://httpbin.org
    &{headers}=  Create Dictionary  Content-Type=application/json
    Set Suite Variable  ${data}   { "elementToken":"token", "matchCriteria":[{"field":"name","dataType":"string","useOr":"false","fieldValue":"Operation check 07", "closeParen": "false", "openParen": "false", "operator": "equalTo"}], "account": { "annualRevenue": "456666", "name": "Account", "numberOfEmployees": "integer", "billingAddress": { "city": "Miami", "country": "US", "countyOrDistrict": "us or fl", "postalCode": "33131", "stateOrProvince": "florida", "street1": "Trade Center", "street2": "North Main rd" }, "number": "432", "industry": "Bank", "type": "string", "shippingAddress": { "city": "denver", "country": "us", "countyOrDistrict": "us or co", "postalCode": "80202", "stateOrProvince": "colorado", "street1": "Main street", "street2": "101 Avenu"}}}
    ${resp}=  Post Request  httpbin  /post  data=${data}  headers=${headers}
    Should Be Equal As Strings  ${resp.status_code}  200