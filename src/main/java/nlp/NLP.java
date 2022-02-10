package nlp;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * This Class is analysing text and set up the pipeline.
 * @author benwerner
 * @date
 */

public class NLP {

    private AnalysisEngine pAE = null;

    public NLP() {
        //  creating a Pipeline
        AggregateBuilder pipeline = new AggregateBuilder();

        // add different Engines to the Pipeline
        try {
            pipeline.add(createEngineDescription(SpaCyMultiTagger3.class,
                    SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"));

            String sPOSMapFile = "src/main/resources/am_posmap.txt";

            pipeline.add(createEngineDescription(LabelAnnotatorDocker.class,
                    LabelAnnotatorDocker.PARAM_FASTTEXT_K, 100,
                    LabelAnnotatorDocker.PARAM_CUTOFF, false,
                    LabelAnnotatorDocker.PARAM_SELECTION, "text",
                    LabelAnnotatorDocker.PARAM_TAGS, "ddc3",
                    LabelAnnotatorDocker.PARAM_USE_LEMMA, true,
                    LabelAnnotatorDocker.PARAM_ADD_POS, true,
                    LabelAnnotatorDocker.PARAM_POSMAP_LOCATION, sPOSMapFile,
                    LabelAnnotatorDocker.PARAM_REMOVE_FUNCTIONWORDS, true,
                    LabelAnnotatorDocker.PARAM_REMOVE_PUNCT, true,
                    LabelAnnotatorDocker.PARAM_REST_ENDPOINT, "http://ddc.prg2021.texttechnologylab.org"));

            pipeline.add(createEngineDescription(GerVaderSentiment.class,
                    GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.prg2021.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"));

            // create an AnalysisEngine for running the Pipeline.
            pAE = pipeline.createAggregate();
            pAE.getLogger().setLevel(Level.OFF);
            pAE.getUimaContext().getLogger().setLevel(Level.OFF);
            pAE.getUimaContextAdmin().getLogger().setLevel(Level.OFF);
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        }
    }

    public AnalysisEngine getPipeline() {
        return pAE;
    }

    public JCas analyse(String text) {
        JCas jCas = null;
        try {
            jCas = JCasFactory.createText(text, "de");
            SimplePipeline.runPipeline(jCas, getPipeline());
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return jCas;
    }

    /**
     * Methode that convert an jCas to an xml.
     * @param jCas
     * @return xml
     */
    public static String getXml(JCas jCas) {
        CAS cas = jCas.getCas();
        ByteArrayOutputStream outTmp = new ByteArrayOutputStream();
        try {
            XCASSerializer.serialize(cas, outTmp);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        String xml = outTmp.toString();
        return xml;
    }
}
