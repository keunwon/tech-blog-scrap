package com.github.keunwon.techblogscrap.jsonnode.medium

object MediumQuery {
    val PUBLICATION_TAGGED_QUERY by lazy {
        javaClass.classLoader.getResourceAsStream("query/PublicationTaggedQuery.txt")!!
            .bufferedReader()
            .use { it.readText() }
    }

    val PUBLICATION_SECTION_POSTS_QUERY by lazy {
        javaClass.classLoader.getResourceAsStream("query/PublicationSectionPostsQuery.txt")!!
            .bufferedReader()
            .use { it.readText() }
    }

    val USER_PROFILE_QUERY by lazy {
        javaClass.classLoader.getResourceAsStream("query/UserProfileQuery.txt")!!
            .bufferedReader()
            .use { it.readText() }
    }

    val PUBLICATION_CONTENT_DATA_QUERY by lazy {
        javaClass.classLoader.getResourceAsStream("query/publicationContentDataQuery.txt")!!
            .bufferedReader()
            .use { it.readText() }
    }
}
