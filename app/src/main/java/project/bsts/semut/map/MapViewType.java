package project.bsts.semut.map;


import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.TranspostMap;

public class MapViewType {

    public static int get(Object object){
        int type = 0;

        if(object instanceof PoliceMap) type = MapViewComponent.POLICE_MAP_COMPONENT;
        else if(object instanceof AccidentMap) type = MapViewComponent.ACCIDENT_MAP_COMPONENT;
        else if(object instanceof TrafficMap) type = MapViewComponent.TRAFFIC_MAP_COMPONENT;
        else if(object instanceof DisasterMap) type = MapViewComponent.DISASTER_MAP_COMPONENT;
        else if(object instanceof ClosureMap) type = MapViewComponent.CLOSURE_MAP_COMPONENT;
        else if(object instanceof OtherMap) type = MapViewComponent.OTHER_MAP_COMPONENT;
        else if(object instanceof TranspostMap) type = MapViewComponent.TRANSPORTATION_POST_MAP_COMPONENT;

        return type;
    }

}
