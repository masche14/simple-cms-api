package com.malgn.controller;

import com.malgn.dto.content.ContentListResponseDTO;
import com.malgn.dto.content.ContentRequestDTO;
import com.malgn.dto.content.ContentResponseDTO;
import com.malgn.service.IContentSerivce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "3. 콘텐츠 (Contents)", description = "콘텐츠 생성, 조회, 수정, 삭제 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contents")
public class ContentController {

    private final IContentSerivce contentService;

    // 콘텐츠 생성
    @Operation(summary = "콘텐츠 생성", description = "새로운 콘텐츠를 생성합니다. (로그인 필요)")
    @PostMapping("/createContent")
    public ResponseEntity<String> createContent(@Valid @RequestBody ContentRequestDTO requestDTO) {

        log.info("{}.createContent Start", this.getClass().getSimpleName());

        int res = contentService.createContent(requestDTO);

        if (res != 1) {
            throw new IllegalStateException("콘텐츠 생성에 실패했습니다.");
        }

        log.info("{}.createContent End", this.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.CREATED).body("콘텐츠 생성에 성공했습니다.");
    }

    // 콘텐츠 목록 조회
    @Operation(summary = "콘텐츠 목록 조회", description = "콘텐츠 목록을 조회합니다. (페이징 처리 / 로그인 불필요)\n\n"+
            "각 콘텐츠의 id/제목/조회수/작성자/생성일자에 대한 정보가 제공됩니다."+
            "sort의 경우 '[]' 형태가 아니라 'createdDate,desc'와 같이 쉼표로 구분하여 입력합니다.")
    @GetMapping
    public ResponseEntity<Page<ContentListResponseDTO>> getContentList(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("{}.getContentList Start", this.getClass().getSimpleName());

        log.info("pageable: {}", pageable);
        log.info("pageable.getSort(): {}", pageable.getSort());

        Page<ContentListResponseDTO> contentPage = contentService.getContentList(pageable);

        log.info("{}.getContentList End", this.getClass().getSimpleName());

        return ResponseEntity.ok(contentPage);
    }

    // 콘텐츠 상세 조회
    @Operation(summary = "콘텐츠 상세 내용 조회", description = "콘텐츠의 상세 내용을 조회하며, 호출 시 조회수가 1 증가합니다. (로그인 불필요)")
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponseDTO> getContentDetail(@PathVariable Long id) {

        log.info("{}.getContentDetail Start", this.getClass().getSimpleName());

        log.info("id: {}", id);

        ContentResponseDTO responseDTO = contentService.getContentDetail(id);

        log.info("{}.getContentDetail End", this.getClass().getSimpleName());

        return ResponseEntity.ok(responseDTO);
    }

    // 콘텐츠 수정
    @Operation(summary = "콘텐츠 수정", description = "작성자 본인 또는 관리자(ADMIN)만 수정 가능합니다.")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateContent(@PathVariable("id") Long id, @Valid @RequestBody ContentRequestDTO requestDTO) {

        log.info("{}.updateContent Start", this.getClass().getSimpleName());

        log.info("id: {}", id);

        int res = contentService.updateContent(id, requestDTO);

        if (res != 1) {
            throw new IllegalStateException("콘텐츠 등록에 실패했습니다.");
        }

        log.info("{}.updateContent End", this.getClass().getSimpleName());

        return ResponseEntity.ok("콘텐츠 업데이트에 성공했습니다.");
    }

    // 콘텐츠 삭제
    @Operation(summary = "콘텐츠 삭제", description = "작성자 본인 또는 관리자(ADMIN)만 삭제 가능합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteContent(@PathVariable("id") Long id) {

        log.info("{}.deleteContent Start", this.getClass().getSimpleName());

        log.info("id: {}", id);

        int res = contentService.deleteContent(id);


        if (res != 1) {
            throw new IllegalStateException("콘텐츠 삭제에 실패했습니다.");
        }

        log.info("{}.deleteContent End", this.getClass().getSimpleName());

        return ResponseEntity.ok("콘텐츠 삭제에 성공했습니다.");
    }
}
