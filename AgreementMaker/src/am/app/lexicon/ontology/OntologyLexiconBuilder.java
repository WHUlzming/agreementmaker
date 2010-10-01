package am.app.lexicon.ontology;

import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import am.Utility;
import am.app.lexicon.GeneralLexicon;
import am.app.lexicon.GeneralLexiconSynSet;
import am.app.lexicon.Lexicon;
import am.app.lexicon.LexiconBuilder;
import am.app.lexicon.LexiconSynSet;
import am.app.mappingEngine.LexiconStore.LexiconRegistry;
import am.app.ontology.Node;
import am.app.ontology.Ontology;

/**
 * Build a lexicon from an ontology (via synonyms and definitions in the ontology).
 * @author cosmin
 *
 */
public class OntologyLexiconBuilder implements LexiconBuilder {

	Property synonymProperty;
	Property labelProperty;
	Property definitionProperty;

	Ontology currentOntology;
	
	Lexicon currentLexicon;
	
	public OntologyLexiconBuilder( Ontology ont, Property label, Property synonym, Property definition ) {
		currentOntology = ont;
		currentLexicon = new GeneralLexicon( LexiconRegistry.ONTOLOGY_LEXICON );
		
		synonymProperty = synonym;
		labelProperty = label;
		definitionProperty = definition;
	}
	
	public Lexicon buildLexicon() {
		
		//long id = 0;
		// Iterate through all the classes of the current ontology
		Iterator<Node> classesIterator = currentOntology.getClassesList().iterator();
		while( classesIterator.hasNext() ) {
			try {
				Node currentClassNode = classesIterator.next();
				OntClass currentClass = currentClassNode.getResource().as(OntClass.class);
				
				ArrayList<String> synonyms = getAllSynonyms( currentClass, labelProperty, synonymProperty );
				if( synonyms.isEmpty() ) continue; // skip this class if it has no synonyms
				
				// Step 1.  Check if any of these word forms are in the Lexicon already. (probably don't need to do this???)
				
				ArrayList<LexiconSynSet> synSetsForCurrentClass = new ArrayList<LexiconSynSet>();
				Iterator<String> currentSynonym = synonyms.iterator();
				while( currentSynonym.hasNext() ) {
					String syn = currentSynonym.next();
					LexiconSynSet synList = currentLexicon.getSynSet(syn);
					
					if( synList == null ) continue; // synset not found
					
					// we have found a synset with this word form (should not happen??)
					synSetsForCurrentClass.add(synList);
				}
				
				
				// Step 2. Create a new synset for this class.
				GeneralLexiconSynSet classSynSet = new GeneralLexiconSynSet();
				
				// add all the synonyms to the synset
				Iterator<String> synonymIter = synonyms.iterator();
				while( synonymIter.hasNext() ) { classSynSet.addSynonym(synonymIter.next()); }
				
				// keep track of what concept these synonyms are for
				classSynSet.setOntologyConcept(currentClass.as(OntResource.class));
				
				
				try {
					// check for a definition
					if( definitionProperty != null ) {
						Statement definition = currentClass.getProperty(definitionProperty);
						if( definition != null ) {
							RDFNode def = definition.getObject();
							if( def.canAs( Literal.class ) ) {
								Literal defLiteral = def.asLiteral();
								classSynSet.setGloss(defLiteral.getString());
							} else if ( def.canAs( Individual.class ) ) {
								Individual defIndividual = def.as(Individual.class);
								classSynSet.setGloss(defIndividual.getLabel(null));
							} else {
								throw new Exception("Cannot understand the definition property.");
							}
						}
					}
				} catch( Exception e ) {
					e.printStackTrace();
				}
	
				
				// add links between the two synsets
				for( LexiconSynSet currentSynSet: synSetsForCurrentClass ) {
					classSynSet.addRelatedSynSet(currentSynSet);
					currentSynSet.addRelatedSynSet(classSynSet);
				}
				
				// done creating the syn set
				currentLexicon.addSynSet(classSynSet); // This probably does NOT need to be done.
			} catch( Exception e ) {
				e.printStackTrace(); // just skip this term
			}
			
		} // while		

		
		return currentLexicon;
	}


	/**
	 * Return a list of all the synonyms associated with a class (label + other synonyms)
	 * @param currentClass
	 * @return
	 * @throws Exception 
	 */
	private ArrayList<String> getAllSynonyms(OntClass currentClass, Property labelProperty, Property synProperty) throws Exception {
		ArrayList<String> synList = new ArrayList<String>();
		
		StmtIterator currentLabels = currentClass.listProperties(labelProperty);
		while( currentLabels.hasNext() ) {
			Statement currentLabel = currentLabels.nextStatement();
			RDFNode label = currentLabel.getObject();
			if( label.canAs(Literal.class) ) {
				// the label points to a literal.  (This should always be the case.)
				Literal labelLiteral = label.asLiteral();
				synList.add(labelLiteral.getString());
			}
		}
		
		
		StmtIterator currentSynonyms = currentClass.listProperties(synProperty);
		while( currentSynonyms.hasNext() ) {
			Statement currentSynonym = currentSynonyms.nextStatement();
			
			// expecting annotation or datatype property (i.e. points to a literal)
			RDFNode syn = currentSynonym.getObject();
			
			if( syn.canAs(Literal.class) ) {
				Literal synLiteral = syn.asLiteral();
				synList.add(synLiteral.getString());
			} else  if( syn.canAs(Individual.class) ){
				Individual synIndividial = syn.as(Individual.class);
				synList.add( synIndividial.getLabel(null) );
			} else {
				throw new Exception("Cannot understand the synonym property.");
			}
		}
		
		return synList;
	}

}
