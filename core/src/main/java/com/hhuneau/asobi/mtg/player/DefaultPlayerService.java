package com.hhuneau.asobi.mtg.player;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DefaultPlayerService implements PlayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPlayerService.class);
    private final PlayerRepository playerRepository;

    public DefaultPlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void save(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void disconnectPlayersWithSession(String sessionId) {
        final List<Player> playerList = playerRepository.findAllByUserId(sessionId).stream()
            .peek(player -> player.setUserId(""))
            .collect(Collectors.toList());
        playerRepository.saveAll(playerList);
    }

    @Override
    public void hashDeck(Player player, List<String> main, List<String> side) {
        //Cockatrice Hash
        final String cockHash = makeCockatriceHash(main, side);
        player.getPlayerState().setCockHash(cockHash);
        playerRepository.save(player);

        //MWS Hash
        final String mwsHash = makeMWSHash(main, side);
        player.getPlayerState().setMwsHash(mwsHash);
        playerRepository.save(player);
    }

    private String makeMWSHash(List<String> main, List<String> side) {
        final List<String> mainList = main.stream()
            .map(name -> name.toUpperCase().replaceAll("[^A-Z]", ""))
            .collect(Collectors.toList());
        final List<String> sideList = side.stream()
            .map(name -> "#" + name.toUpperCase().replaceAll("[^A-Z]", ""))
            .collect(Collectors.toList());
        mainList.addAll(sideList);
        Collections.sort(mainList);
        final String toHash = mainList.stream().reduce((s, s2) -> s + "" + s2).orElse("");
        try {
            final String hash = DigestUtils.md5Hex(toHash.getBytes("ASCII"));
            return hash.substring(0, 8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

        return "";
    }

    private String makeCockatriceHash(List<String> main, List<String> side) {
        final List<String> mainList = main.stream().map(String::toLowerCase).collect(Collectors.toList());
        final List<String> sideList = side.stream().map(String::toLowerCase).map(name -> "SB:" + name).collect(Collectors.toList());
        mainList.addAll(sideList);
        Collections.sort(mainList);
        final String toHash = mainList.stream().reduce((s, s2) -> s + ";" + s2).orElse("");
        try {
            final String hash = DigestUtils.sha1Hex(toHash.getBytes("ASCII"));
            final long i = Long.parseLong(hash.substring(0, 10), 16);
            return Long.toString(i, 32);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

        return "";
    }
}
