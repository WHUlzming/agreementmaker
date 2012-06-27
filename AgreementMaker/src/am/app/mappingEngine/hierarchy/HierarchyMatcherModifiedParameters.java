package am.app.mappingEngine.hierarchy;

import am.app.mappingEngine.DefaultMatcherParameters;

import com.hp.hpl.jena.util.LocationMapper;

public class HierarchyMatcherModifiedParameters extends DefaultMatcherParameters {
	
	public LocationMapper mapper;
	
	public HierarchyMatcherModifiedParameters() {
		super();
	}
	
	public HierarchyMatcherModifiedParameters(double threshold, int maxSourceAlign,
			int maxTargetAlign){
		super(threshold, maxSourceAlign, maxTargetAlign);
	}
	

}
