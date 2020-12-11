package pl.bgora.reactive.client;

import org.reactivestreams.Subscription;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bgora.reactive.client.person.Person;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;

@RestController
public class ClientController {

    private final WebClient webClient;

    public ClientController() {
        webClient = WebClient.create("localhost:8090");
    }

    @GetMapping("getData")
    public void getData() {
        webClient.get().uri("/data")
                 .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Integer.class))
                 .log()
                 .subscribeWith(new BaseSubscriber<Integer>() {

                     private int count = 0;

                     @Override
                     protected void hookOnSubscribe(Subscription subscription) {
                         request(10);
                     }

                     @Override
                     protected void hookOnNext(Integer value) {
                         System.out.println("Count: " + ++count);
                         System.out.println("value: " + value);
                         if (count >= 5) {
                             cancel();
                         }
                     }

                     @Override
                     protected void hookOnCancel() {
                         System.out.print("Canceled!");
                     }

                     @Override
                     protected void hookOnComplete() {
                         System.out.print("Completed!");
                     }
                 });
    }

    @PostMapping("/person")
    public Mono<Person> addPerson(@RequestBody Person person) {
        return webClient.post()
                        .uri("/person")
                        .body(Mono.just(person), Person.class)
                        .exchangeToMono(clientResponse -> {
                            if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                                return clientResponse.bodyToMono(Person.class);
                            } else {
                                return Mono.error(clientResponse.createException());
                            }
                        });
    }
}
