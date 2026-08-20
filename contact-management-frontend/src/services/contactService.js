import axios from "../api/axios";

const getContacts = (page = 0, size = 5) => {
    return axios.get(
        `/contacts?page=${page}&size=${size}&sortBy=firstName`
    );
};

const searchContacts = (keyword) => {
    return axios.get(
        `/contacts/search?keyword=${encodeURIComponent(keyword)}`
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