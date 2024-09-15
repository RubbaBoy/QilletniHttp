import "http:http.ql"

HttpClient client = new HttpClient()
JsonConverter json = JsonConverter.createJsonConverter(true)

HttpRequest request = HttpRequest.createGet("https://f352e044-5a70-40e6-90af-e17f384293b0.mock.pstmn.io/test", Map.fromList(["User-Agent", "Qilletni-http/1.0.0", "Content-Type", "application/json"]), Map.fromList(["one", "two"]))

HttpResponse response = client.send(request)

print("Response status: " + response.statusCode)

print(response.body)

Map mapBody = json.fromJson(response.body)

print("msg = " + mapBody.get("msg"))

print("Posting!")

HttpRequest postRequest = HttpRequest.createPost("https://f352e044-5a70-40e6-90af-e17f384293b0.mock.pstmn.io/posttest", Map.fromList(["User-Agent", "Qilletni-http/1.0.0", "Content-Type", "application/json"]), Map.fromList(any["one", "two", "three", any[1, 2, 3, 4]]))
HttpResponse postResponse = client.send(postRequest)

print("Post response status: " + postResponse.statusCode)
if (postResponse.statusCode == 200) {
    print("Message: " + json.fromJson(postResponse.body).get("msg"))
    print(postResponse.headers)
    if (postResponse.headers.containsKey("some-header")) {
        print("Response Headers: " + postResponse.headers.get("some-header"))
    } else {
        print("Response Headers: No header found")
    }
}

