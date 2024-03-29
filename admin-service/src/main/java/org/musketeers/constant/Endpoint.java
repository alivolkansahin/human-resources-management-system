package org.musketeers.constant;


public abstract class Endpoint {


    private static final String VERSION = "/v1";

    private static final String API = "/api";

    public static final String ROOT = API + VERSION;

    public static final String ADMIN = "/admin";

    public static final String GET = "/get";

    public static final String GET_ALL = "/get-all";

    public static final String UPDATE = "/update";

    public static final String GET_ALL_PENDING_SUPERVISORS = "/get-all-pending-supervisors";

    public static final String HANDLE_SUPERVISOR_REGISTRATION = "/handle-supervisor-registration";

    public static final String GET_ALL_PENDING_COMMENTS = "/get-all-pending-comments";

    public static final String HANDLE_PENDING_COMMENT = "/handle-pending-comment";

    //    public static final String DELETE = "/delete";

}
