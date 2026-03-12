package com.malgn.dto.content;

import com.malgn.entity.Content;

import java.time.LocalDate;

public record ContentListResponseDTO(
        Long id,
        String title,
        Long viewCount,
        LocalDate createdDate,
        String createdBy
) {
    public static ContentListResponseDTO from (Content content) {
        return new ContentListResponseDTO(
                content.getId(),
                content.getTitle(),
                content.getViewCount(),
                LocalDate.from(content.getCreatedDate()),
                content.getCreatedBy()
        );
    }
}
