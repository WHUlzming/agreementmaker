package am.app.mappingEngine.baseSimilarity;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import am.app.Core;
import am.app.mappingEngine.AbstractMatcherParametersPanel;
import am.app.mappingEngine.AbstractParameters;
import am.app.mappingEngine.MatcherSetting;
import am.userInterface.AppPreferences;

public class BaseSimilarityMatcherParametersPanel extends AbstractMatcherParametersPanel implements ItemListener {

	/**
	 * Base Similarity Matcher - The Parameters Panel
	 * @author Cosmin Stroe
	 * @date Nov 22, 2008
	 * ADVIS @ UIC
	 */
	private static final long serialVersionUID = -7652636660460034435L;

	private BaseSimilarityParameters parameters;
	
	private JLabel useDictionaryLabel;
	private JLabel warningLabel;
	private JCheckBox useDictionaryCheckbox;
	
	private AppPreferences prefs;
	
	/*
	 * The constructor creates the GUI elements and adds 
	 * them to this panel.  It also creates the parameters object.
	 * 
	 */
	public BaseSimilarityMatcherParametersPanel() {
		super();
		
		
		this.setPreferredSize(new Dimension(350, 175) );
		
		prefs = Core.getInstance().getUI().getAppPreferences();
		parameters = new BaseSimilarityParameters();
		
		useDictionaryLabel = new JLabel("<html>Would you like to consult a dictionary while performing the Base Similarity Matching ?</html>");
		useDictionaryLabel.setAlignmentX((float) 0.5);
		
		warningLabel = new JLabel("<html><i>Warning: Consulting a dictionary can dramatically increase matching time!</i></html>");
		warningLabel.setAlignmentX((float) 0.5);
		
				
		useDictionaryCheckbox = new JCheckBox("Use Dictionary");
		parameters.useDictionary = prefs.getPanelBool( MatcherSetting.BSIM_USEDICT );  // get the saved setting
		useDictionaryCheckbox.setSelected( parameters.useDictionary ); // update the checkbox
		useDictionaryCheckbox.addItemListener(this);  // when the checkbox toggles, we update our parameters.
		
		
		
		// The GUI layout - a pain in the butt to get right
		
		BoxLayout panelLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		
		Box paddingBox = Box.createHorizontalBox();
		
		Box contentBox = Box.createVerticalBox();
		
		contentBox.add(Box.createVerticalStrut(20));
		contentBox.add(useDictionaryLabel);
		contentBox.add(Box.createVerticalStrut(20));
		contentBox.add(warningLabel);
		contentBox.add(Box.createVerticalStrut(20));
		contentBox.add(useDictionaryCheckbox);
		contentBox.add(Box.createVerticalStrut(20));
		
		paddingBox.add(Box.createHorizontalStrut(20));
		paddingBox.add(contentBox);
		paddingBox.add(Box.createHorizontalStrut(20));
		
		
		this.setLayout(panelLayout);
		
		this.add(paddingBox);
		
	}
	
	
	public AbstractParameters getParameters() {
		
		return parameters;
		
	}
	
	public String checkParameters() {
		
		prefs.savePanelBool( MatcherSetting.BSIM_USEDICT, parameters.useDictionary );
		
		return null;
	}


	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getItemSelectable();
		
		if( source == useDictionaryCheckbox ) {
			if( e.getStateChange() == ItemEvent.SELECTED ) {
				parameters.useDictionary = true;
			} else {  // DESELECTED
				parameters.useDictionary = false;
			}
		}
		
	}
}