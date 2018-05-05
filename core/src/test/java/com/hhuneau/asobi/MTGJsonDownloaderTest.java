package com.hhuneau.asobi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MTGJsonDownloaderTest {

    @Autowired
    private MTGJsonDownloader downloader;

    @Test
    @Ignore
    public void download() throws IOException {
        downloader.download();
    }
}
