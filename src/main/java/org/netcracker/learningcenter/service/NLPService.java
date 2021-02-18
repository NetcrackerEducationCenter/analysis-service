package org.netcracker.learningcenter.service;

import java.math.BigDecimal;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.math3.util.Precision;
import org.netcracker.learningcenter.utils.Pipeline;
import org.netcracker.learningcenter.utils.FrequencyTextAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NLPService {

    private final FrequencyTextAnalysis frequencyTextAnalysis;
    private final Pipeline pipeline;

    @Autowired
    public NLPService(FrequencyTextAnalysis frequencyTextAnalysis, Pipeline pipeline) {
        this.frequencyTextAnalysis = frequencyTextAnalysis;
        this.pipeline = pipeline;
    }

    /**
     * Selects main information from a given list of texts containing words from keywords list
     *
     * @param keywords    a list of words that should be contained in the analyzed texts
     * @param searchTexts a list of texts in which the requested information is searched
     * @return list of selected main information from texts
     */
    public List<String> searchInformation(List<String> keywords, List<String> searchTexts, int accuracy, int minSentenceNumbers, int topWordsCount) {
        List<String> foundInfo = new ArrayList<>();
        for (String text : searchTexts) {
            Annotation annotation = pipeline.getStanfordCoreNLP().process(text);
            List<CoreMap> sentenses = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            List<String> topWords = Counters.topKeys(frequencyTextAnalysis.termFrequency(sentenses), topWordsCount);
            if (containsKeywords(topWords, keywords)) {
                if (sentenses.size() <= minSentenceNumbers) {
                    foundInfo.add(text);
                } else {
                    foundInfo.add(findImportantInfo(sentenses, accuracy));
                }
            }
        }
        return foundInfo;
    }


    private boolean containsKeywords(List<String> topWords, List<String> keywords) {
        for (String word : keywords) {
            if (topWords.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Analyzes the text and based on the weights of the sentences extracts the main information
     *
     * @param sentenses list of sentences from text
     * @return main sentences from the text
     */
    private String findImportantInfo(List<CoreMap> sentenses, int accuracy) {
        StringBuilder sb = new StringBuilder();
        Counter<Integer> weights = frequencyTextAnalysis.sentencesWeights(sentenses);
        double averageWeight = Precision.round(Counters.mean(weights), accuracy, BigDecimal.ROUND_DOWN);
        for (Integer k : weights.keySet()) {
            if (weights.getCount(k) >= averageWeight) {
                sb.append(sentenses.get(k));
            }
        }
        return sb.toString();
    }
}
