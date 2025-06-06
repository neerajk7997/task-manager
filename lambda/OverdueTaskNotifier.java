package com.example.taskmanager.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

public class OverdueTaskNotifier implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        try {
            String today = LocalDate.now().toString();
            String urlStr = "https://your-api-url.com/api/v1/tasks?dueBefore=" + today + "&completed=false";

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            context.getLogger().log("Response Code: " + status);

            if (status == 200) {
                Scanner scanner = new Scanner(con.getInputStream());
                StringBuilder responseBody = new StringBuilder();
                while(scanner.hasNext()) {
                    responseBody.append(scanner.nextLine());
                }
                scanner.close();

                // Log each overdue task (assuming JSON array response)
                context.getLogger().log("Overdue Tasks: " + responseBody.toString());
                // Here you can parse JSON and send alerts (email, SNS, etc.)
            }

            con.disconnect();
            return "Lambda executed successfully";

        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            return "Lambda failed";
        }
    }
}
