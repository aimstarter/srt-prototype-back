package com.example.hodolog.service;

import com.example.hodolog.domain.Post;
import com.example.hodolog.exception.PostNotFound;
import com.example.hodolog.repository.PostRepository;
import com.example.hodolog.request.PostCreate;
import com.example.hodolog.request.PostEdit;
import com.example.hodolog.request.PostSearch;
import com.example.hodolog.response.PostResponse;
import com.example.hodolog.vo.LoginVo;
import com.example.hodolog.vo.ReserveVo;
import com.example.hodolog.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postcreate) {
        // postcreate -> Entity
        // Post post = new Post(postcreate.getTitle(), postcreate.getContent());
        Post post = Post.builder()
                .title(postcreate.getTitle())
                .content(postcreate.getContent())
                .build();

        postRepository.save(post);
    }

    public ResponseEntity<PostResponse> get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

//        PostResponse postResponse = PostResponse.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .build();
//
//        return postResponse;
        ModelMapper mapper = new ModelMapper();

        PostResponse postResponse = mapper.map(post, PostResponse.class);
        log.debug("postResponse: {}", postResponse.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    public ResponseEntity<List<PostResponse>> getList(PostSearch page) {
        Pageable pageable = PageRequest.of(page.getSize(), 5, Sort.by(Sort.Direction.DESC, "id"));

        Iterable<Post> post = postRepository.getList(page);

        List<PostResponse> result = new ArrayList<>();
        post.forEach(v -> {
            result.add(new ModelMapper().map(v, PostResponse.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);

//
//        return postRepository.findAll()
//                .stream()
//                .map(post -> new PostResponse(post)
//                )
//                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                //.orElseThrow(() -> new IllegalArgumentException("존재히지 않는 글입니다."));
                .orElseThrow(() -> new PostNotFound());

//        post.setTitle(postEdit.getTitle());
//        post.setContent(postEdit.getContent());
        post.change(postEdit.getTitle(), postEdit.getContent());

        postRepository.save(post);

        return new PostResponse(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        postRepository.delete(post);
    }

    public ResponseEntity<String> login() {
        StringBuilder sb = new StringBuilder();

        URI uri = UriComponentsBuilder
                .fromUriString("https://app.srail.or.kr:443")
                .path("/apb/selectListApb01080_n.do")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
        LoginVo loginVo = new LoginVo();
        loginVo.setAuto("Y");
        loginVo.setCheck("Y");
        loginVo.setPage("menu");
        loginVo.setDeviceKey("-");
        loginVo.setCustomerYn("");
        loginVo.setLogin_referer("https://app.srail.or.kr=443/main/main.do");
        loginVo.setSrchDvCd("2");
        loginVo.setSrchDvNm("aimstarter01@gmail.com");
        loginVo.setHmpgPwdCphd("7641955munha!");

        RequestEntity<String> requestEntity = RequestEntity
                .post(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6")
                .body(loginVo.toString());

        log.debug("uri : {}", uri.toString());

        log.debug("request-header : {}", requestEntity.getHeaders());
        log.debug("request-body1 : {}", requestEntity.getBody());
        log.debug("request-body2 : {}", requestEntity.toString());
        log.debug("request-body3 : {}", loginVo.toString());

        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });

//        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity,
//                String.class);

//        log.debug("status code : {}", responseEntity.getStatusCode());
//        log.debug("response-header : {}", responseEntity.getHeaders());
//        log.debug("response-body : {}", responseEntity.getBody());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.set("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("auto", "Y");
        map.add("check", "Y");
        map.add("page", "menu");
        map.add("deviceKey", "-");
        map.add("customerYn", "");
        map.add("login_referer", "https://app.srail.or.kr=443/main/main.do");
        map.add("srchDvCd", "2");
        map.add("srchDvNm", "aimstarter01@gmail.com");
        map.add("hmpgPwdCphd", "7641955munha!");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri.toString(), request, String.class);

        log.debug("status code : {}", response.getStatusCode());
        log.debug("response-header : {}", response.getHeaders());
        log.debug("response-body : {}", response.getBody());

        return response;
    }

    public void reserve() {
        StringBuilder sb = new StringBuilder();

        URI uri = UriComponentsBuilder
                .fromUriString("https://app.srail.or.kr:443")
                .path("/arc/selectListArc05013_n.do")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
        ReserveVo reserveVo = new ReserveVo();
        reserveVo.setReserveType("11");
        reserveVo.setJobId("1101");
        reserveVo.setJrnyCnt("1");
        reserveVo.setJrnyTpCd("11");
        reserveVo.setJrnySqno1("001");
        reserveVo.setStndFlg("N");
        reserveVo.setTrnGpCd1("300");
        reserveVo.setStlbTrnClsfCd1("11");
        reserveVo.setDptDt1("20221212");
        reserveVo.setDptTm1("123400");
        reserveVo.setRunDt1("20221212");
        reserveVo.setTrnNo1("00330");
        reserveVo.setDptRsStnCd1("0509");
        reserveVo.setDptRsStnCdNm1("울산(통도사)");
        reserveVo.setArvRsStnCd1("0551");
        reserveVo.setArvRsStnCdNm1("수서");
        reserveVo.setPsgTpCd1("1");
        reserveVo.setPsgInfoPerPrnb1("1");
        reserveVo.setLocSeatAttCd1("000");
        reserveVo.setRqSeatAttCd1("015");
        reserveVo.setDirSeatAttCd1("009");
        reserveVo.setSmkSeatAttCd1("000");
        reserveVo.setEtcSeatAttCd1("000");
        reserveVo.setPsrmClCd1("1");
        reserveVo.setTotPrnb("1");
        reserveVo.setPsgGridcnt("1");

        RequestEntity<String> requestEntity = RequestEntity
                .post(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6")
                .body(reserveVo.toString());

        log.debug("uri : {}", uri.toString());

        log.debug("request-header : {}", requestEntity.getHeaders());
        log.debug("request-body1 : {}", requestEntity.getBody());
        log.debug("request-body2 : {}", requestEntity.toString());
        log.debug("request-body3 : {}", reserveVo.toString());

        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });

    //        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity,
    //                String.class);

    //        log.debug("status code : {}", responseEntity.getStatusCode());
    //        log.debug("response-header : {}", responseEntity.getHeaders());
    //        log.debug("response-body : {}", responseEntity.getBody());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.set("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("reserveType", "11");
        map.add("jobId", "1101");
        map.add("jrnyCnt", "1");
        map.add("jrnyTpCd", "11");
        map.add("jrnySqno1", "001");
        map.add("stndFlg", "N");
        map.add("trnGpCd1", "300");
        map.add("stlbTrnClsfCd1", "11");
        map.add("dptDt1", "20221212");
        map.add("dptTm1", "123400");
        map.add("runDt1", "20221212");
        map.add("trnNo1", "00330");
        map.add("dptRsStnCd1", "0509");
        map.add("dptRsStnCdNm1", "울산(통도사)");
        map.add("arvRsStnCd1", "0551");
        map.add("arvRsStnCdNm1", "수서");
        map.add("psgTpCd1", "1");
        map.add("psgInfoPerPrnb1", "1");
        map.add("locSeatAttCd1", "000");
        map.add("rqSeatAttCd1", "015");
        map.add("dirSeatAttCd1", "009");
        map.add("smkSeatAttCd1", "000");
        map.add("etcSeatAttCd1", "000");
        map.add("psrmClCd1", "1");
        map.add("totPrnb", "1");
        map.add("psgGridcnt", "1");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri.toString(), request, String.class);

        log.debug("status code : {}", response.getStatusCode());
        log.debug("response-header : {}", response.getHeaders());
        log.debug("response-body : {}", response.getBody());

        return;
    }
}
