package com.novelbot.api.service.novel;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.dto.novel.EpisodeDto;
import com.novelbot.api.dto.novel.EpisodeListDto;
import com.novelbot.api.mapper.novel.EpisodeListDtoMapper;
import com.novelbot.api.mapper.novel.EpisodeDtoMapper;
import com.novelbot.api.repository.EpisodeRepository;
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

    // 웹소설 본문 조회
    public Optional<Episode> getEpisodeContent(int novel_id, int episode_number) {
        if(novel_id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(유효하지 않은 novelId)"
            );
        }

        if(episode_number <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(유효하지 않은 episodeNumber)"
            );
        }

        Optional<Episode> episode = episodeRepository.findByNovelIdAndEpisodeNumber(novel_id, episode_number);
        if(episode.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(존재하지 않는 novelId)"
            );
        }

        EpisodeDto episodeDto = episodeDtoMapper.toDto(episode);

        return episode;
    }

    // 특정 소설의 에피소드 목록 조회
    public List<EpisodeListDto> findAllByNovelId(int novelId) throws IOException {
        if(novelId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(유효하지 않은 novelId)"
            );
        }

        List<Episode> episodeList = episodeRepository.findAllByNovelId(novelId);
        if(episodeList.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Conflict(novelId에 해당하는 에피소드가 존재하지 않음)"
            );
        }

        List<EpisodeListDto> episodeListDtos = new ArrayList<>();

        for (Episode episode : episodeList) {
            try{
                episodeListDtos.add(episodeListDtoMapper.toDto(episode));
            }catch(Exception e){
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Error Code: 409, Conflict(에피소드 매핑 중 충돌 발생)"
                );
            }
        }

        return episodeListDtos;
    }
}

