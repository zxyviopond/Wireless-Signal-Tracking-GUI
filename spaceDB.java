import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;


import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.adapter.GeometryAdapter;
import oracle.sdoapi.geom.CoordPoint;
import oracle.sdoapi.geom.Geometry;

import oracle.sql.STRUCT;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.swt.custom.SashForm;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
public class spaceDB{
	private static Text text;
	private static Text text_1;
	private static Text text_2;
	public static MouseListener listener;
	public static Point pointtoquery;
	public static String polygontoquery=null;
	static Connection mainConnection = null;
    static Statement mainStatement = null;
    static ResultSet mainResultSet = null;
    public static PaintListener showap;
    public static PaintListener showbuilding;
    public static PaintListener showpeople;
    public static int querycount=1;
    public static Point APtoquery=null;
    public static int radiusofAPquery=0;
 /**
  * @param args
  */



 public static void main(String[] args) {
  ConnectToDB();	
  // TODO Auto-generated method stub
  final Display display = new Display();
  Shell shlXiaoyunZhang = new Shell(display);
  shlXiaoyunZhang.setSize(1135, 697);
  shlXiaoyunZhang.setText("Xiaoyun Zhang - 8422217173");
  shlXiaoyunZhang.setLayout(new FillLayout(SWT.HORIZONTAL));
  
  Composite composite = new Composite(shlXiaoyunZhang, SWT.NONE);
  composite.setLayout(new FormLayout());
  
  Composite composite_1 = new Composite(composite, SWT.BORDER);
  FormData fd_composite_1 = new FormData();
  fd_composite_1.left = new FormAttachment(0, 10);
  fd_composite_1.top = new FormAttachment(0);
  fd_composite_1.bottom = new FormAttachment(0, 610);
  fd_composite_1.right = new FormAttachment(0, 856);
  composite_1.setLayoutData(fd_composite_1);
  
  final Canvas canvas = new Canvas(composite_1, SWT.NONE);
  canvas.addMouseMoveListener(new MouseMoveListener() {
  	public void mouseMove(MouseEvent e) {
  	text_1.setText(String.valueOf(e.x));
  	text_2.setText(String.valueOf(e.y));
  	}
  });
  canvas.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
  Image image=new Image(null,"map.jpg");
   canvas.setBackgroundImage(image);
  canvas.setBounds(10, 10, 822, 586);
  
  Composite composite_2 = new Composite(composite, SWT.BORDER);
  FormData fd_composite_2 = new FormData();
  fd_composite_2.right = new FormAttachment(composite_1, 251, SWT.RIGHT);
  fd_composite_2.top = new FormAttachment(0);
  fd_composite_2.left = new FormAttachment(composite_1);
  fd_composite_2.bottom = new FormAttachment(0, 610);
  composite_2.setLayoutData(fd_composite_2);
  
  Group group = new Group(composite_2, SWT.NONE);
  group.setBounds(10, 10, 227, 181);
  
  Label lblActiveFeatureType = new Label(group, SWT.NONE);
  lblActiveFeatureType.setBounds(10, 22, 152, 20);
  lblActiveFeatureType.setText("Active Feature Type");
  
  final Button btnBuilding = new Button(group, SWT.CHECK);
  btnBuilding.setBounds(10, 62, 121, 20);
  btnBuilding.setText("Building");
  
  final Button btnActivePoint = new Button(group, SWT.CHECK);
  btnActivePoint.setBounds(10, 88, 121, 20);
  btnActivePoint.setText("Active Point");
  
  final Button btnPeople = new Button(group, SWT.CHECK);
  btnPeople.setBounds(10, 114, 121, 20);
  btnPeople.setText("People");
  
  Group group_1 = new Group(composite_2, SWT.NONE);
  group_1.setBounds(10, 302, 227, 209);
  
  Label lblQuery = new Label(group_1, SWT.NONE);
  lblQuery.setBounds(10, 22, 76, 20);
  lblQuery.setText("Query");
  
  final Button btnWholeRegion = new Button(group_1, SWT.RADIO);
  btnWholeRegion.addSelectionListener(new SelectionAdapter() {
  	@Override
  	public void widgetSelected(SelectionEvent e) {
  		if(listener!=null){
  		canvas.removeMouseListener(listener);	
  		}
  		canvas.redraw();
  		
  	}

	
  }
  );
  btnWholeRegion.setBounds(10, 67, 127, 20);
  btnWholeRegion.setText("Whole Region");
  
  final Button btnRangeQuery = new Button(group_1, SWT.RADIO);
  btnRangeQuery.addSelectionListener(new SelectionAdapter() {
  	@Override
  	public void widgetSelected(SelectionEvent e) {
  		polygontoquery="";
  		if(listener!=null)
  		canvas.removeMouseListener(listener);
  		if(showap!=null) {canvas.removePaintListener(showap);showap=null;}
  		if(showbuilding!=null) {canvas.removePaintListener(showbuilding);showbuilding=null;}
  		if(showpeople!=null){ canvas.removePaintListener(showpeople);showpeople=null;}
  		
  		listener=new rangelistener(canvas);
  		canvas.addMouseListener(listener);
  		canvas.redraw();
  	}
  });
  btnRangeQuery.setBounds(10, 93, 119, 20);
  btnRangeQuery.setText("Range Query");
  
  final Button btnPointQuery = new Button(group_1, SWT.RADIO);
  btnPointQuery.addSelectionListener(new SelectionAdapter() {
	  
  	public void widgetSelected(SelectionEvent e) {
  		canvas.redraw();
  		pointtoquery=null;
  		if(listener!=null)
  	  		canvas.removeMouseListener(listener);
  		
  		if(showap!=null) canvas.removePaintListener(showap);
  		if(showbuilding!=null) canvas.removePaintListener(showbuilding);
  		if(showpeople!=null) canvas.removePaintListener(showpeople);
  		
  		listener=new pointlistener(canvas);
  		
  		canvas.addMouseListener(listener);
  		
  		
  	}
  });
  btnPointQuery.setBounds(10, 119, 119, 20);
  btnPointQuery.setText("Point Query");
  
  final Button btnFindApCovered = new Button(group_1, SWT.RADIO);
  btnFindApCovered.addMouseListener(new MouseAdapter() {
  	@Override
  	public void mouseDown(MouseEvent e) {
  		
  		canvas.redraw();
  		btnWholeRegion.setSelection(false);
  		btnRangeQuery.setSelection(false);
  		btnPointQuery.setSelection(false);
  	}
  	@Override
  	public void mouseUp(MouseEvent e) {
  		String SQL1="select centerx,centery,radius from ap";
  		showallAP(SQL1,canvas);
  		String SQL2="select location from people";
  		showallpeople(SQL2,canvas);
  		String SQL3="select polygon from building";
  		showallbuilding(SQL3,canvas);
  	}
  });
  btnFindApCovered.addSelectionListener(new SelectionAdapter() {
  	@Override
  	
  	public void widgetSelected(SelectionEvent e) {
  		
  		
  		if(listener!=null){
  	  canvas.removeMouseListener(listener);	
  	  	}
  		listener=new APnearestlistener(canvas);
  	  	canvas.addMouseListener(listener);
  		
  		APtoquery=null;
  		
  		
  		
  		
  		
  	
  	}
  });
  btnFindApCovered.setBounds(10, 144, 207, 20);
  btnFindApCovered.setText("Find AP covered People");
  
  Button btnSubmitQuery = new Button(composite_2, SWT.NONE);
  btnSubmitQuery.addMouseListener(new MouseAdapter() {
	  
		
  	@Override
  	public void mouseDown(MouseEvent e){
  		Boolean Wholeregionchecked=btnWholeRegion.getSelection();
  		Boolean FindApCoveredchecked=btnFindApCovered.getSelection();
  		if(Wholeregionchecked||FindApCoveredchecked) canvas.redraw();
  		
  		
  	}
  	public void mouseUp(MouseEvent e) {
  		Boolean Rangequerychecked=btnRangeQuery.getSelection();
		Boolean Pointquerychecked=btnPointQuery.getSelection();
		Boolean FindApCoveredchecked=btnFindApCovered.getSelection();
		Boolean Wholeregionchecked=btnWholeRegion.getSelection();
  		String aQuery = "Query"+querycount+": ";
  		Boolean Buildingchecked=btnBuilding.getSelection();
  		Boolean APchecked=btnActivePoint.getSelection();
  		Boolean Peoplechecked=btnPeople.getSelection();
  		///////FindApcovered People within Radius
  		
  	if(FindApCoveredchecked&&APtoquery!=null){
  		int radius=radiusofAPquery;
  		if (radius==0) System.out.println("No AP selected");
  		String SQL="SELECT location FROM people  WHERE SDO_WITHIN_DISTANCE(people.location, MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE("+APtoquery.x+", "+APtoquery.y+", NULL), NULL, NULL), 'distance = "+radius+"') = 'TRUE'";
  		aQuery+=SQL+";";
  		
  		ShowthisAP(canvas,APtoquery.x,APtoquery.y,radius);
  		showallpeoplewithline(SQL,canvas,APtoquery,display.getSystemColor(SWT.COLOR_YELLOW));
  		System.out.println(radius);
  		int bitoutradius=radius+5;
  		 String SQL1="SELECT location,distance from(SELECT location,SDO_GEOM.SDO_DISTANCE(people.location, MDSYS.SDO_GEOMETRY( 2001, NULL, MDSYS.SDO_POINT_TYPE("+APtoquery.x+","+APtoquery.y+", NULL), NULL, NULL), 1) as distance FROM people WHERE SDO_WITHIN_DISTANCE(people.location, MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE("+APtoquery.x+", "+APtoquery.y+", NULL), NULL, NULL), 'distance = "+bitoutradius+"') = 'TRUE') where distance>"+radius;
  			aQuery+=SQL1+";";
  		showallpeoplewithline(SQL1,canvas,APtoquery,display.getSystemColor(SWT.COLOR_BLUE));
  		int weakerradius=radius+10;
  		String SQL2="SELECT location,distance from(SELECT location,SDO_GEOM.SDO_DISTANCE(people.location, MDSYS.SDO_GEOMETRY( 2001, NULL, MDSYS.SDO_POINT_TYPE("+APtoquery.x+","+APtoquery.y+", NULL), NULL, NULL), 1) as distance FROM people WHERE SDO_WITHIN_DISTANCE(people.location, MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE("+APtoquery.x+", "+APtoquery.y+", NULL), NULL, NULL), 'distance = "+weakerradius+"') = 'TRUE') where distance>"+bitoutradius;
  		aQuery+=SQL2+";";
  		showallpeoplewithline(SQL2,canvas,APtoquery,display.getSystemColor(SWT.COLOR_CYAN));
  		
  		
  		APtoquery=null;radiusofAPquery=0;
  	}
  	
  		
  		////////Point Query: Building
  		
  		if(Buildingchecked&&Pointquerychecked) {
			 String SQL="SELECT polygon,SDO_GEOM.SDO_DISTANCE(building.polygon, MDSYS.SDO_GEOMETRY( 2001, NULL, MDSYS.SDO_POINT_TYPE("+pointtoquery.x+","+pointtoquery.y+", NULL), NULL, NULL), 1) as distance FROM building  WHERE SDO_WITHIN_DISTANCE(building.polygon, MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE("+pointtoquery.x+", "+pointtoquery.y+", NULL), NULL, NULL), 'distance = 70') = 'TRUE' order by distance";
			STRUCT polygon;		//Structure to handle Geometry Objects
			Geometry geom;     	//Structure to handle Geometry Objects
			try {
				
				mainResultSet=mainStatement.executeQuery(SQL);
				aQuery+=SQL+";";
				GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
				boolean ini=true;
				while(mainResultSet.next()){
		        	
					 polygon = (STRUCT)mainResultSet.getObject(1);
					geom = sdoAdapter.importGeometry( polygon );
	      			if ( (geom instanceof oracle.sdoapi.geom.Polygon) )
	      			{
						oracle.sdoapi.geom.Polygon polygon0 = (oracle.sdoapi.geom.Polygon) geom;
						
						CoordPoint[] points=polygon0.getExteriorRing().getPointArray();
						if(ini==true){Showthispolygon(canvas,points); ini=false;}else{Showpolygongreen(canvas,points);}						
					}
					
					
		        }
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
		
		}
 		
 		
 		
 		
 		
 		/////////Point Query: People
 		if(Peoplechecked&&Pointquerychecked) {
			 String SQL="SELECT location,SDO_GEOM.SDO_DISTANCE(people.location, MDSYS.SDO_GEOMETRY( 2001, NULL, MDSYS.SDO_POINT_TYPE("+pointtoquery.x+","+pointtoquery.y+", NULL), NULL, NULL), 1) as distance FROM people  WHERE SDO_WITHIN_DISTANCE(people.location, MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE("+pointtoquery.x+", "+pointtoquery.y+", NULL), NULL, NULL), 'distance = 70') = 'TRUE' order by distance";
			STRUCT point;		//Structure to handle Geometry Objects
			Geometry geom;     	//Structure to handle Geometry Objects
			try {
				
				mainResultSet=mainStatement.executeQuery(SQL);
				aQuery+=SQL+";";
				GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
				boolean ini=true;
				while(mainResultSet.next()){
		        	
					 point = (STRUCT)mainResultSet.getObject(1);
					geom = sdoAdapter.importGeometry( point );
	      			if ( (geom instanceof oracle.sdoapi.geom.Point) )
	      			{
						oracle.sdoapi.geom.Point point0 = (oracle.sdoapi.geom.Point) geom;
						double X = point0.getX();
						double Y = point0.getY();
						int x=(int)X;
						int y=(int)Y;
						if (ini==true){Shownearestpeopleyellow(canvas,x,y);ini=false;}else{
						Showthispeople(canvas,x,y);}
					}
					
					
		        }
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
		
		}
 		
 		
 		/////////Point Query: AP
 		
 		if(APchecked&&Pointquerychecked){ 
  		   if(pointtoquery==null){
  			   System.out.println("Please draw a point!");
  		   }
  		   else{
  			   System.out.println("pointquery:"+pointtoquery.x+","+pointtoquery.y);
  			   String SQL="SELECT centerx,centery,radius,SDO_GEOM.SDO_DISTANCE(ap.circle, MDSYS.SDO_GEOMETRY( 2001, NULL, MDSYS.SDO_POINT_TYPE("+pointtoquery.x+","+pointtoquery.y+", NULL), NULL, NULL), 1) as distance FROM ap  WHERE SDO_WITHIN_DISTANCE(ap.circle, MDSYS.SDO_GEOMETRY(2001, NULL, MDSYS.SDO_POINT_TYPE("+pointtoquery.x+", "+pointtoquery.y+", NULL), NULL, NULL), 'distance = 70') = 'TRUE' order by distance";
  		       aQuery+=SQL+";";
  			   try {
  				   boolean ini=true;
   				mainResultSet=mainStatement.executeQuery(SQL);
   				while(mainResultSet.next()){
   					
   		        	int X = mainResultSet.getInt( 1 );
   		    		int Y= mainResultSet.getInt(2);
   		    		int R=mainResultSet.getInt(3);
   		    		System.out.println(X+","+Y);
   		    		if(ini==true){
   		    		shownearestAPyellow(canvas,X,Y,R);
   		    		ini=false;
   				}else{
   		    		ShowpointqueryAPnotnearest(canvas,X,Y,R);
   				}
   		        }
   				
   				
   			} catch (SQLException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
   		
  			   
  		   }
  			
  			
  		}
 			
 		
 		
 		
 		////////////RangeQuery: AP
 		if(APchecked&&Rangequerychecked){ 
 		   if(polygontoquery==""||polygontoquery==null){
 			   System.out.println("Please draw a polygon");
 		   }
 		   else{
 			   System.out.println(polygontoquery);
 			   String SQL="select centerx,centery,radius from ap where SDO_FILTER(ap.circle,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),MDSYS.SDO_ORDINATE_ARRAY("+polygontoquery+")), 'querytype=WINDOW')='TRUE' and SDO_RELATE(ap.circle,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),MDSYS.SDO_ORDINATE_ARRAY("+polygontoquery+")), 'mask=INSIDE querytype=WINDOW')='TRUE'";
 		       aQuery+=SQL+";";
 			   try {
  				mainResultSet=mainStatement.executeQuery(SQL);
  				while(mainResultSet.next()){
  		        	int X = mainResultSet.getInt( 1 );
  		    		int Y= mainResultSet.getInt(2);
  		    		int R=mainResultSet.getInt(3);
  		    		System.out.println(X+","+Y);
  		    		ShowthisAP(canvas,X,Y,R);
  		        }
  				
  			} catch (SQLException e1) {
  				// TODO Auto-generated catch block
  				e1.printStackTrace();
  			}
  		
 			   
 		   }
 			
 			
 		}
 		//Range Query: People
 		if(Peoplechecked&&Rangequerychecked) {
 			 String SQL="select location from people where SDO_FILTER(people.location,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),MDSYS.SDO_ORDINATE_ARRAY("+polygontoquery+")), 'querytype=WINDOW')='TRUE' and SDO_RELATE(people.location,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),MDSYS.SDO_ORDINATE_ARRAY("+polygontoquery+")), 'mask=INSIDE querytype=WINDOW')='TRUE'";
 			STRUCT point;		//Structure to handle Geometry Objects
 			Geometry geom;     	//Structure to handle Geometry Objects
 			try {
 				
 				mainResultSet=mainStatement.executeQuery(SQL);
 				aQuery+=SQL+";";
 				GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
 				while(mainResultSet.next()){
 		        	
 					 point = (STRUCT)mainResultSet.getObject(1);
 					geom = sdoAdapter.importGeometry( point );
 	      			if ( (geom instanceof oracle.sdoapi.geom.Point) )
 	      			{
 						oracle.sdoapi.geom.Point point0 = (oracle.sdoapi.geom.Point) geom;
 						double X = point0.getX();
 						double Y = point0.getY();
 						int x=(int)X;
 						int y=(int)Y;
 						Showthispeople(canvas,x,y);
 					}
 					
 					
 		        }
 				
 			} catch (Exception e1) {
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			}
 		
 		
 		
 		}
 		/////////////Range Query: Building
 		
 		
 		if(Buildingchecked&&Rangequerychecked) {
 			 String SQL="select polygon from building where SDO_FILTER(building.polygon,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),MDSYS.SDO_ORDINATE_ARRAY("+polygontoquery+")), 'querytype=WINDOW')='TRUE' and SDO_RELATE(building.polygon,MDSYS.SDO_GEOMETRY(2003,NULL,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1),MDSYS.SDO_ORDINATE_ARRAY("+polygontoquery+")), 'mask=INSIDE querytype=WINDOW')='TRUE'";
 			STRUCT polygon;		//Structure to handle Geometry Objects
 			Geometry geom;     	//Structure to handle Geometry Objects
 			try {
 				///////////////has some problem now
 				mainResultSet=mainStatement.executeQuery(SQL);
 				aQuery+=SQL+";";
 				GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
 				while(mainResultSet.next()){
 		        	
 					 polygon = (STRUCT)mainResultSet.getObject(1);
 					geom = sdoAdapter.importGeometry( polygon );
 	      			if ( (geom instanceof oracle.sdoapi.geom.Polygon) )
 	      			{
 						oracle.sdoapi.geom.Polygon polygon0 = (oracle.sdoapi.geom.Polygon) geom;
 						
 						CoordPoint[] points=polygon0.getExteriorRing().getPointArray();
 						Showthispolygon(canvas,points);  						
 					}
 					
 					
 		        }
 				
 			} catch (Exception e1) {
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			}
 		
 		
 		
 		}
 		
  		
  				
  		
  		
  		////////////////////////////////////////////whole Query
  		if((APchecked&&Wholeregionchecked)) {
  			String SQL="select centerx,centery,radius from ap";
  			aQuery+=SQL+";";
  			showallAP(SQL,canvas);
  		  			
  		
  		}
  		//when people as active feature is checked,show all people
  		if((Peoplechecked&&Wholeregionchecked)) {
  			String SQL="select location from people";
  			aQuery+=SQL+";";
  			showallpeople(SQL,canvas); 		
  		
  		}
  		
  		if((Buildingchecked&&Wholeregionchecked)) {
  			String SQL="select polygon from building";
  			aQuery+=SQL+";";
  			showallbuilding(SQL,canvas);
  			
  		
  		
  		}
  		
  		text.setText(aQuery+"\n"+text.getText());
  		querycount++;
  	}
  });
  btnSubmitQuery.setBounds(63, 540, 121, 30);
  btnSubmitQuery.setText("Submit Query");
  
  text = new Text(composite, SWT.BORDER | SWT.V_SCROLL);
  FormData fd_text = new FormData();
  fd_text.right = new FormAttachment(composite_2, 0, SWT.RIGHT);
  
  Group group_2 = new Group(composite_2, SWT.NONE);
  group_2.setBounds(10, 197, 227, 99);
  
  Label lblX = new Label(group_2, SWT.NONE);
  lblX.setBounds(10, 47, 25, 20);
  lblX.setText("X:");
  
  text_1 = new Text(group_2, SWT.BORDER);
  text_1.setEditable(false);
  text_1.setEnabled(false);
  text_1.setBounds(41, 44, 39, 26);
  
  Label lblY = new Label(group_2, SWT.NONE);
  lblY.setBounds(94, 47, 15, 20);
  lblY.setText("Y:");
  
  text_2 = new Text(group_2, SWT.BORDER);
  text_2.setEditable(false);
  text_2.setEnabled(false);
  text_2.setBounds(125, 44, 39, 26);
  fd_text.top = new FormAttachment(composite_1, 5);
  fd_text.left = new FormAttachment(composite_1, 0, SWT.LEFT);  
  text.setLayoutData(fd_text);
  shlXiaoyunZhang.open();
  
  while(!shlXiaoyunZhang.isDisposed()) {
   if(!display.readAndDispatch()) display.sleep();
  }
  display.dispose();
 }
 ///////////function for find all queried AP people and drawline,
 protected static void showallpeoplewithline(String SQL, Canvas canvas,
		Point p, Color color) {
	 STRUCT point;		//Structure to handle Geometry Objects
		Geometry geom;     	//Structure to handle Geometry Objects
		try {
			
			mainResultSet=mainStatement.executeQuery(SQL);
			
			GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
			while(mainResultSet.next()){
	        	
				 point = (STRUCT)mainResultSet.getObject(1);
				geom = sdoAdapter.importGeometry( point );
  			if ( (geom instanceof oracle.sdoapi.geom.Point) )
  			{
					oracle.sdoapi.geom.Point point0 = (oracle.sdoapi.geom.Point) geom;
					double X = point0.getX();
					double Y = point0.getY();
					int x=(int)X;
					int y=(int)Y;
					Showthispeoplewithline(canvas,x,y,p.x,p.y,color);
				}
				
				
	        }
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	 
	
	
}
 private static void Showthispeoplewithline(Canvas canvas, int x, int y, int x2,
		int y2, Color color) {
	 GC gc=new GC(canvas);
		gc.setBackground(color);
		gc.setForeground(color);
		 gc.fillRectangle(x-10/2, y-10/2, 10, 10);
		 gc.drawLine(x, y, x2, y2);
		 gc.dispose();
	
}
/////////////////////// SQL
protected static void showallbuilding(String SQL, Canvas canvas) {
	 STRUCT polygon;		//Structure to handle Geometry Objects
		Geometry geom;     	//Structure to handle Geometry Objects
		try {
			
			mainResultSet=mainStatement.executeQuery(SQL);
			
			GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
			while(mainResultSet.next()){
	        	
				 polygon = (STRUCT)mainResultSet.getObject(1);
				geom = sdoAdapter.importGeometry( polygon );
     			if ( (geom instanceof oracle.sdoapi.geom.Polygon) )
     			{
					oracle.sdoapi.geom.Polygon polygon0 = (oracle.sdoapi.geom.Polygon) geom;
					
					CoordPoint[] points=polygon0.getExteriorRing().getPointArray();
					Showthispolygon(canvas,points);  						
				}
				
				
	        }
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	// TODO Auto-generated method stub
	
}
protected static void showallpeople(String SQL, Canvas canvas) {
	 STRUCT point;		//Structure to handle Geometry Objects
		Geometry geom;     	//Structure to handle Geometry Objects
		try {
			
			mainResultSet=mainStatement.executeQuery(SQL);
			
			GeometryAdapter sdoAdapter = OraSpatialManager.getGeometryAdapter("SDO", "9",STRUCT.class, null, null, mainConnection);
			while(mainResultSet.next()){
	        	
				 point = (STRUCT)mainResultSet.getObject(1);
				geom = sdoAdapter.importGeometry( point );
     			if ( (geom instanceof oracle.sdoapi.geom.Point) )
     			{
					oracle.sdoapi.geom.Point point0 = (oracle.sdoapi.geom.Point) geom;
					double X = point0.getX();
					double Y = point0.getY();
					int x=(int)X;
					int y=(int)Y;
					Showthispeople(canvas,x,y);
				}
				
				
	        }
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	
}
protected static void showallAP(String sql, Canvas canvas) {
	 try {
			mainResultSet=mainStatement.executeQuery(sql);
			while(mainResultSet.next()){
	        	int APx = mainResultSet.getInt( 1 );
	    		int APy= mainResultSet.getInt(2);
	    		int Radius=mainResultSet.getInt(3);
	    		System.out.println(APx+","+APy);
	    		ShowthisAP(canvas,APx,APy,Radius);
	        }
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	// TODO Auto-generated method stub
	
}
//////////////////////////////////////////////
 //show a given AP in canvas
 private static void ShowthisAP(Canvas canvas,int x,int y,int r) {
	 GC gc=new GC(canvas);
		gc.setBackground(SWTResourceManager.getColor(255,   0,   0));
		 gc.fillRectangle(x-15/2, y-15/2, 15, 15);
		 gc.drawOval(x-r, y-r, 2*r, 2*r);
		 gc.dispose();
		
	}
 
 /////////////////////////////////////////////
 //show AP in point Query and not nearest(in green)

 private static void  ShowpointqueryAPnotnearest(Canvas canvas,int x,int y,int r) {
	 GC gc=new GC(canvas);
		gc.setBackground(SWTResourceManager.getColor(0,   255,   0));
		gc.setForeground(SWTResourceManager.getColor(0,   255,   0));
		 gc.fillRectangle(x-15/2, y-15/2, 15, 15);
		 gc.drawOval(x-r, y-r, 2*r, 2*r);
		 gc.dispose();
		
	}
 ////////////////////////////////////////////////////
 //show nearest AP in yellow
 private static void  shownearestAPyellow(Canvas canvas,int x,int y,int r) {
	 GC gc=new GC(canvas);
		gc.setBackground(SWTResourceManager.getColor(255,   255,   0));
		gc.setForeground(SWTResourceManager.getColor(255,   255,   0));
		 gc.fillRectangle(x-15/2, y-15/2, 15, 15);
		 gc.drawOval(x-r, y-r, 2*r, 2*r);
		 gc.dispose();
		
	}
 
 
 
 ////////////////////////////////////////////////////////
 
 //show a given People in canvas
 
 private static void Showthispeople(Canvas canvas,int x,int y) {
	 GC gc=new GC(canvas);
		gc.setBackground(SWTResourceManager.getColor(0,   255,   0));
		 gc.fillRectangle(x-10/2, y-10/2, 10, 10);
		 gc.dispose();
		
	}
 /////////////////////////show nearest people in yellow

 
 private static void  Shownearestpeopleyellow(Canvas canvas,int x,int y) {
	 GC gc=new GC(canvas);
		gc.setBackground(SWTResourceManager.getColor(255,   255,   0));
		 gc.fillRectangle(x-10/2, y-10/2, 10, 10);
		 gc.dispose();
		
	}
 
 
 
 //////////////////////
 private static void Showthispolygon(Canvas canvas,CoordPoint[] points) {
	 GC gc=new GC(canvas);
	 gc.setForeground(SWTResourceManager.getColor(255,   255,   0));
	 int[] intpoint = new int[2*points.length];
	 for(int i=0;i<points.length;i++){
		intpoint[2*i]=(int)points[i].getX();
		intpoint[2*i+1]=(int)points[i].getY();
	 }
    gc.drawPolygon(intpoint);
    
 }
 /////////////////////////show polygon in green for nearest point query
 private static void Showpolygongreen(Canvas canvas,CoordPoint[] points) {
	 GC gc=new GC(canvas);
	 gc.setForeground(SWTResourceManager.getColor(0,   255,   0));
	 int[] intpoint = new int[2*points.length];
	 for(int i=0;i<points.length;i++){
		intpoint[2*i]=(int)points[i].getX();
		intpoint[2*i+1]=(int)points[i].getY();
	 }
    gc.drawPolygon(intpoint);
    
 }
 
 /////////////////////////////////
 //////////////////////////////////////////
 //Listener for Point Query
 protected static class pointlistener implements MouseListener{
protected boolean pointdrawing = false;
protected Point originPoint = new Point(0, 0);
protected Canvas canvas;

public pointlistener(Canvas canvas) {
 this.canvas = canvas;

}
public void mouseDown(MouseEvent e) {
 if(pointdrawing==false){
	GC gc = new GC(canvas);
gc.setBackground(SWTResourceManager.getColor(255,   0,   0));
	 gc.fillRectangle(e.x-5/2, e.y-5/2, 5, 5);
	 gc.drawOval(e.x-70, e.y-70, 140, 140);
	 pointtoquery=new Point(e.x,e.y);

 pointdrawing=true;
 }
}
public void mouseUp(MouseEvent e) {
 
}
public void mouseMove(MouseEvent e) {
 
}
public void mouseDoubleClick(MouseEvent e) {
}
}
 //////////////////////////////////////////////////////////
 //Listener to find the nearest AP point
 
 
 protected static class APnearestlistener implements MouseListener{
	 protected boolean pointdrawing = false;
	 protected Point thispoint;
	 protected Canvas canvas;

	 public APnearestlistener(Canvas canvas) {
	  this.canvas = canvas;

	 }
	 public Point getnearestAP(Point mouseclicked){
		 try
	    	{
	                                // searches for all tuples
		        System.out.println("\n ** Selecting all tuples in the table **" );
		        int mx=mouseclicked.x;
		        int my=mouseclicked.y;
		        String SQL=
		        	"select centerx,centery,radius from(select (centerx-"+mx+")*(centerx-"+mx+")+(centery-"+my+")*(centery-"+my+") as distancesquare, centerx,centery,radius from ap order by distancesquare) where rownum=1";
		        System.out.println(SQL);
		        mainResultSet = mainStatement.executeQuery( SQL );
		        if(mainResultSet.next()){
		        	int APx = mainResultSet.getInt( 1 );
		    		int APy= mainResultSet.getInt(2);
		    		int radius=mainResultSet.getInt(3);
		    		radiusofAPquery=radius;
		    		Point nearAP=new Point(APx,APy);
		    	    return nearAP;
		        	
		        }
		        return null;
			}
	    	catch( Exception e )
		    { System.out.println( " Error : " + e.toString() ); }
		
		 return mouseclicked; 
	 }
	 public void mouseDown(MouseEvent e) {
	  if(pointdrawing==false){
	 	GC gc = new GC(canvas);
	 gc.setBackground(SWTResourceManager.getColor(0,   0,   255));
	 	 thispoint=new Point(e.x,e.y);
	 	 Point nearestAP=this.getnearestAP(thispoint);
	 	 gc.fillRectangle(nearestAP.x-15/2, nearestAP.y-15/2, 15, 15);
	 	APtoquery=new Point(nearestAP.x,nearestAP.y);
	  pointdrawing=true;
	  }
	 }
	 public void mouseUp(MouseEvent e) {
	  
	 }
	 public void mouseMove(MouseEvent e) {
	  
	 }
	 public void mouseDoubleClick(MouseEvent e) {
	 }
	 }
 
  
 
 ////////////////////////////////////////////////////////////
 //Listener to handle the range query and draw polygons

 
 protected static class rangelistener implements MouseListener{
	 protected int pointdrawing = 0;
	 protected Point originPoint;
	 protected Canvas canvas;
	 protected Point previouspoint;
	 protected int pointcount;

	 public rangelistener(Canvas canvas) {
	  this.canvas = canvas;

	 }
	 public void mouseDown(MouseEvent e) {
	  if(pointdrawing==0&&e.button==1){
	 	polygontoquery="";
		  GC gc = new GC(canvas);
	 gc.setBackground(SWTResourceManager.getColor(255,   0,   0));
	 	 gc.fillRectangle(e.x-5/2, e.y-5/2, 5, 5);
	 	 
	 	polygontoquery+=e.x+","+e.y;
	 	 originPoint=new Point(e.x,e.y);
	 	 previouspoint=new Point(e.x,e.y);
	 	 pointcount=1;
	  pointdrawing=1;
	  }
	  else{
		  if (pointdrawing==1){
		  GC gc = new GC(canvas);
			 gc.setBackground(SWTResourceManager.getColor(0,   255,   0));
			 gc.setForeground(SWTResourceManager.getColor(0,   255,   0));
			 	
			if(e.button==1){ 
				 gc.fillRectangle(e.x-5/2, e.y-5/2, 5, 5);
			 	 pointcount++;
			 	polygontoquery+=", "+e.x+","+e.y;
			 	 gc.drawLine(previouspoint.x,previouspoint.y, e.x, e.y);
			 	 previouspoint=new Point(e.x,e.y);}
			else{
				gc.drawLine(previouspoint.x,previouspoint.y, originPoint.x, originPoint.y);
			    pointdrawing=-1;
			}
		  }
	  }
	  }
	public void mouseDoubleClick(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	 }
	 public void mouseUp(MouseEvent e) {
	  
	 }
	 public void mouseMove(MouseEvent e) {
	  
	 }
	 public void mouseDoubleClick(MouseEvent e) {
	 }
	 
	 /////////////////////////////////////////////////////////
	 //Listener to handle the click of Whole region
	 
	 
	 
	 
	 
	 
	 ///////////////////////////////////////////////////////
	 //function to connect to DB
 
	 public static void ConnectToDB()
	 	{
			try
			{
				// loading Oracle Driver
	    		System.out.print("Looking for Oracle's jdbc-odbc driver ... ");
		    	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		    	System.out.println(", Loaded.");

				String URL = "jdbc:oracle:thin:@127.0.0.1:1521:dbhomework1";
		    	String userName = "SYSTEM";
		    	String password = "Zxy213213";

		    	System.out.print("Connecting to DB...");
		    	mainConnection = DriverManager.getConnection(URL, userName, password);
		    	System.out.println(", Connected!");

	    		mainStatement = mainConnection.createStatement();

	   		}
	   		catch (Exception e)
	   		{
	     		System.out.println( "Error while connecting to DB: "+ e.toString() );
	     		e.printStackTrace();
	     		System.exit(-1);
	   		}
	 	}
}
