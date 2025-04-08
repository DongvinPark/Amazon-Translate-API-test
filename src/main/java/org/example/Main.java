package org.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Main {

    public static final String SECRET_KEY_FOR_ACCESS_KEY_FILE = "?";

    public static void main(String[] args) {
        // Amazon Translate API를 호출하는 예제코드이다.
        // 실행하면,
        // Translated Text: 안녕하세요, 어떻게 지내세요?
        // 라는 결과물이 출력된다.

        String encryptedKes = readFromFile("/Users/dongvin99/Documents/api-keys.txt");
        String[] decryptedKeys = decrypt(encryptedKes).split(",");

        String accessKey = decryptedKeys[0];
        String secretKey = decryptedKeys[1];

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

        String textToTranslate2 = "안녕? 난 동빈 이야.";
        String sourceLanguage2 = "ko";
        String targetLanguage2 = "en";

        TranslateTextRequest request2 = TranslateTextRequest.builder()
                .text(textToTranslate2)
                .sourceLanguageCode(sourceLanguage2)
                .targetLanguageCode(targetLanguage2)
                .build();

        TranslateTextResponse response2 = translateClient.translateText(request2);

        System.out.println("Translated Text: " + response2.translatedText());

        translateClient.close();
    }//main

    public static String readFromFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            return "";
        }
    }

    public static String decrypt(String encryptedSdpMessage) {
        try {
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY_FOR_ACCESS_KEY_FILE.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedSdpMessage));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}//end of Main class