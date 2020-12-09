package pl.bgora.reactive.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ClientController {

    private final WebClient webClient;

    public ClientController() {
        webClient = WebClient.create("localhost:8080/data");
    }

    @GetMapping("getData")
    public void getData() {
        webClient.get()
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(String.class))
                .limitRate(1)
                .doOnNext(System.out::println)
                .log()
                .subscribe();
    }
}
