import { motion } from "framer-motion";
import {
    FaEnvelope,
    FaPhone,
    FaEdit,
    FaTrash,
    FaStar,
    FaRegStar,
    FaBriefcase
} from "react-icons/fa";

const ContactTable = ({
    contacts,
    onEdit,
    onDelete,
    onToggleFavorite
}) => {

    if (!contacts || contacts.length === 0) {
        return (
            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-12 text-center">

                <div className="text-5xl mb-4">
                    👥
                </div>

                <h3 className="text-xl font-bold text-slate-800">
                    No Contacts Found
                </h3>

                <p className="text-slate-500 mt-2">
                    Add a contact to get started.
                </p>

            </div>
        );
    }

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">

            {/* Desktop Table */}

            <div className="hidden lg:block overflow-x-auto">

                <table className="w-full">

                    <thead>

                        <tr className="bg-slate-50 border-b border-slate-200">

                            <th className="px-6 py-4 text-left text-xs font-bold text-slate-500 uppercase tracking-wider">
                                Contact
                            </th>

                            <th className="px-6 py-4 text-left text-xs font-bold text-slate-500 uppercase tracking-wider">
                                Email
                            </th>

                            <th className="px-6 py-4 text-left text-xs font-bold text-slate-500 uppercase tracking-wider">
                                Phone
                            </th>

                            <th className="px-6 py-4 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">
                                Favorite
                            </th>

                            <th className="px-6 py-4 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">
                                Actions
                            </th>

                        </tr>

                    </thead>

                    <tbody className="divide-y divide-slate-100">

                        {contacts.map((contact, index) => {

                            const fullName =
                                `${contact.firstName || ""} ${contact.lastName || ""}`.trim();

                            const initials =
                                `${contact.firstName?.charAt(0) || ""}${contact.lastName?.charAt(0) || ""}`
                                    .toUpperCase();

                            return (

                                <motion.tr
                                    key={contact.id}
                                    initial={{
                                        opacity: 0,
                                        y: 10
                                    }}
                                    animate={{
                                        opacity: 1,
                                        y: 0
                                    }}
                                    transition={{
                                        delay: index * 0.03
                                    }}
                                    className="hover:bg-slate-50 transition"
                                >

                                    {/* Contact */}

                                    <td className="px-6 py-5">

                                        <div className="flex items-center gap-4">

                                            <div className="w-11 h-11 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 text-white flex items-center justify-center font-bold shadow-sm">
                                                {initials || "?"}
                                            </div>

                                            <div>

                                                <p className="font-bold text-slate-800">
                                                    {fullName || "Unnamed Contact"}
                                                </p>

                                                {contact.title && (
                                                    <div className="flex items-center gap-2 mt-1">

                                                        <FaBriefcase
                                                            className="text-slate-400"
                                                            size={12}
                                                        />

                                                        <p className="text-sm text-slate-500">
                                                            {contact.title}
                                                        </p>

                                                    </div>
                                                )}

                                            </div>

                                        </div>

                                    </td>


                                    {/* Email */}

                                    <td className="px-6 py-5">

                                        <div className="flex items-start gap-3">

                                            <div className="mt-1 text-blue-500">
                                                <FaEnvelope size={15} />
                                            </div>

                                            <div>

                                                <p className="text-sm font-medium text-slate-700">
                                                    {contact.email}
                                                </p>

                                                <span className="inline-flex items-center mt-1 px-2 py-0.5 rounded-full bg-blue-50 text-blue-600 text-xs font-semibold">
                                                    {contact.emailType || "Personal"}
                                                </span>

                                            </div>

                                        </div>

                                    </td>


                                    {/* Phone */}

                                    <td className="px-6 py-5">

                                        <div className="flex items-start gap-3">

                                            <div className="mt-1 text-emerald-500">
                                                <FaPhone size={14} />
                                            </div>

                                            <div>

                                                <p className="text-sm font-medium text-slate-700">
                                                    {contact.phoneNumber}
                                                </p>

                                                <span className="inline-flex items-center mt-1 px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-600 text-xs font-semibold">
                                                    {contact.phoneType || "Personal"}
                                                </span>

                                            </div>

                                        </div>

                                    </td>


                                    {/* Favorite */}

                                    <td className="px-6 py-5 text-center">

                                        <button
                                            type="button"
                                            onClick={() =>
                                                onToggleFavorite(contact.id)
                                            }
                                            className="inline-flex items-center justify-center w-10 h-10 rounded-full hover:bg-yellow-50 transition"
                                            title={
                                                contact.favorite
                                                    ? "Remove from favorites"
                                                    : "Add to favorites"
                                            }
                                        >

                                            {contact.favorite ? (
                                                <FaStar
                                                    className="text-yellow-400"
                                                    size={20}
                                                />
                                            ) : (
                                                <FaRegStar
                                                    className="text-slate-400 hover:text-yellow-400"
                                                    size={20}
                                                />
                                            )}

                                        </button>

                                    </td>


                                    {/* Actions */}

                                    <td className="px-6 py-5">

                                        <div className="flex justify-center gap-2">

                                            <button
                                                type="button"
                                                onClick={() =>
                                                    onEdit(contact)
                                                }
                                                className="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 hover:bg-blue-100 transition flex items-center justify-center"
                                                title="Edit Contact"
                                            >
                                                <FaEdit />
                                            </button>

                                            <button
                                                type="button"
                                                onClick={() =>
                                                    onDelete(contact)
                                                }
                                                className="w-10 h-10 rounded-xl bg-red-50 text-red-600 hover:bg-red-100 transition flex items-center justify-center"
                                                title="Delete Contact"
                                            >
                                                <FaTrash />
                                            </button>

                                        </div>

                                    </td>

                                </motion.tr>

                            );
                        })}

                    </tbody>

                </table>

            </div>


            {/* Mobile Cards */}

            <div className="lg:hidden divide-y divide-slate-100">

                {contacts.map((contact, index) => {

                    const fullName =
                        `${contact.firstName || ""} ${contact.lastName || ""}`.trim();

                    const initials =
                        `${contact.firstName?.charAt(0) || ""}${contact.lastName?.charAt(0) || ""}`
                            .toUpperCase();

                    return (

                        <motion.div
                            key={contact.id}
                            initial={{
                                opacity: 0,
                                y: 10
                            }}
                            animate={{
                                opacity: 1,
                                y: 0
                            }}
                            transition={{
                                delay: index * 0.03
                            }}
                            className="p-5"
                        >

                            <div className="flex items-start justify-between">

                                <div className="flex items-center gap-3">

                                    <div className="w-11 h-11 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 text-white flex items-center justify-center font-bold">
                                        {initials || "?"}
                                    </div>

                                    <div>

                                        <h3 className="font-bold text-slate-800">
                                            {fullName || "Unnamed Contact"}
                                        </h3>

                                        {contact.title && (
                                            <p className="text-sm text-slate-500">
                                                {contact.title}
                                            </p>
                                        )}

                                    </div>

                                </div>

                                <button
                                    type="button"
                                    onClick={() =>
                                        onToggleFavorite(contact.id)
                                    }
                                    className="p-2"
                                >

                                    {contact.favorite ? (
                                        <FaStar className="text-yellow-400" />
                                    ) : (
                                        <FaRegStar className="text-slate-400" />
                                    )}

                                </button>

                            </div>


                            <div className="mt-5 space-y-3">

                                <div className="flex items-center gap-3">

                                    <FaEnvelope className="text-blue-500" />

                                    <div>

                                        <p className="text-sm text-slate-700">
                                            {contact.email}
                                        </p>

                                        <span className="text-xs font-semibold text-blue-600">
                                            {contact.emailType || "Personal"}
                                        </span>

                                    </div>

                                </div>


                                <div className="flex items-center gap-3">

                                    <FaPhone className="text-emerald-500" />

                                    <div>

                                        <p className="text-sm text-slate-700">
                                            {contact.phoneNumber}
                                        </p>

                                        <span className="text-xs font-semibold text-emerald-600">
                                            {contact.phoneType || "Personal"}
                                        </span>

                                    </div>

                                </div>

                            </div>


                            <div className="flex gap-3 mt-5">

                                <button
                                    type="button"
                                    onClick={() =>
                                        onEdit(contact)
                                    }
                                    className="flex-1 py-2.5 rounded-xl bg-blue-50 text-blue-600 font-semibold hover:bg-blue-100 transition flex items-center justify-center gap-2"
                                >
                                    <FaEdit />
                                    Edit
                                </button>

                                <button
                                    type="button"
                                    onClick={() =>
                                        onDelete(contact)
                                    }
                                    className="flex-1 py-2.5 rounded-xl bg-red-50 text-red-600 font-semibold hover:bg-red-100 transition flex items-center justify-center gap-2"
                                >
                                    <FaTrash />
                                    Delete
                                </button>

                            </div>

                        </motion.div>

                    );
                })}

            </div>

        </div>
    );
};

export default ContactTable;