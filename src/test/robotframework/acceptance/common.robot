*** Settings ***
Library    HttpRequestLibrary
Library    Collections
Library    String
Library    OperatingSystem

*** Variables ***
${testServer}    ${${environment}TestServer}
${proxyPort}     ${${environment}ProxyPort}

*** Keywords ***
