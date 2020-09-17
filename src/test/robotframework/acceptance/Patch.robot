*** Settings ***
Resource    common.robot
Force Tags    PATCH

*** Test Cases ***
Patch Requests
    Create Session    httpbin    ${testServer} 
    &{data}=    Create Dictionary    name=bulkan    surname=evcimen
    &{headers}=    Create Dictionary    Content-Type=application/x-www-form-urlencoded
    ${resp}=    Patch Request    httpbin    /patch    data=${data}    headers=${headers}
    Dictionary Should Contain Value    ${resp.json['form']}    bulkan
    Dictionary Should Contain Value    ${resp.json['form']}    evcimen