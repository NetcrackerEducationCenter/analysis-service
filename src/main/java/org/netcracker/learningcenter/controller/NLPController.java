package org.netcracker.learningcenter.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.models.auth.In;
import org.netcracker.learningcenter.service.NLPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class NLPController {
    private final NLPService nlpService;
    private static final String NEEDEDINFO = "neededInfo";
    private static final String SEARCHTEXT = "searchText";

    @Autowired
    public NLPController(NLPService nlpService) {
        this.nlpService = nlpService;
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @PostMapping(value = "/split", produces = "application/json", consumes = "application/json")
    public List<String> splitText(@RequestBody JsonNode jsonNode) {
        JsonNode neededInfo = jsonNode.path(NEEDEDINFO);
        JsonNode searchText = jsonNode.path(SEARCHTEXT);
        return nlpService.searchRequiredInformation(neededInfo.asText(), searchText.asText());

    }
}
