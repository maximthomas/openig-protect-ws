# Protect Web-Services with OpenIG

## How to run

### Build
Build sample service project
```bash
$ mvnw install
```

Build and run docker images 
```bash
$ docker-compose up --build
```  

### Test requests

Test unathorized service
```bash
curl -v -X POST -H 'Content-Type: application/json' -H 'Accept: application/json'  http://localhost:8080/secure
```  


Test athorized service
```
curl -v -X POST -H 'Content-Type: application/json' -H 'Accept: application/json' -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzYW1wbGUtc2VydmljZSIsInN1YiI6IjEyMzQ1Njc4OTAiLCJuYW1lIjoiSm9obiBEb2UiLCJhZG1pbiI6dHJ1ZSwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE2MTYyMzkwMjJ9.Vxo5eGo9WVhHrf5cVO1Rjam9RfyrUSCdVeAr6mZz7bsNbR7cPz-qHHH-2Odrrk1INVyPgQH0yukf-n58WWZhAG0rTOkMF__eSSLDj-I85PxniJQvd85-cHdCyaklHJ1hA4LVSP27tJMc_7VBEuwnFB2XlqQs7dLbqmCd6skvDpMJASLHcwYadg-i-2os9hJsc6d1uzlpeJaaUxb-TD-ofEJpAfD9YaMO595bOFJvtNVhbXRxEDgAn2w73MS9GkC-uL91SuS1nQEy9HKxRQUHBstLNiuPtW3zdmI9f0SWqbaTqEz428S67VKQ6RuMVIMN5vjlqclJqVYNO2nyrc4TkyV-1PYJdlWyDRdbcCHuh0JWdC2aBa7bEBN0QTAOhCx6hflo5DEW8ehTp_9LsSI1hJTcyGBmQkc5EhvDMsrjxh2K1x-Zsa_2cBJbczSvk4TL70FT32FRpzJs-mdwID_lvoK4LFnA51z79jN0e8yXlHXJLPGsv8DhZxTziDGbBSBEiqxLGkelto6x85b8YJ-Y4dUgpoIlXJ7B6SsgNo6jcQBuYvgtD5c7cKHraGIZCu_i_exCyqd6CupsBzONTIqKwnzEVNtx1Ars5PfN3ncVN6CE_tIYOYYVBD_Dl9vlAWtImqhaxb5XoEOv5bKUbRNMIEDYy-3_K5w_BLquESc39MM' http://localhost:8080/secure
```  
