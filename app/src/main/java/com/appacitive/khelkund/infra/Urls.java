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

    public static class UserUrls
    {
        public static String getLoginUrl()
        {
            return baseUrl + "Loginservice.svc/login";
        }
    }
}
