package com.malgn.service;

import com.malgn.dto.content.ContentListResponseDTO;
import com.malgn.dto.content.ContentRequestDTO;
import com.malgn.dto.content.ContentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IContentSerivce {

    public int createContent(ContentRequestDTO pDTO);
    public Page<ContentListResponseDTO> getContentList(Pageable pageable);
    public ContentResponseDTO getContentDetail(Long id);
    public int updateContent(Long id, ContentRequestDTO pDTO);
    public int deleteContent(Long id);
}
