package org.netcracker.learningcenter.utils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class FrequencyTextAnalysis {

    private List<String> stopWords;

    public FrequencyTextAnalysis(@Value("${stop.words.file}") String pathToStopWordsFile) throws IOException {
        stopWords = Files.readAllLines(Paths.get(pathToStopWordsFile));
    }

    public Counter<String> termFrequency(List<CoreMap> sentenses) {
        Counter<String> counter = new ClassicCounter<>();
        for (CoreMap sentence : sentenses) {
            for (CoreLabel cl : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                if (!stopWords.contains(cl.originalText().toLowerCase())) {
                    counter.incrementCount(cl.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase());
                }
            }
        }
        return Counters.divideInPlace(counter, counter.size());
    }

    public double sentenceWeight(Counter c, CoreMap s) {
        double weight = 0;
        int wordsCount = 0;
        for (CoreLabel cl : s.get(CoreAnnotations.TokensAnnotation.class)) {
            if (c.getCount(cl.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase()) != 0.0) {
                wordsCount++;
            }
            weight += c.getCount(cl.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase());
        }
        return weight / wordsCount;
    }

    public Counter<Integer> sentencesWeights(List<CoreMap> sentenses) {
        Counter<String> tf = termFrequency(sentenses);
        int sentenseNum = 0;
        Counter<Integer> sentencesWeights = new ClassicCounter<>();
        for (CoreMap s : sentenses) {
            sentencesWeights.setCount(sentenseNum, sentenceWeight(tf, s));
            sentenseNum++;
        }
        return sentencesWeights;
    }
}
