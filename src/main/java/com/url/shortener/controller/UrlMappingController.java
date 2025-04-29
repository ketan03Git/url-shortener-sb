//1.This controller allows authenticated users with the "USER" role to shorten URLs.
//2.It extracts the originalUrl from the request body.
//3.It fetches the logged-in user using UserService.
//4.It creates a short URL using UrlMappingService.

package com.url.shortener.controller;

import com.url.shortener.dtos.UrlMappingDTO;
import com.url.shortener.dtos.clickEventDTO;
import com.url.shortener.models.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController     //It allows the controller to handle HTTP requests and automatically converts responses to JSON
@RequestMapping("/api/urls")
@AllArgsConstructor     //This automatically generates a constructor that injects dependencies (UrlMappingService and UserService) into the controller.
public class UrlMappingController  {
    private UrlMappingService urlMappingService;
    private UserService userService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")    //Ensures that only authenticated users with the role "USER" can access this endpoint.
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request,
                                                        Principal principal){
        String originalUrl =  request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());    //Fetches the User object based on the logged-in username.
        UrlMappingDTO urlMappingDto = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDto);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")    //Ensures that only authenticated users with the role "USER" can access this endpoint.
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal){
        User user = userService.findByUsername(principal.getName());    //Fetches the User object based on the logged-in username.
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);   //Retrieves all URLs created by this user. Returns them as DTOs (List<UrlMappingDTO>).
        return ResponseEntity.ok(urls);
    }

    // REST API endpoint that retrieves analytics (click event data) for a given shortened URL, within a specific date-time range
    @GetMapping("/analytics/{shortUrl}")    //{shortUrl} is a path variable
    @PreAuthorize("hasRole('USER')")    //Ensures that only authenticated users with the role "USER" can access this endpoint.
    public ResponseEntity<List<clickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
                                                               @RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);    //allows comparing, filtering, and processing parsed startDate,endDate as actual date-time objects in Java
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<clickEventDTO> clickEventDTOS = urlMappingService.getClickEventByDate(shortUrl, start, end);   //method in the urlMappingService to retrieve click events between the start and end dates for the given short URL
        return ResponseEntity.ok(clickEventDTOS);
    }
}
