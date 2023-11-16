package me.golf.blog.global.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDSLConfig(
    @PersistenceContext
    private val entityManager: EntityManager
) {

    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}