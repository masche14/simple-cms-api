package com.malgn.dto.content;

import com.malgn.entity.Content;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ContentResponseDTO(
        Long id,
        String title,
        String description,
        Long viewCount,
        LocalDate createdDate,
        String createdBy,
        LocalDateTime lastModifiedDate
) {
    public static ContentResponseDTO from (Content content) {
        return ContentResponseDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .viewCount(content.getViewCount())
                .createdDate(LocalDate.from(content.getCreatedDate()))
                .createdBy(content.getCreatedBy())
                .lastModifiedDate(content.getLastModifiedDate())
                .build();
    }
}
