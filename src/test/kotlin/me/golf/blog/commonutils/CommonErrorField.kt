package me.golf.blog.commonutils

import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.ResponseFieldsSnippet

object CommonErrorField {

    val commonResponseFields: ResponseFieldsSnippet = PayloadDocumentation.responseFields(
        PayloadDocumentation.fieldWithPath("message").description("메시지"),
        PayloadDocumentation.fieldWithPath("status").description("상태 코드"),
        PayloadDocumentation.fieldWithPath("errors[].field").description("오류가 발생한 필드"),
        PayloadDocumentation.fieldWithPath("errors[].value").description("오류가 발생한 값"),
        PayloadDocumentation.fieldWithPath("errors[].reason").description("오류 사유")
    )
}