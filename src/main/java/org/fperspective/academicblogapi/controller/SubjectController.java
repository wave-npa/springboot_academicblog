package org.fperspective.academicblogapi.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import org.fperspective.academicblogapi.model.Subject;
import org.fperspective.academicblogapi.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@Tag(name = "Subject", description = "Subject Management API")
@RequestMapping("/api/v1/subject")
@CrossOrigin
@ApiResponses(value = {
    @ApiResponse (responseCode = "200", content = { @Content(schema = @Schema(implementation = Subject.class), mediaType = "application/json") }),
    @ApiResponse (responseCode = "404", content = { @Content(schema = @Schema()) }),
    @ApiResponse (responseCode = "500", content = { @Content(schema = @Schema()) }) })
public class SubjectController {
    
    @Autowired
    // @Lazy
    private SubjectService subjectService;

    @Hidden
    @RequestMapping("/")
    @CrossOrigin
    public void redirect(HttpServletResponse response) throws IOException{
        response.sendRedirect("/swagger-ui.html");
    }

    @GetMapping("/show")
    @CrossOrigin
    public Collection<Subject> get(){
        return subjectService.get();
    }

    @GetMapping("/show/{subjectId}")
    @CrossOrigin
    public Subject get(@PathVariable String subjectId) {
        Subject subject = subjectService.get(subjectId);
        if (subject == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return subject;
    }

    @GetMapping("/sort/{limit}")
    @CrossOrigin
    //Show most popular subjects and its used count as Hashmap
    public List<Subject> findMostUsedSubject(@PathVariable String limit) {
        List<Subject> subjects = subjectService.findMostUsedSubject(limit);
        if (subjects == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return subjects;
    }

    @GetMapping("/date/{limit}/{startDate}/{endDate}")
    @CrossOrigin
    //Show most popular subjects and its used count as Hashmap
    public List<Subject> findMostUsedSubjectByDate(@PathVariable("limit") String limit, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) throws ParseException {
        List<Subject> subjects = subjectService.findMostUsedSubjectByDate(limit, startDate, endDate);
        if (subjects == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return subjects;
    }

    @GetMapping("/search/blog/{blogId}")
    @CrossOrigin
    //Show most popular subjects and its used count as Hashmap
    public List<Subject> findTagByBlog(@PathVariable String blogId) {
        List<Subject> subjects = subjectService.findTagByBlog(blogId);
        if (subjects == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return subjects;
    }

    @GetMapping("/search/text/{text}")
    @CrossOrigin
    //Show most popular subjects and its used count as Hashmap
    public List<Subject> findTagByName(@PathVariable String text) {
        List<Subject> subjects = subjectService.search(text);
        if (subjects == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return subjects;
    }

    @GetMapping("/count/{subjectName}")
    @CrossOrigin
    //Show most popular subjects and its used count as Hashmap
    public Integer searchCount(@PathVariable String subjectName) {
        Integer subjects = subjectService.findMostUsedTagCount(subjectName);
        if (subjects == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return subjects;
    }

    @DeleteMapping("/delete/{subjectId}")
    @CrossOrigin
    public void delete(@PathVariable String subjectId) {
        subjectService.remove(subjectId);
    }

    @DeleteMapping("/enable/{subjectId}")
    @CrossOrigin
    public void enable(@PathVariable String subjectId) {
        subjectService.enable(subjectId);
    }

    @PostMapping("/show")
    @CrossOrigin
    public Subject save(@RequestBody Subject subject){
        return subjectService.save(subject);
    }

    @PostMapping("/update")
    @CrossOrigin
    public Subject update(@RequestBody Subject subject){
        return subjectService.update(subject);
    }
}
