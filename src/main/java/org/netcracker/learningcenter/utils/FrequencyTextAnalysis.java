package org.netcracker.learningcenter.utils;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Component
public class FrequencyTextAnalysis {

    /**
     * A list of words and symbols that are ignored when determining
     * the frequency of occurrence of words in the text
     */
    private List<String> stopWords;

    public FrequencyTextAnalysis(@Value("${stop.words.file}") String pathToStopWordsFile) throws IOException {
        stopWords = FileResourcesUtils.readListFromFile(pathToStopWordsFile);
    }

    /**
     * Determines the frequency of occurrence of words in the text
     *
     * @param sentenses list of sentences from text
     * @return a counter whose key is a word from the text and the value is the frequency of its use in it
     */
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

    /**
     * Calculates sentence weight based on word frequency
     *
     * @param c counter with the frequency of occurrence of words in the text
     * @param s sentence whose weight is determined
     * @return sentence weight
     */
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

    /**
     * Calculates the weight of all sentences in the text
     *
     * @param sentenses list of sentences from text
     * @return a counter whose key is a ordinal number of the sentence and the value is weight of this sentence
     */
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
