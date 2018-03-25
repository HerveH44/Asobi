package com.hhuneau.asobi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);
    private final MTGJsonDownloader downloader;

    public WebController(MTGJsonDownloader downloader) {
        this.downloader = downloader;
    }

    @GetMapping("/download")
    public void triggerDownload() throws IOException {
        LOGGER.info("downloading");
        downloader.download();
    }
}
