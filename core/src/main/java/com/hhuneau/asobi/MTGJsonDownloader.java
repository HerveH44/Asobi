package com.hhuneau.asobi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.sets.MTGSet;
import com.hhuneau.asobi.sets.MTGSetsService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class MTGJsonDownloader {
    private static final String URI = "http://mtgjson.com/json/AllSets.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(MTGJsonDownloader.class);

    private final ObjectMapper mapper;
    private final MTGSetsService setsService;

    public MTGJsonDownloader(ObjectMapper mapper, MTGSetsService setsService) {
        this.mapper = mapper;
        this.setsService = setsService;
    }

    @Async
    public void download() throws IOException {

        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(URI);
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            final HttpEntity entity1 = response1.getEntity();
            final InputStream inputStream = entity1.getContent();
            final File file = saveToJsonFile(inputStream);
            final Map<String, MTGSet> sets = mapper.readValue(file, new TypeReference<Map<String, MTGSet>>() {
            });
            sets.forEach((setName, mtgSet) -> {
                try {
                    mtgSet.getCards().forEach(mtgCard -> {
                        mtgCard.setSet(mtgSet);
                        setsService.saveCard(mtgCard);
                    });
                    setsService.saveSet(mtgSet);
                } catch (Exception e) {
                    LOGGER.error("can't save " + setName + " " + e.getMessage());
                }
            });

        }

    }

    private File saveToJsonFile(InputStream inputStream) throws IOException {
        final File file = File.createTempFile("temp-sets", ".json");
        final OutputStream outputStream = new FileOutputStream(file);

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        outputStream.close();
        return file;
    }


}
