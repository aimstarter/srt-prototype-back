package com.example.hodolog.repository;

import com.example.hodolog.domain.Post;
import com.example.hodolog.domain.QPost;
import com.example.hodolog.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset((postSearch.getPage() - 1) * postSearch.getSize())
                .fetch();
    }
}
