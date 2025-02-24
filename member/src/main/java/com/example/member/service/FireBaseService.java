package com.example.member.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class FireBaseService {

    @Value("${image.base.url}")
    private String imageBaseUrl;

    public String upload(MultipartFile imageFile,int memberId) throws IOException, InterruptedException {
        String imageId = System.currentTimeMillis() + "_" + String.valueOf(memberId);
        InputStream inputStream=imageFile.getInputStream();
        Bucket bucket= StorageClient.getInstance().bucket();
        bucket.create(imageId,inputStream,"image/jpeg");
        String imageUrl=getImageUrl(imageId);
        System.out.println("imageUrl "+imageUrl);
        return imageUrl;
    }

    public String getImageUrl(String imageId) throws IOException, InterruptedException {
        String imageUrl=imageBaseUrl+imageId;
        HttpClient client=HttpClient.newHttpClient();
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .build();

        HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
        String jsonResponse=response.body();
        JsonNode node =new ObjectMapper().readTree(jsonResponse);
        String imageToken=node.get("downloadTokens").asText();
        return imageUrl + "?alt=media&token=" + imageToken;

    }


}
