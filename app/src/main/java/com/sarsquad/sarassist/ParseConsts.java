package com.sarsquad.sarassist;

/**
 * Created by CHRIS on 7/18/2015.
 */
public final class ParseConsts {

    private ParseConsts() { }

    public static final String CreatedAt = "createdAt";
    public static final String UpdatedAt = "updatedAt";
    public static final String ACL = "ACL";
    public static final String ObjectID = "objectId";

    public static final class SearchArea{

        private SearchArea(){}

        public static final String _Class = "SearchArea";
        public static final String Name = "Name";
        public static final String SearchAreaID = "SearchAreaID";
        public static final String NorthEastLat = "NorthEastLat";
        public static final String NorthEastLng = "NorthEastLng";
        public static final String SouthWestLat = "SouthWestLat";
        public static final String SouthWestLng = "SouthWestLng";
        public static final String IsComplete = "IsComplete";
        public static final String Location = "Location";
    }

    public static final class Block{
        private Block() {}

        public static final String _Class = "Block";
        public static final String Row = "Row";
        public static final String Column = "Column";
        public static final String IsComplete = "IsComplete";
        public static final String Location = "Location";
        public static final String SearchAreaID = "SearchAreaID";
        public static final String AssignedTo = "AssignedTo";
        public static final String Latitude = "Latitude";
        public static final String Longitude = "Longitude";


    }
}
