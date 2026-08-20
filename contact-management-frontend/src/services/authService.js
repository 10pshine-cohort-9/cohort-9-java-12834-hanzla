import api from "../api/axios";


/*
 * Get CSRF token from Spring Security.
 *
 * Spring returns the token in the response body.
 * We then explicitly put that token into the
 * X-XSRF-TOKEN header for future POST/PUT/PATCH/DELETE
 * requests.
 */
const getCsrfToken = async () => {

    const response = await api.get("/auth/csrf");

    const token = response.data;

    if (token) {
        api.defaults.headers.common["X-XSRF-TOKEN"] = token;
    }

    return response;
};


/*
 * Keep this alias because some existing parts
 * of the application may call initializeCsrf().
 */
const initializeCsrf = getCsrfToken;


const login = (data) => {
    return api.post("/auth/login", data);
};


const register = (data) => {
    return api.post("/auth/register", data);
};


const changePassword = (data) => {
    return api.put("/auth/change-password", data);
};


const logout = ( ) => {
    return api.post("/auth/logout");
};


export default {
    getCsrfToken,
    initializeCsrf,
    login,
    register,
    changePassword,
    logout
};