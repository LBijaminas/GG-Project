package com.example.lukas.googleglassproject;

/**
 * Created by hussainallehyani on 11/13/14.
 */

import java.util.HashMap;

public class Buildings {

    private String BuildingName = null;

    private  double GarnerCoords[][] =
            {{25.879950, -80.197901},{25.879979, -80.197241},{25.879803, -80.197230},{25.879771, -80.197686}, {25.879281, -80.197678},{25.879298, -80.197903} };

    private  double LibraryCoords[][] =
            { {25.879195, -80.197876},{25.879219, -80.197547},{25.879380, -80.197544},{25.879383, -80.197270},{25.878808, -80.197246},{25.878806, -80.197520},{25.878968, -80.197541},{25.878960, -80.197866} };

    private  double LandonCoords[][] =
            {{25.879361, -80.199177},{25.879354, -80.198405},{25.878668, -80.198362},{25.878642, -80.199105}};

    //constructor
    public Buildings(){}


    //public method to return name of the building
    public String GetBuildingName(double x, double y){
        //computes the building coords and sets name in BuildingName
        findBuilding(x,y);

        //return the BuildingName
        return this.BuildingName;
    }



    private void findBuilding(double x,double y){
        //clean BuildingName
        this.BuildingName = null;

        //try which building
        while(true){
            if(contains(x,y,LibraryCoords)){this.BuildingName = "Monsignor William Barry Memorial Library";break;}
            else if(contains(x,y,GarnerCoords)){this.BuildingName = "Garner Hall";break;}
            else if(contains(x,y,LandonCoords)){this.BuildingName = "R.Kirk Landon Student Union";break;}
            else{break;}
        }

    }

    private boolean contains(double x , double y,double buildingCoords[][]) {
        //loop counters
         int i; int j;

        // result of testing
        boolean result = false;

        // read coords of the given building
        double points[][] = buildingCoords;

        //check if point x,y is within  all coords that define the building
        for (i = 0, j = points.length - 1; i < points.length; j = i++)
        {
            if((points[i][1] > y) != (points[j][1] > y) &&
                    (x < (points[j][0] - points[i][0]) * (y - points[i][1]) / (points[j][1]-points[i][1]) + points[i][0]))
            {result = !result;}
        }

        return result;
    }

}