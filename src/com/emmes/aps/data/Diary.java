/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 2, 2014 1:07:03 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

// TODO: Auto-generated Javadoc
/**
 * The Class Diary.
 */
public final class Diary implements java.io.Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	

	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.emmes.aps";	// The Content Provider Authority
	
	/** The sdf. */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
	
	/** The Diary date. */
	public long DiaryDate;	
	
	/** The Wheezing. */
	public String Wheezing;
	
	/** The Coughing. */
	public String Coughing;
	
	/** The Short breath. */
	public String ShortBreath;
	
	/** The Chest tightness. */
	public String ChestTightness;
	
	/** The Chest pain. */
	public String ChestPain;
	
	/** The Fever. */
	public String Fever;
	
	/** The Nausea. */
	public String Nausea;
	
	/** The Runny nose. */
	public String RunnyNose;
	
	/** The Other. */
	public String Other;
	
	/** The Miss school work. */
	public String MissSchoolWork;
	
	/** The Pescription. */
	public String Pescription;
	
	/** The otc. */
	public String OTC;
	
	/** The Hrs slept. */
	public String HrsSlept;
	
	/** The Min slept. */
	public String MinSlept;
	
	/** The Wake. */
	public String Wake;
	
	/** The Times woke. */
	public String TimesWoke;
	
	/** The Hrs sitting. */
	public String HrsSitting;
	
	/** The Min sitting. */
	public String MinSitting;
	
	/** The Exercise. */
	public String Exercise ;
	
	/** The Min indoors. */
	public String MinIndoors;
	
	/** The Min outdoors. */
	public String MinOutdoors;
	
	/** The Hrs vehicle. */
	public String HrsVehicle;
	
	/** The Min vehicle. */
	public String MinVehicle;
	
	/** The Smoke. */
	public String Smoke;
	
	/** The Num smoke. */
	public String NumSmoke;
	
	/** The People smoking. */
	public String PeopleSmoking;
	
	/** The Caffein. */
	public String Caffein;
	
	/** The Stress. */
	public String Stress;
	
	/** The Peak flow1. */
	public String PeakFlow1;
	
	/** The Peak flow2. */
	public String PeakFlow2;
	
	/** The Peak flow3. */
	public String PeakFlow3;
	
	/** The Nitric oxide. */
	public String NitricOxide;

	/**
	 * Instantiates a new diary.
	 *
	 * @param values the values
	 */
	public Diary(ContentValues values){
		DiaryDate = values.getAsLong(Diary.DiaryItem.COLUMN_NAME_DIARYDATE);
		Wheezing= values.getAsString(Diary.DiaryItem.COLUMN_NAME_WHEEZING );
		Coughing= values.getAsString(Diary.DiaryItem.COLUMN_NAME_COUGHING );
		ShortBreath= values.getAsString(Diary.DiaryItem.COLUMN_NAME_SHORT_BREATH );
		ChestTightness= values.getAsString(Diary.DiaryItem.COLUMN_NAME_CHEST_TIGHTNESS );
		ChestPain= values.getAsString(Diary.DiaryItem.COLUMN_NAME_CHEST_PAIN );
		Fever= values.getAsString(Diary.DiaryItem.COLUMN_NAME_FEVER );
		Nausea= values.getAsString(Diary.DiaryItem.COLUMN_NAME_NAUSEA_VOMITING );
		RunnyNose= values.getAsString(Diary.DiaryItem.COLUMN_NAME_RUNNY_NOSE );
		Other= values.getAsString(Diary.DiaryItem.COLUMN_NAME_OTHER );
		MissSchoolWork= values.getAsString(Diary.DiaryItem.COLUMN_NAME_MISS_SCHOOL_WORK );
		Pescription= values.getAsString(Diary.DiaryItem.COLUMN_NAME_PRESCRIPTION );
		OTC= values.getAsString(Diary.DiaryItem.COLUMN_NAME_OVER_THE_COUNTER );
		HrsSlept= values.getAsString(Diary.DiaryItem.COLUMN_NAME_HOURS_SLEPT );
		MinSlept= values.getAsString(Diary.DiaryItem.COLUMN_NAME_MINUTES_SLEPT );
		Wake= values.getAsString(Diary.DiaryItem.COLUMN_NAME_WAKE );
		TimesWoke= values.getAsString(Diary.DiaryItem.COLUMN_NAME_TIMES_WOKE );
		HrsSitting= values.getAsString(Diary.DiaryItem.COLUMN_NAME_HOURS_SITTING );
		MinSitting= values.getAsString(Diary.DiaryItem.COLUMN_NAME_MINUTES_SITTING );
		Exercise = values.getAsString(Diary.DiaryItem.COLUMN_NAME_EXERCISE );
		MinIndoors= values.getAsString(Diary.DiaryItem.COLUMN_NAME_MINUTES_INDOORS );
		MinOutdoors= values.getAsString(Diary.DiaryItem.COLUMN_NAME_MINUTES_OUTDOORS );
		HrsVehicle= values.getAsString(Diary.DiaryItem.COLUMN_NAME_HOURS_VEHICLE );
		MinVehicle= values.getAsString(Diary.DiaryItem.COLUMN_NAME_MINUTES_VEHICLE );
		Smoke= values.getAsString(Diary.DiaryItem.COLUMN_NAME_SMOKE );
		NumSmoke= values.getAsString(Diary.DiaryItem.COLUMN_NAME_NUM_SMOKE );
		PeopleSmoking= values.getAsString(Diary.DiaryItem.COLUMN_NAME_PEOPLE_SMOKING );
		Caffein= values.getAsString(Diary.DiaryItem.COLUMN_NAME_CAFFEIN );
		Stress= values.getAsString(Diary.DiaryItem.COLUMN_NAME_STRESS );
		PeakFlow1= values.getAsString(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW1 );
		PeakFlow2= values.getAsString(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW2 );
		PeakFlow3= values.getAsString(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW3 );
		NitricOxide= values.getAsString(Diary.DiaryItem.COLUMN_NAME_NITRICOXIDE);
	}
	
	/**
	 * Instantiates a new diary.
	 *
	 * @param date the date
	 */
	public Diary(long date ){
		this.DiaryDate = date;
		this.Wheezing="";
		this.Coughing="";
		this.ShortBreath="";
		this.ChestTightness="";
		this.ChestPain="";
		this.Fever="";
		this.Nausea="";
		this.RunnyNose="";
		this.Other="";
		this.MissSchoolWork="";
		this.Pescription="";
		this.OTC="";
		this.HrsSlept="";
		this.MinSlept="";
		this.Wake="";
		this.TimesWoke="";
		this.HrsSitting="";
		this.MinSitting="";
		this.Exercise ="";
		this.MinIndoors="";
		this.MinOutdoors="";
		this.HrsVehicle="";
		this.MinVehicle="";
		this.Smoke="";
		this.NumSmoke="";
		this.PeopleSmoking="";
		this.Caffein="";
		this.Stress="";
		this.PeakFlow1="";
		this.PeakFlow2="";
		this.PeakFlow3="";
		this.NitricOxide="";
	}	
	
    /*
     * Returns a ContentValues instance (a map) for this diaryInfo instance. This is useful for
     * inserting a diaryInfo into a database.
     */
    /**
     * Gets the content values.
     *
     * @return the content values
     */
    public ContentValues getContentValues() {
        // Gets a new ContentValues object
        ContentValues v = new ContentValues();

        // Adds map entries for the user-controlled fields in the map
        v.put(Diary.DiaryItem.COLUMN_NAME_DIARYDATE, DiaryDate);
        v.put(Diary.DiaryItem.COLUMN_NAME_WHEEZING,Wheezing);
        v.put(Diary.DiaryItem.COLUMN_NAME_COUGHING,Coughing);
        v.put(Diary.DiaryItem.COLUMN_NAME_SHORT_BREATH,ShortBreath);
        v.put(Diary.DiaryItem.COLUMN_NAME_CHEST_TIGHTNESS,ChestTightness);
        v.put(Diary.DiaryItem.COLUMN_NAME_CHEST_PAIN,ChestPain);
        v.put(Diary.DiaryItem.COLUMN_NAME_FEVER,Fever);
        v.put(Diary.DiaryItem.COLUMN_NAME_NAUSEA_VOMITING,Nausea);
        v.put(Diary.DiaryItem.COLUMN_NAME_RUNNY_NOSE,RunnyNose);
        v.put(Diary.DiaryItem.COLUMN_NAME_OTHER,Other);
        v.put(Diary.DiaryItem.COLUMN_NAME_MISS_SCHOOL_WORK,MissSchoolWork);
        v.put(Diary.DiaryItem.COLUMN_NAME_PRESCRIPTION,Pescription);
        v.put(Diary.DiaryItem.COLUMN_NAME_OVER_THE_COUNTER,OTC);
        v.put(Diary.DiaryItem.COLUMN_NAME_HOURS_SLEPT,HrsSlept);
        v.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_SLEPT,MinSlept);
        v.put(Diary.DiaryItem.COLUMN_NAME_WAKE,Wake);
        v.put(Diary.DiaryItem.COLUMN_NAME_TIMES_WOKE,TimesWoke);
        v.put(Diary.DiaryItem.COLUMN_NAME_HOURS_SITTING,HrsSitting);
        v.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_SITTING,MinSitting);
        v.put(Diary.DiaryItem.COLUMN_NAME_EXERCISE,Exercise );
        v.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_INDOORS,MinIndoors);
        v.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_OUTDOORS,MinOutdoors);
        v.put(Diary.DiaryItem.COLUMN_NAME_HOURS_VEHICLE,HrsVehicle);
        v.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_VEHICLE,MinVehicle);
        v.put(Diary.DiaryItem.COLUMN_NAME_SMOKE,Smoke);
        v.put(Diary.DiaryItem.COLUMN_NAME_NUM_SMOKE,NumSmoke);
        v.put(Diary.DiaryItem.COLUMN_NAME_PEOPLE_SMOKING,PeopleSmoking);
        v.put(Diary.DiaryItem.COLUMN_NAME_CAFFEIN,Caffein);
        v.put(Diary.DiaryItem.COLUMN_NAME_STRESS,Stress);
        v.put(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW1,PeakFlow1);
        v.put(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW2,PeakFlow2);
        v.put(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW3,PeakFlow3);
        v.put(Diary.DiaryItem.COLUMN_NAME_NITRICOXIDE,NitricOxide);
        return v;

    }    	
	
	/**
	 * Constant definition to define the mapping of a Diary to the underlying database
	 * Also provides constants to help define the Content Provider.
	 */
	public static final class DiaryItem implements BaseColumns {
		
        // This class cannot be instantiated
        /**
         * Instantiates a new diary item.
         */
        private DiaryItem() {}

        /** The table name offered by this provider. */
        public static final String TABLE_NAME = "diary";

        /*
         * URI definitions
         */

        /** The scheme part for this provider's URI. */
        private static final String SCHEME = "content://";

        /** Path parts for the URIs. */

        /**
         * Path part for the diary URI
         */
        private static final String PATH_DIARIES = "/diary";

        /** Path part for the diary ID URI. */
        private static final String PATH_DIARY_ID = "/diary/";

        /** 0-relative position of a diary item ID segment in the path part of a diary item ID URI. */
        public static final int DIARY_ID_PATH_POSITION = 1;


        /** The content:// style URL for this table. */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_DIARIES);
        
        /**
         * The content URI base for a single diary item. Callers must
         * append a numeric diary id to this Uri to retrieve an diary item
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_DIARY_ID);        
        
        /** The default sort order for this table. */
        public static final String DEFAULT_SORT_ORDER = Diary.DiaryItem.COLUMN_NAME_DIARYDATE + " ASC";        
        
        
        /*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of diaries.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aps.diaries.diary";

        /** The MIME type of a {@link #CONTENT_URI} sub-directory of a single diary item. */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aps.diaries.diary";
        
        /*
         * Column definitions
         */

        /**
         * Column name for the creation timestamp
         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
         */
        public static final String COLUMN_NAME_DIARYDATE = "diarydate";       
        
        /** The Constant COLUMN_NAME_WHEEZING. */
        public static final String COLUMN_NAME_WHEEZING = "wheezing";
        
        /** The Constant COLUMN_NAME_COUGHING. */
        public static final String COLUMN_NAME_COUGHING = "coughing";
        
        /** The Constant COLUMN_NAME_SHORT_BREATH. */
        public static final String COLUMN_NAME_SHORT_BREATH = "short_breath";
        
        /** The Constant COLUMN_NAME_CHEST_TIGHTNESS. */
        public static final String COLUMN_NAME_CHEST_TIGHTNESS = "chest_tightness";
        
        /** The Constant COLUMN_NAME_CHEST_PAIN. */
        public static final String COLUMN_NAME_CHEST_PAIN = "chest_pain";
        
        /** The Constant COLUMN_NAME_FEVER. */
        public static final String COLUMN_NAME_FEVER = "fever";
        
        /** The Constant COLUMN_NAME_NAUSEA_VOMITING. */
        public static final String COLUMN_NAME_NAUSEA_VOMITING = "nausea_vomiting";
        
        /** The Constant COLUMN_NAME_RUNNY_NOSE. */
        public static final String COLUMN_NAME_RUNNY_NOSE = "runny_nose";
        
        /** The Constant COLUMN_NAME_OTHER. */
        public static final String COLUMN_NAME_OTHER = "other";
        
        /** The Constant COLUMN_NAME_MISS_SCHOOL_WORK. */
        public static final String COLUMN_NAME_MISS_SCHOOL_WORK = "miss_school_work";
        
        /** The Constant COLUMN_NAME_PRESCRIPTION. */
        public static final String COLUMN_NAME_PRESCRIPTION = "prescription";
        
        /** The Constant COLUMN_NAME_OVER_THE_COUNTER. */
        public static final String COLUMN_NAME_OVER_THE_COUNTER = "over_the_couanter";
        
        /** The Constant COLUMN_NAME_HOURS_SLEPT. */
        public static final String COLUMN_NAME_HOURS_SLEPT = "hours_slept";
        
        /** The Constant COLUMN_NAME_MINUTES_SLEPT. */
        public static final String COLUMN_NAME_MINUTES_SLEPT = "minutes_slept";
        
        /** The Constant COLUMN_NAME_WAKE. */
        public static final String COLUMN_NAME_WAKE = "wake";
        
        /** The Constant COLUMN_NAME_TIMES_WOKE. */
        public static final String COLUMN_NAME_TIMES_WOKE = "times_woke";
        
        /** The Constant COLUMN_NAME_HOURS_SITTING. */
        public static final String COLUMN_NAME_HOURS_SITTING = "hours_sitting";
        
        /** The Constant COLUMN_NAME_MINUTES_SITTING. */
        public static final String COLUMN_NAME_MINUTES_SITTING = "minutes_sitting";
        
        /** The Constant COLUMN_NAME_EXERCISE. */
        public static final String COLUMN_NAME_EXERCISE = "exercise";
        
        /** The Constant COLUMN_NAME_MINUTES_INDOORS. */
        public static final String COLUMN_NAME_MINUTES_INDOORS = "minutes_indoors";
        
        /** The Constant COLUMN_NAME_MINUTES_OUTDOORS. */
        public static final String COLUMN_NAME_MINUTES_OUTDOORS = "minutes_outdoors";
        
        /** The Constant COLUMN_NAME_HOURS_VEHICLE. */
        public static final String COLUMN_NAME_HOURS_VEHICLE = "hours_vehicle";
        
        /** The Constant COLUMN_NAME_MINUTES_VEHICLE. */
        public static final String COLUMN_NAME_MINUTES_VEHICLE = "minutes_vehicle";
        
        /** The Constant COLUMN_NAME_SMOKE. */
        public static final String COLUMN_NAME_SMOKE = "smoke";
        
        /** The Constant COLUMN_NAME_NUM_SMOKE. */
        public static final String COLUMN_NAME_NUM_SMOKE = "num_smoke";
        
        /** The Constant COLUMN_NAME_PEOPLE_SMOKING. */
        public static final String COLUMN_NAME_PEOPLE_SMOKING = "people_smoking";
        
        /** The Constant COLUMN_NAME_CAFFEIN. */
        public static final String COLUMN_NAME_CAFFEIN = "caffein";
        
        /** The Constant COLUMN_NAME_STRESS. */
        public static final String COLUMN_NAME_STRESS = "stress";
        
        /** The Constant COLUMN_NAME_PEAKFLOW1. */
        public static final String COLUMN_NAME_PEAKFLOW1 = "peakflow1";
        
        /** The Constant COLUMN_NAME_PEAKFLOW2. */
        public static final String COLUMN_NAME_PEAKFLOW2= "peakflow1";
        
        /** The Constant COLUMN_NAME_PEAKFLOW3. */
        public static final String COLUMN_NAME_PEAKFLOW3 = "peakflow1";
        
        /** The Constant COLUMN_NAME_NITRICOXIDE. */
        public static final String COLUMN_NAME_NITRICOXIDE = "NitricOxide";
        
        /** The Constant COLUMN_NAME_ID. */
        public static final String COLUMN_NAME_ID = BaseColumns._ID;
        
        /**  Projection holding all the columns required to populate and Diary item. */
        public static final String[] FULL_PROJECTION = {
        	COLUMN_NAME_DIARYDATE,
        	COLUMN_NAME_WHEEZING,
        	COLUMN_NAME_COUGHING,
        	COLUMN_NAME_SHORT_BREATH,
        	COLUMN_NAME_CHEST_TIGHTNESS,
        	COLUMN_NAME_CHEST_PAIN,
        	COLUMN_NAME_FEVER,
        	COLUMN_NAME_NAUSEA_VOMITING,
        	COLUMN_NAME_RUNNY_NOSE,
        	COLUMN_NAME_OTHER,
        	COLUMN_NAME_MISS_SCHOOL_WORK,
        	COLUMN_NAME_PRESCRIPTION ,
        	COLUMN_NAME_OVER_THE_COUNTER,
        	COLUMN_NAME_HOURS_SLEPT,
        	COLUMN_NAME_MINUTES_SLEPT,
        	COLUMN_NAME_WAKE,
        	COLUMN_NAME_TIMES_WOKE,
        	COLUMN_NAME_HOURS_SITTING,
        	COLUMN_NAME_MINUTES_SITTING,
        	COLUMN_NAME_EXERCISE,
        	COLUMN_NAME_MINUTES_INDOORS,
        	COLUMN_NAME_MINUTES_OUTDOORS,
        	COLUMN_NAME_HOURS_VEHICLE,
        	COLUMN_NAME_MINUTES_VEHICLE,
        	COLUMN_NAME_SMOKE,
        	COLUMN_NAME_NUM_SMOKE,
        	COLUMN_NAME_PEOPLE_SMOKING,
        	COLUMN_NAME_CAFFEIN,
        	COLUMN_NAME_STRESS,
        	COLUMN_NAME_PEAKFLOW1,
        	COLUMN_NAME_PEAKFLOW2,
        	COLUMN_NAME_PEAKFLOW3,
        	COLUMN_NAME_NITRICOXIDE
            };        
        
        /** The Constant LIST_PROJECTION. */
        public static final String[] LIST_PROJECTION =
            new String[] {
                Diary.DiaryItem._ID
        };            

	}

	/**
	 * The Class Helper.
	 */
	public static final class Helper {
		/**
		 * Converts a cursor to an array of Diary 
		 * Note that this method is "mean and lean" with little error checking.
		 * It assumes that the projection used is DiaryItem.FULL_PROJECTION
		 * @param cursor A cursor loaded with Diary data
		 * @return populated array of Diaries
		 */
		public static final Diary[] getDiariesFromCursor(Cursor cursor){
			Diary[] diaries = null;
			int rows = cursor.getCount();
			if(rows > 0){
				diaries = new Diary[rows];
				int i=0;
				while(cursor.moveToNext()){
					diaries[i++] = new Diary( cursor.getLong(0));
				}
			}
			return diaries;
		}
		
		/**
		 * Diary to json.
		 *
		 * @param e the e
		 * @return the JSON object
		 * @throws JSONException the JSON exception
		 */
		public static final JSONObject diaryToJSON(Diary e) throws JSONException{
			JSONObject jObj = new JSONObject();
			jObj.put("diarydate", e.DiaryDate);
			jObj.put("wheezing", e.Wheezing);
			jObj.put("coughing", e.Coughing);
			jObj.put("shortbreath", e.ShortBreath);
			jObj.put("chesttightness", e.ChestTightness);
			jObj.put("chestpain", e.ChestPain);
			jObj.put("fever", e.Fever);
			jObj.put("nausea", e.Nausea);
			jObj.put("runnynose", e.RunnyNose);
			jObj.put("other", e.Other);
			jObj.put("missschoolwork", e.MissSchoolWork);
			jObj.put("pescription", e.Pescription);
			jObj.put("otc", e.OTC);
			jObj.put("hrsslept", e.HrsSlept);
			jObj.put("minslept", e.MinSlept);
			jObj.put("wake", e.Wake);
			jObj.put("timeswoke", e.TimesWoke);
			jObj.put("hrssitting", e.HrsSitting);
			jObj.put("minsitting", e.MinSitting);
			jObj.put("exercise", e.Exercise );
			jObj.put("minindoors", e.MinIndoors);
			jObj.put("minoutdoors", e.MinOutdoors);
			jObj.put("hrsvehicle", e.HrsVehicle);
			jObj.put("minvehicle", e.MinVehicle);
			jObj.put("smoke", e.Smoke);
			jObj.put("numsmoke", e.NumSmoke);
			jObj.put("peoplesmoking", e.PeopleSmoking);
			jObj.put("caffein", e.Caffein);
			jObj.put("stress", e.Stress);
			jObj.put("peakflow1", e.PeakFlow1);
			jObj.put("peakflow1", e.PeakFlow2);
			jObj.put("peakflow3", e.PeakFlow3);
			jObj.put("nitricoxide", e.NitricOxide);
			return jObj;
		}
		
		/**
		 * Diary array to json.
		 *
		 * @param diaries the diaries
		 * @return the JSON array
		 * @throws JSONException the JSON exception
		 */
		public static final JSONArray diaryArrayToJSON(Diary[] diaries)  throws JSONException{
			JSONArray jArray = new JSONArray();
			System.out.println("Converting  " + diaries.length + " diaries to JSON");
			for(Diary e: diaries){
				jArray.put(diaryToJSON(e));
			}
			return jArray;
		}	
		
		/**
		 * Diary array to csv.
		 *
		 * @param diaries the diaries
		 * @return the string builder
		 */
		public static final StringBuilder diaryArrayToCsv(Diary[] diaries){
			StringBuilder result= new StringBuilder();
			for(Diary e: diaries){
				result.append(diaryToCsv(e));
			}
			return result;
		}		
		
		/**
		 * Diary to csv.
		 *
		 * @param e the e
		 * @return the string builder
		 */
		public static final StringBuilder diaryToCsv(Diary e){
			StringBuilder builder = new StringBuilder();
			builder.append(sdf.format(new Date(e.DiaryDate)));
			builder.append(e.Wheezing);
			builder.append("\",\"");
			builder.append(e.Coughing);
			builder.append("\",\"");
			builder.append(e.ShortBreath);
			builder.append("\",\"");
			builder.append(e.ChestTightness);
			builder.append("\",\"");
			builder.append(e.ChestPain);
			builder.append("\",\"");
			builder.append(e.Fever);
			builder.append("\",\"");
			builder.append(e.Nausea);
			builder.append("\",\"");
			builder.append(e.RunnyNose);
			builder.append("\",\"");
			builder.append(e.Other);
			builder.append("\",\"");
			builder.append(e.MissSchoolWork);
			builder.append("\",\"");
			builder.append(e.Pescription);
			builder.append("\",\"");
			builder.append(e.OTC);
			builder.append("\",\"");
			builder.append(e.HrsSlept);
			builder.append("\",\"");
			builder.append(e.MinSlept);
			builder.append("\",\"");
			builder.append(e.Wake);
			builder.append("\",\"");
			builder.append(e.TimesWoke);
			builder.append("\",\"");
			builder.append(e.HrsSitting);
			builder.append("\",\"");
			builder.append(e.MinSitting);
			builder.append("\",\"");
			builder.append(e.Exercise );
			builder.append("\",\"");
			builder.append(e.MinIndoors);
			builder.append("\",\"");
			builder.append(e.MinOutdoors);
			builder.append("\",\"");
			builder.append(e.HrsVehicle);
			builder.append("\",\"");
			builder.append(e.MinVehicle);
			builder.append("\",\"");
			builder.append(e.Smoke);
			builder.append("\",\"");
			builder.append(e.NumSmoke);
			builder.append("\",\"");
			builder.append(e.PeopleSmoking);
			builder.append("\",\"");
			builder.append(e.Caffein);
			builder.append("\",\"");
			builder.append(e.Stress);
			builder.append("\",\"");
			builder.append(e.PeakFlow1);
			builder.append("\",\"");
			builder.append(e.PeakFlow2);
			builder.append("\",\"");
			builder.append(e.PeakFlow3);
			builder.append("\",\"");
			builder.append(e.NitricOxide);
			builder.append("\"");
			builder.append('\n');
			return builder;
		}	
				
	}
	
}
