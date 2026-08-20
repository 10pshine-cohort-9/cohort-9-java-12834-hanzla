import axios from "axios";

const configuredApiUrl = import.meta.env.VITE_API_URL?.trim();

const getBaseURL = () => {
    if (!configuredApiUrl) {
        return "/api/v1";
    }

    try {
        const url = new URL(configuredApiUrl);

        if (
            url.protocol !== "http:" &&
            url.protocol !== "https:"
        ) {
            throw new Error("Invalid API URL protocol");
        }

        return url.toString().replace(/\/$/, "");

    } catch {
        console.warn(
            "Invalid VITE_API_URL. Using /api/v1."
        );

        return "/api/v1";
    }
};

const api = axios.create({
    baseURL: getBaseURL(),

    withCredentials: true,

    headers: {
        "Content-Type": "application/json"
    },

    xsrfCookieName: "XSRF-TOKEN",
    xsrfHeaderName: "X-XSRF-TOKEN"
});

export default api;