import {
    FaChevronLeft,
    FaChevronRight
} from "react-icons/fa";

const Pagination = ({

    page,

    setPage

}) => {

    return (

        <div className="flex items-center gap-4 mt-8">

            <button

                disabled={page === 0}

                onClick={() => setPage(page - 1)}

                className="flex items-center gap-2 px-5 py-3 rounded-xl bg-white shadow hover:bg-blue-600 hover:text-white disabled:opacity-40 disabled:hover:bg-white disabled:hover:text-black transition"

            >

                <FaChevronLeft />

                Previous

            </button>

            <div className="bg-blue-600 text-white px-6 py-3 rounded-xl shadow">

                Page {page + 1}

            </div>

            <button

                onClick={() => setPage(page + 1)}

                className="flex items-center gap-2 px-5 py-3 rounded-xl bg-white shadow hover:bg-blue-600 hover:text-white transition"

            >

                Next

                <FaChevronRight />

            </button>

        </div>

    );

};

export default Pagination;