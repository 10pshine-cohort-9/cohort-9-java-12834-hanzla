const PrimaryButton = ({
    children,
    type = "button",
    onClick,
    disabled = false,
    className = ""
}) => {

    return (

        <button

            type={type}

            onClick={onClick}

            disabled={disabled}

            className={`
                bg-blue-600
                hover:bg-blue-700
                text-white
                px-6
                py-3
                rounded-xl
                shadow-lg
                transition-all
                duration-200
                hover:scale-105
                disabled:opacity-60
                disabled:cursor-not-allowed
                flex
                justify-center
                items-center
                gap-2
                ${className}
            `}

        >

            {children}

        </button>

    );

};

export default PrimaryButton;