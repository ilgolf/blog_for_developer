package me.golf.blog.global.config

import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderedParams
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import jakarta.persistence.Query

inline fun <reified T> EntityManager.createQuery(
    query: SelectQuery<*>,
    context: JpqlRenderContext = JpqlRenderContext(),
    renderer: JpqlRenderer = JpqlRenderer(),
): T? {

    val jpql = renderer.render(query, context)
    return this.createQuery(jpql.query, T::class.java)
        .apply { setParams(this, jpql.params) }
        .singleResult
}

inline fun <reified T> EntityManager.createQueryList(
    query: SelectQuery<*>,
    context: JpqlRenderContext = JpqlRenderContext(),
    renderer: JpqlRenderer = JpqlRenderer(),
): List<T> {

    val jpql = renderer.render(query, context)
    return this.createQuery(jpql.query, T::class.java)
        .apply { setParams(this, jpql.params) }
        .resultList
}

inline fun <reified T> EntityManager.createQueryCount(
    query: SelectQuery<*>,
    context: JpqlRenderContext = JpqlRenderContext(),
    renderer: JpqlRenderer = JpqlRenderer(),
): Long {

    val jpql = renderer.render(query, context)
    return this.createQuery(jpql.query, T::class.java)
        .apply { setParams(this, jpql.params) }
        .firstResult.toLong()
}

fun setParams(query: Query, params: JpqlRenderedParams) {
    params.forEach { (name, value) ->
        query.setParameter(name, value)
    }
}