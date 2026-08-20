import { motion } from "framer-motion";

const QuickActions = ({ navigate }) => {

    return (

        <motion.div

            initial={{
                opacity: 0,
                x: 40
            }}

            animate={{
                opacity: 1,
                x: 0
            }}

            className="bg-white rounded-3xl shadow-lg p-8 self-start"

        >

            <h2 className="text-2xl font-bold">

                Quick Actions

            </h2>

            <p className="text-slate-500 mt-2 mb-8">

                Quickly navigate to contact management.

            </p>

            <button

                onClick={() => navigate("/contacts")}

                className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl py-4 transition"

            >

                Manage Contacts

            </button>

        </motion.div>

    );

};

export default QuickActions;