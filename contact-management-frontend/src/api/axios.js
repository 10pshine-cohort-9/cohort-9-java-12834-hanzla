import axios from "axios";


const api = axios.create({

    baseURL: "http://localhost:8080/api/v1",

    /*
     * Required because Spring Security uses
     * JSESSIONID and XSRF-TOKEN cookies.
     */
    withCredentials: true,

    headers: {
        "Content-Type": "application/json"
    },

    /*
     * Spring Security CSRF configuration.
     */
    xsrfCookieName: "XSRF-TOKEN",

    xsrfHeaderName: "X-XSRF-TOKEN"

});


export default api;