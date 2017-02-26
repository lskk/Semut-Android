package project.bsts.semut.helper;


public class TagsType {

    private final static String[] TITLES = {"Traffic", "Police", "Incident", "Disaster", "Closure", "Other"};

    public static String[] get(int title, int subTitle){

        String[] subTitlesStr = {};
        switch (title){
            case 0:
                subTitlesStr = new String[]{"Normal Traffic", "Heavy Traffic", "Standstill Traffic"};
                break;
            case 1:
                subTitlesStr = new String[]{"Police Patrol", "Police Raid"};
                break;
            case 2:
                subTitlesStr = new String[]{"Incident", "Incident with Victim", "Vehicle Broke Down"};
                break;
            case 3:
                subTitlesStr = new String[]{"Fallen Tree", "Flood"};
                break;
            case 4:
                subTitlesStr = new String[]{"Minor Road Repair", "Medium Road Repair", "Event", "Construction", "Demonstration"};
                break;
            case 5:
                subTitlesStr = new String[]{"No Sign", "Damaged Road", "Bus Stop", "Crowded Places"};
                break;
        }

        return new String[]{TITLES[title], subTitlesStr[subTitle]};

    }
}
