package me.golf.blog.product.member.controller

import jakarta.validation.Valid
import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.member.application.MemberService
import me.golf.blog.product.member.dto.MemberDetailGetResponseDto
import me.golf.blog.product.member.dto.MemberSavePostRequestDto
import me.golf.blog.product.member.dto.MemberUpdatePutRequestDto
import me.golf.blog.product.member.dto.SimpleMemberResponseDto
import me.golf.blog.product.member.exception.MemberException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping
    fun save(
        @Valid @RequestBody requestDto: MemberSavePostRequestDto
    ): ResponseEntity<SimpleMemberResponseDto> {

        if (!requestDto.matchPasswordAndConfirm()) {
            throw MemberException.PasswordConfirmMissMatchException()
        }

        return ResponseEntity.ok(memberService.save(requestDto.toServiceDto()))
    }

    @PutMapping
    fun update(
        @Valid @RequestBody requestDto: MemberUpdatePutRequestDto,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<SimpleMemberResponseDto> {

        return ResponseEntity.ok(memberService.update(requestDto.toServiceDto(userDetails.memberId)))
    }

    @GetMapping("/detail")
    fun getDetail(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<MemberDetailGetResponseDto> {

        return ResponseEntity.ok(MemberDetailGetResponseDto.of(memberService.getDetail(userDetails.memberId)))
    }

    @DeleteMapping
    fun withdraw(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<Unit> {

        memberService.withdraw(userDetails.memberId)
        return ResponseEntity.noContent().build()
    }
}