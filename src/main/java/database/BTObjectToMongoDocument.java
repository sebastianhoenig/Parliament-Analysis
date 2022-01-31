package database;


import data.Comment;
import data.Speech;
import data.helper.XMLFileReader;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import nlp.NLP;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.hucompute.textimager.uima.type.Sentiment;
import org.texttechnologylab.annotation.AnnotationComment;
import org.texttechnologylab.uimadb.wrapper.mongo.MongoSerialization;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.CasSerializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.SerializerInitializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.UnknownFactoryException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to create the Documents for DB with NLP analyse
 * @author benwerner
 * @date
 */
public class BTObjectToMongoDocument {

    private static NLP nlp = new NLP();

//    public static Document createMongoDocument(Speaker speaker) {
//        Document speakerDoc = new Document();
//        speakerDoc.append("_id", speaker.getID());
//        // @Todo: Andere Attribute müssen ergänzt werden
//        return speakerDoc;
//    }

    public static Document createMongoDocument(Speech speech) {
        Document speechDoc = new Document();
        speechDoc.append("_id", speech.getSpeechID());
        speechDoc.append("text", speech.getText());
        speechDoc.append("speaker", speech.getSpeakerID());

        ArrayList<String> commentIDs = new ArrayList<String>();
        for (Comment comment : speech.getAllComments()) {
            commentIDs.add(comment.getCommentID());
        }
        speechDoc.append("comment", commentIDs);

        // Todo: update some Attributes
        Document protocolDoc = new Document();
        protocolDoc.append("date", speech.getProtocol().getDate()); //Todo: convert to realy Date ?
        protocolDoc.append("electionPeriod", speech.getProtocol().getElectionPeriod());
        protocolDoc.append("sessionID", speech.getProtocol().getSessionID());
        protocolDoc.append("title", speech.getProtocol().getTitle()); //Todo: update
        //protocolDoc.append("startPageNr", speech.getProtocol().getStartPageNr());

        speechDoc.append("protocol", protocolDoc);

        speechDoc.append("agendaItem", speech.getAgendaItem().getAgendaItemID());

        // @Todo: NLP
        // NLP größten teils aus der Musterlösung aus Aufgabe 2.
        JCas jCas = nlp.analyse(speech.getText());

        try {
            MongoSerialization.serializeJCas(jCas);
        } catch (UnknownFactoryException e) {
            e.printStackTrace();
        } catch (SerializerInitializationException e) {
            e.printStackTrace();
        } catch (CasSerializationException e) {
            e.printStackTrace();
        }

        List<String> personsList = new ArrayList<>(0);
        List<String> locationsList = new ArrayList<>(0);
        List<String> organisationsList = new ArrayList<>(0);
        for (NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            String value = namedEntity.getValue();
            if (value.matches("PER")) {
                personsList.add(namedEntity.getCoveredText());
            } else if (value.matches("LOC")) {
                locationsList.add(namedEntity.getCoveredText());
            } else if (value.matches("ORG")) {
                organisationsList.add(namedEntity.getCoveredText());
            }
        }

        List<String> tokenList = new ArrayList<>(0);
        for (Token token : JCasUtil.select(jCas, Token.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            tokenList.add(token.getCoveredText());
        }


        List<String> sentencesList = new ArrayList<>(0);
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            sentencesList.add(sentence.getCoveredText());
        }

        Double sentimentList = 0.0;
        if (getDocumentSentiment(jCas) != null) {
            sentimentList = getDocumentSentiment(jCas).getSentiment();
        } else {
            sentimentList = JCasUtil.select(jCas, Sentiment.class).stream().mapToDouble(sentiment -> sentiment.getSentiment()).sum();
        }

        //List<String> lemmaList = new ArrayList<>(0);
        //for (Lemma lemma : JCasUtil.select(jCas, Lemma.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
        //    lemmaList.add(lemma.getCoveredText());
        //}


        List<String> posList = new ArrayList<>(0);
        for (POS pos : JCasUtil.select(jCas, POS.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            posList.add(pos.getPosValue());
        }

        speechDoc.append("sentences", sentencesList);
        speechDoc.append("token", tokenList);
        speechDoc.append("sentiment", sentimentList);
        //speechDoc.append("lemma", lemmaList);
        speechDoc.append("pos", posList);
        speechDoc.append("persons", personsList);
        speechDoc.append("locations", locationsList);
        speechDoc.append("organisations", organisationsList);


        return speechDoc;
    }

    public static Document createMongoDocument(Comment comment) {
        Document commentDoc = new Document();
        commentDoc.append("_id", comment.getCommentID());
        commentDoc.append("text", comment.getText());
        // @Todo: Speaker und NLP (und gegebenenfalls Speech??)

        // @Todo: NLP
        // NLP größten teils aus der Musterlösung aus Aufgabe 2.
        JCas jCas = nlp.analyse(comment.getText());

        try {
            MongoSerialization.serializeJCas(jCas);
        } catch (UnknownFactoryException e) {
            e.printStackTrace();
        } catch (SerializerInitializationException e) {
            e.printStackTrace();
        } catch (CasSerializationException e) {
            e.printStackTrace();
        }

        List<String> personsList = new ArrayList<>(0);
        List<String> locationsList = new ArrayList<>(0);
        List<String> organisationsList = new ArrayList<>(0);
        for (NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            String value = namedEntity.getValue();
            if (value.matches("PER")) {
                personsList.add(namedEntity.getCoveredText());
            } else if (value.matches("LOC")) {
                locationsList.add(namedEntity.getCoveredText());
            } else if (value.matches("ORG")) {
                organisationsList.add(namedEntity.getCoveredText());
            }
        }

        List<String> tokenList = new ArrayList<>(0);
        for (Token token : JCasUtil.select(jCas, Token.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            tokenList.add(token.getCoveredText());
        }


        List<String> sentencesList = new ArrayList<>(0);
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            sentencesList.add(sentence.getCoveredText());
        }

        Double sentimentList = 0.0;
        if (getDocumentSentiment(jCas) != null) {
            sentimentList = getDocumentSentiment(jCas).getSentiment();
        } else {
            sentimentList = JCasUtil.select(jCas, Sentiment.class).stream().mapToDouble(sentiment -> sentiment.getSentiment()).sum();
        }

        //List<String> lemmaList = new ArrayList<>(0);
        //for (Lemma lemma : JCasUtil.select(jCas, Lemma.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
        //    lemmaList.add(lemma.getCoveredText());
        //}


        List<String> posList = new ArrayList<>(0);
        for (POS pos : JCasUtil.select(jCas, POS.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            posList.add(pos.getPosValue());
        }

        commentDoc.append("sentences", sentencesList);
        commentDoc.append("token", tokenList);
        commentDoc.append("sentiment", sentimentList);
        //commentDoc.append("lemma", lemmaList);
        commentDoc.append("pos", posList);
        commentDoc.append("persons", personsList);
        commentDoc.append("locations", locationsList);
        commentDoc.append("organisations", organisationsList);

        return commentDoc;
    }

    /**
     * Copied by Musterlösung aus Übung2
     * @param jCas
     * @return
     */
    public static Sentiment getDocumentSentiment(JCas jCas) {
        List<AnnotationComment> annoComments = JCasUtil.select(jCas, AnnotationComment.class).stream().filter(d->{

            if(d.getReference() instanceof Sentiment){
                return d.getValue().equalsIgnoreCase("text");
            }
            return false;

        }).collect(Collectors.toList());

        if(annoComments.size()>=1){
            return (Sentiment)annoComments.get(0).getReference();
        }

        return null;
    }
}
