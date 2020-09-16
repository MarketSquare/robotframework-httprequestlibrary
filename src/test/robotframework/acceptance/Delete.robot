*** Settings ***
Resource    common.robot
Force Tags    DELETE
*** Test Cases ***
Delete Request With URL Params
    Create Session  httpbin  ${testServer} 
    &{params}=   Create Dictionary   key=value     key2=value2
    ${resp}=  Delete Request  httpbin  /delete		${params}
    Should Be Equal As Strings  ${resp.status_code}  200


Delete Request With No Data
    Create Session  httpbin  ${testServer} 
    ${resp}=  Delete Request  httpbin  /delete
    Should Be Equal As Strings  ${resp.status_code}  200


Delete Request With Data
    Create Session  httpbin  ${testServer}     debug=True
    &{data}=  Create Dictionary  name=bulkan  surname=evcimen
    ${resp}=  Delete Request  httpbin  /delete  data=${data}
    Should Be Equal As Strings  ${resp.status_code}  200
    Log  ${resp.content}
    Comment  Dictionary Should Contain Value  ${resp.json['data']}  bulkan
    Comment  Dictionary Should Contain Value  ${resp.json['data']}  evcimen
    
Delete Request With JSON Data
    ${JSON_DATA}  Set Variable  {"file":{"path":"/logo1.png"},"token":"some-valid-oauth-token"}
    Create Session   httpbin   ${testServer} 
    &{headers}=  Create Dictionary  Content-Type=application/json
    ${resp}=   Delete Request   httpbin   /delete   data=${JSON_DATA}   headers=${headers}
    Should Be Equal As Strings  ${JSON_DATA}  ${resp.json['data']}