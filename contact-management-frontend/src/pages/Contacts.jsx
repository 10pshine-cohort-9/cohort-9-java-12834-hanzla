import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { FaPlus } from "react-icons/fa";
import { ToastContainer, toast } from "react-toastify";

import DashboardLayout from "../layouts/DashboardLayout";

import SearchBar from "../components/contacts/SearchBar";
import ContactTable from "../components/contacts/ContactTable";
import Pagination from "../components/contacts/Pagination";
import ContactForm from "../components/contacts/ContactForm";
import Loader from "../components/common/Loader";
import PrimaryButton from "../components/ui/PrimaryButton";

import contactService from "../services/contactService";

import "react-toastify/dist/ReactToastify.css";

const Contacts = () => {

    const [contacts, setContacts] = useState([]);

    const [loading, setLoading] = useState(true);

    const [keyword, setKeyword] = useState("");

    const [page, setPage] = useState(0);

    const [showModal, setShowModal] = useState(false);

    const [selectedContact, setSelectedContact] = useState(null);

    const size = 6;

const loadContacts = async () => {

    try {

        setLoading(true);

        const response = await contactService.getContacts(page, size);

        console.log("FULL RESPONSE:", response);
        console.log("DATA:", response.data);
        console.log("IS ARRAY:", Array.isArray(response.data));

        setContacts(response.data);

    }

    catch (error) {

        console.error(error);

        toast.error("Unable to load contacts");

    }

    finally {

        setLoading(false);

    }

};

    useEffect(() => {

        if (keyword.trim() === "") {

            loadContacts();

        }

        else {

            contactService

                .searchContacts(keyword)

                .then((response) => {

                    setContacts(response.data);

                })

                .catch(() => {

                    toast.error("Search failed");

                    loadContacts();

                });

        }

    }, [page, keyword]);



    const handleAddClick = () => {

        setSelectedContact(null);

        setShowModal(true);

    };



    const handleEditClick = (contact) => {

        setSelectedContact(contact);

        setShowModal(true);

    };



    const handleSuccess = async () => {

        await loadContacts();

        setSelectedContact(null);

        setShowModal(false);

    };
    return (

        <DashboardLayout>

            <ToastContainer
                position="top-right"
                autoClose={2500}
                theme="colored"
            />

            <motion.div

                initial={{ opacity: 0, y: 25 }}

                animate={{ opacity: 1, y: 0 }}

                transition={{ duration: 0.5 }}

                className="space-y-8"

            >

                <div className="flex flex-col lg:flex-row lg:justify-between lg:items-center gap-6">

                    <div>

                        <h1 className="text-4xl font-bold text-slate-800">

                            Contacts

                        </h1>

                        <p className="text-slate-500 mt-2">

                            Manage your contacts professionally

                        </p>

                    </div>

                    <PrimaryButton
                        onClick={handleAddClick}
                        className="hover:shadow-xl"
                    >

                        <FaPlus />

                        Add Contact

                    </PrimaryButton>

                </div>



                <motion.div

                    initial={{ opacity: 0 }}

                    animate={{ opacity: 1 }}

                    transition={{ delay: 0.15 }}

                    className="bg-white rounded-2xl shadow-md p-6"

                >

                    <SearchBar

                        keyword={keyword}

                        setKeyword={setKeyword}

                    />

                </motion.div>



                {

                    loading ? (

                        <Loader />

                    ) : (

                        <motion.div

                            initial={{ opacity: 0 }}

                            animate={{ opacity: 1 }}

                            transition={{ delay: 0.2 }}

                            className="bg-white rounded-2xl shadow-md overflow-hidden"

                        >

                            <ContactTable

                                contacts={contacts}

                                onEdit={handleEditClick}

                                onRefresh={loadContacts}

                            />

                        </motion.div>

                    )

                }



                <div className="flex justify-center">

                    <Pagination

                        page={page}

                        setPage={setPage}

                    />

                </div>



                {

                    showModal &&

                    (

                        <ContactForm

                            contact={selectedContact}

                            onClose={() => {

                                setSelectedContact(null);

                                setShowModal(false);

                            }}

                            onSuccess={handleSuccess}

                        />

                    )

                }

            </motion.div>

        </DashboardLayout>

    );

};

export default Contacts;