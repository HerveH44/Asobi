package com.hhuneau.asobi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;

@Component
public class MTGJsonDownloader {
    private static final String URI = "http://mtgjson.com/json/AllSets.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(MTGJsonDownloader.class);
    private static final List<String> allowedTypes = List.of("masterpiece", "expansion", "core", "commander", "planechase", "starter", "un");
    private static final List<String> allowedSets = List.of("EMA", "MMA", "VMA", "CNS", "TPR", "MM2", "CN2", "MM3", "IMA");

    private final ObjectMapper mapper;
    private final MTGSetsService setsService;

    public MTGJsonDownloader(ObjectMapper mapper, MTGSetsService setsService) {
        this.mapper = mapper;
        this.setsService = setsService;
    }

    void download() throws IOException {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(URI);
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            final HttpEntity entity1 = response1.getEntity();
            final InputStream inputStream = entity1.getContent();
            final File file = saveToJsonFile(inputStream);
            importSetsFromFile(file);
        }
    }

    void download(String path) {
        final File file = new File(path);
        try {
            importSetsFromFile(file);
        } catch (IOException e) {
            LOGGER.error(String.format("cannot import file with path %s : %s", path, e.getMessage()));
        }
    }

    private void importSetsFromFile(File file) throws IOException {
        LOGGER.info("Parsing json sets from file " + file.getAbsolutePath());
        final Map<String, MTGSet> sets = mapper.readValue(file, new TypeReference<Map<String, MTGSet>>() {
        });
        sets.forEach((setName, mtgSet) -> {
            if (!allowedTypes.contains(mtgSet.getType()) && !allowedSets.contains(mtgSet.getCode())) {
                return;
            }
            try {
                LOGGER.info("saving set {}", mtgSet.getCode());
                setsService.saveSet(mtgSet);
                LOGGER.info("set {} successfully saved", mtgSet.getCode());
            } catch (Exception e) {
                LOGGER.error("can't save " + setName + " " + e.getCause());
            }
        });
        LOGGER.info("Finished importing MTGJson sets");
    }

    private File saveToJsonFile(InputStream inputStream) throws IOException {
        final File file = File.createTempFile("temp-sets", ".json");
        final OutputStream outputStream = new FileOutputStream(file);

        int read;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        outputStream.close();
        return file;
    }

}
