import axios from "../api/axios";

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem("user"));
};
const getContacts = (page = 0, size = 5) => {

    const user = getCurrentUser();

    return axios.get(
        `/contacts/user/${user.id}?page=${page}&size=${size}&sortBy=firstName`
    );

};

const searchContacts = (keyword) => {

    const user = getCurrentUser();

    return axios.get(
        `/contacts/user/${user.id}/search?keyword=${keyword}`
    );

};

const createContact = (contact) => {
    return axios.post("/contacts", contact);
};

const updateContact = (id, contact) => {
    return axios.put(`/contacts/${id}`, contact);
};

const deleteContact = (id) => {
    return axios.delete(`/contacts/${id}`);
};

const toggleFavorite = (id) => {
    return axios.patch(`/contacts/${id}/favorite`);
};

export default {
    getContacts,
    searchContacts,
    createContact,
    updateContact,
    deleteContact,
    toggleFavorite
};