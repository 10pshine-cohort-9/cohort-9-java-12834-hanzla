import { motion } from "framer-motion";
import {
    FaStar,
    FaRegStar,
    FaEdit,
    FaTrash,
    FaEnvelope,
    FaPhone
} from "react-icons/fa";
import Swal from "sweetalert2";
import { toast } from "react-toastify";

import contactService from "../../services/contactService";

const ContactTable = ({
    contacts,
    onEdit,
    onRefresh
}) => {

    const handleDelete = async (contact) => {

        const result = await Swal.fire({
            title: "Delete Contact?",
            text: `Are you sure you want to permanently delete ${contact.firstName} ${contact.lastName}?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc2626",
            cancelButtonColor: "#2563eb",
            confirmButtonText: "Delete",
            cancelButtonText: "Cancel"
        });

        if (!result.isConfirmed) return;

        try {

            await contactService.deleteContact(contact.id);

            toast.success("Contact Deleted");

            onRefresh();

        } catch {

            toast.error("Unable to delete contact");

        }

    };

    const handleFavorite = async (id) => {

        try {

            await contactService.toggleFavorite(id);

            onRefresh();

        } catch {

            toast.error("Unable to update favorite");

        }

    };

    return (

        <div className="overflow-x-auto">

            <table className="min-w-full">

                <thead>

                    <tr className="bg-slate-50 border-b text-slate-600 uppercase tracking-wider text-sm">

                        <th className="text-left px-8 py-5">Contact</th>

                        <th className="text-left px-8 py-5">Title</th>

                        <th className="text-left px-8 py-5">Email</th>

                        <th className="text-left px-8 py-5">Phone</th>

                        <th className="text-center px-8 py-5">Favorite</th>

                        <th className="text-center px-8 py-5">Actions</th>

                    </tr>

                </thead>

                <tbody>

                    {

                        contacts.length === 0 ? (

                            <tr>

                                <td
                                    colSpan="6"
                                    className="text-center py-20 text-slate-400"
                                >

                                    <div className="flex flex-col items-center">

                                        <h3 className="text-2xl font-semibold">

                                            No Contacts Found

                                        </h3>

                                        <p className="text-slate-500 mt-2">

                                            Click "Add Contact" to create your first contact.

                                        </p>

                                    </div>

                                </td>

                            </tr>

                        ) : (

                            contacts.map((contact, index) => (

                                <motion.tr

                                    key={contact.id}

                                    initial={{
                                        opacity: 0,
                                        y: 20
                                    }}

                                    animate={{
                                        opacity: 1,
                                        y: 0
                                    }}

                                    transition={{
                                        delay: index * 0.05
                                    }}

                                    className="border-b hover:bg-blue-50 hover:shadow-sm transition-all duration-300"

                                >

                                    <td className="px-8 py-6">

                                        <div className="flex items-center gap-4">

                                            <div className="w-14 h-14 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 text-white flex justify-center items-center font-bold text-xl shadow">

                                                {`${contact.firstName?.charAt(0) || ""}${contact.lastName?.charAt(0) || ""}`.toUpperCase()}

                                            </div>

                                            <div>

                                                <h3 className="font-semibold text-slate-800 text-lg">

                                                    {contact.firstName} {contact.lastName}

                                                </h3>

                                                <p className="text-sm text-slate-400 mt-1">

                                                    Contact #{contact.id}

                                                </p>

                                            </div>

                                        </div>

                                    </td>

                                    <td className="px-8 py-6">

                                        <span className="bg-blue-100 text-blue-700 px-4 py-2 rounded-full text-sm font-medium">

                                            {contact.title || "No Title"}

                                        </span>

                                    </td>

                                    <td className="px-8 py-6">

                                        <div className="flex items-center gap-3 text-slate-600">

                                            <FaEnvelope className="text-blue-600" />

                                            {contact.email}

                                        </div>

                                    </td>

                                    <td className="px-8 py-6">

                                        <div className="flex items-center gap-3 text-slate-600">

                                            <FaPhone className="text-green-600" />

                                            {contact.phoneNumber}

                                        </div>

                                    </td>

                                    <td className="px-8 py-6 text-center">

                                        <button

                                            onClick={() => handleFavorite(contact.id)}

                                            className="hover:scale-125 transition"

                                        >

                                            {

                                                contact.favorite

                                                    ?

                                                    <FaStar className="text-yellow-500 text-2xl" />

                                                    :

                                                    <FaRegStar className="text-slate-400 text-2xl" />

                                            }

                                        </button>

                                    </td>

                                    <td className="px-8 py-6">

                                        <div className="flex justify-center gap-4">

                                            <button

                                                onClick={() => onEdit(contact)}

                                                className="bg-blue-100 text-blue-700 p-3 rounded-xl shadow-sm hover:shadow-md hover:bg-blue-600 hover:text-white transition-all duration-300"

                                            >

                                                <FaEdit />

                                            </button>

                                            <button

                                                onClick={() => handleDelete(contact)}

                                                className="bg-red-100 text-red-700 p-3 rounded-xl shadow-sm hover:shadow-md hover:bg-red-600 hover:text-white transition-all duration-300"

                                            >

                                                <FaTrash />

                                            </button>

                                        </div>

                                    </td>

                                </motion.tr>

                            ))

                        )

                    }

                </tbody>

            </table>

        </div>

    );

};

export default ContactTable;