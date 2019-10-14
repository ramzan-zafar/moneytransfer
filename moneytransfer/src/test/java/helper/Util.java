package helper;

import helper.TestResponse;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.fail;

public class Util {
    public static TestResponse request(String method, String path) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://localhost:8088" + path);
            connection  = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        } finally {
            connection.disconnect();
        }
    }
}
