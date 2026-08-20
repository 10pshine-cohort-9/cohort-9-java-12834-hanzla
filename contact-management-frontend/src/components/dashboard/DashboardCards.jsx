import { motion } from "framer-motion";

const DashboardCards = ({ cards }) => {

    return (

        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-7">

            {

                cards.map((card, index) => (

                    <motion.div

                        key={index}

                        initial={{
                            opacity: 0,
                            y: 30
                        }}

                        animate={{
                            opacity: 1,
                            y: 0
                        }}

                        transition={{
                            delay: index * 0.12
                        }}

                        whileHover={{
                            y: -6,
                            scale: 1.02
                        }}

                        className={`
                            bg-gradient-to-r
                            ${card.color}
                            rounded-3xl
                            shadow-lg
                            hover:shadow-2xl
                            text-white
                            p-8
                            transition-all
                            duration-300
                            cursor-default
                        `}

                    >

                        <div className="flex justify-between items-start">

                            <div>

                                <p className="text-lg font-medium opacity-90">

                                    {card.title}

                                </p>

                                <h2 className="text-5xl font-bold mt-5 tracking-tight">

                                    {card.value}

                                </h2>

                            </div>

                            <div className="text-5xl opacity-75 mt-1">

                                {card.icon}

                            </div>

                        </div>

                    </motion.div>

                ))

            }

        </div>

    );

};

export default DashboardCards;