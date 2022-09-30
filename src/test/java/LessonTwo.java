import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.lang.Thread;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void ex7Test() {
        int respCode = 0;
        int count = 0;
        String location = "https://playground.learnqa.ru/api/long_redirect";

        while (respCode != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(location)
                    .andReturn();
            response.prettyPrint();

            location = response.getHeader("location");
            respCode = response.getStatusCode();
            count++;
        }
        System.out.println(count);
    }
    @Test
    public void ex8Test() throws InterruptedException {
        //создание задачи и получение токена и time
        JsonPath resp = createTask();
        int time = resp.get("seconds");
        String token = resp.get("token");

        //вызов метода до того, как задача будет готова, проверка статуса задачи
        JsonPath resp_with_status = getResp(token);
        String status = resp_with_status.get("status");
        assertEquals(status,"Job is NOT ready");

        //ожидание готовности задачи
        long pause = Long.valueOf(time)*1000;
        Thread.sleep(pause);

        //вызов метода после готовности задачи
        JsonPath resp_after_sleep = getResp(token);
        //проверка статуса задачи
        String status_after_sleep = resp_after_sleep.get("status");
        assertEquals(status_after_sleep,"Job is ready");
        //проверка наличия результата
        String result = resp_after_sleep.get("result");
        assertNotNull(result);
    }

    private JsonPath createTask() {
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        return response;
    }
    private JsonPath getResp(String token) {
        JsonPath response = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        return response;
    }
}
