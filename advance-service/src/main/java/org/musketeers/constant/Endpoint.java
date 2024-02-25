package org.musketeers.constant;


public abstract class Endpoint {

    private static final String VERSION = "/v1";

    private static final String API = "/api";

    public static final String ROOT = API + VERSION;

    public static final String ADVANCE = "/advance";

    public static final String CREATE_REQUEST = "/create-request";

    public static final String CANCEL_REQUEST = "/cancel-request";

    public static final String UPDATE_REQUEST = "/update-request";

    public static final String GET_ALL_REQUESTS = "/get-all-requests";

    public static final String GET_ALL_MY_REQUESTS = "/get-all-my-requests";

}
