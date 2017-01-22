*** Settings ***
Resource    common.robot

*** Test Cases ***
Patch Requests
    [Tags]    patch
    Create Session    httpbin    http://httpbin.org
    &{data}=    Create Dictionary    name=bulkan    surname=evcimen
    &{headers}=    Create Dictionary    Content-Type=application/x-www-form-urlencoded
    ${resp}=    Patch Request    httpbin    /patch    data=${data}    headers=${headers}
    Dictionary Should Contain Value    ${resp.json['form']}    bulkan
    Dictionary Should Contain Value    ${resp.json['form']}    evcimen