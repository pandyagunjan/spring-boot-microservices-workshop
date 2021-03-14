package com.javabrains.movieinfoservice.resources;

import com.javabrains.movieinfoservice.model.Movie;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @RequestMapping("/{movieId}")
    public Movie getMoviewInfo(@PathVariable("movieId") String movieId)
    {
      return new Movie(movieId,"Test name");
    }
}
