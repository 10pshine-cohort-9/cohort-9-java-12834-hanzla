import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { toast } from "react-toastify";
import {
    FaUser,
    FaEnvelope,
    FaPhone,
    FaBriefcase,
    FaTimes,
    FaSave
} from "react-icons/fa";

import contactService from "../../services/contactService";

const ContactForm = ({
    contact,
    onClose,
    onSuccess
}) => {

    // Logged in user
    const currentUser = JSON.parse(localStorage.getItem("user"));

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
            toast.error("Email Type is required");
            return false;
        }

        if (!formData.phoneNumber.trim()) {
            toast.error("Phone Number is required");
            return false;
        }

        if (!formData.phoneType.trim()) {
            toast.error("Phone Type is required");
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

                toast.success("Contact Updated Successfully");

            } else {

                await contactService.createContact(
                    formData
                );

                toast.success("Contact Added Successfully");
            }

            onSuccess();

        } catch (error) {

            console.error(error);

            toast.error(
                error.response?.data?.message ||
                "Operation Failed"
            );

        } finally {

            setLoading(false);

        }
    };

    return (

        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50 p-4">

            <motion.div
                initial={{ opacity: 0, scale: 0.85 }}
                animate={{ opacity: 1, scale: 1 }}
                exit={{ opacity: 0, scale: 0.85 }}
                transition={{ duration: 0.25 }}
                className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto"
            >

                {/* Header */}

                <div className="bg-gradient-to-r from-blue-600 to-indigo-600 p-6 flex justify-between items-center">

                    <div>

                        <h2 className="text-3xl font-bold text-white">
                            {contact ? "Edit Contact" : "Add Contact"}
                        </h2>

                        <p className="text-blue-100 mt-1">
                            {contact
                                ? "Update your contact information"
                                : "Add a new contact to your list"}
                        </p>

                    </div>

                    <button
                        type="button"
                        onClick={onClose}
                        className="text-white hover:text-red-200 transition"
                        aria-label="Close contact form"
                    >
                        <FaTimes size={24} />
                    </button>

                </div>

                {/* Form */}

                <form
                    onSubmit={handleSubmit}
                    className="p-8"
                >

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
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
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
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                />

                            </div>

                        </div>

                        {/* Title */}

                        <div>

                            <label className="font-semibold text-slate-700">
                                Title
                            </label>

                            <div className="relative mt-2">

                                <FaBriefcase className="absolute left-4 top-4 text-slate-400" />

                                <input
                                    type="text"
                                    name="title"
                                    value={formData.title}
                                    onChange={handleChange}
                                    placeholder="e.g. Software Engineer"
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
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
                                    className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                />

                            </div>

                        </div>

                    </div>

                    {/* Email Type */}

                    <div className="mt-6">

                        <label className="font-semibold text-slate-700">
                            Email Type
                        </label>

                        <select
                            name="emailType"
                            value={formData.emailType}
                            onChange={handleChange}
                            className="w-full border border-slate-200 rounded-xl px-4 py-3 mt-2 bg-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        >

                            <option value="Personal">
                                Personal Email
                            </option>

                            <option value="Work">
                                Work Email
                            </option>

                            <option value="Other">
                                Other Email
                            </option>

                        </select>

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
                                placeholder="Enter phone number"
                                className="w-full border border-slate-200 rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            />

                        </div>

                    </div>

                    {/* Phone Type */}

                    <div className="mt-6">

                        <label className="font-semibold text-slate-700">
                            Phone Type
                        </label>

                        <select
                            name="phoneType"
                            value={formData.phoneType}
                            onChange={handleChange}
                            className="w-full border border-slate-200 rounded-xl px-4 py-3 mt-2 bg-white focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        >

                            <option value="Personal">
                                Personal Phone
                            </option>

                            <option value="Work">
                                Work Phone
                            </option>

                            <option value="Home">
                                Home Phone
                            </option>

                            <option value="Other">
                                Other Phone
                            </option>

                        </select>

                    </div>

                    {/* Actions */}

                    <div className="flex justify-end gap-4 mt-10">

                        <button
                            type="button"
                            onClick={onClose}
                            disabled={loading}
                            className="px-6 py-3 rounded-xl border border-slate-200 hover:bg-slate-100 transition disabled:opacity-50"
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-3 rounded-xl flex items-center gap-3 transition shadow-lg disabled:opacity-60"
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