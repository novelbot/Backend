package com.novelbot.api.service.novel;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.dto.novel.EpisodeCreateRequest;
import com.novelbot.api.dto.novel.EpisodeDto;
import com.novelbot.api.dto.novel.EpisodeListDto;
import com.novelbot.api.mapper.novel.EpisodeListDtoMapper;
import com.novelbot.api.mapper.novel.EpisodeDtoMapper;
import com.novelbot.api.repository.EpisodeRepository;
import com.novelbot.api.repository.NovelRepository;
import com.novelbot.api.repository.PurchaseRepository;
import com.novelbot.api.config.JwtTokenValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private EpisodeListDtoMapper episodeListDtoMapper;

    @Autowired
    private EpisodeDtoMapper episodeDtoMapper;

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    private PurchaseRepository purchaseRepository;

    // 웹소설 본문 조회
    public Optional<EpisodeDto> getEpisodeContent(int novelId, int episodeNumber) {
        if(novelId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(유효하지 않은 novelId)"
            );
        }

        if(episodeNumber <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(유효하지 않은 episodeNumber)"
            );
        }

        Optional<Episode> episode = episodeRepository.findByNovelIdAndEpisodeNumber(novelId, episodeNumber);
        if(episode.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Error Code: 404, Conflict(존재하지 않는 novelId)"
            );
        }

        EpisodeDto episodeDto = episodeDtoMapper.toDto(episode);

        return Optional.ofNullable(episodeDto);
    }

    // 특정 소설의 에피소드 목록 조회
    public List<EpisodeListDto> findAllByNovelId(int novelId, String token) throws IOException {
        if(novelId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(유효하지 않은 novelId)"
            );
        }

        String username = null;
        if (token != null && !token.isEmpty()) {
            try {
                username = jwtTokenValidator.getUsername(token);
            } catch (Exception e) {
                username = null;
            }
        }

        List<Episode> episodeList = episodeRepository.findAllByNovelId(novelId);
        if(episodeList.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Error Code: 404, Conflict(novelId에 해당하는 에피소드가 존재하지 않음)"
            );
        }

        List<EpisodeListDto> episodeListDtos = new ArrayList<>();

        for (Episode episode : episodeList) {
            try{
                EpisodeListDto dto = episodeListDtoMapper.toDto(episode);
                dto.setIsPurchased(false);
                
                if (username != null) {
                    boolean isPurchased = checkIfPurchased(username, episode.getId());
                    dto.setIsPurchased(isPurchased);
                }
                
                episodeListDtos.add(dto);
            }catch(Exception e){
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Error Code: 409, Conflict(에피소드 매핑 중 충돌 발생)"
                );
            }
        }

        return episodeListDtos;
    }

    private boolean checkIfPurchased(String username, Integer episodeId) {
        return purchaseRepository.existsByUserUserNameAndEpisodeId(username, episodeId);
    }

    // 에피소드 등록
    public void createEpisode(EpisodeCreateRequest episodeCreateRequest) {
        if(episodeCreateRequest.getNovelId() == null || episodeCreateRequest.getNovelId() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "novelId가 올바르지 않은 형식입니다.");
        }
        if(episodeCreateRequest.getEpisodeNumber() == null || episodeCreateRequest.getEpisodeNumber() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"episodeId가 올바르지 않은 형식입니다.");
        }
        if (episodeCreateRequest.getEpisodeTitle() == null || episodeCreateRequest.getEpisodeTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "episodeTitle이 null이거나 비어 있습니다.");
        }
        if (episodeCreateRequest.getContent() == null || episodeCreateRequest.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "content가 null이거나 비어 있습니다.");
        }

        Novel novel = novelRepository.findById(episodeCreateRequest.getNovelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소설을 찾을 수 없습니다."));

        Optional<Episode> existingEpisode = episodeRepository.findByNovelIdAndEpisodeNumber(
                episodeCreateRequest.getNovelId(), episodeCreateRequest.getEpisodeNumber());
        if (existingEpisode.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 에피소드 번호입니다.");
        }

        Episode episode = new Episode();

        episode.setNovel(novel);
        episode.setEpisodeNumber(episodeCreateRequest.getEpisodeNumber());
        episode.setEpisodeTitle(episodeCreateRequest.getEpisodeTitle());
        episode.setContent(episodeCreateRequest.getContent());

        try {
            Episode savedEpisode = episodeRepository.save(episode);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "에피소드 등록 중 오류가 발생했습니다.", e);
        }

    }
}

