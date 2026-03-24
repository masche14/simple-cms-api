package com.malgn.service;

import com.malgn.dto.content.ContentListResponseDTO;
import com.malgn.dto.content.ContentRequestDTO;
import com.malgn.dto.content.ContentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IContentSerivce {

    int createContent(ContentRequestDTO pDTO);
    Page<ContentListResponseDTO> getContentList(Pageable pageable);
    ContentResponseDTO getContentDetail(Long id);
    int updateContent(Long id, ContentRequestDTO pDTO);
    int deleteContent(Long id);
}
