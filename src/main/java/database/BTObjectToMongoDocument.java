package database;

import data.Comment;
import data.Member;
import data.Speech;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import nlp.NLP;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.annotation.AnnotationComment;
import org.texttechnologylab.uimadb.wrapper.mongo.MongoSerialization;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.CasSerializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.SerializerInitializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.UnknownFactoryException;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to create the Documents for DB with NLP analyse
 * @author benwerner
 * @date
 */
public class BTObjectToMongoDocument {

    private static NLP nlp = new NLP();
    private static HashMap<String, String> dcc = new HashMap();
    private static HashMap<String, String> posMap = new HashMap();

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

//        List<String> lemmaList = new ArrayList<>(0);
//        for (Lemma lemma : JCasUtil.select(jCas, Lemma.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
//            System.out.println(lemma);
//            lemmaList.add(lemma.getCoveredText());
//        }

        List<String> posList = new ArrayList<>(0);
        for (POS pos : JCasUtil.select(jCas, POS.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            String posC = pos.getPosValue();
            posList.add(posMap.get(posC));
        }

        List<Document> topicList = new ArrayList<>(0);
        for (CategoryCoveredTagged category : JCasUtil.select(jCas, CategoryCoveredTagged.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            String num = category.getValue().replace("__label_ddc__", "");
            Document topicDoc = new Document().append(dcc.get(num), category.getScore());
            topicList.add(topicDoc);
        }

        speechDoc.append("sentences", sentencesList);
        speechDoc.append("token", tokenList);
        speechDoc.append("sentiment", sentiment);
        speechDoc.append("sentimentList", sentimentList);
        //speechDoc.append("lemma", lemmaList);
        speechDoc.append("pos", posList);
        speechDoc.append("persons", personsList);
        speechDoc.append("locations", locationsList);
        speechDoc.append("organisations", organisationsList);
        speechDoc.append("category", topicList);

        String xml = nlp.getXml(jCas);
        if (xml.getBytes().length <= (16000000 - speechDoc.size())){
            speechDoc.append("uima", xml);
        } else {
            speechDoc.append("uima", null);
        }

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

        //List<String> lemmaList = new ArrayList<>(0);
        //for (Lemma lemma : JCasUtil.select(jCas, Lemma.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
        //    lemmaList.add(lemma.getCoveredText());
        //}


        List<String> posList = new ArrayList<>(0);
        for (POS pos : JCasUtil.select(jCas, POS.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            String posC = pos.getPosValue();
            posList.add(posMap.get(posC));
        }

        List<Document> topicList = new ArrayList<>(0);
        for (CategoryCoveredTagged category : JCasUtil.select(jCas, CategoryCoveredTagged.class).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
            String num = category.getValue().replace("__label_ddc__", "");
            Document topicDoc = new Document().append(dcc.get(num), category.getScore());
            topicList.add(topicDoc);
        }

        commentDoc.append("sentences", sentencesList);
        commentDoc.append("token", tokenList);
        commentDoc.append("sentiment", sentiment);
        commentDoc.append("sentimentList", sentimentList);
        //commentDoc.append("lemma", lemmaList);
        commentDoc.append("pos", posList);
        commentDoc.append("persons", personsList);
        commentDoc.append("locations", locationsList);
        commentDoc.append("organisations", organisationsList);
        commentDoc.append("category", topicList);

        String xml = nlp.getXml(jCas);
        if (xml.getBytes().length <= (16000000 - commentDoc.size())){
            commentDoc.append("uima", xml);
        } else {
            commentDoc.append("uima", null);
        }

        return commentDoc;
    }
}
