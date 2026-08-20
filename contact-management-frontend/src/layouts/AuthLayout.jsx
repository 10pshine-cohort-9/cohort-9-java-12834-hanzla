function AuthLayout({ children }) {
    return (
        <div className="min-h-screen bg-slate-100 flex justify-center items-center">
            {children}
        </div>
    );
}

export default AuthLayout;