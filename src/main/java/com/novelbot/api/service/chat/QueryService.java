package com.novelbot.api.service.chat;

import com.novelbot.api.domain.*;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.mapper.chat.QueryDtoMapper;
import com.novelbot.api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryService {

        private final QueryRepository queriesRepository;
        private final ChatRepository chatroomRepository;
        private final EpisodeRepository episodeRepository;
        private final QueryEpisodeRepository episodeQueryRepository;
        private final UserRepository userRepository; // 사용자 인증 정보를 통해 User 객체를 가져와야 함

        /**
         * 새로운 질문 생성
         */
        @Transactional
        public QueryDto.QueryResponse createQuery(int chatId, QueryDto.CreateRequest request, int userId) {
                // 1. 엔티티 조회
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. id=" + userId));
                Chatroom chatroom = chatroomRepository.findById(chatId)
                                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다. id=" + chatId));

                // 2. DTO -> Entity 변환
                Queries newQuery = QueryMapper.toEntity(request, chatroom, user);
                queriesRepository.save(newQuery);

                // 3. 페이지 번호가 있는 경우, EpisodeQuery 생성
                if (request.getPageNumber() != null) {
                        // novelId와 pageNumber(episode_number로 가정)로 Episode 조회
                        // 주의: pageNumber가 episode_number와 다르다면 별도 로직 필요
                        int novelId = chatroom.getNovel().getId();
                        episodeRepository.findByNovelIdAndEpisodeNumber(novelId, request.getPageNumber())
                                        .ifPresent(episode -> {
                                                EpisodeQuery episodeQuery = EpisodeQuery.builder()
                                                                .query(newQuery)
                                                                .episode(episode)
                                                                .build();
                                                episodeQueryRepository.save(episodeQuery);
                                        });
                }

                // 4. 응답 DTO로 변환하여 반환
                return new QueryDto.QueryResponse(newQuery);
        }

        /**
         * 채팅방의 모든 질문 목록 조회
         */
        public List<QueryDto.QueryResponse> getQueriesByChatId(int chatId) {
                List<Queries> queries = queriesRepository.findByChatroomId(chatId);
                return queries.stream()
                                .map(QueryDto.QueryResponse::new)
                                .collect(Collectors.toList());
        }

        /**
         * 특정 질문 조회
         */
        public QueryDto.QueryResponse getQuery(int queryId) {
                Queries query = queriesRepository.findById(queryId)
                                .orElseThrow(() -> new EntityNotFoundException("질문을 찾을 수 없습니다. id=" + queryId));
                return new QueryDto.QueryResponse(query);
        }

        /**
         * 질문에 대한 AI 답변 생성 및 저장
         */
        @Transactional
        public QueryDto.AnswerResponse generateAnswer(int queryId) {
                Queries query = queriesRepository.findById(queryId)
                                .orElseThrow(() -> new EntityNotFoundException("질문을 찾을 수 없습니다. id=" + queryId));

                // TODO: AI 모델을 호출하여 답변을 생성하는 로직
                // 예: String generatedAnswer = aiModelClient.generate(query.getQueryContent());
                String generatedAnswer = "AI가 생성한 답변입니다: '" + query.getQueryContent() + "'에 대한 답변.";

                query.updateAnswer(generatedAnswer);
                // 변경 감지(dirty checking)에 의해 트랜잭션 종료 시 update 쿼리 실행

                return new QueryDto.AnswerResponse(query.getId(), generatedAnswer);
        }

        /**
         * 질문 삭제
         */
        @Transactional
        public void deleteQuery(int queryId) {
                // 연관된 EpisodeQuery도 함께 삭제되도록 cascade 설정을 하거나, 직접 삭제 로직을 추가해야 합니다.
                // 여기서는 Queries만 삭제하는 것으로 가정합니다.
                if (!queriesRepository.existsById(queryId)) {
                        throw new EntityNotFoundException("삭제할 질문을 찾을 수 없습니다. id=" + queryId);
                }
                queriesRepository.deleteById(queryId);
        }
}