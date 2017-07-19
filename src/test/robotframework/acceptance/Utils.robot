*** Settings ***
Resource    common.robot
Suite Setup    Setup Suite

*** Test Cases ***
Do Not Pretty Print a JSON object
    [Tags]    json
    Should Be Equal As Strings    ${resp.status_code}    200
    ${jsondata}=    To Json    ${resp.content}
    Dictionaries Should Be Equal   ${jsondata['args']}    ${var}

Pretty Print a JSON object
    [Tags]    json
    Comment    Define json variable.
    Log    ${resp}
    ${output}=    Pretty Print Json    ${resp.content}
    Log    ${output}
    Should Contain    ${output}    "key_one": "true"
    Should Contain    ${output}    "key_two": "this is a test string"
    Should Not Contain    ${output}    {u'key_two': u'this is a test string', u'key_one': u'true'}

Set Pretty Print to non-Boolean value
    [Tags]    json
    Comment    Define json variable.
    Log    ${resp}
    ${output}=    Pretty Print Json    ${resp.content}
    Log    ${output}
    Should Contain    ${output}    "key_one": "true"
    Should Contain    ${output}    "key_two": "this is a test string"
    Should Not Contain    ${output}    {u'key_two': u'this is a test string', u'key_one': u'true'}
    
Pretty Print Json List
    [Tags]    json
    ${jsonString}    Set Variable    [{"key_one": "true","key_two": "this is a test string"}]
    ${output}    Pretty Print Json    ${jsonString}
    Log    ${output}
    Should Contain    ${output}    "key_one": "true"
    Should Contain    ${output}    "key_two": "this is a test string"
    Should Not Contain    ${output}    ${jsonString}
   

Json Parsing Tests
    [Tags]    json
    ${jsonString}    Set Variable    {"key_one": "true","key_two": "this is a test string"}
    ${json}    To Json    ${jsonString}
    Should Be Equal As Strings    ${json['key_one']}    true
    Should Be Equal As Strings    ${json['key_two']}    this is a test string

Json Parsing Tests With List
    [Tags]    json
    ${jsonString}    Set Variable    [{"key_one": "true","key_two": "this is a test string"}]
    ${json}    To Json    ${jsonString}
    Should Be Equal As Strings    ${json[0]['key_one']}    true
    Should Be Equal As Strings    ${json[0]['key_two']}    this is a test string
    
    
*** Keywords ***
Setup Suite
    Comment    Define json variable.
    Create Session  httpbin  http://httpbin.org
    &{var}=    Create Dictionary    key_one=true    key_two=this is a test string
    Set Suite Variable    ${var}
    ${resp}=    Get Request    httpbin    /get    params=${var}
    Set Suite Variable    ${resp}