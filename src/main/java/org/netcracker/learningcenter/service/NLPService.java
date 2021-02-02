package org.netcracker.learningcenter.service;

import java.util.*;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
import org.netcracker.learningcenter.utils.Pipeline;
import org.springframework.stereotype.Service;

@Service
public class NLPService {

    public List<String> searchRequiredInformation(String neededInformation, String searchText) {
        List<String> s = new ArrayList<>();
        Set<Integer> sentenceNumbers = new TreeSet<>();
        CoreDocument document = new CoreDocument(searchText);
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        StanfordCoreNLP pipeline = Pipeline.getPipeline();
        pipeline.annotate(document);
        System.out.println("Необходимое упоминание "+neededInformation);
        for (CorefChain cc : document.corefChains().values()) {
            System.out.println("\t" + cc);
            String word = new Sentence(cc.getRepresentativeMention().mentionSpan).lemma(0);
            System.out.println("Главное упоминание в предложении "+word);
            if (word.equals(neededInformation.toLowerCase())) {
                System.out.println("Искомое слово упоминается в ");
                for (CorefChain.CorefMention m : cc.getMentionsInTextualOrder()) {
                    System.out.println(m.sentNum);
                    sentenceNumbers.add(m.sentNum);

                }
            }
        }
        List<CoreSentence> coreSentences = document.sentences();
        for (Integer number:sentenceNumbers) {
            s.add(coreSentences.get(number-1).text());
        }
        return s;
    }
}
