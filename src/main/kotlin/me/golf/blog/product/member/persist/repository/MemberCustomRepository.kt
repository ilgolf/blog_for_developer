package me.golf.blog.product.member.persist.repository

import me.golf.blog.global.security.CustomUserDetails
import me.golf.blog.product.member.dto.MemberDetailResponseDto

interface MemberCustomRepository {

    fun getAuthInfoById(memberId: Long): CustomUserDetails?
    fun getAuthInfoByEmail(email: String): CustomUserDetails?
    fun getDetailInfo(memberId: Long): MemberDetailResponseDto?
}
