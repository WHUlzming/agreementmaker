package am.userInterface.canvas2.nodes;

import java.awt.Graphics;

import am.userInterface.canvas2.graphical.GraphicalData;
import am.userInterface.canvas2.graphical.MappingData;
import am.userInterface.canvas2.graphical.MappingData.MappingType;
import am.userInterface.canvas2.utility.Canvas2Edge;
import am.userInterface.canvas2.utility.Canvas2Vertex;

public class LegacyMapping extends Canvas2Edge {
	
	public LegacyMapping(Canvas2Vertex orig, Canvas2Vertex dest, GraphicalData o, int matcherID, String label ) {
		super(orig, dest, o);
		
		GraphicalData source = orig.getObject();
		GraphicalData target = dest.getObject();
		
		int sourceX = source.x + source.width;
		int sourceY = source.y + (source.height / 2);
		
		int targetX = target.x + LegacyNode.circlePadding;
		int targetY = target.y + (target.height / 2); 
		
		int width = targetX - sourceX;
		int height = targetY - sourceY;
		
		MappingType mappingType = MappingType.NOT_SET; 
		if( source.type == GraphicalData.NodeType.CLASS_NODE || source.type == GraphicalData.NodeType.CLASSES_ROOT ) {
			mappingType = MappingType.ALIGNING_CLASSES;
		} else if( source.type == GraphicalData.NodeType.PROPERTY_NODE || source.type == GraphicalData.NodeType.PROPERTIES_ROOT ) {
			mappingType = MappingType.ALIGNING_PROPERTIES;
		}
		
		MappingData data = new MappingData( sourceX, sourceY, width, height, source.layout, 
											source.r, target.r, source.ontologyID, target.ontologyID, matcherID, mappingType );
		data.setLabel(label);
		
		setObject( data );
		
		
	}
	
	@Override
	public void draw(Graphics g ) {
		g.setColor( ((MappingData)d).color );
		g.drawLine(d.x , d.y, d.x+d.width, d.y+d.height);
		if( ((MappingData)d).label != null )
			g.drawString( ((MappingData)d).label, d.x+d.width/2, d.y+d.height/2);
	}

}
