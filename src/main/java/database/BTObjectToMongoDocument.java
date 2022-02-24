package database;

import data.Comment;
import data.Member;
import data.Speech;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import nlp.NLP;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.uimadb.wrapper.mongo.MongoSerialization;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.CasSerializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.SerializerInitializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.UnknownFactoryException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Class to create the Documents for DB with NLP analyse
 * @author benwerner
 * @date
 */
public class BTObjectToMongoDocument {

    /**
     * The nlp param is saving one pipeline.
     */
    private static NLP nlp = new NLP();

    /**
     * The dcc param is to map the old dcc's to the new one from the given file.
     */
    private static HashMap<String, String> dcc = new HashMap();

    /**
     * The posMap param is to map the old posMap's to the new one from the given file.
     */
    private static HashMap<String, String> posMap = new HashMap();

    /**
     * This function creates the DCC HashMap from the given file.
     */
    public static void createDCCMap() {
        String file = "src/main/resources/ddc3-names-de.csv";
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.length() > 0) {
                String[] splitLine = line.split("\t");
                dcc.put(splitLine[0], splitLine[1]);
            }
        }
        scanner.close();
    }

    /**
     * This function creates the POS HashMap from the given file.
     */
    public static void createPOSMap() {
        String file = "src/main/resources/am_posmap.txt";
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.length() > 0) {
                String[] splitLine = line.split("\t");
                posMap.put(splitLine[0], splitLine[1]);
            }
        }
        scanner.close();
    }

    /**
     * This methode is converting a Member Object to a DB document.
     * @param member
     * @return document
     */
    public static Document createMongoDocument(Member member) {
        Document memberDoc = new Document();
        memberDoc.append("_id", member.getId());
        memberDoc.append("name", member.getName());
        memberDoc.append("surname", member.getSurname());
        memberDoc.append("party", member.getParty());
        memberDoc.append("birthDate", member.getBirthDate());
        memberDoc.append("birthPlace", member.getPlaceOfBirth());
        memberDoc.append("birthCountry", member.getCountryOfBirth());
        memberDoc.append("deathDate", member.getDateOfDeath());
        memberDoc.append("gender", member.getGender());
        memberDoc.append("maritalStatus", member.getMaritalStatus());
        memberDoc.append("religion", member.getReligion());
        memberDoc.append("occupation", member.getOccupation());
        memberDoc.append("picture", member.getMetaData()[0]);
        memberDoc.append("pictureMetadata", member.getMetaData()[1]);
        memberDoc.append("allSpeeches", member.getallSpeeches());

        return memberDoc;
    }

    /**
     * This methode running the pipeline and is converting a Speech Object to a DB document.
     * @param speech
     * @return
     */
    public static Document createMongoDocument(Speech speech) {
        Document speechDoc = new Document();
        speechDoc.append("_id", speech.getSpeechID());
        speechDoc.append("text", speech.getText());
        speechDoc.append("plainText", speech.getPlainText());
        speechDoc.append("speaker", speech.getSpeakerID());

        ArrayList<String> commentIDs = new ArrayList<String>();
        for (Comment comment : speech.getAllComments()) {
            commentIDs.add(comment.getCommentID());
        }
        speechDoc.append("comment", commentIDs);

        Document protocolDoc = new Document();
        protocolDoc.append("date", speech.getProtocol().getDate());
        protocolDoc.append("electionPeriod", speech.getProtocol().getElectionPeriod());
        protocolDoc.append("sessionID", speech.getProtocol().getSessionID());
        protocolDoc.append("title", speech.getProtocol().getTitle());

        speechDoc.append("protocol", protocolDoc);

        speechDoc.append("agendaItem", speech.getAgendaItem().getAgendaItemID());


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

        List<String> sentencesList = new ArrayList<>(0);
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class).stream().collect(Collectors.toList())) {
            sentencesList.add(sentence.getCoveredText());
        }

        Double sentiment = 0.0;
        List<Double> sentimentList = new ArrayList<>(0);
        for (Sentiment sent : JCasUtil.select(jCas, Sentiment.class).stream().collect(Collectors.toList())) {
            if (sent.getBegin() == 0 && sent.getEnd() == speech.getText().length()) {
                continue;
            }
            sentiment += sent.getSentiment();
            sentimentList.add(sent.getSentiment());
        }
        sentiment /= sentimentList.size();

        List<String> posList = new ArrayList<>(0);
        List<String> lemmaList = new ArrayList<>(0);
        List<String> tokenList = new ArrayList<>(0);
        for (Token token : JCasUtil.select(jCas, Token.class).stream().collect(Collectors.toList())) {
            tokenList.add(token.getCoveredText());
            lemmaList.add(token.getLemmaValue());
            String posC = token.getPosValue();
            posList.add(posMap.get(posC));
        }

        Document topicDoc = new Document();
        for (CategoryCoveredTagged category : JCasUtil.select(jCas, CategoryCoveredTagged.class).stream().collect(Collectors.toList())) {
            String num = category.getValue().replace("__label_ddc__", "");
            if (category.getScore() >= 0.01){
                topicDoc.append(dcc.get(num), category.getScore());
            }
        }

        speechDoc.append("sentences", sentencesList);
        speechDoc.append("sentiment", sentiment);
        speechDoc.append("sentimentList", sentimentList);
        speechDoc.append("token", tokenList);
        speechDoc.append("lemma", lemmaList);
        speechDoc.append("pos", posList);
        speechDoc.append("persons", personsList);
        speechDoc.append("locations", locationsList);
        speechDoc.append("organisations", organisationsList);
        speechDoc.append("category", topicDoc);

        String xml = nlp.getXml(jCas);
        if (xml.getBytes().length <= (16000000 - speechDoc.size())){
            speechDoc.append("uima", xml);
        } else {
            speechDoc.append("uima", null);
        }

        return speechDoc;
    }

    /**
     * This methode running the pipeline and is converting a Comment Object to a DB document.
     * @param comment
     * @return document
     */
    public static Document createMongoDocument(Comment comment) {
        Document commentDoc = new Document();
        commentDoc.append("_id", comment.getCommentID());
        commentDoc.append("text", comment.getText());

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


        List<String> sentencesList = new ArrayList<>(0);
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class).stream().collect(Collectors.toList())) {
            sentencesList.add(sentence.getCoveredText());
        }

        Double sentiment = 0.0;
        List<Double> sentimentList = new ArrayList<>(0);
        for (Sentiment sent : JCasUtil.select(jCas, Sentiment.class).stream().collect(Collectors.toList())) {
            if (sent.getBegin() == 0 && sent.getEnd() == comment.getText().length()) {
                continue;
            }
            sentiment += sent.getSentiment();
            sentimentList.add(sent.getSentiment());
        }
        sentiment /= sentimentList.size();

        List<String> posList = new ArrayList<>(0);
        List<String> lemmaList = new ArrayList<>(0);
        List<String> tokenList = new ArrayList<>(0);
        for (Token token : JCasUtil.select(jCas, Token.class).stream().collect(Collectors.toList())) {
            tokenList.add(token.getCoveredText());
            lemmaList.add(token.getLemmaValue());
            String posC = token.getPosValue();
            posList.add(posMap.get(posC));
        }

        Document topicDoc = new Document();
        List<Document> topicList = new ArrayList<>(0);
        for (CategoryCoveredTagged category : JCasUtil.select(jCas, CategoryCoveredTagged.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            String num = category.getValue().replace("__label_ddc__", "");
            if (category.getScore() >= 0.01){
                topicDoc.append(dcc.get(num), category.getScore());
            }
        }

        commentDoc.append("sentences", sentencesList);
        commentDoc.append("sentiment", sentiment);
        commentDoc.append("sentimentList", sentimentList);
        commentDoc.append("token", tokenList);
        commentDoc.append("lemma", lemmaList);
        commentDoc.append("pos", posList);
        commentDoc.append("persons", personsList);
        commentDoc.append("locations", locationsList);
        commentDoc.append("organisations", organisationsList);
        commentDoc.append("category", topicDoc);

        String xml = nlp.getXml(jCas);
        if (xml.getBytes().length <= (16000000 - commentDoc.size())){
            commentDoc.append("uima", xml);
        } else {
            commentDoc.append("uima", null);
        }

        return commentDoc;
    }
}
