package me.golf.blog.global.security

import com.querydsl.core.annotations.QueryProjection
import me.golf.blog.product.member.persist.Member
import me.golf.blog.product.member.persist.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Collections

class CustomUserDetails

@QueryProjection
constructor(
    var memberId: Long,
    var email: String,
    var roleType: Role
) : UserDetails {

    companion object {
        fun of(member: Member): CustomUserDetails {
            return CustomUserDetails(memberId = member.id, email = member.email, roleType = member.role)
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singleton(SimpleGrantedAuthority("ROLE_${roleType.name}"))
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}