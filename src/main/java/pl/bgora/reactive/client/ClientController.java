package pl.bgora.reactive.client;

import org.reactivestreams.Subscription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;

@RestController
public class ClientController {

    private final WebClient webClient;

    public ClientController() {
        webClient = WebClient.create("localhost:8090/data");
    }

    @GetMapping("getData")
    public void getData() {
        webClient.get()
                 .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Integer.class))
                 .log()
        .subscribeWith(new BaseSubscriber<Integer>() {

            private int count = 0;
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(5);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println("Count:" + ++count);
                System.out.println("value" + value);
                if(count >=5) {
                    cancel();
                }
            }
        });
    }
}
