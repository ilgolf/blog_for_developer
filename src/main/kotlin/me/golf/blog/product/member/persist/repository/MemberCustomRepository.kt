package me.golf.blog.product.member.persist.repository

import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.member.dto.MemberDetailResponseDto

interface MemberCustomRepository {

    fun findAuthInfoById(id: Long): CustomUserDetails?
    fun findAuthInfoByEmail(email: String): CustomUserDetails?
    fun findDetailById(id: Long): MemberDetailResponseDto?
}
