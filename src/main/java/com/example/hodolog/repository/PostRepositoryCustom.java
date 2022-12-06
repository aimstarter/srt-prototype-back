package com.example.hodolog.repository;

import com.example.hodolog.domain.Post;
import com.example.hodolog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
