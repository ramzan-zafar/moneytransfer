package helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestResponse {

    public final String body;
    public final int status;

    public TestResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public JsonElement jsonElement() {
        return new JsonParser().parse(body);
    }
}
