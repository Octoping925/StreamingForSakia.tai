package com.octoping.boardproject_spring.contoller;

import com.octoping.boardproject_spring.domain.Movie;
import com.octoping.boardproject_spring.service.MovieService;
import com.octoping.boardproject_spring.exception.NoMovieFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MovieController {
    MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movie/movieList")
    public String movieList(Model model) {
        List<Movie> movieList = movieService.getMovieList();
        model.addAttribute("movieList", movieList);
        return "/movie/movieList";
    }

    @GetMapping("/movie/watch/{movieId}")
    public ModelAndView watchMovie(@PathVariable String movieId) {
        ModelAndView mav = new ModelAndView();
        Optional<Movie> movie = movieService.getMovie(Long.parseLong(movieId));

        try {
            movie.orElseThrow(() -> new NoMovieFoundException(movieId));
            mav.setViewName("/movie/watch");
            mav.addObject("movie", movie.get());
        }
        catch(NoMovieFoundException e) {
            e.printStackTrace();
            mav.setViewName("redirect:/movie/movieList");
        }

        return mav;
    }

    @GetMapping("/movie/download/{movieId}")
    public ResponseEntity<ResourceRegion> getVideo(@RequestHeader HttpHeaders headers, @PathVariable String movieId) throws IOException {
        try {
            Movie movie =  movieService.getMovie(Long.parseLong(movieId)).orElseThrow(() -> new NoMovieFoundException(movieId));
            UrlResource video = new UrlResource("file:" + movie.getFilePath());
            ResourceRegion resourceRegion;

            final long chunkSize = 1000000L;
            long contentLength = video.contentLength();

            Optional<HttpRange> optional = headers.getRange().stream().findFirst();
            HttpRange httpRange;
            if (optional.isPresent()) {
                httpRange = optional.get();
                long start = httpRange.getRangeStart(contentLength);
                long end = httpRange.getRangeEnd(contentLength);
                long rangeLength = Long.min(chunkSize, end - start + 1);
                resourceRegion = new ResourceRegion(video, start, rangeLength);
            } else {
                long rangeLength = Long.min(chunkSize, contentLength);
                resourceRegion = new ResourceRegion(video, 0, rangeLength);
            }

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
        }
        catch(NoMovieFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
