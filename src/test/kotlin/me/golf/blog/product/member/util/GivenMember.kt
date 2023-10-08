package me.golf.blog.product.member.util

import me.golf.blog.product.member.persist.JobType
import me.golf.blog.product.member.persist.Member

object GivenMember {
    const val EMAIL = "ilgolc@naver.com"
    const val PASSWORD = "1234"
    const val NAME = "노경태"
    const val NICKNAME = "nokt"
    const val DESCRIPTION: String = "자기소개"
    const val COMPANY: String = "company"
    const val PROFILE_IMAGE: String = "https://www.signedUrl.com/image3"
    const val EXPERIENCE: Int = 3
    val jobType: JobType = JobType.WORKING


    fun toMember() =
        Member(
            email = this.EMAIL,
            password = this.PASSWORD,
            name = this.NAME,
            nickname = this.NICKNAME,
            description = this.DESCRIPTION,
            company = this.COMPANY,
            profileImageUrl = this.PROFILE_IMAGE,
            experience = this.EXPERIENCE,
            jobType = this.jobType
        )
}