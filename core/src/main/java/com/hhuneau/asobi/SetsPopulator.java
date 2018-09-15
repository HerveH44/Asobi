package com.hhuneau.asobi;

import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SetsPopulator implements ApplicationListener<ContextRefreshedEvent> {

    private final MTGSetsService setsService;
    private final MTGJsonDownloader downloader;

    public SetsPopulator(MTGSetsService setsService, MTGJsonDownloader downloader) {
        this.setsService = setsService;
        this.downloader = downloader;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (setsService.isEmpty()) {
            try {
                downloader.download();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
