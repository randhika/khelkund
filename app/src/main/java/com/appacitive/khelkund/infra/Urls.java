package com.appacitive.khelkund.infra;

/**
 * Created by sathley on 3/24/2015.
 */
public class Urls {

    private static String baseUrl = "http://www.khelkund.com/ipl/Service/";

    public static class PlayerUrls
    {
        public static String getAllPlayersUrl() {
            return baseUrl + "PlayerService.svc/Players";
        }

        public static String getPlayerDetailsUrl(String playerId) {
            return baseUrl + "PlayerService.svc/PlayerDetails/" + playerId + "/allrounder";
        }
    }

    public static class Pick5Urls
    {
        public static String getAllMatchesUrl()
        {
            return baseUrl + "PickThemFiveService.svc/GetMatches";
        }
    }

    public static class TeamUrls
    {
        public static String getMyTeamUrl(String userId)
        {
            return baseUrl + "UserTeamService.svc/UserTeam/" + userId;
        }

        public static String saveTeamUrl()
        {
            return baseUrl + "UserTeamService.svc/SaveTeam";
        }
    }

    public static class AppacitiveUrls
    {
        public static String getRegisterDeviceUrl()
        {
            return "https://apis.appacitive.com/v1.0/connection/user_device";
        }
    }

    public static class UserUrls
    {
        public static String getLoginUrl()
        {
            return baseUrl + "Loginservice.svc/login";
        }

        public static String getFacebookLoginUrl()
        {
            return baseUrl + "Loginservice.svc/registerfacebook";
        }

        public static String getTwitterLoginUrl()
        {
            return baseUrl + "Loginservice.svc/registertwitter";
        }

        public static String getSignupUrl()
        {
            return baseUrl + "Loginservice.svc/signup";
        }
    }
}
