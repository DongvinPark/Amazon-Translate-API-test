package org.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

public class Main {
    public static void main(String[] args) {
        // Amazon Translate API를 호출하는 예제코드이다.
        // 실행하면,
        // Translated Text: 안녕하세요, 어떻게 지내세요?
        // 라는 결과물이 출력된다.

        String accessKey = "AWS-CLI-PUBLIC-ACCESS-KEY";
        String secretKey = "AWS-CLI-SECRET-ACCESS-KEY";

        // Create AWS credentials
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        TranslateClient translateClient = TranslateClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String textToTranslate = "Hello, how are you?";
        String sourceLanguage = "en";
        String targetLanguage = "ko";

        TranslateTextRequest request = TranslateTextRequest.builder()
                .text(textToTranslate)
                .sourceLanguageCode(sourceLanguage)
                .targetLanguageCode(targetLanguage)
                .build();

        TranslateTextResponse response = translateClient.translateText(request);

        System.out.println("Translated Text: " + response.translatedText());

        translateClient.close();
    }//main
}//end of Main class