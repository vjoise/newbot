package com.stampbot.service.task.provider;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        PropertiesClient propertiesClient = new PropertiesClient();
        JiraOAuthClient jiraOAuthClient = new JiraOAuthClient(propertiesClient);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String elements[] = scanner.nextLine().split(" ");
            List<String> argumentsWithoutFirst = Arrays.asList(elements).subList(1, elements.length);
            new OAuthClient(propertiesClient, jiraOAuthClient).execute(Command.fromString(elements[0]), argumentsWithoutFirst);
        }
    }
}
