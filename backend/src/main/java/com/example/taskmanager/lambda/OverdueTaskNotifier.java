package com.example.taskmanager.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

public class OverdueTaskNotifier implements RequestHandler<Object, String> {

    private static final String SNS_TOPIC_ARN = "arn:aws:sns:us-east-1:123456789012:MyTopic";

    private final AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();

    @Override
    public String handleRequest(Object input, Context context) {
        try {
            String today = LocalDate.now().toString();
            String urlStr = "https://localhost:8080/api/v1/tasks?dueBefore=" + today + "&completed=false";

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

                JSONArray tasks = new JSONArray(responseBody.toString());

                for (int i = 0; i < tasks.length(); i++) {
                    JSONObject task = tasks.getJSONObject(i);
                    String id = task.optString("id", "N/A");
                    String name = task.optString("name", "Unnamed Task");
                    String dueDate = task.optString("dueDate", "Unknown");

                    String alert = String.format("🔔 Overdue Task:\n• ID: %s\n• Name: %s\n• Due: %s", id, name, dueDate);
                    context.getLogger().log(alert);

                    // Send alert to SNS
                    sendAlert(alert, context);
                }
            }

            con.disconnect();
            return "Lambda executed successfully";

        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            return "Lambda failed";
        }
    }

    private void sendAlert(String message, Context context) {
        try {
            snsClient.publish(SNS_TOPIC_ARN, message, "Overdue Task Alert");
            context.getLogger().log("Alert sent to SNS: " + message);
        } catch (Exception e) {
            context.getLogger().log("Failed to send SNS alert: " + e.getMessage());
        }
    }
}
