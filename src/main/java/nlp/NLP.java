package nlp;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

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
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        }
    }

    public AnalysisEngine getPipline() {
        return pAE;
    }

    public JCas analyse(String text) {
        JCas jCas = null;
        try {
            jCas = JCasFactory.createText(text, "de");
            SimplePipeline.runPipeline(jCas, getPipline());
        } catch (UIMAException e) {
            e.printStackTrace();
        }
        return jCas;
    }
}
