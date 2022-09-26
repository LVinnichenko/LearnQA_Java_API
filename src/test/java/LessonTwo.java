import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LessonTwo {

    @Test
    public void getJsonTest(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message_2 = response.get("messages.message[1]");
        System.out.println(message_2);
    }
    @Test
    public void ex6Test(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        response.prettyPrint();

        String headers = response.getHeader("location");
        System.out.println(headers);
    }
}
