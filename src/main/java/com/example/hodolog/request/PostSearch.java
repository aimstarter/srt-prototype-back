package com.example.hodolog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 5;

//    @Builder
//    public PostSearch(int page, int size) {
//        this.page = page;
//        this.size = size;
//    }
}
