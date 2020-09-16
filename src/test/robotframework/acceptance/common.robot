*** Settings ***
Library    HttpRequestLibrary
Library    Collections
Library    String
Library    OperatingSystem

*** Variables ***
${testServer}    http://httpbin.org
${proxyPort}     8080

*** Keywords ***
