package org.fperspective.academicblogapi.service;

import java.util.Collection;
import java.util.List;

import org.fperspective.academicblogapi.model.BTag;
import org.fperspective.academicblogapi.repository.SearchRepository;
import org.fperspective.academicblogapi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SearchRepository searchRepository;

    public Collection<BTag> get() {
        return tagRepository.findAll();
    }

    public BTag get(String tagId) {
        return tagRepository.findById(tagId).orElse(null);
    }

    public void remove(String tagId) {
        tagRepository.deleteById(tagId);
    }

    public BTag save(BTag tag) {
        return tagRepository.save(tag);
    }

    public List<BTag> findMostUsedTag() {
        return searchRepository.findMostUsedTag();
    }

    public List<Integer> findMostUsedTagCount() {
        return searchRepository.findMostUsedTagCount();
    }

}
