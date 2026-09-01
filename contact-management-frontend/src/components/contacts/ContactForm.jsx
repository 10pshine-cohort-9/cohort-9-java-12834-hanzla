import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import {
    FaUser,
    FaEnvelope,
    FaPhone,
    FaBriefcase,
    FaTimes,
    FaSave,
    FaTag
} from "react-icons/fa";
import { toast } from "react-toastify";

import contactService from "../../services/contactService";

const ContactForm = ({
    contact,
    onClose,
    onSuccess
}) => {

    const currentUser = JSON.parse(
        localStorage.getItem("user")
    );

    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        title: "",
        email: "",
        emailType: "Personal",
        phoneNumber: "",
        phoneType: "Personal",
        userId: currentUser?.id
    });

    const [loading, setLoading] = useState(false);

    useEffect(() => {

        if (contact) {

            setFormData({
                firstName: contact.firstName || "",
                lastName: contact.lastName || "",
                title: contact.title || "",
                email: contact.email || "",
                emailType: contact.emailType || "Personal",
                phoneNumber: contact.phoneNumber || "",
                phoneType: contact.phoneType || "Personal",
                userId: currentUser?.id
            });

        } else {

            setFormData({
                firstName: "",
                lastName: "",
                title: "",
                email: "",
                emailType: "Personal",
                phoneNumber: "",
                phoneType: "Personal",
                userId: currentUser?.id
            });

        }

    }, [contact]);

    const handleChange = (e) => {

        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });

    };

    const validateForm = () => {

        if (!formData.firstName.trim()) {
            toast.error("First Name is required");
            return false;
        }

        if (!formData.lastName.trim()) {
            toast.error("Last Name is required");
            return false;
        }

        if (!formData.email.trim()) {
            toast.error("Email is required");
            return false;
        }

        if (!formData.emailType.trim()) {
            toast.error("Email type is required");
            return false;
        }

        if (!formData.phoneNumber.trim()) {
            toast.error("Phone Number is required");
            return false;
        }

        if (!formData.phoneType.trim()) {
            toast.error("Phone type is required");
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        if (!validateForm()) return;

        try {

            setLoading(true);

            if (contact) {

                await contactService.updateContact(
                    contact.id,
                    formData
                );

                toast.success(
                    "Contact updated successfully"
                );

            } else {

                await contactService.createContact(
                    formData
                );

                toast.success(
                    "Contact added successfully"
                );
            }

            onSuccess();

        } catch (error) {

            console.error(
                "Contact operation failed:",
                error
            );

            const message =
                error.response?.data?.message ||
                "Operation failed. Please try again.";

            toast.error(message);

        } finally {

            setLoading(false);

        }
    };

    return (

        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50 p-4">

            <motion.div
                initial={{
                    opacity: 0,
                    scale: 0.85
                }}
                animate={{
                    opacity: 1,
                    scale: 1
                }}
                exit={{
                    opacity: 0,
                    scale: 0.85
                }}
                transition={{
                    duration: 0.25
                }}
                className="bg-white rounded-3xl shadow-2xl w-full max-w-3xl overflow-hidden max-h-[95vh] overflow-y-auto"
            >

                {/* Header */}

                <div className="bg-gradient-to-r from-blue-600 to-indigo-600 p-6 flex justify-between items-center">

                    <div>

                        <h2 className="text-3xl font-bold text-white">
                            {contact
                                ? "Edit Contact"
                                : "Add Contact"}
                        </h2>

                        <p className="text-blue-100 mt-1">
                            {contact
                                ? "Update your contact information"
                                : "Add someone to your contact list"}
                        </p>

                    </div>

                    <button
                        type="button"
                        onClick={onClose}
                        className="text-white hover:text-red-200 transition"
                        aria-label="Close"
                    >
                        <FaTimes size={24} />
                    </button>

                </div>

                {/* Form */}

                <form
                    onSubmit={handleSubmit}
                    className="p-8"
                >

                    {/* Name */}

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

                        {/* First Name */}

                        <div>

                            <label className="font-semibold text-slate-700">
                                First Name
                            </label>

                            <div className="relative mt-2">

                                <FaUser className="absolute left-4 top-4 text-slate-400" />

                                <input
                                    type="text"
                                    name="firstName"
                                    value={formData.firstName}
                                    onChange={handleChange}
                                    placeholder="Enter first name"
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                                />

                            </div>

                        </div>

                        {/* Last Name */}

                        <div>

                            <label className="font-semibold text-slate-700">
                                Last Name
                            </label>

                            <div className="relative mt-2">

                                <FaUser className="absolute left-4 top-4 text-slate-400" />

                                <input
                                    type="text"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                    placeholder="Enter last name"
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                                />

                            </div>

                        </div>

                        {/* Title */}

                        <div>

                            <label className="font-semibold text-slate-700">
                                Job Title
                            </label>

                            <div className="relative mt-2">

                                <FaBriefcase className="absolute left-4 top-4 text-slate-400" />

                                <input
                                    type="text"
                                    name="title"
                                    value={formData.title}
                                    onChange={handleChange}
                                    placeholder="e.g. Software Engineer"
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                                />

                            </div>

                        </div>

                        {/* Email */}

                        <div>

                            <label className="font-semibold text-slate-700">
                                Email Address
                            </label>

                            <div className="relative mt-2">

                                <FaEnvelope className="absolute left-4 top-4 text-slate-400" />

                                <input
                                    type="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    placeholder="example@email.com"
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                                />

                            </div>

                        </div>

                    </div>

                    {/* Email Type */}

                    <div className="mt-6">

                        <label
                            htmlFor="emailType"
                            className="font-semibold text-slate-700"
                        >
                            Email Type
                        </label>

                        <div className="relative mt-2">

                            <FaTag className="absolute left-4 top-4 text-slate-400 pointer-events-none" />

                            <select
                                id="emailType"
                                name="emailType"
                                value={formData.emailType}
                                onChange={handleChange}
                                className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 bg-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition cursor-pointer"
                            >

                                <option value="Personal">
                                    Personal
                                </option>

                                <option value="Work">
                                    Work
                                </option>

                                <option value="Other">
                                    Other
                                </option>

                            </select>

                        </div>

                    </div>

                    {/* Phone */}

                    <div className="mt-6">

                        <label className="font-semibold text-slate-700">
                            Phone Number
                        </label>

                        <div className="relative mt-2">

                            <FaPhone className="absolute left-4 top-4 text-slate-400" />

                            <input
                                type="text"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                placeholder="e.g. 03001234567"
                                className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                            />

                        </div>

                    </div>

                    {/* Phone Type */}

                    <div className="mt-6">

                        <label
                            htmlFor="phoneType"
                            className="font-semibold text-slate-700"
                        >
                            Phone Type
                        </label>

                        <div className="relative mt-2">

                            <FaTag className="absolute left-4 top-4 text-slate-400 pointer-events-none" />

                            <select
                                id="phoneType"
                                name="phoneType"
                                value={formData.phoneType}
                                onChange={handleChange}
                                className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 bg-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition cursor-pointer"
                            >

                                <option value="Personal">
                                    Personal
                                </option>

                                <option value="Work">
                                    Work
                                </option>

                                <option value="Home">
                                    Home
                                </option>

                                <option value="Other">
                                    Other
                                </option>

                            </select>

                        </div>

                    </div>

                    {/* Actions */}

                    <div className="flex justify-end gap-4 mt-10 pt-6 border-t border-slate-100">

                        <button
                            type="button"
                            onClick={onClose}
                            disabled={loading}
                            className="px-6 py-3 rounded-xl border border-slate-200 text-slate-700 font-medium hover:bg-slate-100 transition disabled:opacity-50"
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-3 rounded-xl flex items-center gap-3 transition shadow-lg disabled:opacity-60 disabled:cursor-not-allowed"
                        >

                            <FaSave />

                            {loading
                                ? "Saving..."
                                : contact
                                    ? "Update Contact"
                                    : "Save Contact"}

                        </button>

                    </div>

                </form>

            </motion.div>

        </div>

    );
};

export default ContactForm;