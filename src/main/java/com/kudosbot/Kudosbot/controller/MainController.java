package com.kudosbot.Kudosbot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.slack.api.Slack;

import com.kudosbot.Kudosbot.utils.CommandBody;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import java.util.Random;


@RestController
@RequestMapping(value = "/")
public class MainController {

    @Autowired
    private Environment env;

    @PostMapping
    public ResponseEntity<String> handleCommand(CommandBody body) throws Exception {

        if( !body.getToken().equals(env.getProperty( "slack.token") ) ){
            throw new Exception("Invalid token");
        }

        String[] splitString = body.getText().split(" ", 2);

        String receiver = splitString[0];

        String custom_message = splitString[1];

        Slack slack = Slack.getInstance();

        MethodsClient methods = slack.methods(env.getProperty("slack.auth.token"));

        String message = "\n\n Hi " + receiver + " :wave:\nYou've just been kudo-ed by <@" + body.getUser_id() + ">.";

        String fullMessage = message.concat("\n_" + custom_message + "_ :cherry_blossom:\nSay thank you now! :cake:");

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(env.getProperty("slack.channel"))
                .text(fullMessage)
                .build();

        ChatPostMessageResponse response = methods.chatPostMessage(request);


        String[] responses = {
                "Fantastic! You just made someone's day better :dog: ",
                "They get a kudos! You get a kudos! EVERYBODY :clap: GETS :clap: KUDOS :clap:",
                "Good shit! Keep that shit up, yo :sunglasses:",
                "Tu toh dev manus nikla re!"
        };

        Random random = new Random();

        int index = random.nextInt(responses.length);

        return ResponseEntity.ok(responses[index]);

    }

}
