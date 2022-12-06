package com.example.hodolog.controller;

import com.example.hodolog.domain.Post;
import com.example.hodolog.exception.InvalidRequest;
import com.example.hodolog.request.PostCreate;
import com.example.hodolog.request.PostEdit;
import com.example.hodolog.request.PostSearch;
import com.example.hodolog.response.PostResponse;
import com.example.hodolog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
//
//    @GetMapping("/posts")
//    public String get(@RequestParam String title, @RequestParam String content) {
//        log.debug("title={}, content={}", title, content);
//        System.out.println("title=" + title);
//        return "Hello World";
//    }

//    @GetMapping("/posts")
//    public String get(@RequestParam Map<String, String> params) {
//        log.debug("params = {}", params);
//
//        return "Hello World";
//    }
//
//    @PostMapping("/posts")
//    public String post(@ModelAttribute PostCreate params) {
//        System.out.println("params=" + params.toString());
//
//        return "Hello World";
//    }
    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate params) throws Exception {
        params.validate();

        String title = params.getTitle();
        String content = params.getContent();
        System.out.println("params=" + params.toString());
        postService.write(params);


//        if(result.hasErrors()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get(0);
//            String fieldName = firstFieldError.getField();
//            String errorMessage = firstFieldError.getDefaultMessage();
//
//            Map<String, String> error = new HashMap<>();
//            error.put(fieldName, errorMessage);
//            return error;
//        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getList(PostSearch postSearch) {

        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        PostResponse postResponse = postService.edit(postId, request);

        return postResponse;
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }


    @PostMapping("/login")
    public void login() throws Exception {
        postService.login();
    }
}
