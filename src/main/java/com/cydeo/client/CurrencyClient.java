package com.cydeo.client;


import com.cydeo.dto.response.CurrencyLayerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//currency API
@FeignClient(url = "http://apilayer.net/api", name = "CURRENCY-CLIENT")
public interface CurrencyClient {

    @GetMapping("/live")
    ResponseEntity<CurrencyLayerResponse> getCurrency(
            @RequestParam("access_key") String accessKey,
            @RequestParam("currencies") String currencies
    );
}
