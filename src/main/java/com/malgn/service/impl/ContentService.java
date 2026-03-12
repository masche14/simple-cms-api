package com.malgn.service.impl;

import com.malgn.dto.content.ContentListResponseDTO;
import com.malgn.dto.content.ContentRequestDTO;
import com.malgn.dto.content.ContentResponseDTO;
import com.malgn.entity.Content;
import com.malgn.exception.ContentNotFoundException;
import com.malgn.repository.ContentRepository;
import com.malgn.service.IContentSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContentService implements IContentSerivce {

    private final ContentRepository contentRepository;

    // 콘텐츠 추가
    @Transactional
    @Override
    public int createContent(ContentRequestDTO pDTO) {

        log.info("{}.createContent Start", this.getClass().getSimpleName());

        int res = 0;

        Content content = Content.builder()
                .title(pDTO.title())
                .description(pDTO.description())
                .build();

        contentRepository.save(content);

        res = 1;

        log.info("{}.createContent End", this.getClass().getSimpleName());

        return res;
    }

    // 콘텐츠 목록 조회
    @Transactional(readOnly = true)
    @Override
    public Page<ContentListResponseDTO> getContentList(Pageable pageable) {

        log.info("{}.getContentList Start", this.getClass().getSimpleName());

        Page<ContentListResponseDTO> contentList = contentRepository.findAll(pageable)
                .map(ContentListResponseDTO::from);

        log.info("{}.getContentList End", this.getClass().getSimpleName());

        return contentList;
    }

    // 콘텐츠 상세 내용 조회
    @Transactional
    @Override
    public ContentResponseDTO getContentDetail(Long id) {

        log.info("{}.getContentDetail Start", this.getClass().getSimpleName());

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("해당 콘텐츠가 존재하지 않습니다. id: " + id));

        content.increaseViewCount();

        ContentResponseDTO rDTO = ContentResponseDTO.from(content);

        log.info("{}.getContentDetail End", this.getClass().getSimpleName());

        return rDTO;
    }

    // 콘텐츠 업데이트
    @Transactional
    @Override
    public int updateContent(Long id, ContentRequestDTO pDTO) {

        log.info("{}.updateContent Start", this.getClass().getSimpleName());

        int res = 0;

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("해당 콘텐츠가 존재하지 않습니다. id: " + id));

        validateAuth(content);

        content.update(pDTO);

        res = 1;

        log.info("{}.updateContent End", this.getClass().getSimpleName());

        return res;
    }

    // 콘텐츠 삭제
    @Transactional
    @Override
    public int deleteContent(Long id) {

        log.info("{}.deleteContent Start", this.getClass().getSimpleName());

        int res = 0;

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("해당 콘텐츠가 존재하지 않습니다. id: " + id));

        validateAuth(content);

        contentRepository.delete(content);

        res = 1;

        log.info("{}.deleteContent End", this.getClass().getSimpleName());

        return res;
    }

    // 콘텐츠 수정 및 삭제 권환 확인
    private void validateAuth(Content content) {
        log.info("{}.validateAuth Start", this.getClass().getSimpleName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!content.getCreatedBy().equals(currentUserId) && !isAdmin) {
            throw new AccessDeniedException("해당 컨텐츠에 대한 수정 및 삭제 권한이 없습니다.");
        }

        log.info("{}.validateAuth End", this.getClass().getSimpleName());
    }
}
