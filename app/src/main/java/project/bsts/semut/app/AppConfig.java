package project.bsts.semut.app;

public class AppConfig {

    public static String BASE_URL = "http://bsts-svc.lskk.ee.itb.ac.id/dev/api/";

    //POST method
    public static String API_LOGIN = BASE_URL +"users/signin/";
    public static String API_REGISTER = BASE_URL +"users/signup/";
    public static String API_REGFB = BASE_URL +"users/registerfb/";
    public static String API_LOGINFB = BASE_URL +"users/fbsignin/";
    public static String API_SETNOTIF = BASE_URL +"users/setnotif";
    public static String API_SETVISIBILITY = BASE_URL +"users/setvisibility/";
    public static String API_SETAVATAR = BASE_URL +"users/setavatar/";
    public static String API_SEARCH = BASE_URL +"users/search/";
    public static String API_RESETPASS = BASE_URL +"users/resetpass/";
    public static String API_EMAILVERIFYING = BASE_URL +"users/verification/";
    public static String API_REQFRIEND = BASE_URL +"friend/request/";
    public static String API_ACCEPTFRIEND = BASE_URL +"friend/accept/";
    public static String API_STORELOCATION = BASE_URL +"location/store/";
    public static String API_ADSHINT = BASE_URL +"ads/adshint/";
    public static String API_TAGING = BASE_URL +"post/submit/";
    public static String API_RETAG = BASE_URL +"post/retag/";
    public static String API_REPORTTAG = BASE_URL +"post/reporttag/";
    public static String API_CHECKIN = BASE_URL +"checkin/submit/";
    public static String API_ADDPLCE = BASE_URL +"place/add/";
    public static String API_RESERVATION = BASE_URL +"taxi/reservation/";
    public static String API_CONFIRM = BASE_URL +"taxi/sendmark/";
    public static String API_CANCEL = BASE_URL +"taxi/cancelrequest/";
    public static String API_RATETAXI = BASE_URL +"taxi/rate/";
    public static String API_CCTV = BASE_URL +"cctv/list/CityID/";

    //GET method
    public static String API_MYPROFILE = BASE_URL+"users/myprofile";
    public static String API_USERPROFILE = BASE_URL+"users/userprofile";
    public static String API_FRIENDLIST = BASE_URL+"friend/friendlist";
    public static String API_MAPVIEW = BASE_URL+"location/mapview";
    public static String API_PLACEVIEW = BASE_URL+"location/placeview";
    public static String API_TAGLIST = BASE_URL+"post/list";
    public static String API_MYCHECKINLIST = BASE_URL+"checkin/ownlist";
    public static String API_USERCHECKINLIST = BASE_URL+"checkin/list";
    public static String API_PLACELIST = BASE_URL+"place/list";
    public static String API_PLACETYPELIST = BASE_URL+"place/typelist";
    public static String API_CEKRESERVATION = BASE_URL+"taxi/cektaker";
    public static String API_CEKORDEREXIST = BASE_URL+"taxi/cekisorder";
    public static String API_ORDERDETAIL = BASE_URL+"taxi/orderdetail";
    public static String API_GETTAXILOCATION = BASE_URL+"taxi/taxilocation";
    public static String API_TAXIORDERHISTORY = BASE_URL+"taxi/reservationhistory";
    public static String API_GENERATEBARCODE = BASE_URL+"users/generatebarcode";
    public static String API_PARKING_CCTV = BASE_URL +"parking/list/";



	// Server user register url
	public static String URL_REGISTER = "http://192.168.0.104/android_login_api/";
}
