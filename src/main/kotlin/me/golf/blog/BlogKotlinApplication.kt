package me.golf.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogKotlinApplication

fun main(args: Array<String>) {
    runApplication<BlogKotlinApplication>(*args)
}
