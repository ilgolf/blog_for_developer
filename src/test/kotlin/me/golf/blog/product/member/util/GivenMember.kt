package me.golf.blog.product.member.util

import me.golf.blog.product.member.persist.Member
import java.time.LocalDate

object GivenMember {
    const val email = "ilgolc@naver.com"
    const val password = "1234"
    const val name = "노경태"
    const val nickname = "nokt"
    val birth = LocalDate.of(1996, 10, 25)


    fun toMember() =
        Member(
            email = email,
            password = password,
            name = name,
            nickname = nickname,
            birth = birth
        )
}