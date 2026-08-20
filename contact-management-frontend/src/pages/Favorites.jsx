import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { toast } from "react-toastify";

import DashboardLayout from "../layouts/DashboardLayout";
import ContactTable from "../components/contacts/ContactTable";
import ContactForm from "../components/contacts/ContactForm";
import Loader from "../components/common/Loader";

import contactService from "../services/contactService";

const Favorites = () => {

    const [contacts, setContacts] = useState([]);
    const [loading, setLoading] = useState(true);

    const [showModal, setShowModal] = useState(false);
    const [selectedContact, setSelectedContact] = useState(null);

    const loadFavorites = async () => {

        try {

            setLoading(true);

            const response = await contactService.getContacts(0, 100);

            const favorites = response.data.filter(
                (contact) => contact.favorite === true
            );

            setContacts(favorites);

        } catch (error) {

            console.error(error);

            toast.error("Unable to load favorite contacts");

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {

        loadFavorites();

    }, []);

    const handleEdit = (contact) => {

        setSelectedContact(contact);

        setShowModal(true);

    };

    const handleSuccess = () => {

        loadFavorites();

        setShowModal(false);

    };

    return (

        <DashboardLayout>

            <motion.div

                initial={{ opacity: 0, y: 20 }}

                animate={{ opacity: 1, y: 0 }}

                className="space-y-8"

            >

                <div>

                    <h1 className="text-4xl font-bold text-slate-800">

                        Favorite Contacts ⭐

                    </h1>

<p className="text-slate-500 mt-2">

    Manage all your favorite contacts from one place.

</p>

                </div>

{
    loading ? (

        <Loader />

    ) : contacts.length === 0 ? (

        <div className="bg-white rounded-3xl shadow-lg p-16 text-center">

            <h2 className="text-3xl mb-4">

                ⭐

            </h2>

            <h3 className="text-2xl font-bold text-slate-700">

                No Favorite Contacts

            </h3>

            <p className="text-slate-500 mt-3">

                Mark contacts as favorites to see them here.

            </p>

        </div>

    ) : (

        <div className="bg-white rounded-2xl shadow-md overflow-hidden">

            <ContactTable

                contacts={contacts}

                onEdit={handleEdit}

                onRefresh={loadFavorites}

            />

        </div>

    )
}

                {

                    showModal &&

                    <ContactForm

                        contact={selectedContact}

                        onClose={() => setShowModal(false)}

                        onSuccess={handleSuccess}

                    />

                }

            </motion.div>

        </DashboardLayout>

    );

};

export default Favorites;