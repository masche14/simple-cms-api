package com.malgn.configure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("(주)맑은기술 CMS API 문서")
                        .description("간단한 CMS 콘텐츠 관리 API\n\n"+
                                "**테스트 계정 안내**\n" +
                                "- 관리자: `admin` / `1234`\n\n"+
                                "- 일반 사용자는 `/api/reg/insertUser` API를 통해 직접 추가할 수 있습니다.\n\n" +
                                "**사용자 등록 안내**\n" +
                                "- 회원가입 요청 경로: `POST /api/reg/insertUser")
                        .version("1.0.0"))
                .addTagsItem(new Tag().name("1. 등록 (Reg)").description("사용자 등록 API"))
                .addTagsItem(new Tag().name("2. 인증 (Auth)").description("사용자 로그인 및 로그아웃 API"))
                .addTagsItem(new Tag().name("3. 콘텐츠 (Contents)").description("콘텐츠 생성, 조회, 수정, 삭제 API"));
    }
}
