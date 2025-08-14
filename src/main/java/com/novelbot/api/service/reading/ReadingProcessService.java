package com.novelbot.api.service.reading;

import com.novelbot.api.domain.UserReadingProgress;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.reading.ReadingProgressDto;
import com.novelbot.api.dto.reading.ReadingProgressRequest;
import com.novelbot.api.repository.NovelRepository;
import com.novelbot.api.repository.EpisodeRepository;
import com.novelbot.api.repository.UserRepository;
import com.novelbot.api.repository.ReadingProgressRepository;
import com.novelbot.api.config.JwtTokenValidator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReadingProcessService {
    private final ReadingProgressRepository readingProgressRepository;
    private final NovelRepository novelRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final JwtTokenValidator jwtTokenValidator;

    public ReadingProcessService(ReadingProgressRepository readingProgressRepository,
                                 NovelRepository novelRepository,
                                 UserRepository userRepository,
                                 EpisodeRepository episodeRepository,
                                 JwtTokenValidator jwtTokenValidator) {
        this.novelRepository = novelRepository;
        this.userRepository = userRepository;
        this.episodeRepository = episodeRepository;
        this.readingProgressRepository = readingProgressRepository;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    // 독서 진도 저장
    public void initializeReading(ReadingProgressRequest request, String token) {
        if (request.getNovelId() == null || request.getNovelId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "novelId가 올바르지 않은 형식입니다.");
        }
        if (request.getEpisodeId() == null || request.getEpisodeId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "episodeId가 올바르지 않은 형식입니다.");
        }

        String username;
        try {
            username = jwtTokenValidator.getUsername(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", e);
        }

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Episode episode = episodeRepository.findById(request.getEpisodeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "에피소드를 찾을 수 없습니다."));

        Novel novel = novelRepository.findById(request.getNovelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소설을 찾을 수 없습니다."));

        UserReadingProgress progress = readingProgressRepository.findByUserAndEpisodeAndNovel(user, episode, novel);

        if (progress == null) {
            progress = new UserReadingProgress();
            progress.setUser(user);
            progress.setEpisode(episode);
            progress.setNovel(novel);
        }

        progress.setLastReadPage(0);

        try{
            readingProgressRepository.save(progress);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "진도 초기화 중 오류가 발생했습니다.", e);
        }
    }

    // 독서 진도 업데이트
    public void updateProgress(ReadingProgressRequest readingProgressRequest, String token) {
        if (readingProgressRequest.getNovelId() == null || readingProgressRequest.getNovelId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "novelId가 올바르지 않은 형식입니다.");
        }
        if (readingProgressRequest.getEpisodeId() == null || readingProgressRequest.getEpisodeId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "episodeId가 올바르지 않은 형식입니다.");
        }
        if (readingProgressRequest.getLastReadPage() == null || readingProgressRequest.getLastReadPage() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "페이지 번호는 0 이상이어야 합니다.");
        }

        String username;
        try {
            username = jwtTokenValidator.getUsername(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", e);
        }

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Episode episode = episodeRepository.findById(readingProgressRequest.getEpisodeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "에피소드를 찾을 수 없습니다."));

        Novel novel = novelRepository.findById(readingProgressRequest.getNovelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소설을 찾을 수 없습니다."));

        UserReadingProgress progress = readingProgressRepository.findByUserAndEpisodeAndNovel(user, episode, novel);

        if (progress == null) {
            progress = new UserReadingProgress();
            progress.setUser(user);
            progress.setEpisode(episode);
            progress.setNovel(novel);
        }

        progress.setLastReadPage(readingProgressRequest.getLastReadPage());

        try {
            readingProgressRepository.save(progress);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "진도 갱신 중 오류가 발생했습니다.", e);
        }
    }

    // 읽던 페이지로 이동 -> 최근 저장된 독서 진도 반환
    public int getProgress(ReadingProgressDto readingProgressDto, String token) {
        if (readingProgressDto.getNovelId() == null || readingProgressDto.getNovelId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "novelId가 올바르지 않은 형식입니다.");
        }
        if (readingProgressDto.getEpisodeId() == null || readingProgressDto.getEpisodeId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "episodeId가 올바르지 않은 형식입니다.");
        }

        String username;
        try {
            username = jwtTokenValidator.getUsername(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", e);
        }

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Novel novel = novelRepository.findById(readingProgressDto.getNovelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소설을 찾을 수 없습니다."));

        Episode episode = episodeRepository.findById(readingProgressDto.getEpisodeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "에피소드를 찾을 수 없습니다."));

        UserReadingProgress progress = readingProgressRepository.findByUserAndEpisodeAndNovel(user, episode, novel);

        return progress != null ? progress.getLastReadPage() : 0;
    }
}
