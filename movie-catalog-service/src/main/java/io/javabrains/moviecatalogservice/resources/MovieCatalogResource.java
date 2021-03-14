package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired //Advanced Loadbalancing !
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebClient.Builder webClientBuilder ;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId)
    {
        // Replaced this http://localhost:8083/ratingsdata/users/foo
      UserRating ratings =restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/foo" + userId,UserRating.class);
      return ratings.getUserRating().stream().map( rating ->
      {
          // For each movie ID , call movie info service and get details
         Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+ rating.getMovieID() ,Movie.class);
    //Another way to see how asynch method works
       /*  Movie movie= webClientBuilder.build()
                  .get() // REST Call
                  .uri("http://localhost:8082/movies/"+ rating.getMovieID())// URL
                  .retrieve() // fetch the data
                  .bodyToMono(Movie.class) // Convert the body in instance of Movie class
                   // Mono=Reactive way of promising that you will get back data ,not right away (asynchronous)
                  .block(); //Blocking the execution until data is gathered (mono is fulfilled) !!*/

          //Put them all together
          return new CatalogItem(movie.getName(), "Desc", rating.getRating());
         })
        .collect(Collectors.toList());


//        return Collections.singletonList(
//                new CatalogItem("Transformers", "Test", 4)
//        );

    }
}


