package org.netcracker.learningcenter.utils;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class Pipeline {
    private String propertiesName = "tokenize,ssplit,pos,lemma";
    private StanfordCoreNLP stanfordCoreNLP;

    public Pipeline() {
        Properties properties = new Properties();
        properties.setProperty("annotators", propertiesName);
        stanfordCoreNLP = new StanfordCoreNLP(properties);
    }

    public StanfordCoreNLP getStanfordCoreNLP() {
        return stanfordCoreNLP;
    }
}
