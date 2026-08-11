import api from "../api/axios";

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem("user"));
};

const getDashboard = () => {

    const user = getCurrentUser();

    return api.get(`/dashboard/${user.id}`);

};

export default {
    getDashboard
};