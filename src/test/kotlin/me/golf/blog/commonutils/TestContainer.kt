package me.golf.blog.commonutils

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container

abstract class TestContainer {

    companion object {
        private const val REDIS_IMAGE = "redis:7.0-alpine"
        private const val MYSQL_IMAGE = "mysql:8.0.22"

        @JvmStatic
        @Container
        val REDIS_CONTAINER: GenericContainer<Nothing> = GenericContainer<Nothing>(REDIS_IMAGE)
            .apply { withExposedPorts(6379) }
            .apply { withReuse(true) }
            .apply { start() }

        @JvmStatic
        @Container
        val MYSQL_CONTAINER: MySQLContainer<Nothing> = MySQLContainer<Nothing>(MYSQL_IMAGE)
            .apply {
                withDatabaseName("blog")
                withUsername("nokt")
                withPassword("1234")
                withCreateContainerCmdModifier { cmd -> cmd.withPlatform("linux/x86_64") }
            }
            .apply { withExposedPorts(3306) }
            .apply { withReuse(true ) }
            .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost)
            registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort)
            registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername)
            registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword)
        }
    }
}